package io.github.simonxwei.template;

import io.github.simonxwei.template.client.TemplateModClientCommon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = TemplateConstants.MOD_ID, dist = Dist.CLIENT)
public final class TemplateModClient {

    public TemplateModClient(final ModContainer container) {
        TemplateModClientCommon.init();
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
