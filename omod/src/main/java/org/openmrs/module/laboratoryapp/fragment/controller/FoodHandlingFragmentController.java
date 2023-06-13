package org.openmrs.module.laboratoryapp.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.LabService;
import org.openmrs.module.hospitalcore.model.FoodHandling;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

public class FoodHandlingFragmentController {

    public void controller() {

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
}
