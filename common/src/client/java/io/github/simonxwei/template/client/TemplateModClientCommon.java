package io.github.simonxwei.template.client;

import io.github.simonxwei.template.TemplateConstants;
import io.github.simonxwei.template.platform.TemplateServices;

public final class TemplateModClientCommon {

    private TemplateModClientCommon() {}

    public static void init() {
        TemplateConstants.LOGGER.info(
                "Hello from common client on {} in a {} environment",
                TemplateServices.PLATFORM.getPlatformName(),
                TemplateServices.PLATFORM.getEnvironmentName()
        );

        if (TemplateServices.PLATFORM.isModLoaded(TemplateConstants.MOD_ID)) {
            TemplateConstants.LOGGER.info(
                    "{} is loaded", TemplateConstants.MOD_NAME
            );
        }
    }
}
