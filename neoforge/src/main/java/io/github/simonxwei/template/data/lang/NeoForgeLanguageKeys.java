package io.github.simonxwei.template.data.lang;

import io.github.simonxwei.template.TemplateConstants;

public final class NeoForgeLanguageKeys {

    // See https://neoforged.net/mod-generator/
    private static final String CONFIGURATION;
    public static final String CONFIGURATION_TITLE;
    public static final String CONFIGURATION_SECTION;
    public static final String CONFIGURATION_SECTION_TITLE;

    private NeoForgeLanguageKeys() {}

    static {
        CONFIGURATION = TemplateConstants.MOD_ID + "." + "configuration";
        CONFIGURATION_TITLE = CONFIGURATION + "." + "title";
        CONFIGURATION_SECTION = CONFIGURATION + "." + "section" + "." + TemplateConstants.MOD_ID + "." + "local.toml";
        CONFIGURATION_SECTION_TITLE = CONFIGURATION_SECTION + "." + "title";
    }
}
