package com.zenvia.api.sdk.client.exceptions;

import com.zenvia.api.sdk.client.ChannelType;


@SuppressWarnings( "serial" )
public class UnsupportedChannelException extends ApiException {
	private final String channelType;


	public UnsupportedChannelException( ChannelType channelType ) {
		this( channelType == null ? null : channelType.name() );
	}


	public UnsupportedChannelException( String channelType ) {
		super( "Unsupported channel: " + channelType );
		this.channelType = channelType;
	}


	public String getChannelType() {
		return channelType;
	}
}
