package com.zenvia.api.sdk.client.subscriptions;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.Json;


@JsonIgnoreProperties( ignoreUnknown = true )
public class Webhook {
	public final String url;

	public final Map<String, String> headers;


	@JsonCreator
	public Webhook(
		@JsonProperty( "url" ) String url,
		@JsonProperty( "headers" ) Map<String, String> headers
	) {
		this.url = url;
		this.headers = headers == null ? Collections.emptyMap() : Collections.unmodifiableMap( headers );
	}


	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
