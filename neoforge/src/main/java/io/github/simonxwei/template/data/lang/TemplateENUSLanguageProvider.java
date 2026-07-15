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
        // neoforge
        this.add("template.configuration.title", "Example Mod Configs");
        this.add("template.configuration.section.template.common.toml", "Example Mod Configs");
        this.add("template.configuration.section.template.common.toml.title", "Example Mod Configs");
    }
}
