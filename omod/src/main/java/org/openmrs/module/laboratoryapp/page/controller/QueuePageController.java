package org.openmrs.module.laboratoryapp.page.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.Lab;
import org.openmrs.module.ehrlaboratory.LaboratoryService;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.laboratoryapp.LaboratoryConstants;
import org.openmrs.module.laboratoryapp.ReferenceApplicationWebConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.ui.framework.page.PageRequest;


@AppPage(LaboratoryConstants.APP_LABORATORY_APP)
public class QueuePageController {

	public String get(UiSessionContext sessionContext,
					PageModel model,
					PageRequest pageRequest,
					UiUtils ui) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dateStr = sdf.format(new Date());
		model.addAttribute("currentDate", dateStr);
		LaboratoryService ls = (LaboratoryService) Context.getService(LaboratoryService.class);
		Lab department = ls.getCurrentDepartment();
		Set<Concept> investigations = new HashSet<Concept>();
		if(department!=null){
			investigations = department.getInvestigationsToDisplay();
		}
		model.addAttribute("investigations", investigations);
		return null;
	}

}
