package io.github.simonxwei.template;

import net.fabricmc.api.ModInitializer;

/**
 * @see net.minecraft.server.Bootstrap
 * @see net.minecraft.core.registries.BuiltInRegistries
 */
public final class TemplateMod implements ModInitializer {

    @Override
    public void onInitialize() {
        TemplateModCommon.init();
    }
}
