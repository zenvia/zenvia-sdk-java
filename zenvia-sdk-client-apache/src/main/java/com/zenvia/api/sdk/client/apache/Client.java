package com.zenvia.api.sdk.client.apache;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
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
import com.zenvia.api.sdk.client.exceptions.HttpRequestException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnexpectedResponseBodyException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.MessageResponse;


public class Client extends AbstractClient implements Closeable {
	private static final Logger LOG = LoggerFactory.getLogger( Client.class );
	
	private final ConnectionConfig connectionConfig;
	
	private final RequestConfig requestConfig;
	
	private final ConnectionReuseStrategy connectionReuseStrategy;
	
	private final DefaultHttpRequestRetryHandler requestRetryHandler;
	
	private final PoolingHttpClientConnectionManager connectionPool;

	private final HttpClient httpClient;
	
	private final ObjectMapper jsonMapper = new ObjectMapper();


	public Client( String apiToken ) {
		this( apiToken, null );
	}


	public Client(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		this( apiToken, connectionTimeout, socketTimeout, maxAutoRetries, null );
	}


	public Client(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer maxConnections
	) {
		this( apiToken, connectionTimeout, socketTimeout, maxAutoRetries, maxConnections, null, null );
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
		this( apiToken, null, connectionTimeout, socketTimeout, maxAutoRetries, maxConnections, connectionPoolTimeout, checkStaleConnectionAfterInactivityTime );
	}


	public Client( String apiToken, String apiUrl ) {
		this( apiToken, apiUrl, null, null, null );
	}


	public Client(
		String apiToken,
		String apiUrl,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		this( apiToken, apiUrl, connectionTimeout, socketTimeout, maxAutoRetries, null );
	}


	public Client(
		String apiToken,
		String apiUrl,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer maxConnections
	) {
		this( apiToken, apiUrl, connectionTimeout, socketTimeout, maxAutoRetries, maxConnections, null, null );
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
		this(
			apiToken,
			valueOrDefault( apiUrl, DEFAULT_URI ),
			buildConnectionPool(
				valueOrDefault( maxConnections, 100 ),
				valueOrDefault( checkStaleConnectionAfterInactivityTime, 5000 )
			),
			buildRequestConfig(
				valueOrDefault( connectionTimeout, 25000 ),
				valueOrDefault( socketTimeout, 60000 ),
				valueOrDefault( connectionPoolTimeout, 0 )
			),
			new DefaultHttpRequestRetryHandler(
				valueOrDefault( maxAutoRetries, 4 ),
				false
			),
			ConnectionConfig.custom().setCharset( StandardCharsets.UTF_8 ).build(),
			DefaultConnectionReuseStrategy.INSTANCE
		);
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
		super( apiToken, apiUrl );
		
		this.connectionPool = connectionPool;
		this.connectionConfig = defaultConnectionConfig;
		this.requestConfig = defaultRequestConfig;
		this.connectionReuseStrategy = connectionReuseStrategy;
		this.requestRetryHandler = requestRetryHandler;
		
		httpClient = HttpClientBuilder.create()
			.setConnectionManager( this.connectionPool )
			.setDefaultConnectionConfig( this.connectionConfig )
			.setDefaultRequestConfig( this.requestConfig )
			.setConnectionReuseStrategy( this.connectionReuseStrategy )
			.setRetryHandler( this.requestRetryHandler )
			.disableCookieManagement()
			.build();
	}


	@Override
	public MessageResponse sendMessage( Channel channel, MessageRequest messageRequest )
		throws UnsuccessfulRequestException, UnexpectedResponseBodyException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException{
		HttpResponse httpResponse = executeRequest( channel.url, THttpMethod.POST, messageRequest );
		int httpStatus = httpResponse.getStatusLine().getStatusCode();
		if ( httpStatus < 200 || httpStatus >= 300 ) {
			throw new UnsuccessfulRequestException(
				channel.url,
				httpResponse.getStatusLine().getStatusCode(),
				deserialize( httpResponse.getEntity(), ErrorResponse.class, channel.url )
			);
		}
		return deserialize( httpResponse.getEntity(), MessageResponse.class, channel.url );
	}


