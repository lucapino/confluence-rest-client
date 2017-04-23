# ConfluenceRestClient

A simple Java Client to communicate with the Confluence Rest API.

## Version

`SNAPSHOT-1.0` is Work in Progress...

## Usage

Create a new instance of ConfluenceRestClient and call the method 'connect()'.
You need the URI of the Confluence Server an your login credentials.

```java
ExecutorService executorService = Executors.newCachedThreadPool();
ConfluenceRestClient confluenceClient = new ConfluenceRestClient(executorService);
URI confluenceBaseUri = new URI("hhtps://example.com);
confluenceClient.connect(confluenceBaseUri, "username", "password");
```

Then you get seperated clients for the different entities:

* SpaceClient
* UserClient
* ContentClient

```java
ContentClient contentClient = confluenceClient.getContentClient();
List<String> expand = new ArrayList<>();
expand.add(ExpandField.BODY_VIEW.getName());
expand.add(ExpandField.BODY_STORAGE.getName());
Future<ContentBean> future = contentClient.getContentById(123456, 0, expand);
ContentBean content = future.get();
...
```

# License

Copyright 2017 Martin Böhmer

Licensed under the Apache License, Version 2.0 (the "License"); you may not use these files except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
