package com.zenvia.api.sdk.client.exceptions;

import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.contents.ContentType;


@SuppressWarnings( "serial" )
public class UnsupportedContentException extends ApiException {
	private final String contentType;
	
	private final String channelType;


	public UnsupportedContentException( ContentType contentType, ChannelType channelType ) {
		this( contentType == null ? null : contentType.name(), channelType == null ? null : channelType.name() );
	}


	public UnsupportedContentException( String contentType, ChannelType channelType ) {
		this( contentType, channelType == null ? null : channelType.name() );
	}


	public UnsupportedContentException( String contentType, String channelType ) {
		super( "Content type " + contentType + " is not supported by " + channelType + " channel" );
		this.contentType = contentType;
		this.channelType = channelType;
	}


	public String getContentType() {
		return contentType;
	}


	public String getChannelType() {
		return channelType;
	}
}
