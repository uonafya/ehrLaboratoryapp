package org.openmrs.module.laboratoryapp.metadata;

import org.springframework.stereotype.Component;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.metadatadeploy.bundle.Requires;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.idSet;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.privilege;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.role;

/**
 * Implementation of access control to the app.
 */
@Component
@Requires(org.openmrs.module.kenyaemr.metadata.SecurityMetadata.class)
public class LaboratoryMetadata extends AbstractMetadataBundle {

    public static class _Privilege {

        public static final String APP_LAB_MODULE_APP_PRIV = "Access Laboratory";

        public static final String APP_LABORATORY_APP = "App: laboratoryapp";
    }

    public static final class _Role {

        public static final String APPLICATION_LAB_MODULE = "Access Laboratory Module";
    }

    /**
     * @see AbstractMetadataBundle#install()
     */
    @Override
    public void install() {
        install(privilege(_Privilege.APP_LAB_MODULE_APP_PRIV, "Able to access Key Laboratory  module features"));
        install(privilege(_Privilege.APP_LABORATORY_APP, "Able to access the laboratory app"));
        install(role(_Role.APPLICATION_LAB_MODULE, "Can access lab module Application",
                idSet(org.openmrs.module.kenyaemr.metadata.SecurityMetadata._Role.API_PRIVILEGES_VIEW_AND_EDIT),
                idSet(_Privilege.APP_LAB_MODULE_APP_PRIV)));
    }
}
