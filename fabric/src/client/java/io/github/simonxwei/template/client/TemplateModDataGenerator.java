package io.github.simonxwei.template.client;

import io.github.simonxwei.template.TemplateConstants;
import io.github.simonxwei.template.data.lang.FabricENUSLanguageProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/**
 * @see net.minecraft.data.Main
 * @see net.minecraft.client.data.Main
 */
@Environment(EnvType.CLIENT)
public final class TemplateModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public String getEffectiveModId() {
        return TemplateConstants.MOD_ID;
    }

    @Override
    public void onInitializeDataGenerator(final FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(FabricENUSLanguageProvider::new);
    }
}
