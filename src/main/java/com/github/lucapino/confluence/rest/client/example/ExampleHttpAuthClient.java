package com.github.lucapino.confluence.rest.client.example;

import static com.github.lucapino.confluence.rest.core.impl.HttpAuthProperties.BASE_URL;
import static com.github.lucapino.confluence.rest.core.impl.HttpAuthProperties.PASSWORD;
import static com.github.lucapino.confluence.rest.core.impl.HttpAuthProperties.USER;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.lucapino.confluence.rest.core.impl.APIAuthConfig;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;
import com.github.lucapino.confluence.rest.core.impl.HttpAuthRequestService;

public class ExampleHttpAuthClient {

    public static void main(String[] args) {
        new ExampleHttpAuthClient().run();
    }

    private void run() {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(100);

            APIAuthConfig conf = loadAuthConfig();
            HttpAuthRequestService requestService = new HttpAuthRequestService();
            requestService.connect(new URI(conf.getBaseUrl()), conf.getUser(), conf.getPassword());

            APIUriProvider uriProvider = new APIUriProvider(new URI(conf.getBaseUrl() + "/confluence"));

            new ExampleApp(executorService, requestService, uriProvider).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private APIAuthConfig loadAuthConfig() throws Exception {
        HashMap<String, String> defaultProperties = getDefaultProperties();
        PropertiesFileStore store = new PropertiesFileStore("target/example-httpauth.properties", defaultProperties);
        Map<String, String> props = store.getProperties();
        return new APIAuthConfig(props.get(BASE_URL), props.get(USER), props.get(PASSWORD));
    }

    private HashMap<String, String> getDefaultProperties() {
        HashMap<String, String> defaultProperties = new HashMap<>();
        defaultProperties.put(BASE_URL, "https://example.atlassian.net");
        defaultProperties.put(USER, "admin");
        defaultProperties.put(PASSWORD, "admin");
        return defaultProperties;
    }

}
