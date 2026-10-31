package io.github.simonxwei.template;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TemplateConstants {

    // Match `mod_id` and `mod_name` in `gradle.properties`.
    public static final String MOD_ID = "template";
    public static final String MOD_NAME = "Template";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    private TemplateConstants() {}

    public static Identifier id(final String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }
}
