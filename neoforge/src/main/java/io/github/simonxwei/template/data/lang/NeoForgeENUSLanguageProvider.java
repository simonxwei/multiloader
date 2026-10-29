package io.github.simonxwei.template.data.lang;

import io.github.simonxwei.template.TemplateConstants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class NeoForgeENUSLanguageProvider extends LanguageProvider {

    // See https://neoforged.net/mod-generator/
    private static final String TITLE = TemplateConstants.MOD_NAME + " " + "Configs";

    public NeoForgeENUSLanguageProvider(final PackOutput output) {
        super(output, TemplateConstants.MOD_ID, TemplateLanguageKeys.EN_US);
    }

    @Override
    protected void addTranslations() {
        TemplateENUSLanguageProvider.add(this::add);
        this.add(NeoForgeLanguageKeys.CONFIGURATION_TITLE, TITLE);
        this.add(NeoForgeLanguageKeys.CONFIGURATION_SECTION, TITLE);
        this.add(NeoForgeLanguageKeys.CONFIGURATION_SECTION_TITLE, TITLE);
    }
}
