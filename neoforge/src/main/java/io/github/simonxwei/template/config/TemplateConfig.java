package io.github.simonxwei.template.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class TemplateConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue EXAMPLE_OPTION = BUILDER
            .comment("An example configuration option")
            .define("exampleOption", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private TemplateConfig() {}
}
