package com.github.lucapino.confluence.rest.client.example;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.github.lucapino.confluence.rest.client.api.ClientFactory;
import com.github.lucapino.confluence.rest.client.api.SpaceClient;
import com.github.lucapino.confluence.rest.client.api.UserClient;
import com.github.lucapino.confluence.rest.client.impl.ClientFactoryImpl;
import com.github.lucapino.confluence.rest.core.api.RequestService;
import com.github.lucapino.confluence.rest.core.api.domain.UserBean;
import com.github.lucapino.confluence.rest.core.api.domain.space.SpaceResultsBean;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;

public class ExampleApp {

    private final ExecutorService executorService;
    private final RequestService requestService;
    private final APIUriProvider apiConfig;

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
