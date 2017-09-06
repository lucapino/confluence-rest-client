package dk.mikkelrj.confluence.rest.example;

import static dk.mikkelrj.confluence.rest.core.impl.HttpAuthProperties.BASE_URL;
import static dk.mikkelrj.confluence.rest.core.impl.HttpAuthProperties.PASSWORD;
import static dk.mikkelrj.confluence.rest.core.impl.HttpAuthProperties.USER;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.ImmutableMap;

import dk.mikkelrj.confluence.rest.core.impl.AtlassianAPIConfig;
import dk.mikkelrj.confluence.rest.core.impl.HttpAuthProperties;
import dk.mikkelrj.confluence.rest.core.impl.HttpAuthRequestService;

public class ExampleHttpAuthClient {

	public static void main(String[] args) {
		new ExampleHttpAuthClient().run();
	}
	
    private final static Map<String, String> DEFAULT_PROPERTY_VALUES = ImmutableMap.<String, String>builder()
            .put(BASE_URL, "https://example.atlassian.net")
            .put(USER, "admin")
            .put(PASSWORD, "admin")
            .build();

	private void run() {
		try {
			ExecutorService executorService = Executors.newFixedThreadPool(100);
			HttpAuthProperties authProps = loadAuthProperties();
			AtlassianAPIConfig apiConfig = new AtlassianAPIConfig(new URI(authProps.get(BASE_URL) + "/wiki"));
			HttpAuthRequestService requestService = new HttpAuthRequestService(
					new URI(authProps.get(BASE_URL)), authProps.get(USER), authProps.get(PASSWORD));
			new ExampleApp(executorService, requestService, apiConfig).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HttpAuthProperties loadAuthProperties() throws Exception {
		PropertiesFileStore store = new PropertiesFileStore("./example-httpauth.properties", DEFAULT_PROPERTY_VALUES);
		HttpAuthProperties authProperties = new HttpAuthProperties(store.getPropertiesMap());
		return authProperties;
	}
	
}
