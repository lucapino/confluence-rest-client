package de.itboehmer.confluence.rest.example;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import de.itboehmer.confluence.rest.client.ClientFactory;
import de.itboehmer.confluence.rest.client.SpaceClient;
import de.itboehmer.confluence.rest.client.UserClient;
import de.itboehmer.confluence.rest.client.impl.ClientFactoryImpl;
import de.itboehmer.confluence.rest.core.RequestService;
import de.itboehmer.confluence.rest.core.domain.UserBean;
import de.itboehmer.confluence.rest.core.domain.space.SpaceResultsBean;
import de.itboehmer.confluence.rest.core.impl.APIUriProvider;

public class ExampleApp {

	private ExecutorService executorService;
	private RequestService requestService;
	private APIUriProvider apiConfig;

	public ExampleApp(ExecutorService executorService, RequestService requestService, APIUriProvider apiConfig) {
		this.executorService = executorService;
		this.requestService = requestService;
		this.apiConfig = apiConfig;
	}

	public void run() {
		ClientFactory factory = new ClientFactoryImpl(executorService, requestService, apiConfig);
		UserClient userClient = factory.getUserClient();
		try {
			Future<UserBean> anonymousUser = userClient.getCurrentUser();
			UserBean user = anonymousUser.get();
			System.out.println("Anonymous user: " + user.getDisplayName());
		} catch (URISyntaxException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		SpaceClient spaceClient = factory.getSpaceClient();
		try {
			Future<SpaceResultsBean> spaces = spaceClient.getSpaces(null, null, null, null, null, 0, 0);
			SpaceResultsBean resultsBean = spaces.get();
			resultsBean.getResults().forEach(space -> {
				System.out.println("Space: " + space.getName());
			});
		} catch (URISyntaxException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
	}

}
