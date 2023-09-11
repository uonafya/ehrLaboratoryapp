package org.openmrs.module.laboratoryapp.fragment.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.ehrlaboratory.LaboratoryService;
import org.openmrs.module.hospitalcore.PatientQueueService;
import org.openmrs.module.hospitalcore.model.LabTest;
import org.openmrs.module.hospitalcore.model.OpdPatientQueue;
import org.openmrs.module.hospitalcore.model.OpdPatientQueueLog;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.module.laboratoryapp.util.LaboratoryUtil;
import org.openmrs.module.laboratoryapp.util.ParameterModel;
import org.openmrs.module.laboratoryapp.util.ParameterOption;
import org.openmrs.module.laboratoryapp.util.ResultModel;
import org.openmrs.module.laboratoryapp.util.ResultModelWrapper;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

public class ResultFragmentController {
	private static final Integer LAB_CONCEPT_ID = Integer.valueOf(1283);

	private Logger log = LoggerFactory.getLogger(ResultFragmentController.class);

	public List<SimpleObject> getResultTemplate(@RequestParam("testId") Integer testId, UiUtils ui) {
		LaboratoryService ls = Context.getService(LaboratoryService.class);
		LabTest test = ls.getLaboratoryTest(testId);
		List<ParameterModel> parameters = new ArrayList<ParameterModel>();
		LaboratoryUtil.generateParameterModels(parameters, test.getConcept(), null, test.getEncounter());
		List<SimpleObject> resultsTemplate = new ArrayList<SimpleObject>();
		for (ParameterModel parameter : parameters) {
			SimpleObject resultTemplate = new SimpleObject();
			resultTemplate.put("type", parameter.getType());
			resultTemplate.put("id", parameter.getId());
			resultTemplate.put("container", parameter.getContainer());
			resultTemplate.put("containerId", parameter.getContainerId());
			resultTemplate.put("title", parameter.getTitle());
			resultTemplate.put("unit", parameter.getUnit());
			resultTemplate.put("validator", parameter.getValidator());
			resultTemplate.put("defaultValue", parameter.getDefaultValue());
			List<SimpleObject> options = new ArrayList<SimpleObject>();
			for (ParameterOption option : parameter.getOptions()) {
				SimpleObject parameterOption = new SimpleObject();
				parameterOption.put("label", option.getLabel());
				parameterOption.put("value", option.getValue());
				options.add(parameterOption);
			}
			resultTemplate.put("options", options);
			resultsTemplate.add(resultTemplate);
		}
		return resultsTemplate;
	}

	public SimpleObject saveResult(@BindParams("wrap") ResultModelWrapper resultWrapper,  @RequestParam(value = "test_result_comment",required = false) String comment) {
		LaboratoryService ls = Context.getService(LaboratoryService.class);
		LabTest test = ls.getLaboratoryTest(resultWrapper.getTestId());
		Encounter encounter = getEncounter(test);

		for (ResultModel resultModel : resultWrapper.getResults()) {
			String result = (resultModel.getSelectedOption() == null) ? resultModel.getValue() : resultModel.getSelectedOption();
			if (StringUtils.isBlank(result))
				continue;
			if (StringUtils.contains(resultModel.getConceptName(), ".")) {
				String[] parentChildConceptIds = StringUtils.split(resultModel.getConceptName(), ".");
				Concept testGroupConcept = Context.getConceptService().getConcept(parentChildConceptIds[0]);
				Concept testConcept = Context.getConceptService().getConcept(parentChildConceptIds[1]);
				addLaboratoryTestObservation(encounter, testConcept, testGroupConcept, result, test, comment);
				continue;
			}
			Concept concept = Context.getConceptService().getConcept(resultModel.getConceptName());
			addLaboratoryTestObservation(encounter, concept, null, result, test, comment);
		}

		encounter = Context.getEncounterService().saveEncounter(encounter);
		test.setEncounter(encounter);
		ls.completeTest(test);
		sendPatientToOpdQueue(encounter);
		return SimpleObject.create("status", "success", "message", "Saved!");
	}

	private Encounter getEncounter(LabTest test) {
		if (test.getEncounter() != null)
			return test.getEncounter();
		String encounterTypeStr = GlobalPropertyUtil.getString("billing.labEncounterType", "LABENCOUNTER");
		EncounterType encounterType = Context.getEncounterService().getEncounterType(encounterTypeStr);
		Encounter encounter = new Encounter();
		encounter.setCreator(Context.getAuthenticatedUser());
		encounter.setDateCreated(new Date());
		Location loc = ((KenyaEmrService) Context.getService(KenyaEmrService.class)).getDefaultLocation();
		encounter.setLocation(loc);
		encounter.setPatient(test.getPatient());
		encounter.setEncounterType(encounterType);
		encounter.setVoided(Boolean.valueOf(false));
		encounter.setCreator(Context.getAuthenticatedUser());
		encounter.setUuid(UUID.randomUUID().toString());
		encounter.setEncounterDatetime(new Date());
		return encounter;
	}

