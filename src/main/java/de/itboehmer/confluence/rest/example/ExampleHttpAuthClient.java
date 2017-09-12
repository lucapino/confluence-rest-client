package de.itboehmer.confluence.rest.example;

import static de.itboehmer.confluence.rest.core.impl.HttpAuthProperties.BASE_URL;
import static de.itboehmer.confluence.rest.core.impl.HttpAuthProperties.PASSWORD;
import static de.itboehmer.confluence.rest.core.impl.HttpAuthProperties.USER;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.itboehmer.confluence.rest.core.impl.APIAuthConfig;
import de.itboehmer.confluence.rest.core.impl.APIUriProvider;
import de.itboehmer.confluence.rest.core.impl.HttpAuthRequestService;

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
			
			APIUriProvider uriProvider = new APIUriProvider(new URI(conf.getBaseUrl() + "/wiki"));
			
			new ExampleApp(executorService, requestService, uriProvider).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private APIAuthConfig loadAuthConfig() throws Exception {
		HashMap<String, String> defaultProperties = getDefaultProperties();
		PropertiesFileStore store = new PropertiesFileStore("./example-httpauth.properties", defaultProperties);
		Map<String, String> props = store.getProperties();
		return new APIAuthConfig(props.get(BASE_URL), props.get(USER), props.get(PASSWORD));
	}

	private HashMap<String, String> getDefaultProperties() {
		HashMap<String, String> defaultProperties = new HashMap<String, String>();
		defaultProperties.put(BASE_URL, "https://example.atlassian.net");
		defaultProperties.put(USER, "admin");
		defaultProperties.put(PASSWORD, "admin");
		return defaultProperties;
	}

}
