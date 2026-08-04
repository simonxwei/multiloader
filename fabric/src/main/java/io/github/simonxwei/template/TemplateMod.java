package io.github.simonxwei.template;

import net.fabricmc.api.ModInitializer;

public final class TemplateMod implements ModInitializer {

    @Override
    public void onInitialize() {
        TemplateConstants.LOGGER.info("Hello Fabric world");
        TemplateModCommon.init();
    }
}
