package de.itboehmer.confluence.rest.client;

public interface ClientFactory {

	UserClient getUserClient();

	SpaceClient getSpaceClient();

	ContentClient getContentClient();
	
	SearchClient getSearchClient();
}
