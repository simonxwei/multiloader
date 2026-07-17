package io.github.simonxwei.template.platform;

import io.github.simonxwei.template.platform.services.TemplatePlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricPlatformHelper implements TemplatePlatformHelper {

    public FabricPlatformHelper() {}

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(final String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
