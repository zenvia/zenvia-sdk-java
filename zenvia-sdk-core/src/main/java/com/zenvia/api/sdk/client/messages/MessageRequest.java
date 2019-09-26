package com.zenvia.api.sdk.client.messages;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.contents.Content;


@JsonIgnoreProperties( ignoreUnknown = true )
public class MessageRequest {
	public final String from;

	public final String to;

	public final List<Content> contents;


	public MessageRequest( String from, String to, Content... contents ) {
		this( from, to, contents == null ? null : Arrays.asList( contents ) );
	}


	public MessageRequest( String from, String to, Collection<Content> contents ) {
		this( from, to, contents == null ? null : new ArrayList<>( contents ) );
	}


	@JsonCreator
	public MessageRequest(
		@JsonProperty( "from" ) String from,
		@JsonProperty( "to" ) String to,
		@JsonProperty( "contents" ) List<Content> contents
	) {
		this.from = from;
		this.to = to;
		this.contents = contents == null ? Collections.emptyList() : Collections.unmodifiableList( contents );
	}
}
