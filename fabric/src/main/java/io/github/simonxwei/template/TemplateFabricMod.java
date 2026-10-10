package io.github.simonxwei.template;

import net.fabricmc.api.ModInitializer;

public final class TemplateFabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        TemplateCommonMod.init();
    }
}
