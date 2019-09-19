package com.zenvia.api.sdk.client;

import com.zenvia.api.sdk.JsonMapper;
import com.zenvia.api.sdk.client.exceptions.UnsupportedChannelException;


public abstract class AbstractClient implements IClient {
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


	@Override
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
