package dk.mikkelrj.confluence.rest.client;

import de.itboehmer.confluence.rest.client.ContentClient;
import de.itboehmer.confluence.rest.client.SearchClient;
import de.itboehmer.confluence.rest.client.SpaceClient;
import de.itboehmer.confluence.rest.client.UserClient;

public interface ClientFactory {

	UserClient getUserClient();

	SpaceClient getSpaceClient();

	ContentClient getContentClient();
	
	SearchClient getSearchClient();
}
