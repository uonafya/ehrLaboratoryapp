package org.openmrs.module.laboratoryapp.page.controller;

import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.laboratoryapp.LaboratoryConstants;
import org.openmrs.module.laboratoryapp.ReferenceApplicationWebConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;

import java.util.Date;

/**
 * Created by HealthIT
 */
@AppPage(LaboratoryConstants.APP_LABORATORY_APP)
public class MainPageController {
    public String get( UiSessionContext sessionContext,
                    PageModel model,
                    PageRequest pageRequest,
                    UiUtils ui) {
        model.addAttribute("date", new Date());
        return null;
    }
}
