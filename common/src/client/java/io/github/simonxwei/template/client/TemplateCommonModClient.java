package io.github.simonxwei.template.client;

import io.github.simonxwei.template.TemplateConstants;
import io.github.simonxwei.template.platform.TemplateServices;
import net.minecraft.client.Minecraft;

public final class TemplateCommonModClient {

    private TemplateCommonModClient() {}

    public static void init() {
        TemplateConstants.LOGGER.info(
                "Initializing {} client on {} ({})",
                TemplateConstants.MOD_NAME,
                TemplateServices.PLATFORM.getPlatformName(),
                TemplateServices.PLATFORM.getEnvironmentName()
        );

        TemplateConstants.LOGGER.debug(
                "Shared client links against {}", Minecraft.class.getName()
        );
    }
}
