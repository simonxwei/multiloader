package io.github.simonxwei.template;

import io.github.simonxwei.template.config.TemplateConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MOD_ID)
public final class TemplateMod {

    public TemplateMod(final IEventBus modEventBus, final ModContainer modContainer) {
        Constants.LOGGER.info("Hello NeoForge world");
        CommonClass.init();
        modContainer.registerConfig(ModConfig.Type.COMMON, TemplateConfig.SPEC);
    }
}
