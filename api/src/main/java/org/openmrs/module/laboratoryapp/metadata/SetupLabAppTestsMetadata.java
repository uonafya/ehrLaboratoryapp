package org.openmrs.module.laboratoryapp.metadata;

import org.openmrs.Concept;
import org.openmrs.Role;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ehrlaboratory.LaboratoryService;
import org.openmrs.module.hospitalcore.model.Lab;
import org.openmrs.module.laboratoryapp.LaboratoryConstants;

import java.util.HashSet;
import java.util.Set;

public class SetupLabAppTestsMetadata {


    public static void getAllInvestigationsSaved() {
        LaboratoryService laboratoryService = Context.getService(LaboratoryService.class);
        ConceptService conceptService = Context.getConceptService();
        Set<Concept> investigationsToDisplay = new HashSet<Concept>();
        Set<Concept> confidentialInvestigationsToDisplay = new HashSet<Concept>();
        for(String str : LaboratoryConstants.allInvestigations()) {
            Concept concept = conceptService.getConcept(str);
            if(concept != null) {
                investigationsToDisplay.add(concept);
            }
        }

        for(String str : LaboratoryConstants.allConfidentialInvestigations()) {
            Concept concept1 = conceptService.getConcept(str);
            if(concept1 != null) {
                confidentialInvestigationsToDisplay.add(concept1);
            }
        }
        //for a lab object and commit to the database

        Lab lab = new Lab();
        lab.setName("GENERAL LABORATORY");
        lab.setDescription("General Laboratory");
        lab.setRole(new Role("Access Laboratory Module"));
        if(investigationsToDisplay.size() > 0) {
            lab.setInvestigationsToDisplay(investigationsToDisplay);
        }
        if(confidentialInvestigationsToDisplay.size() > 0) {
            lab.setInvestigationsToDisplay(confidentialInvestigationsToDisplay);
        }
        //delete an already existing record
        if(laboratoryService.getCurrentDepartment() != null) {
            laboratoryService.deleteLaboratoryDepartment(laboratoryService.getCurrentDepartment());
        }
        //save the lab order
        laboratoryService.saveLaboratoryDepartment(lab);
    }
}
