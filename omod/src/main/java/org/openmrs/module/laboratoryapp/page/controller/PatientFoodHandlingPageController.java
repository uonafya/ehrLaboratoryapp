package org.openmrs.module.laboratoryapp.page.controller;

import org.openmrs.Patient;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class PatientFoodHandlingPageController {

    public void controller(PageModel model, @RequestParam("patientId") Patient currentPatient) {
        model.addAttribute("currentPatient", currentPatient);
    }
}
