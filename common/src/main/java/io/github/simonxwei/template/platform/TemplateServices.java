package io.github.simonxwei.template.platform;

import io.github.simonxwei.template.TemplateConstants;
import io.github.simonxwei.template.platform.services.TemplatePlatformHelper;

import java.util.ServiceLoader;

// Java services provide loader-specific implementations to common code without
// introducing Fabric or NeoForge APIs into the common project.
public final class TemplateServices {

    public static final TemplatePlatformHelper PLATFORM;

    private TemplateServices() {}

    public static <T> T load(final Class<T> serviceClass) {
        final T loadedService = ServiceLoader
                .load(serviceClass, TemplateServices.class.getClassLoader())
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Failed to load service for " + serviceClass.getName()
                        )
                );
        TemplateConstants.LOGGER.debug(
                "Loaded {} for service {}", loadedService, serviceClass
        );
        return loadedService;
    }

    static {
        PLATFORM = load(TemplatePlatformHelper.class);
    }
}
