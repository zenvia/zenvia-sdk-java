package com.zenvia.api.sdk.client.subscriptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.Json;
import com.zenvia.api.sdk.client.ChannelType;


/** Indicates the criteria necessary to trigger a callback on a
 *  {@link Subscription subscription}.
 *
 *  @since 0.9.0 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Criteria {
	/** Channel which will trigger the callbacks.
	 *
	 *  @since 0.9.0 */
	public final ChannelType channel;


	/** @param channel Channel which will trigger the callbacks.
	 *
	 *  @since 0.9.0 */
	@JsonCreator
	public Criteria( @JsonProperty( "channel" ) ChannelType channel ) {
		this.channel = channel;
	}


	/** @param channel Channel which will trigger the callbacks.
	 *
	 *  @since 0.9.0 */
	public Criteria( String channel ) {
		this( ChannelType.valueOf( channel ) );
	}


	/** String containg the object as an indented JSON.
	 *
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
