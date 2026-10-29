package io.github.simonxwei.template.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * @see net.minecraft.client.ClientBootstrap
 */
@Environment(EnvType.CLIENT)
public final class TemplateModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TemplateModCommonClient.init();
    }
}
