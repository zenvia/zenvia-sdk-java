package com.zenvia.api.sdk.client;

import com.zenvia.api.sdk.JsonMapper;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpProtocolException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.exceptions.UnsupportedChannelException;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.MessageResponse;


public abstract class AbstractClient {
	public static final String DEFAULT_URI = "https://api.zenvia.com";

	protected final String apiToken;

	protected final String apiUrl;
	
	protected final JsonMapper jsonMapper = new JsonMapper();


	public AbstractClient( String apiToken ) {
		this( apiToken, DEFAULT_URI );
	}


	public AbstractClient( String apiToken, String apiUrl ) {
		this.apiToken = apiToken;
		this.apiUrl = apiUrl;
	}


	protected abstract MessageResponse sendMessage( Channel channel, MessageRequest messageRequest )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException;


	public String getApiUrl() {
		return apiUrl;
	}


	public Channel getChannel( String channelType ) throws UnsupportedChannelException {
		return getChannel( ChannelType.parse( channelType ) );
	}


	public Channel getChannel( ChannelType channelType ) throws UnsupportedChannelException {
		return new Channel( channelType, this );
	}
}
