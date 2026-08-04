package io.github.simonxwei.template.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@Environment(EnvType.CLIENT)
public final class TemplateModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(final FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        // Add Fabric data generator providers here.
    }
}
