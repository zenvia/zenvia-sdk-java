package com.zenvia.api.sdk.client.subscriptions;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.Json;


/** Setup how to execute de callbacks on webhooks.
 *
 *  @since 0.9.0 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Webhook {
	/** URL to be called on callbacks.
	 *
	 *  @since 0.9.0 */
	public final String url;

	/** Custom headers to be sent on callbacks.
	 *
	 *  @since 0.9.0 */
	public final Map<String, String> headers;


	/** For when custom headers are not needed for callbacks.
	 *  
	 *  @param url URL to be called on callbacks.
	 *
	 *  @since 1.0.0 */
	public Webhook( String url ) {
		this( url, null );
	}


	/** @param url URL to be called on callbacks.
	 *
	 *  @param headers Custom headers to be sent on callbacks. Useful for authentication.
	 *
	 *  @since 0.9.0 */
	@JsonCreator
	public Webhook(
		@JsonProperty( "url" ) String url,
		@JsonProperty( "headers" ) Map<String, String> headers
	) {
		this.url = url;
		this.headers = headers == null ? Collections.emptyMap() : Collections.unmodifiableMap( headers );
	}


	/** String containg the object as an indented JSON.
	 *
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
