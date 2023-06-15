package org.openmrs.module.laboratoryapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.LabService;
import org.openmrs.module.hospitalcore.model.FoodHandling;
import org.openmrs.module.laboratoryapp.model.FoodHandlerResultsSimplifier;
import org.openmrs.module.laboratoryapp.utils.LabUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FoodHandlerPrintOutFragmentController {

    public void controller(FragmentModel model, @FragmentParam("patient") Patient currentPatient) {
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
        List<Obs> foundObsList = new ArrayList<Obs>();
        if(!labObsList.isEmpty()) {
            for(Obs obs:labObsList) {
                if(!foodHandlingConcepts.isEmpty() && foodHandlingConcepts.contains(obs.getConcept())) {
                    foundObsList.add(obs);
                }
            }
        }
        List<FoodHandlerResultsSimplifier> foodHandlerResultsSimplifierList = new ArrayList<FoodHandlerResultsSimplifier>();
        FoodHandlerResultsSimplifier foodHandlerResultsSimplifier = null;
        if(!foundObsList.isEmpty()) {
           for(Obs obs : foundObsList) {
               foodHandlerResultsSimplifier = new FoodHandlerResultsSimplifier();
               foodHandlerResultsSimplifier.setTestName(obs.getConcept().getDisplayString());
               foodHandlerResultsSimplifier.setResults(processObs(obs));
               foodHandlerResultsSimplifier.setDescription(obs.getComment());
               foodHandlerResultsSimplifier.setDatePerformed(LabUtils.formatDateTime(obs.getObsDatetime()));
               foodHandlerResultsSimplifierList.add(foodHandlerResultsSimplifier);
           }
        }
        model.addAttribute("testsDone", foodHandlerResultsSimplifierList);
        model.addAttribute("user", Context.getAuthenticatedUser().getGivenName()+" "+Context.getAuthenticatedUser().getFamilyName());
        model.addAttribute("today", LabUtils.formatDateTime(new Date()));
    }
    private String processObs(Obs obs) {
        String results = "";
        if(obs.getValueCoded() != null){
            results = obs.getValueCoded().getDisplayString();
        }
        else if(obs.getValueText() != null) {
            results = obs.getValueText();
        }
        else if(obs.getValueNumeric() != null) {
            results = String.valueOf(obs.getValueNumeric());
            if(Context.getConceptService().getConceptNumeric(obs.getConcept().getConceptId()) != null && StringUtils.isNotBlank(Context.getConceptService().getConceptNumeric(obs.getConcept().getConceptId()).getUnits())){
                results = results + Context.getConceptService().getConceptNumeric(obs.getConcept().getConceptId()).getUnits();
            }
        }
        else {
            //do something else
        }
        return results;
    }
}