	private void sendPatientToOpdQueue(Encounter encounter) {
		Patient patient = encounter.getPatient();
		PatientQueueService queueService = Context.getService(PatientQueueService.class);
		Concept referralConcept = Context.getConceptService().getConcept(LAB_CONCEPT_ID);
		Encounter queueEncounter = queueService.getLastOPDEncounter(encounter.getPatient());
		OpdPatientQueueLog patientQueueLog = queueService.getOpdPatientQueueLogByEncounter(queueEncounter);
		if (patientQueueLog == null)
			return;
		Concept selectedOPDConcept = patientQueueLog.getOpdConcept();
		String selectedCategory = patientQueueLog.getCategory();
		String visitStatus = patientQueueLog.getVisitStatus();
		OpdPatientQueue patientInQueue = queueService.getOpdPatientQueue(patient
				.getPatientIdentifier().getIdentifier(), selectedOPDConcept.getConceptId());
		if (patientInQueue == null) {
			patientInQueue = new OpdPatientQueue();
			patientInQueue.setUser(Context.getAuthenticatedUser());
			patientInQueue.setPatient(patient);
			patientInQueue.setCreatedOn(new Date());
			patientInQueue.setBirthDate(patient.getBirthdate());
			patientInQueue.setPatientIdentifier(patient.getPatientIdentifier().getIdentifier());
			patientInQueue.setOpdConcept(selectedOPDConcept);
			patientInQueue.setTriageDataId(patientQueueLog.getTriageDataId());
			patientInQueue.setOpdConceptName(selectedOPDConcept.getName().getName());
			if (null != patient.getMiddleName()) {
				patientInQueue.setPatientName(patient.getGivenName() + " " + patient.getFamilyName() + " " + patient.getMiddleName());
			} else {
				patientInQueue.setPatientName(patient.getGivenName() + " " + patient.getFamilyName());
			}
			patientInQueue.setReferralConcept(referralConcept);
			patientInQueue.setSex(patient.getGender());
			patientInQueue.setCategory(selectedCategory);
			patientInQueue.setVisitStatus(visitStatus);
			patientInQueue.setClearedToNextServicePoint(1);
			queueService.saveOpdPatientQueue(patientInQueue);
		} else {
			patientInQueue.setReferralConcept(referralConcept);
			patientInQueue.setClearedToNextServicePoint(1);
			queueService.saveOpdPatientQueue(patientInQueue);
		}
	}

	private void addLaboratoryTestObservation(Encounter encounter, Concept testConcept, Concept testGroupConcept, String result, LabTest test, String comment) {
		this.log.warn("testConceptId=" + testConcept);
		this.log.warn("testGroupConceptId=" + testGroupConcept);
		System.out.println("Got into the adding lab test observation with testConceptId as>>" + testConcept + " and testGroupConceptId as >>" + testGroupConcept);
		Obs obs = getObs(encounter, testConcept, testGroupConcept);
		setObsAttributes(obs, encounter);
		obs.setConcept(testConcept);
		obs.setOrder(test.getOrder());
		if (testConcept.getDatatype().getName().equalsIgnoreCase("Text")) {
			obs.setValueText(result);
		} else if (testConcept.getDatatype().getName().equalsIgnoreCase("Numeric")) {
			if (StringUtils.isNotBlank(result))
				obs.setValueNumeric(Double.valueOf(Double.parseDouble(result)));
			    obs.setComment(comment);
		} else if (testConcept.getDatatype().getName().equalsIgnoreCase("Coded")) {
			Concept answerConcept = LaboratoryUtil.searchConcept(result);
			obs.setValueCoded(answerConcept);
			obs.setComment(comment);
		}
		if (testGroupConcept != null) {
			Obs testGroupObs = getObs(encounter, testGroupConcept, null);
			if (testGroupObs.getConcept() == null) {
				testGroupObs.setConcept(testGroupConcept);
				testGroupObs.setOrder(test.getOrder());
				setObsAttributes(testGroupObs, encounter);
				encounter.addObs(testGroupObs);
			}
			this.log.warn("Adding obs[concept=" + obs.getConcept() + ",uuid=" + obs.getUuid() + "] to obsgroup[concept=" + testGroupObs.getConcept() + ", uuid=" + testGroupObs.getUuid() + "]");
			testGroupObs.addGroupMember(obs);
		} else {
			encounter.addObs(obs);
		}
		this.log.warn("Obs size is: " + encounter.getObs().size());
	}

	private void setObsAttributes(Obs obs, Encounter encounter) {
		obs.setObsDatetime(encounter.getEncounterDatetime());
		obs.setPerson((Person) encounter.getPatient());
		obs.setLocation(encounter.getLocation());
		obs.setEncounter(encounter);
	}

	private Obs getObs(Encounter encounter, Concept concept, Concept groupingConcept) {
		for (Obs obs : encounter.getAllObs()) {
			if (groupingConcept != null) {
				Obs obsGroup = getObs(encounter, groupingConcept, null);
				if (obsGroup.getGroupMembers() != null)
					for (Obs member : obsGroup.getGroupMembers()) {
						if (member.getConcept().equals(concept))
							return member;
					}
				continue;
			}
			if (obs.getConcept().equals(concept))
				return obs;
		}
		return new Obs();
	}
}
