/**
 * Copyright 2016 Micromata GmbH
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

import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.EXPAND;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.LABEL;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.LIMIT;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.SPACEKEY;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.START;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.STATUS;
import static de.itboehmer.confluence.rest.core.misc.RestParamConstants.TYPE;
import static de.itboehmer.confluence.rest.core.misc.RestPathConstants.SPACE;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import de.itboehmer.confluence.rest.client.SpaceClient;
import de.itboehmer.confluence.rest.core.RequestService;
import de.itboehmer.confluence.rest.core.domain.space.SpaceBean;
import de.itboehmer.confluence.rest.core.domain.space.SpaceResultsBean;
import de.itboehmer.confluence.rest.core.impl.APIUriProvider;
import de.itboehmer.confluence.rest.core.misc.SpaceStatus;
import de.itboehmer.confluence.rest.core.misc.SpaceType;

/**
 * Author: Christian Schulze (c.schulze@micromata.de) Date: 02.07.2016
 */
public class SpaceClientImpl extends BaseClientImpl implements SpaceClient {

	public SpaceClientImpl(ExecutorService executorService, RequestService requestService, APIUriProvider apiConfig) {
		super(executorService, requestService, apiConfig);
	}

	@Override
	public Future<SpaceResultsBean> getSpaces(List<String> keys, SpaceType type, SpaceStatus status,
			List<String> labels, List<String> expand, int start, int limit) throws URISyntaxException {
		URIBuilder uriBuilder = buildPath(SPACE);
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		if (keys != null && keys.isEmpty() == false) {
			String join = StringUtils.join(keys, ",");
			nameValuePairs.add(new BasicNameValuePair(SPACEKEY, join));
		}
		if (type != null) {
			nameValuePairs.add(new BasicNameValuePair(TYPE, type.getName()));
		}
		if (status != null) {
			nameValuePairs.add(new BasicNameValuePair(STATUS, status.getName()));
		}
		if (labels != null && labels.isEmpty() == false) {
			String join = StringUtils.join(labels, ",");
			nameValuePairs.add(new BasicNameValuePair(LABEL, join));
		}
		if (expand != null && expand.isEmpty() == false) {
			String join = StringUtils.join(expand, ",");
			nameValuePairs.add(new BasicNameValuePair(EXPAND, join));
		}
		if (start > 0) {
			nameValuePairs.add(new BasicNameValuePair(START, String.valueOf(start)));
		}
		if (limit > 0) {
			nameValuePairs.add(new BasicNameValuePair(LIMIT, String.valueOf(limit)));
		}
		uriBuilder.addParameters(nameValuePairs);
		return executorService.submit(() -> {
			return executeGetRequest(uriBuilder.build(), SpaceResultsBean.class);
		});
	}

	@Override
	public Future<SpaceBean> getSpaceByKey(String key, List<String> expand) {
		URIBuilder uriBuilder = buildPath(SPACE, key);
		if (expand != null && expand.isEmpty() == false) {
			String join = StringUtils.join(expand, ",");
			uriBuilder.addParameter(EXPAND, join);
		}
		return executorService.submit(() -> {
			return executeGetRequest(uriBuilder.build(), SpaceBean.class);
		});
	}
}
