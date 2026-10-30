package io.github.simonxwei.template.data.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public final class FabricENUSLanguageProvider extends FabricLanguageProvider {

    public FabricENUSLanguageProvider(final FabricPackOutput packOutput, final CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, TemplateLanguage.Codes.EN_US, registryLookup);
    }

    @Override
    public void generateTranslations(final HolderLookup.Provider registryLookup, final TranslationBuilder translationBuilder) {
        TemplateENUSLanguageProvider.add(translationBuilder::add);
    }
}
