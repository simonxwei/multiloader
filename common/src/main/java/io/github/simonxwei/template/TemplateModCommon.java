package io.github.simonxwei.template;

import io.github.simonxwei.template.platform.TemplateServices;

/**
 * @see net.minecraft.server.Bootstrap
 * @see net.minecraft.core.registries.BuiltInRegistries
 */
public final class TemplateModCommon {

    private TemplateModCommon() {}

    public static void init() {
        TemplateConstants.LOGGER.info(
                "TemplateModCommon#init(): initializing '{}' on '{}' ('{}')",
                TemplateConstants.MOD_NAME,
                TemplateServices.PLATFORM.template$getPlatformName(),
                TemplateServices.PLATFORM.template$getEnvironmentName()
        );
    }
}
