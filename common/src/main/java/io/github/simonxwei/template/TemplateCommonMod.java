package io.github.simonxwei.template;

import io.github.simonxwei.template.platform.TemplateServices;

public final class TemplateCommonMod {

    private TemplateCommonMod() {}

    public static void init() {
        TemplateConstants.LOGGER.info(
                "Initializing {} on {} ({})",
                TemplateConstants.MOD_NAME,
                TemplateServices.PLATFORM.getPlatformName(),
                TemplateServices.PLATFORM.getEnvironmentName()
        );
    }
}
