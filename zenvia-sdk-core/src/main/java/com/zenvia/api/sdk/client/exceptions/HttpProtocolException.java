package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class HttpProtocolException extends HttpRequestException {
	public HttpProtocolException( String url, Exception cause ) {
		super( "HTTP error on: " + url, cause );
	}
}
