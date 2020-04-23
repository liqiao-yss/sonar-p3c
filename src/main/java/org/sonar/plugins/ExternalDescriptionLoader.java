package org.sonar.plugins;

import org.sonar.api.server.rule.RulesDefinition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Reads the corresponding classpath resource to add HTML descriptions to a given rule.
 * Taken from <code>sslr-squid-bridge:org.sonar.squidbridge.rules.ExternalDescriptionLoader</code>.
 */
public class ExternalDescriptionLoader {

    private final String resourceBasePath;

    public ExternalDescriptionLoader(String resourceBasePath) {
        this.resourceBasePath = resourceBasePath;
    }

    public static void loadHtmlDescriptions(RulesDefinition.NewRepository repository, String languageKey) {
        ExternalDescriptionLoader loader = new ExternalDescriptionLoader(languageKey);
        for (RulesDefinition.NewRule newRule : repository.rules()) {
            loader.addHtmlDescription(newRule);
        }
    }

    public void addHtmlDescription(RulesDefinition.NewRule rule) {
        URL resource = ExternalDescriptionLoader.class.getResource(resourceBasePath + "/" + rule.key() + ".html");
        if (resource != null) {
            addHtmlDescription(rule, resource);
        }
    }

    public void addHtmlDescription(RulesDefinition.NewRule rule, URL resource) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))) {
            rule.setHtmlDescription(reader.lines().collect(Collectors.joining("\r\n")));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read: " + resource, e);
        }
    }

}