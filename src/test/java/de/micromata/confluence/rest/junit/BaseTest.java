package de.micromata.confluence.rest.junit;

import de.itboehmer.confluence.rest.ConfluenceRestClient;
import de.itboehmer.confluence.rest.core.misc.SecurityException;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cschulc on 01.07.2016.
 */
public class BaseTest {

    static final String TEST_SYSTEM_URL = "http://localhost:1990/confluence";
    static final String USERNAME = "admin";
    static final String PASSWORD = "admin";
    static final String USERNAME_TO_SEARCH = "admin";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    ConfluenceRestClient confluenceRestClient;

    @Before
    public void connect() throws URISyntaxException, IOException, ExecutionException, InterruptedException, SecurityException {
        ExecutorService executorService	= Executors.newFixedThreadPool(100);
//        ProxyHost proxy = new ProxyHost("proxy", 3128);
        URI uri = new URI(TEST_SYSTEM_URL);
        confluenceRestClient = new ConfluenceRestClient(executorService);
        confluenceRestClient.connect(uri, USERNAME, PASSWORD);
    }
}
