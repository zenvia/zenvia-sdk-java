package com.zenvia.api.sdk.client;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpProtocolException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.exceptions.UnsupportedChannelException;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.MessageResponse;
import com.zenvia.api.sdk.client.subscriptions.PartialSubscription;
import com.zenvia.api.sdk.client.subscriptions.Subscription;


public abstract class AbstractClient implements Closeable {
	private static final String DEFAULT_URI = "https://api.zenvia.com";

	protected final String apiToken;

	protected final String apiUrl;
	
	protected final String subscriptionApiUrl;
	
	protected final HttpClient httpClient;
	
	private final ConnectionConfig connectionConfig;
	
	private final RequestConfig requestConfig;
	
	private final ConnectionReuseStrategy connectionReuseStrategy;
	
	private final DefaultHttpRequestRetryHandler requestRetryHandler;
	
	private final PoolingHttpClientConnectionManager connectionPool;


	public AbstractClient( String apiToken ) {
		this( apiToken, (String) null );
	}


	public AbstractClient(
		String apiToken,
		Integer maxConnections
	) {
		this( apiToken, maxConnections, null, null, null );
	}


	public AbstractClient(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		this( apiToken, (Integer) null, connectionTimeout, socketTimeout, maxAutoRetries );
	}


	public AbstractClient(
		String apiToken,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		this( apiToken, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries, null, null );
	}


	public AbstractClient(
		String apiToken,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer connectionPoolTimeout,
		Integer checkStaleConnectionAfterInactivityTime
	) {
		this( apiToken, null, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries, connectionPoolTimeout, checkStaleConnectionAfterInactivityTime );
	}


	public AbstractClient( String apiToken, String apiUrl ) {
		this( apiToken, apiUrl, null, null, null );
	}


	public AbstractClient(
		String apiToken,
		String apiUrl,
		Integer maxConnections
	) {
		this( apiToken, apiUrl, maxConnections, null, null, null );
	}


	public AbstractClient(
		String apiToken,
		String apiUrl,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		this( apiToken, apiUrl, null, connectionTimeout, socketTimeout, maxAutoRetries );
	}


	public AbstractClient(
		String apiToken,
		String apiUrl,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		this( apiToken, apiUrl, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries, null, null );
	}


	public AbstractClient(
		String apiToken,
		String apiUrl,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer connectionPoolTimeout,
		Integer checkStaleConnectionAfterInactivityTime
	) {
		this(
			apiToken,
			apiUrl,
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


	public AbstractClient(
		String apiToken,
		String apiUrl,
		PoolingHttpClientConnectionManager connectionPool,
		RequestConfig defaultRequestConfig,
		DefaultHttpRequestRetryHandler requestRetryHandler,
		ConnectionConfig defaultConnectionConfig,
		ConnectionReuseStrategy connectionReuseStrategy
	) {
		this.apiToken = apiToken;
		this.apiUrl = valueOrDefault( apiUrl, DEFAULT_URI );
		this.subscriptionApiUrl = this.apiUrl + "/v1/subscriptions";
		
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


	public Channel getChannel( String channelType ) throws UnsupportedChannelException {
		return getChannel( ChannelType.parse( channelType ) );
	}


	public Channel getChannel( ChannelType channelType ) throws UnsupportedChannelException {
		return new Channel( channelType, this );
	}


	public List<Subscription> listSubscriptions()
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return Arrays.asList( list( subscriptionApiUrl, Subscription[].class ) );
	}


	@SuppressWarnings( "unchecked" )
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION createSubscription( SUBSCRIPTION subscription )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return (SUBSCRIPTION) post( subscriptionApiUrl, subscription, Subscription.class );
	}


	@SuppressWarnings( "unchecked" )
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION getSubscription( String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return (SUBSCRIPTION) get( subscriptionApiUrl, id, Subscription.class );
	}


	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION updateSubscription( SUBSCRIPTION subscription )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return updateSubscription( subscription.id, new PartialSubscription( subscription ) );
	}


	@SuppressWarnings( "unchecked" )
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION updateSubscription( String id, PartialSubscription partialSubscription )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return (SUBSCRIPTION) patch( subscriptionApiUrl, id, partialSubscription, Subscription.class );
	}


	public void deleteSubscription( String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		delete( subscriptionApiUrl, id );
	}


	protected MessageResponse sendMessage( Channel channel, MessageRequest messageRequest )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return post( channel.url, messageRequest, MessageResponse.class );
	}


	protected abstract <RESPONSE> RESPONSE list( String url, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException;


	protected abstract <RESPONSE> RESPONSE get( String url, String id, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException;


	protected abstract <REQUEST,RESPONSE> RESPONSE post( String url, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException;


	protected abstract <REQUEST,RESPONSE> RESPONSE patch( String url, String id, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException;


	protected abstract void delete( String url, String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException;


	public String getApiUrl() {
		return apiUrl;
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
