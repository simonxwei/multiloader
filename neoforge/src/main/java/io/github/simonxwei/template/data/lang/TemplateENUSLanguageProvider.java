package io.github.simonxwei.template.data.lang;

import io.github.simonxwei.template.TemplateConstants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class TemplateENUSLanguageProvider extends LanguageProvider {

    // neoforge configuration
    private static final String TITLE = TemplateConstants.MOD_NAME + " Configs";
    private static final String CONFIGURATION = TemplateConstants.MOD_ID + ".configuration";
    private static final String SECTION = CONFIGURATION + ".section." + TemplateConstants.MOD_ID + ".common.toml";

    public TemplateENUSLanguageProvider(final PackOutput output) {
        super(output, TemplateConstants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // neoforge configuration
        this.add(CONFIGURATION + ".title", TITLE);
        this.add(SECTION, TITLE);
        this.add(SECTION + ".title", TITLE);
    }
}
