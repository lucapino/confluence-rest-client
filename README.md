# ConfluenceRestClient

A simple Java Client to communicate with the Confluence REST API.

## Version

Current release: `1.0.0`

This is a reworked version of [ConfluenceRestClient](https://github.com/MartinBoehmer/ConfluenceRestClient).
Thsi version decouples the HTTP-specific code from the Confluence client code so
that it is possible, for example, to implement the client using other third party libraries, 
or using a different form of authentication). This version also separates the API
(now in the xxx.api package) from the implementation (in the xxx.impl package).

## Usage

You must first get a RequestService to perform REST calls. 
One called HttpAuthRequestService is provided, which uses Basic HTTP 
authentication. Then connect using the URI of the Confluence server 
and your credentials:

```java
HttpAuthRequestService requestService = new HttpAuthRequestService();
requestService.connect(new URI("https://example.com"), "admin", "admin");
```

You can then create the client factory, providing the RequestService, an 
ExecutorService, and an APIUriProvider that configures the Confluence REST 
end point:

```java
ExecutorService executorService = Executors.newCachedThreadPool();
APIUriProvider uriProvider = new APIUriProvider(new URI(conf.getBaseUrl() + "/confluence"));
ClientFactory factory = new ClientFactoryImpl(executorService, requestService, apiConfig);
```

With this factory you can then create one of four separate clients for the different parts 
of the REST API:

* SpaceClient
* UserClient
* ContentClient
* SearchClient

To create an interface for working with content:

```java
ContentClient contentClient = clientFactory.getContentClient();
List<String> expand = new ArrayList<>();
expand.add(ExpandField.BODY_VIEW.getName());
expand.add(ExpandField.BODY_STORAGE.getName());
Future<ContentBean> future = contentClient.getContentById(123456, 0, expand);
ContentBean content = future.get();
...
```

## License

Copyright 2016 Micromata GmbH
Modifications Copyright 2017 Martin Böhmer
Modifications Copyright 2017 Mikkel R. Jakobsen
Modifications Copyright 2017 Luca Tagliani

Licensed under the Apache License, Version 2.0 (the "License"); you may not use these files except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
