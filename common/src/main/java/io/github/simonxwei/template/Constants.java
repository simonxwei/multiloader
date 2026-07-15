package io.github.simonxwei.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Constants {

    public static final String MOD_ID = "template";
    public static final String MOD_NAME = "Template";
    public static final Logger LOGGER;

    private Constants() {}

    static {
        LOGGER = LoggerFactory.getLogger(MOD_NAME);
    }
}
