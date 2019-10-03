package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpProtocolException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.exceptions.UnsupportedChannelException;
import com.zenvia.api.sdk.client.messages.MessageDirection;
import com.zenvia.api.sdk.client.subscriptions.MessageCriteria;
import com.zenvia.api.sdk.client.subscriptions.MessageSubscription;
import com.zenvia.api.sdk.client.subscriptions.PartialSubscription;
import com.zenvia.api.sdk.client.subscriptions.Subscription;
import com.zenvia.api.sdk.client.subscriptions.SubscriptionStatus;
import com.zenvia.api.sdk.client.subscriptions.Webhook;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class AbstractClientTest {
	private final Field apiTokenField;


	public AbstractClientTest() throws Exception {
		apiTokenField = AbstractClient.class.getDeclaredField( "apiToken" );
		apiTokenField.setAccessible( true );
	}


	@Test
	public void getUnsupportedChannel() {
		try {
			AbstractClient client = new TestClient( "API_TOKEN" );
			client.getChannel( "new" );
			fail();
			client.close();
		} catch( UnsupportedChannelException exception ) {
			assertEquals( "new" , exception.getChannelType() );
			assertEquals( "Unsupported channel: new" , exception.getMessage() );
		}
	}


	@Test
	public void getWhatsappChannelByString() {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Channel channel = client.getChannel( "whatsapp" );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.whatsapp );
		client.close();
	}


	@Test
	public void getSmsChannelByString() {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Channel channel = client.getChannel( "sms" );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.sms );
		client.close();
	}


	@Test
	public void getFacebookChannelByString() {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Channel channel = client.getChannel( "facebook" );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.facebook );
		client.close();
	}


	@Test
	public void getWhatsappChannelByType() {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Channel channel = client.getChannel( ChannelType.whatsapp );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.whatsapp );
		client.close();
	}


	@Test
	public void getSmsChannelByType() {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Channel channel = client.getChannel( ChannelType.sms );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.sms );
		client.close();
	}


	@Test
	public void getFacebookChannelByType() {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Channel channel = client.getChannel( ChannelType.facebook );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.facebook );
		client.close();
	}


	@Test
	public void listSubscriptions() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN" );
		List<Subscription> result = client.listSubscriptions();
		assertNotNull( result );
		assertEquals( 1, result.size() );
		assertNotNull( result.get( 0 ) );
		client.close();
	}


	@Test
	public void createSubscription() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Subscription expected = subscription();
		Subscription subscription = client.createSubscription( expected );
		assertSame( expected, subscription );
		client.close();
	}


	@Test
	public void getSubscription() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Subscription subscription = client.getSubscription( "123" );
		assertNotNull( subscription );
		client.close();
	}


	@Test
	public void updateSubscriptionUsingSubscription() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Webhook webhook = new Webhook( "url", null );
		Subscription input = subscription().apply( webhook, SubscriptionStatus.INACTIVE );
		Subscription output = client.updateSubscription( input );
		assertNotSame( input, output );
		assertSame( webhook, output.webhook );
		assertSame( SubscriptionStatus.INACTIVE, output.status );
		client.close();
	}


	@Test
	public void updateSubscriptionUsingPartialSubscription() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN" );
		Webhook webhook = new Webhook( "url", null );
		Subscription output = client.updateSubscription(
			"123",
			new PartialSubscription( webhook, SubscriptionStatus.INACTIVE )
		);
		assertSame( webhook, output.webhook );
		assertSame( SubscriptionStatus.INACTIVE, output.status );
		client.close();
	}


	@Test
	public void deleteSubscription() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN" );
		client.deleteSubscription( "123" );
		client.close();
	}


	@Test
	public void constructor1() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN" );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 25000, client.getConnectionTimeout() );
		assertEquals( 60000, client.getSocketTimeout() );
		assertEquals( 4, client.getMaxAutoRetries() );
		assertEquals( 100, client.getMaxTotalConnections() );
		assertEquals( 100, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructor2() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN", 10 );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 25000, client.getConnectionTimeout() );
		assertEquals( 60000, client.getSocketTimeout() );
		assertEquals( 4, client.getMaxAutoRetries() );
		assertEquals( 10, client.getMaxTotalConnections() );
		assertEquals( 10, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructor4() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN", 11, 12, 13 );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 100, client.getMaxTotalConnections() );
		assertEquals( 100, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructor5() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN", 14, 11, 12, 13 );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 14, client.getMaxTotalConnections() );
		assertEquals( 14, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructor7() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN", 14, 11, 12, 13, 15, 16 );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 14, client.getMaxTotalConnections() );
		assertEquals( 14, client.getMaxPerHostConnections() );
		assertEquals( 15, client.getConnectionPoolTimeout() );
		assertEquals( 16, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl1() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN", "https://zenvia.com" );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 25000, client.getConnectionTimeout() );
		assertEquals( 60000, client.getSocketTimeout() );
		assertEquals( 4, client.getMaxAutoRetries() );
		assertEquals( 100, client.getMaxTotalConnections() );
		assertEquals( 100, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl2() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN", "https://zenvia.com", 10 );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 25000, client.getConnectionTimeout() );
		assertEquals( 60000, client.getSocketTimeout() );
		assertEquals( 4, client.getMaxAutoRetries() );
		assertEquals( 10, client.getMaxTotalConnections() );
		assertEquals( 10, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl4() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN", "https://zenvia.com", 11, 12, 13 );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 100, client.getMaxTotalConnections() );
		assertEquals( 100, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl5() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN", "https://zenvia.com", 14, 11, 12, 13 );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 14, client.getMaxTotalConnections() );
		assertEquals( 14, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl7() throws Exception {
		AbstractClient client = new TestClient( "API_TOKEN", "https://zenvia.com", 14, 11, 12, 13, 15, 16 );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 14, client.getMaxTotalConnections() );
		assertEquals( 14, client.getMaxPerHostConnections() );
		assertEquals( 15, client.getConnectionPoolTimeout() );
		assertEquals( 16, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	private static MessageSubscription subscription() {
		return new MessageSubscription(
			"123",
			new Webhook( "http://localhost", null ),
			new MessageCriteria( ChannelType.whatsapp, MessageDirection.IN ),
			SubscriptionStatus.ACTIVE,
			ZonedDateTime.of( 2019, 9, 24, 21, 1, 30, 500000000, ZoneId.of( "America/Sao_Paulo" ) ),
			ZonedDateTime.of( 2019, 9, 24, 21, 8, 0, 100000000, ZoneId.of( "America/Sao_Paulo" ) )
		);
	}


	private static class TestClient extends AbstractClient {
		private TestClient( String apiToken ) {
			super( apiToken );
		}


		private TestClient(
			String apiToken,
			Integer maxConnections
		) {
			super( apiToken, maxConnections, null, null, null );
		}


		private TestClient(
			String apiToken,
			Integer connectionTimeout,
			Integer socketTimeout,
			Integer maxAutoRetries
		) {
			super( apiToken, connectionTimeout, socketTimeout, maxAutoRetries );
		}


		private TestClient(
			String apiToken,
			Integer maxConnections,
			Integer connectionTimeout,
			Integer socketTimeout,
			Integer maxAutoRetries
		) {
			super( apiToken, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries );
		}


		private TestClient(
			String apiToken,
			Integer maxConnections,
			Integer connectionTimeout,
			Integer socketTimeout,
			Integer maxAutoRetries,
			Integer connectionPoolTimeout,
			Integer checkStaleConnectionAfterInactivityTime
		) {
			super( apiToken, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries, connectionPoolTimeout, checkStaleConnectionAfterInactivityTime );
		}


		private TestClient( String apiToken, String apiUrl ) {
			super( apiToken, apiUrl );
		}


		private TestClient(
			String apiToken,
			String apiUrl,
			Integer maxConnections
		) {
			super( apiToken, apiUrl, maxConnections, null, null, null );
		}


		private TestClient(
			String apiToken,
			String apiUrl,
			Integer connectionTimeout,
			Integer socketTimeout,
			Integer maxAutoRetries
		) {
			super( apiToken, apiUrl, connectionTimeout, socketTimeout, maxAutoRetries );
		}


		private TestClient(
			String apiToken,
			String apiUrl,
			Integer maxConnections,
			Integer connectionTimeout,
			Integer socketTimeout,
			Integer maxAutoRetries
		) {
			super( apiToken, apiUrl, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries );
		}


		private TestClient(
			String apiToken,
			String apiUrl,
			Integer maxConnections,
			Integer connectionTimeout,
			Integer socketTimeout,
			Integer maxAutoRetries,
			Integer connectionPoolTimeout,
			Integer checkStaleConnectionAfterInactivityTime
		) {
			super( apiToken, apiUrl, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries, connectionPoolTimeout, checkStaleConnectionAfterInactivityTime );
		}


		private TestClient(
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
		@SuppressWarnings( "unchecked" )
		protected <RESPONSE> RESPONSE list( String url, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
			if( url.equals( "https://api.zenvia.com/v1/subscriptions" ) && responseBodyType == Subscription[].class ) {
				return (RESPONSE) new Subscription[] { subscription() };
			}
			throw new IllegalArgumentException();
		}


		@Override
		@SuppressWarnings( "unchecked" )
		protected <RESPONSE> RESPONSE get( String url, String id, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
			if( url.equals( "https://api.zenvia.com/v1/subscriptions" ) && id.equals( "123" ) && responseBodyType == Subscription.class ) {
				return (RESPONSE) subscription();
			}
			throw new IllegalArgumentException();
		}


		@Override
		@SuppressWarnings( "unchecked" )
		protected <REQUEST,RESPONSE> RESPONSE post( String url, REQUEST requestBody, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
			if( url.equals( "https://api.zenvia.com/v1/subscriptions" ) && Subscription.class.isAssignableFrom( requestBody.getClass() ) && responseBodyType == Subscription.class ) {
				return (RESPONSE) requestBody;
			}
			throw new IllegalArgumentException();
		}


		@Override
		@SuppressWarnings( "unchecked" )
		protected <REQUEST,RESPONSE> RESPONSE patch( String url, String id, REQUEST requestBody, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
			if( url.equals( "https://api.zenvia.com/v1/subscriptions" ) && PartialSubscription.class == requestBody.getClass() && responseBodyType == Subscription.class ) {
				return (RESPONSE) subscription().apply( (PartialSubscription) requestBody );
			}
			throw new IllegalArgumentException();
		}


		@Override
		protected void delete( String url, String id )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException,
			HttpIOException {
			if( !url.equals( "https://api.zenvia.com/v1/subscriptions" ) || !id.equals( "123" ) ) {
				throw new IllegalArgumentException();
			}
		}
	}
}
