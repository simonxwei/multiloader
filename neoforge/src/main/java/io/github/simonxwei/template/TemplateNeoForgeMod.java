package io.github.simonxwei.template;

import io.github.simonxwei.template.config.TemplateConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(TemplateConstants.MOD_ID)
public final class TemplateNeoForgeMod {

    public TemplateNeoForgeMod(final IEventBus modEventBus, final ModContainer modContainer) {
        TemplateCommonMod.init();
        modContainer.registerConfig(ModConfig.Type.LOCAL, TemplateConfig.SPEC);
    }
}
