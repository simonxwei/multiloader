package io.github.simonxwei.template.data.lang;

@FunctionalInterface
public interface TemplateLanguageProvider {

    void template$add(final String key, final String value);
}
