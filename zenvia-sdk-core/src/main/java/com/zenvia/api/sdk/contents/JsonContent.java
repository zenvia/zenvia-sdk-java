package com.zenvia.api.sdk.contents;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class JsonContent extends Content {
	public static final String TYPE = "json";


	public final Map<String,Object> payload;


	@JsonCreator
	public JsonContent( @JsonProperty Map<String,Object> payload ) {
		super( ContentType.json );
		this.payload = payload;
	}
}
