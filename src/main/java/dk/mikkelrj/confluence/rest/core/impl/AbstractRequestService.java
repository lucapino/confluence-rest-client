package dk.mikkelrj.confluence.rest.core.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.Validate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import de.itboehmer.confluence.rest.core.custom.CqlSearchResultDeserializer;
import de.itboehmer.confluence.rest.core.domain.cql.CqlSearchResult;

public abstract class AbstractRequestService {

	public AbstractRequestService() {
	}

	private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
			.registerTypeAdapter(CqlSearchResult.class, new CqlSearchResultDeserializer()).create();
	
	protected Gson getGson() {
		return gson;
	}

	protected JsonReader toJsonReader(InputStream inputStream) throws UnsupportedEncodingException {
		Validate.notNull(inputStream);
		InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
		JsonReader jsonReader = new JsonReader(reader);
		jsonReader.setLenient(true);
		return jsonReader;
	}

}
