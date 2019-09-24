package com.zenvia.api.sdk.client.apache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.client.Channel;
import com.zenvia.api.sdk.client.errors.ErrorResponse;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpProtocolException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.MessageResponse;


public class Client extends AbstractClient {
	private static final Logger LOG = LoggerFactory.getLogger( Client.class );
	
	private final ObjectMapper jsonMapper = new ObjectMapper();


	public Client( String apiToken ) {
		super( apiToken );
	}


	public Client(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		super( apiToken, connectionTimeout, socketTimeout, maxAutoRetries );
	}


	public Client(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer maxConnections
	) {
		super( apiToken, connectionTimeout, socketTimeout, maxAutoRetries, maxConnections );
	}


	public Client(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer maxConnections,
		Integer connectionPoolTimeout,
		Integer checkStaleConnectionAfterInactivityTime
	) {
		super( apiToken, connectionTimeout, socketTimeout, maxAutoRetries, maxConnections, connectionPoolTimeout, checkStaleConnectionAfterInactivityTime );
	}


	public Client( String apiToken, String apiUrl ) {
		super( apiToken, apiUrl );
	}


	public Client(
		String apiToken,
		String apiUrl,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		super( apiToken, apiUrl, connectionTimeout, socketTimeout, maxAutoRetries );
	}


	public Client(
		String apiToken,
		String apiUrl,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer maxConnections
	) {
		super( apiToken, apiUrl, connectionTimeout, socketTimeout, maxAutoRetries, maxConnections );
	}


	public Client(
		String apiToken,
		String apiUrl,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer maxConnections,
		Integer connectionPoolTimeout,
		Integer checkStaleConnectionAfterInactivityTime
	) {
		super( apiToken, apiUrl, connectionTimeout, socketTimeout, maxAutoRetries, maxConnections, connectionPoolTimeout, checkStaleConnectionAfterInactivityTime );
	}


	public Client(
		String apiToken,
		String apiUrl,
		PoolingHttpClientConnectionManager connectionPool,
		RequestConfig defaultRequestConfig,
		DefaultHttpRequestRetryHandler requestRetryHandler,
		ConnectionConfig defaultConnectionConfig,
		ConnectionReuseStrategy connectionReuseStrategy
	) {
		super( apiToken, apiUrl, connectionPool, defaultRequestConfig, requestRetryHandler, defaultConnectionConfig, connectionReuseStrategy );
	}


	@Override
	public MessageResponse sendMessage( Channel channel, MessageRequest messageRequest )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException{
		HttpResponse httpResponse = executeRequest( channel.url, new HttpPost( channel.url ), messageRequest );
		int httpStatus = httpResponse.getStatusLine().getStatusCode();
		if ( httpStatus < 200 || httpStatus >= 300 ) {
			throw logException( new UnsuccessfulRequestException(
				channel.url,
				httpStatus,
				deserialize( httpResponse.getEntity(), ErrorResponse.class, channel.url, httpStatus )
			) );
		}
		return deserialize( httpResponse.getEntity(), MessageResponse.class, channel.url, httpStatus );
	}


	protected <TYPE> TYPE deserialize( HttpEntity entity, Class<TYPE>type, String url, int httpStatus ) throws UnsuccessfulRequestException, HttpIOException {
		if( entity == null || entity.getContentLength() == 0 ) {
			return null;
		}
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			entity.writeTo( buffer );
			byte[] data = buffer.toByteArray();
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "Response body: {}", new String( data, StandardCharsets.UTF_8 ) );
			}
			return jsonMapper.readValue( data, type );
		} catch( JsonMappingException | JsonParseException exception ) {
			throw logException( new UnsuccessfulRequestException( url, httpStatus, exception ) );
		} catch( IOException exception ) {
			throw logException( new HttpIOException( url, exception ) );
		}
	}


	protected <REQUEST> HttpResponse executeRequest( String url, HttpUriRequest httpMethod, REQUEST requestBody )
		throws HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		
		HttpClientContext httpContext = new HttpClientContext();
		
		httpMethod.addHeader( "X-API-Token", apiToken );
		if( requestBody != null && httpMethod instanceof HttpEntityEnclosingRequest ) {
			( (HttpEntityEnclosingRequest) httpMethod ).setEntity(
				new ByteArrayEntity( serialize( requestBody ), ContentType.APPLICATION_JSON )
			);
		}
		
		try {
			return httpClient.execute( httpMethod, httpContext );
		} catch( SocketTimeoutException cause ) {
			throw logException( new HttpSocketTimeoutException( url, cause ) );
		} catch( ConnectTimeoutException cause ) {
			throw logException( new HttpConnectionTimeoutException( url, cause ) );
		} catch( ConnectException cause ) {
			throw logException( new HttpConnectionFailException( url, cause ) );
		} catch( ClientProtocolException cause ) {
			throw logException( new HttpProtocolException( url, cause ) );
		} catch( IOException cause ) {
			throw logException( new HttpIOException( url, cause ) );
		}
	}


	protected byte[] serialize( Object data ) throws IllegalArgumentException {
		try {
			byte[] serialized = jsonMapper.writeValueAsBytes( data );
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "Request body: {}", new String( serialized, StandardCharsets.UTF_8 ) );
			}
			return serialized;
		}
		catch( JsonProcessingException exception ) {
			LOG.error( "Exception serializing request body", exception );
			throw new IllegalArgumentException( "Exception serializing request body", exception );
		}
	}


	private <EXCEPTION extends Exception> EXCEPTION logException( EXCEPTION exception ) {
		LOG.warn( exception.getMessage() );
		LOG.debug( "Request error", exception );
		return exception;
	}
}
