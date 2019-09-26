package com.zenvia.api.sdk.client;

import java.util.Collection;
import java.util.List;

import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpProtocolException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.exceptions.UnsupportedContentException;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.Message;
import com.zenvia.api.sdk.contents.Content;


public class Channel {
	public final ChannelType type;
	
	public final String url;

	protected final AbstractClient client;


	public Channel( ChannelType type, AbstractClient client ) {
		this.type = type;
		this.client = client;
		this.url = client.getApiUrl() + type.path;
	}


	public Message sendMessage( String from, String to, Content... contents )
		throws UnsupportedContentException, UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return sendMessage( new MessageRequest( from, to, contents ) );
	}


	public Message sendMessage( String from, String to, Collection<Content> contents )
		throws UnsupportedContentException, UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return sendMessage( new MessageRequest( from, to, contents ) );
	}


	public Message sendMessage( String from, String to, List<Content> contents )
		throws UnsupportedContentException, UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return sendMessage( new MessageRequest( from, to, contents ) );
	}


	public Message sendMessage( MessageRequest messageRequest )
		throws UnsupportedContentException, UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		messageRequest.contents.forEach( ( content ) -> contentSupportValidation( content ) );
		return client.sendMessage( this, messageRequest );
	}


	protected void contentSupportValidation( Content content ) throws UnsupportedContentException {
		if( !type.supportsContent( content.type ) ) {
			throw new UnsupportedContentException( content.type, type );
		}
	}
}
