package io.github.simonxwei.template.platform;

import io.github.simonxwei.template.Constants;
import io.github.simonxwei.template.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

// Java services provide loader-specific implementations to common code without
// introducing Fabric or NeoForge APIs into the common project.
public final class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    private Services() {}

    public static <T> T load(Class<T> serviceClass) {
        final T loadedService = ServiceLoader.load(serviceClass, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to load service for " + serviceClass.getName()));
        Constants.LOGGER.debug("Loaded {} for service {}", loadedService, serviceClass);
        return loadedService;
    }
}
