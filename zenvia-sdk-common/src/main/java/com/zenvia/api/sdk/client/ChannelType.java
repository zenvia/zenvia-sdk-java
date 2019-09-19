package com.zenvia.api.sdk.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.zenvia.api.sdk.client.exceptions.UnsupportedChannelException;
import com.zenvia.api.sdk.contents.ContentType;

import static com.zenvia.api.sdk.contents.ContentType.*;


public enum ChannelType {
	whatsapp( text, file, template ),
	sms( text ),
	facebook( text, file );


	public final String path;
	
	private final Set<ContentType> supportedContentTypes;


	private ChannelType( ContentType... contentTypes ) {
		this.path = "/v1/channels/" + name() + "/messages";
		this.supportedContentTypes = Collections.unmodifiableSet( new HashSet<>( Arrays.asList( contentTypes ) ) );
	}


	public boolean supportsContent( ContentType contentType ) {
		return supportedContentTypes.contains( contentType );
	}


	public static ChannelType parse( String channel ) throws UnsupportedChannelException {
		try {
			return ChannelType.valueOf( channel  );
		} catch ( RuntimeException exception ) {
			throw new UnsupportedChannelException( channel );
		}
	}
}
