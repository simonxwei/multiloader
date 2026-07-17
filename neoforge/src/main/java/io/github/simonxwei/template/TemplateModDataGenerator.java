package io.github.simonxwei.template;

import io.github.simonxwei.template.data.lang.TemplateENUSLanguageProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = TemplateConstants.MOD_ID)
public final class TemplateModDataGenerator {

    private TemplateModDataGenerator() {}

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent.Client event) {
        event.createProvider(TemplateENUSLanguageProvider::new);
    }
}
