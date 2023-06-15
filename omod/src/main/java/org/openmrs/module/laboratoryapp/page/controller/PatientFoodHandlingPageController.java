package org.openmrs.module.laboratoryapp.page.controller;

import org.openmrs.Patient;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.laboratoryapp.LaboratoryConstants;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

@AppPage(LaboratoryConstants.APP_LABORATORY_APP)
public class PatientFoodHandlingPageController {

    public void controller(PageModel model, @RequestParam("patientId") Patient currentPatient) {
        model.addAttribute("currentPatient", currentPatient);
    }
}
