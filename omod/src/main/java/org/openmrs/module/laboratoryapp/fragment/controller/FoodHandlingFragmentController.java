package org.openmrs.module.laboratoryapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.LabService;
import org.openmrs.module.hospitalcore.model.FoodHandlerSimplifier;
import org.openmrs.module.hospitalcore.model.FoodHandling;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FoodHandlingFragmentController {

    public void controller(FragmentModel model) {
        LabService labService = Context.getService(LabService.class);

        List<FoodHandling> getFoodHandlerList = new ArrayList<FoodHandling>(labService.getAllFoodHandlerProfiles());
        List<FoodHandlerSimplifier> simplifierList =  new ArrayList<FoodHandlerSimplifier>();
        FoodHandlerSimplifier foodHandlerSimplifier = null;

        for(FoodHandling foodHandling : getFoodHandlerList) {
            foodHandlerSimplifier = new FoodHandlerSimplifier();
            foodHandlerSimplifier.setTestName(foodHandling.getName());
            foodHandlerSimplifier.setConceptReference(Context.getConceptService().getConcept(foodHandling.getConceptId()).getDisplayString());
            foodHandlerSimplifier.setDescription(foodHandling.getDescription());
            foodHandlerSimplifier.setCreator(Context.getAuthenticatedUser().getGivenName()+" "+Context.getAuthenticatedUser().getFamilyName());
            foodHandlerSimplifier.setDateCreated(String.valueOf(foodHandling.getCreatedDate()));
            simplifierList.add(foodHandlerSimplifier);
        }
        model.addAttribute("list", simplifierList);
    }

    public void addFoodHandlerTest(
            @RequestParam(value = "testName", required = false) String testName,
            @RequestParam(value = "conceptReference", required = false) String conceptReference,
            @RequestParam(value = "testDescription", required = false) String testDescription
                                   ) {
            LabService labService = Context.getService(LabService.class);
            FoodHandling foodHandling = null;
            if(StringUtils.isNotBlank(testName) && StringUtils.isNotBlank(conceptReference)) {
                foodHandling = new FoodHandling();
                foodHandling.setName(testName);
                foodHandling.setConceptId(Context.getConceptService().getConceptByName(conceptReference).getConceptId());
                if(StringUtils.isNotBlank(testDescription)) {
                    foodHandling.setDescription(testDescription);
                }
                foodHandling.setCreator(Context.getAuthenticatedUser());
                foodHandling.setCreatedDate(new Date());

                //save the object
                labService.saveFoodHandlerProfile(foodHandling);

            }
    }
    public List<SimpleObject> getFoodHandlerTests(UiUtils uiUtils) {
        LabService labService = Context.getService(LabService.class);

        List<FoodHandling> getFoodHandlerList = new ArrayList<FoodHandling>(labService.getAllFoodHandlerProfiles());
        List<FoodHandlerSimplifier> simplifierList =  new ArrayList<FoodHandlerSimplifier>();
        FoodHandlerSimplifier foodHandlerSimplifier = null;

        for(FoodHandling foodHandling : getFoodHandlerList) {
            foodHandlerSimplifier = new FoodHandlerSimplifier();
            foodHandlerSimplifier.setTestName(foodHandling.getName());
            foodHandlerSimplifier.setConceptReference(Context.getConceptService().getConcept(foodHandling.getConceptId()).getDisplayString());
            foodHandlerSimplifier.setDescription(foodHandling.getDescription());
            foodHandlerSimplifier.setCreator(Context.getAuthenticatedUser().getGivenName()+" "+Context.getAuthenticatedUser().getFamilyName());
            foodHandlerSimplifier.setDateCreated(String.valueOf(foodHandling.getCreatedDate()));
            simplifierList.add(foodHandlerSimplifier);
        }

        return SimpleObject.fromCollection(simplifierList, uiUtils, "testName", "conceptReference", "description", "creator", "dateCreated");
    }

    public List<SimpleObject> getTestsConcepts(@RequestParam(value="query") String conceptTerm, UiUtils ui)
    {
        List<Concept> conceptsList = Context.getConceptService().getConceptsByName(conceptTerm, Locale.getDefault(), false);
        List<Concept> conceptsListBasedOnTestClass = Context.getConceptService().getConceptsByClass(Context.getConceptService().getConceptClassByName("Test"));
        conceptsList.retainAll(conceptsListBasedOnTestClass);
        return SimpleObject.fromCollection(conceptsList, ui, "conceptId", "names.name", "uuid", "displayString");
    }
}
