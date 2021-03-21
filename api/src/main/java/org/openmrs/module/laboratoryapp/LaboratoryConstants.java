/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.laboratoryapp;

import java.util.Arrays;
import java.util.List;

public class LaboratoryConstants {

    /**
     * Module ID
     */
    public static final String MODULE_ID = "laboratoryapp";

    public static final String APP_LABORATORY_APP = MODULE_ID + ".ehrlaboratory";

    //Investigations
    public static String SEROLOGY = "SEROLOGY";
    public static String BIOCHEMISTRY = "BIOCHEMISTRY";

    //confidential investigations
    public static String TEST = "SEROLOGY";
    public static String TEST1 = "BIOCHEMISTRY";


    public static List<String> allInvestigations() {
        return Arrays.asList(SEROLOGY, BIOCHEMISTRY);
    }

    public static List<String> allConfidentialInvestigations() {
        return Arrays.asList(TEST, TEST1);
    }
}
