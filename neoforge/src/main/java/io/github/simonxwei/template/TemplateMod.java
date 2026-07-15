package io.github.simonxwei.template;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class TemplateMod {

    public TemplateMod(final IEventBus modEventBus, final ModContainer modContainer) {
        Constants.LOGGER.info("Hello NeoForge world");
        CommonClass.init();
    }
}
