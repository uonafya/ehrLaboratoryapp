package org.openmrs.module.laboratoryapp.page.controller;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.LabService;
import org.openmrs.module.hospitalcore.model.FoodHandling;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.laboratoryapp.LaboratoryConstants;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AppPage(LaboratoryConstants.APP_LABORATORY_APP)
public class PatientFoodHandlingPageController {

    public void controller(PageModel model, @RequestParam("patientId") Patient currentPatient) {
        model.addAttribute("currentPatient", currentPatient);

        LabService labService = Context.getService(LabService.class);

        List<FoodHandling> foodHandlingList = new ArrayList<FoodHandling>(labService.getAllFoodHandlerProfiles());
        List<Concept> foodHandlingConcepts = new ArrayList<Concept>();
        for(FoodHandling foodHandling: foodHandlingList) {
            if(foodHandling != null && foodHandling.getConceptId() != null) {
                foodHandlingConcepts.add(Context.getConceptService().getConcept(foodHandling.getConceptId()));
            }
        }
        EncounterType labEncounterType = Context.getEncounterService().getEncounterTypeByUuid("11d3f37a-f282-11ea-a825-1b5b1ff1b854");
        List<Obs> labObsList = new ArrayList<Obs>();
        List<Encounter> patientLabEncounter = Context.getEncounterService().getEncounters(currentPatient, null, null, null, null, Arrays.asList(labEncounterType), null, null, null,false);
        for(Encounter encounter:patientLabEncounter){
            labObsList.addAll(encounter.getAllObs());
        }
        //loop through to get the concepts into a list
        List<Concept> labObsConceptsList = new ArrayList<Concept>();
        if(!labObsList.isEmpty()) {
            for(Obs obs:labObsList) {
                labObsConceptsList.add(obs.getConcept());
            }
        }
        boolean qualified = false;
        if(!foodHandlingConcepts.isEmpty() && !labObsConceptsList.isEmpty() && foodHandlingConcepts.retainAll(labObsConceptsList)) {
            qualified = true;
        }
        model.addAttribute("qualified", qualified);
    }
}
