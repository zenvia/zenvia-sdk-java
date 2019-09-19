package com.zenvia.api.sdk.client.messages;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.zenvia.api.sdk.contents.Content;


public class MessageRequest {
	public final String from;

	public final String to;

	public final List<Content> contents;


	public MessageRequest( String from, String to, Content... contents ) {
		this( from, to, Arrays.asList( contents ) );
	}


	public MessageRequest( String from, String to, Collection<Content> contents ) {
		this( from, to, new ArrayList<>( contents ) );
	}


	public MessageRequest( String from, String to, List<Content> contents ) {
		this.from = from;
		this.to = to;
		this.contents = Collections.unmodifiableList( contents );
	}
}
