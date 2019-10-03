package com.zenvia.api.sdk.client.errors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.Json;


@JsonIgnoreProperties( ignoreUnknown = true )
public class ErrorDetail {
	public final String code;
	
	public final String path;

	public final String message;


	@JsonCreator
	public ErrorDetail(
		@JsonProperty( "code" ) String code,
		@JsonProperty( "path" ) String path,
		@JsonProperty( "message" ) String message
	) {
		this.code = code;
		this.path = path;
		this.message = message;
	}


	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
