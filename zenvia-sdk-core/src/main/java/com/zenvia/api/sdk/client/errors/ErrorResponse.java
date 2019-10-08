package com.zenvia.api.sdk.client.errors;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.Json;


/** Response for unsuccessful API requests.
*
*  @since 0.9.0 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ErrorResponse {
	/** Code indicating the nature of the error.
	 *
	 *  @since 0.9.0 */
	public final String code;

	/** Message explaining the nature of the error.
	 *
	 *  @since 0.9.0 */
	public final String message;

	/** Detailed error causes, when known and available.
	 *
	 *  @since 0.9.0 */
	public final List<ErrorDetail> details;


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


	/** String containg the object as an indented JSON.
	 * 
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
