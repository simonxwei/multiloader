package io.github.simonxwei.template;

import io.github.simonxwei.template.platform.TemplateServices;

public final class TemplateModCommon {

    private TemplateModCommon() {}

    public static void init() {
        TemplateConstants.LOGGER.info(
                "Hello from common on {} in a {} environment",
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
