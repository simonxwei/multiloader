package io.github.simonxwei.template.data.lang;

import io.github.simonxwei.template.Constants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class TemplateENUSLanguageProvider extends LanguageProvider {

    public TemplateENUSLanguageProvider(final PackOutput output) {
        super(output, Constants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // neoforge configuration
        this.add("template.configuration.title", "Template Configs");
        this.add("template.configuration.section.template.common.toml", "Template Configs");
        this.add("template.configuration.section.template.common.toml.title", "Template Configs");
    }
}
