package com.zenvia.api.sdk.client.errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties( ignoreUnknown = true )
public class ErrorResponse {
	public final String code;

	public final String message;

	public final List<ErrorDetail> details;


	public ErrorResponse( String code, String message, ErrorDetail... details ) {
		this( code, message, Arrays.asList( details ) );
	}


	public ErrorResponse( String code, String message, Collection<ErrorDetail> details ) {
		this( code, message, new ArrayList<>( details ) );
	}


	@JsonCreator
	public ErrorResponse(
		@JsonProperty( "code" ) String code,
		@JsonProperty( "message" ) String message,
		@JsonProperty( "details" ) List<ErrorDetail> details
	) {
		this.code = code;
		this.message = message;
		this.details = details == null ? Collections.emptyList() : Collections.unmodifiableList( details );
	}
}
