package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class HttpConnectionFailException extends HttpRequestException {
	public HttpConnectionFailException( String url, Exception cause ) {
		super( "Connection fail on: " + url, cause );
	}
}
