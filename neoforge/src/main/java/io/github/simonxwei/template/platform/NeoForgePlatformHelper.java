package io.github.simonxwei.template.platform;

import io.github.simonxwei.template.platform.services.TemplatePlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public final class NeoForgePlatformHelper implements TemplatePlatformHelper {

    public NeoForgePlatformHelper() {}

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(final String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.getCurrent().isProduction();
    }
}
