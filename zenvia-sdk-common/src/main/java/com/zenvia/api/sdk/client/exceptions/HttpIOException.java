package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class HttpIOException extends HttpRequestException {
	public HttpIOException( String url, Throwable cause ) {
		super( "IO error: " + url, cause );
	}
}