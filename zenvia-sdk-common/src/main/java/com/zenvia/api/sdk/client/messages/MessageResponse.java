package com.zenvia.api.sdk.client.messages;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.contents.Content;


@JsonIgnoreProperties( ignoreUnknown = true )
public class MessageResponse {
	public final String id;

	public final String from;

	public final String to;

	public final TMessageDirection direction;

	public final ChannelType channel;

	public final List<Content> contents;


	@JsonCreator
	public MessageResponse(
		@JsonProperty( "id" ) String id,
		@JsonProperty( "from" ) String from,
		@JsonProperty( "to" ) String to,
		@JsonProperty( "direction" ) TMessageDirection direction,
		@JsonProperty( "channel" ) ChannelType channel,
		@JsonProperty( "contents" ) List<Content> contents
	) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.direction = direction;
		this.channel = channel;
		this.contents = contents == null ? Collections.emptyList() : Collections.unmodifiableList( contents );
	}
}
