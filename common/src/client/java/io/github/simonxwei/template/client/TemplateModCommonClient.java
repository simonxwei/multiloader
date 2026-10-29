package io.github.simonxwei.template.client;

import io.github.simonxwei.template.TemplateConstants;
import io.github.simonxwei.template.platform.TemplateServices;
import net.minecraft.client.Minecraft;

/**
 * @see net.minecraft.client.ClientBootstrap
 */
public final class TemplateModCommonClient {

    private TemplateModCommonClient() {}

    public static void init() {
        TemplateConstants.LOGGER.info(
                "TemplateModCommonClient#init(): initializing '{}' client on '{}' ('{}')",
                TemplateConstants.MOD_NAME,
                TemplateServices.PLATFORM.getPlatformName(),
                TemplateServices.PLATFORM.getEnvironmentName()
        );

        TemplateConstants.LOGGER.debug(
                "TemplateModCommonClient#init(): shared client links against '{}'", Minecraft.class.getName()
        );
    }
}
