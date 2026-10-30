package io.github.simonxwei.template.platform.services;

public interface TemplatePlatformHelper {

    String template$getPlatformName();

    boolean template$isModLoaded(final String modId);

    boolean template$isDevelopmentEnvironment();

    default String template$getEnvironmentName() {
        return this.template$isDevelopmentEnvironment() ? "development" : "production";
    }
}
