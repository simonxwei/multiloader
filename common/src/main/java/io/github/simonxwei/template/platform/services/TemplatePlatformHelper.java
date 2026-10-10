package io.github.simonxwei.template.platform.services;

public interface TemplatePlatformHelper {

    String getPlatformName();

    boolean isModLoaded(final String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return this.isDevelopmentEnvironment() ? "development" : "production";
    }
}
