package io.github.simonxwei.template.platform;

import io.github.simonxwei.template.TemplateConstants;
import io.github.simonxwei.template.platform.services.TemplatePlatformHelper;

import java.util.ServiceLoader;

// Load platform implementations without loader APIs in common code.
public final class TemplateServices {

    public static final TemplatePlatformHelper PLATFORM;

    private TemplateServices() {}

    public static <T> T load(final Class<T> serviceClass) {
        final T loadedService = ServiceLoader
                .load(serviceClass, TemplateServices.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("serviceClass: cannot load service, type='" + serviceClass.getName() + "'"));
        TemplateConstants.LOGGER.debug("TemplateServices#load(Class): loaded '{}' for service '{}'", loadedService, serviceClass);
        return loadedService;
    }

    static {
        PLATFORM = load(TemplatePlatformHelper.class);
    }
}
