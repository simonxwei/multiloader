package io.github.simonxwei.template;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TemplateConstants {

    public static final String MOD_ID = "template";
    public static final String MOD_NAME = "Template";
    public static final Logger LOGGER;

    private TemplateConstants() {}

    public static Identifier id(final String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    static {
        LOGGER = LoggerFactory.getLogger(MOD_NAME);
    }
}
