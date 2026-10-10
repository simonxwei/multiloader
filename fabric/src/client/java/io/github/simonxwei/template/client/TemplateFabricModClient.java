package io.github.simonxwei.template.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class TemplateFabricModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TemplateCommonModClient.init();
    }
}
