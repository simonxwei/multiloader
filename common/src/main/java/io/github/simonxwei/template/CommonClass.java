package io.github.simonxwei.template;

import io.github.simonxwei.template.platform.Services;

public final class CommonClass {

    private CommonClass() {}

    public static void init() {
        Constants.LOGGER.info(
                "Hello from common on {} in a {} environment",
                Services.PLATFORM.getPlatformName(),
                Services.PLATFORM.getEnvironmentName()
        );

        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Constants.LOGGER.info("{} is loaded", Constants.MOD_NAME);
        }
    }
}
