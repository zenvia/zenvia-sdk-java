package com.zenvia.api.sdk.client.subscriptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.client.ChannelType;


@JsonIgnoreProperties( ignoreUnknown = true )
public class Criteria {
	public final ChannelType channel;


	@JsonCreator
	public Criteria( @JsonProperty( "channel" ) ChannelType channel ) {
		this.channel = channel;
	}


	public Criteria( String channel ) {
		this( ChannelType.valueOf( channel ) );
	}
}
