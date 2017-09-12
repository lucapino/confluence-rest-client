/**
 * Copyright 2016 Micromata GmbH
 * Modifications Copyright 2017 Martin Böhmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.itboehmer.confluence.rest.client.impl;

import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.CQL;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.CQL_CONTEXT;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.EXCERPT;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.EXPAND;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.LIMIT;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.START;
import static de.itboehmer.confluence.rest.core.misc.RestPathConstants.SEARCH;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import de.itboehmer.confluence.rest.client.SearchClient;
import de.itboehmer.confluence.rest.core.RequestService;
import de.itboehmer.confluence.rest.core.cql.CqlSearchBean;
import de.itboehmer.confluence.rest.core.domain.cql.CqlSearchResult;
import de.itboehmer.confluence.rest.core.impl.APIUriProvider;

/**
 * Implementation of {@link SearchClient} to query content by CQL.
 *
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer
 */
public class SearchClientImpl extends BaseClientImpl implements SearchClient {

	public SearchClientImpl(ExecutorService executorService, RequestService requestService, APIUriProvider apiConfig) {
		super(executorService, requestService, apiConfig);
	}

	@Override
	public Future<CqlSearchResult> searchContent(CqlSearchBean searchBean) {
		Validate.notNull(searchBean);
		Validate.notNull(StringUtils.trimToNull(searchBean.getCql()));

		String cql = searchBean.getCql();
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair(CQL, cql));
		if (StringUtils.trimToNull(searchBean.getCqlcontext()) != null) {
			nameValuePairs.add(new BasicNameValuePair(CQL_CONTEXT, searchBean.getCqlcontext()));
		}
		if (searchBean.getExcerpt() != null) {
			nameValuePairs.add(new BasicNameValuePair(EXCERPT, searchBean.getExcerpt().getName()));
		}
		if (CollectionUtils.isNotEmpty(searchBean.getExpand()) == true) {
			String join = StringUtils.join(searchBean.getExpand(), ",");
			nameValuePairs.add(new BasicNameValuePair(EXPAND, join));
		}
		if (searchBean.getStart() > 0) {
			nameValuePairs.add(new BasicNameValuePair(START, String.valueOf(searchBean.getStart())));
		}
		if (searchBean.getLimit() > 0) {
			nameValuePairs.add(new BasicNameValuePair(LIMIT, String.valueOf(searchBean.getLimit())));
		}
		URIBuilder uriBuilder = buildPath(SEARCH).addParameters(nameValuePairs);
		return executorService.submit(() -> {
			return executeGetRequest(uriBuilder.build(), CqlSearchResult.class);
		});

	}

}