	protected <TYPE> TYPE deserialize( HttpEntity entity, Class<TYPE>type, String url ) throws HttpIOException, UnexpectedResponseBodyException {
		if( entity == null ) {
			return null;
		}
		byte[] data = null;
		try {
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "Deserialing: {}", new String( data, StandardCharsets.UTF_8 ) );
			}

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			entity.writeTo( buffer );
			data = buffer.toByteArray();
			return jsonMapper.readValue( data, type );
		} catch( JsonMappingException | JsonParseException exception ) {
			throw  logException( new UnexpectedResponseBodyException( url, data, exception ) );
		} catch( IOException exception ) {
			throw logException( new HttpIOException( url, exception ) );
		}
	}


	protected <REQUEST> HttpResponse executeRequest( String url, THttpMethod method, REQUEST requestBody )
		throws HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		
		HttpClientContext httpContext = new HttpClientContext();
		HttpUriRequest httpMethod = method.create( url );
		
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
				LOG.trace( "Serialized: {}", new String( serialized, StandardCharsets.UTF_8 ) );
			}
			return serialized;
		}
		catch( JsonProcessingException exception ) {
			LOG.error( "Exception serializing", exception );
			throw new IllegalArgumentException( "Exception serializing", exception );
		}
	}


	private <EXCEPTION extends HttpRequestException> EXCEPTION logException( EXCEPTION exception ) {
		LOG.warn( exception.getMessage() );
		LOG.debug( "Request error", exception );
		return exception;
	}


	@Override
	public void close() {
		if( this.connectionPool != null ) {
			this.connectionPool.shutdown();
		}
	}


	public static final PoolingHttpClientConnectionManager buildConnectionPool(
		int maxConnections, int validateAfterInactivity
	) {
		PoolingHttpClientConnectionManager connectionPool = new PoolingHttpClientConnectionManager();
		connectionPool.setMaxTotal( maxConnections );
		connectionPool.setDefaultMaxPerRoute( maxConnections );
		connectionPool.setValidateAfterInactivity( validateAfterInactivity );
		
		return connectionPool;
	}


	public static final RequestConfig buildRequestConfig( int connectionTimeout, int socketTimeout, int poolTimeout ) {
		return RequestConfig.custom()
			.setConnectionRequestTimeout( poolTimeout )
			.setConnectTimeout( connectionTimeout )
			.setSocketTimeout( socketTimeout )
			.build();
	}


	private static final <TYPE> TYPE valueOrDefault( TYPE value, TYPE defaultValue ) {
		return value == null ? defaultValue : value;
	}


	public int getMaxTotalConnections() {
		return connectionPool.getMaxTotal();
	}


	public int getMaxPerHostConnections() {
		return connectionPool.getDefaultMaxPerRoute();
	}


	public int getCheckStaleConnectionAfterInactivityTime() {
		return connectionPool.getValidateAfterInactivity();
	}


	public int getConnectionPoolTimeout() {
		return requestConfig.getConnectionRequestTimeout();
	}


	public int getConnectionTimeout() {
		return requestConfig.getConnectTimeout();
	}


	public int getSocketTimeout() {
		return requestConfig.getSocketTimeout();
	}


	public int getMaxAutoRetries() {
		return requestRetryHandler.getRetryCount();
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + "{"
			+ "\n apiUrl = [" + getApiUrl() + "]"
			+ "\n maxTotalConnections = [" + getMaxTotalConnections() + "]"
			+ "\n maxPerHostConnections = [" + getMaxPerHostConnections() + "]"
			+ "\n checkStaleConnectionAfterInactivityTime = [" + getCheckStaleConnectionAfterInactivityTime() + "]"
			+ "\n connectionPoolTimeout = [" + getConnectionPoolTimeout() + "]"
			+ "\n connectionTimeout = [" + getConnectionTimeout() + "]"
			+ "\n socketTimeout = [" + getSocketTimeout() + "]"
			+ "\n}";
	}
}
