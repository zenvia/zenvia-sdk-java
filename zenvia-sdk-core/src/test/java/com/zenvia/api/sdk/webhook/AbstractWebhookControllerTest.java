package com.zenvia.api.sdk.webhook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.subscriptions.Criteria;
import com.zenvia.api.sdk.client.subscriptions.MessageCriteria;
import com.zenvia.api.sdk.client.subscriptions.MessageStatusSubscription;
import com.zenvia.api.sdk.client.subscriptions.MessageSubscription;
import com.zenvia.api.sdk.client.subscriptions.Subscription;
import com.zenvia.api.sdk.client.subscriptions.SubscriptionStatus;
import com.zenvia.api.sdk.client.subscriptions.Webhook;
import com.zenvia.api.sdk.messages.MessageDirection;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class AbstractWebhookControllerTest {
	
	private final Field messageEventCallbackField;
	
	protected final Field messageStatusEventCallbackField;

	private final Field pathField;

	private final Field clientField;
	
	private final Field urlField;

	private final Field channelField;

	private static boolean subscriptionListed;
	private static boolean messageSubscriptionCreated;
	private static boolean messageStatusSubscriptionCreated;
	
	private static Subscription[] subscriptions;
	
	private MessageEventCallback messageCallback = new MessageEventCallback() {
		public void onMessageEvent(MessageEvent message) {}
	};
	
	private MessageStatusEventCallback messageStatusCallback = new MessageStatusEventCallback() {
		public void onMessageStatusEvent(MessageStatusEvent status) {}
	};
	
	@Before
	public void reset() {
		subscriptionListed = false;
		messageSubscriptionCreated = false;
		messageStatusSubscriptionCreated = false;
		subscriptions = new Subscription[] {}; 
	}

	public AbstractWebhookControllerTest() throws Exception {
		messageEventCallbackField = AbstractWebhookController.class.getDeclaredField( "messageEventCallback" );
		messageEventCallbackField.setAccessible( true );
		
		messageStatusEventCallbackField = AbstractWebhookController.class.getDeclaredField( "messageStatusEventCallback" );
		messageStatusEventCallbackField.setAccessible( true );
		
		pathField = AbstractWebhookController.class.getDeclaredField( "path" );
		pathField.setAccessible( true );
		
		clientField = AbstractWebhookController.class.getDeclaredField( "client" );
		clientField.setAccessible( true );
		
		urlField = AbstractWebhookController.class.getDeclaredField( "url" );
		urlField.setAccessible( true );
		
		channelField = AbstractWebhookController.class.getDeclaredField( "channel" );
		channelField.setAccessible( true );
	}

	@Test
	public void shouldNotTryToCreateSubscription1() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController(messageCallback);
		webhook.init();
		assertFalse(subscriptionListed);
		assertFalse(messageSubscriptionCreated);
		assertFalse(messageStatusSubscriptionCreated);
	}
	
	@Test
	public void shouldNotTryToCreateSubscription2() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController(messageCallback, messageStatusCallback, "/hook");
		webhook.init();
		assertFalse(subscriptionListed);
		assertFalse(messageSubscriptionCreated);
		assertFalse(messageStatusSubscriptionCreated);
	}
	
	@Test
	public void shouldNotTryToCreateSubscription3() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController(messageCallback, "/hook");
		webhook.init();
		assertFalse(subscriptionListed);
		assertFalse(messageSubscriptionCreated);
		assertFalse(messageStatusSubscriptionCreated);
	}
	
	@Test
	public void shouldNotTryToCreateSubscription4() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController(messageStatusCallback);
		webhook.init();
		assertFalse(subscriptionListed);
		assertFalse(messageSubscriptionCreated);
		assertFalse(messageStatusSubscriptionCreated);
	}
	
	@Test
	public void shouldNotTryToCreateSubscription5() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController(messageStatusCallback, "/hook");
		webhook.init();
		assertFalse(subscriptionListed);
		assertFalse(messageSubscriptionCreated);
		assertFalse(messageStatusSubscriptionCreated);
	}
	
	@Test
	public void shouldCheckThatMessageSubscriptionIsAlreadyCreated() throws Exception {
		subscriptions = new Subscription[] { messageSubscription() };
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController(messageCallback, client, "http://localhost", ChannelType.whatsapp );
		webhook.init();
		assertTrue(subscriptionListed);
		assertFalse(messageSubscriptionCreated);
		assertFalse(messageStatusSubscriptionCreated);
	}

	@Test
	public void shouldCheckThatMessageStatusSubscriptionIsAlreadyCreated() throws Exception {
		subscriptions = new Subscription[] { messageStatusSubscription() };
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController(messageStatusCallback, client, "http://localhost", ChannelType.whatsapp );
		webhook.init();
		assertTrue(subscriptionListed);
		assertFalse(messageSubscriptionCreated);
		assertFalse(messageStatusSubscriptionCreated);
	}

	@Test
	public void shouldCheckThatBothSubscriptionsIsAlreadyCreated() throws Exception {
		subscriptions = new Subscription[] { messageSubscription(), messageStatusSubscription() };
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController(messageCallback, messageStatusCallback, client, "http://localhost", ChannelType.whatsapp );
		webhook.init();
		assertTrue(subscriptionListed);
		assertFalse(messageSubscriptionCreated);
		assertFalse(messageStatusSubscriptionCreated);
	}

	@Test
	public void shouldCreateMessageSubscription() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController(messageCallback, client, "http://localhost", ChannelType.whatsapp );
		webhook.init();
		assertTrue(subscriptionListed);
		assertTrue(messageSubscriptionCreated);
		assertFalse(messageStatusSubscriptionCreated);
	}
	
	@Test
	public void shouldCreateMessageStatusSubscription() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController(messageStatusCallback, client, "http://localhost", ChannelType.whatsapp );
		webhook.init();
		assertTrue(subscriptionListed);
		assertFalse(messageSubscriptionCreated);
		assertTrue(messageStatusSubscriptionCreated);
	}
	
	@Test
	public void shouldCreateBothSubscriptions() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController(messageCallback, messageStatusCallback, client, "http://localhost", ChannelType.whatsapp );
		webhook.init();
		assertTrue(subscriptionListed);
		assertTrue(messageSubscriptionCreated);
		assertTrue(messageStatusSubscriptionCreated);
	}

	@Test
	public void constructor1() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController( messageCallback );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( AbstractWebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertNull( messageStatusEventCallbackField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
	}

	@Test
	public void constructor2() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController( messageCallback, messageStatusCallback );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( AbstractWebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
	}

	@Test
	public void constructor3() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController( messageCallback, messageStatusCallback, "/hook" );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( "/hook", pathField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
	}

	@Test
	public void constructor4() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController( messageCallback, "/hook" );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( "/hook", pathField.get( webhook ) );
		assertNull( messageStatusEventCallbackField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
	}

	@Test
	public void constructor5() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController( messageStatusCallback );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( AbstractWebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertNull( messageEventCallbackField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
	}

	@Test
	public void constructor6() throws Exception {
		AbstractWebhookController webhook = new TestWebhookController( messageStatusCallback, "/hook" );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( "/hook", pathField.get( webhook ) );
		assertNull( messageEventCallbackField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
	}
	
	@Test
	public void constructor7() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController( messageCallback, client, "http://localhost", ChannelType.whatsapp );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( AbstractWebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertEquals( client, clientField.get( webhook ) );
		assertEquals( "http://localhost", urlField.get( webhook ) );
		assertEquals( ChannelType.whatsapp, channelField.get( webhook ) );
		assertNull( messageStatusEventCallbackField.get( webhook ) );
	}

	@Test
	public void constructor8() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController( messageStatusCallback, client, "http://localhost", ChannelType.whatsapp );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( AbstractWebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertEquals( client, clientField.get( webhook ) );
		assertEquals( "http://localhost", urlField.get( webhook ) );
		assertEquals( ChannelType.whatsapp, channelField.get( webhook ) );
		assertNull( messageEventCallbackField.get( webhook ) );
	}

	@Test
	public void constructor9() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController( messageCallback, messageStatusCallback, client, "http://localhost", ChannelType.whatsapp );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( AbstractWebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertEquals( client, clientField.get( webhook ) );
		assertEquals( "http://localhost", urlField.get( webhook ) );
		assertEquals( ChannelType.whatsapp, channelField.get( webhook ) );
	}

	@Test
	public void constructor10() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		AbstractWebhookController webhook = new TestWebhookController( messageCallback, messageStatusCallback, "/hook", client, "http://localhost", ChannelType.whatsapp );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( "/hook", pathField.get( webhook ) );
		assertEquals( client, clientField.get( webhook ) );
		assertEquals( "http://localhost", urlField.get( webhook ) );
		assertEquals( ChannelType.whatsapp, channelField.get( webhook ) );
	}

	private static MessageSubscription messageSubscription() {
		return new MessageSubscription(
			"123",
			new Webhook( "http://localhost", null ),
			new MessageCriteria( ChannelType.whatsapp, MessageDirection.IN ),
			SubscriptionStatus.ACTIVE,
			ZonedDateTime.of( 2019, 9, 24, 21, 1, 30, 500000000, ZoneId.of( "America/Sao_Paulo" ) ),
			ZonedDateTime.of( 2019, 9, 24, 21, 8, 0, 100000000, ZoneId.of( "America/Sao_Paulo" ) )
		);
	}
	
	private static MessageStatusSubscription messageStatusSubscription() {
		return new MessageStatusSubscription(
			"123",
			new Webhook( "http://localhost", null ),
			new Criteria( ChannelType.whatsapp ),
			SubscriptionStatus.ACTIVE,
			ZonedDateTime.of( 2019, 9, 24, 21, 1, 30, 500000000, ZoneId.of( "America/Sao_Paulo" ) ),
			ZonedDateTime.of( 2019, 9, 24, 21, 8, 0, 100000000, ZoneId.of( "America/Sao_Paulo" ) )
		);
	}


	private static class TestWebhookController extends AbstractWebhookController {
		public TestWebhookController(MessageEventCallback messageEventCallback) {
			super(messageEventCallback);
		}
		
		public TestWebhookController(
			MessageEventCallback messageEventCallback,
			MessageStatusEventCallback messageStatusEventCallback
		) {
			super(messageEventCallback, messageStatusEventCallback);
		}
		
		public TestWebhookController(
			MessageEventCallback messageEventCallback,
			MessageStatusEventCallback messageStatusEventCallback,
			String path
		) {
			super(messageEventCallback, messageStatusEventCallback, path);
		}
		
		public TestWebhookController(MessageEventCallback messageEventCallback, String path) {
			super(messageEventCallback, path);
		}

		public TestWebhookController(MessageStatusEventCallback messageStatusEventCallback) {
			super(messageStatusEventCallback);
		}
		
		public TestWebhookController(MessageStatusEventCallback messageStatusEventCallback, String path) {
			super(messageStatusEventCallback, path);
		}

		public TestWebhookController(
			MessageEventCallback messageEventCallback,
			AbstractClient client,
			String url,
			ChannelType channel
		) {
			super(messageEventCallback, client, url, channel);
		}

		public TestWebhookController(
			MessageStatusEventCallback messageStatusEventCallback,
			AbstractClient client,
			String url,
			ChannelType channel
		) {
			super(messageStatusEventCallback, client, url, channel);
		}
		
		public TestWebhookController(
			MessageEventCallback messageEventCallback,
			MessageStatusEventCallback messageStatusEventCallback,
			AbstractClient client,
			String url,
			ChannelType
			channel
		) {
			super(messageEventCallback, messageStatusEventCallback, client, url, channel);
		}
		
		public TestWebhookController(
			MessageEventCallback messageEventCallback,
			MessageStatusEventCallback messageStatusEventCallback,
			String path,
			AbstractClient client,
			String url,
			ChannelType channel
		) {
			super(messageEventCallback, messageStatusEventCallback, path, client, url, channel);
		}
	}
	
	private static class TestClient extends AbstractClient {
		private TestClient( String apiToken ) {
			super( apiToken );
		}

		@Override
		@SuppressWarnings( "unchecked" )
		protected <RESPONSE> RESPONSE list( String url, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
			if( url.equals( "https://api.zenvia.com/v1/subscriptions" ) && responseBodyType == Subscription[].class ) {
				subscriptionListed = true;
				return (RESPONSE) subscriptions;
			}
			throw new IllegalArgumentException();
		}

		@Override
		protected <RESPONSE> RESPONSE get( String url, String id, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
			return null;
		}

		@Override
		@SuppressWarnings( "unchecked" )
		protected <REQUEST,RESPONSE> RESPONSE post( String url, REQUEST requestBody, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
			if( url.equals( "https://api.zenvia.com/v1/subscriptions" ) && MessageSubscription.class.isAssignableFrom( requestBody.getClass() ) && responseBodyType == Subscription.class ) {
				messageSubscriptionCreated = true;
				return (RESPONSE) requestBody;
			}
			if( url.equals( "https://api.zenvia.com/v1/subscriptions" ) && MessageStatusSubscription.class.isAssignableFrom( requestBody.getClass() ) && responseBodyType == Subscription.class ) {
				messageStatusSubscriptionCreated = true;
				return (RESPONSE) requestBody;
			}
			throw new IllegalArgumentException();
		}

		@Override
		protected <REQUEST,RESPONSE> RESPONSE patch( String url, String id, REQUEST requestBody, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
			return null;
		}

		@Override
		protected void delete( String url, String id )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		}
	}
}
