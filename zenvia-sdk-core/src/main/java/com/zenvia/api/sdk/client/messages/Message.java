package com.zenvia.api.sdk.client.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.contents.Content;


public class Message extends MessageRequest {
	public final String id;

	public final MessageDirection direction;

	public final ChannelType channel;


	public Message( String id, String from, String to, MessageDirection direction, ChannelType channel, Content... contents ) {
		this( id, from, to, direction, channel, contents == null ? null : Arrays.asList(  ) );
	}


	public Message( String id, String from, String to, MessageDirection direction, ChannelType channel, Collection<Content> contents ) {
		this( id, from, to, direction, channel, contents == null ? null : new ArrayList<>( contents ) );
	}


	@JsonCreator
	public Message(
		@JsonProperty( "id" ) String id,
		@JsonProperty( "from" ) String from,
		@JsonProperty( "to" ) String to,
		@JsonProperty( "direction" ) MessageDirection direction,
		@JsonProperty( "channel" ) ChannelType channel,
		@JsonProperty( "contents" ) List<Content> contents
	) {
		super( from, to, contents );
		this.id = id;
		this.direction = direction;
		this.channel = channel;
	}
}
