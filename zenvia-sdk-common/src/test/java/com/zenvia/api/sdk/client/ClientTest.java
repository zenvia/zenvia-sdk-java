package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

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
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.MessageResponse;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ClientTest {
	
	private final Field apiTokenField;


	public ClientTest() throws Exception {
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
		AbstractClient client = new TestClient( "API_TOKEN", 11, 12, 13, 14 );
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
		AbstractClient client = new TestClient( "API_TOKEN", 11, 12, 13, 14, 15, 16 );
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
		AbstractClient client = new TestClient( "API_TOKEN", "https://zenvia.com", 11, 12, 13, 14 );
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
		AbstractClient client = new TestClient( "API_TOKEN", "https://zenvia.com", 11, 12, 13, 14, 15, 16 );
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


	private static class TestClient extends AbstractClient {
		private TestClient( String apiToken ) {
			super( apiToken );
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
			Integer connectionTimeout,
			Integer socketTimeout,
			Integer maxAutoRetries,
			Integer maxConnections
		) {
			super( apiToken, connectionTimeout, socketTimeout, maxAutoRetries, maxConnections );
		}


		private TestClient(
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


		private TestClient( String apiToken, String apiUrl ) {
			super( apiToken, apiUrl );
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
			Integer connectionTimeout,
			Integer socketTimeout,
			Integer maxAutoRetries,
			Integer maxConnections
		) {
			super( apiToken, apiUrl, connectionTimeout, socketTimeout, maxAutoRetries, maxConnections );
		}


		private TestClient(
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
		protected MessageResponse sendMessage( Channel channel, MessageRequest messageRequest )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException,
			HttpConnectionFailException, HttpProtocolException, HttpIOException {
			return null;
		}
	}
}
