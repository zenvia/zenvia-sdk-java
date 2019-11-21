package com.zenvia.api.sdk.webhook.jersey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
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
import com.zenvia.api.sdk.client.subscriptions.Subscription;
import com.zenvia.api.sdk.webhook.AbstractWebhookController;
import com.zenvia.api.sdk.webhook.MessageEvent;
import com.zenvia.api.sdk.webhook.MessageEventCallback;
import com.zenvia.api.sdk.webhook.MessageStatusEvent;
import com.zenvia.api.sdk.webhook.MessageStatusEventCallback;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class WebhookControllerTest {
	
	private final Field messageEventCallbackField;
	
	protected final Field messageStatusEventCallbackField;

	private final Field pathField;

	private final Field clientField;
	
	private final Field urlField;

	private final Field channelField;

	private ResourceConfig resourceConfig;
	
	private MessageEventCallback messageCallback = new MessageEventCallback() {
		public void onMessageEvent(MessageEvent message) {}
	};
	
	private MessageStatusEventCallback messageStatusCallback = new MessageStatusEventCallback() {
		public void onMessageStatusEvent(MessageStatusEvent status) {}
	};
	
	@Before
	public void reset() {
		resourceConfig = new ResourceConfig(); 
	}

	public WebhookControllerTest() throws Exception {
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
	public void constructor1() throws Exception {
		WebhookController webhook = new WebhookController( resourceConfig, messageCallback );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( WebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertNull( messageStatusEventCallbackField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
		assertResource(WebhookController.DEFAULT_PATH);
	}

	@Test
	public void constructor2() throws Exception {
		WebhookController webhook = new WebhookController( resourceConfig, messageCallback, messageStatusCallback );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( WebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
		assertResource(WebhookController.DEFAULT_PATH);
	}

	@Test
	public void constructor3() throws Exception {
		WebhookController webhook = new WebhookController( resourceConfig, messageCallback, messageStatusCallback, "/hook" );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( "/hook", pathField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
		assertResource("/hook");
	}

	@Test
	public void constructor4() throws Exception {
		WebhookController webhook = new WebhookController( resourceConfig, messageCallback, "/hook" );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( "/hook", pathField.get( webhook ) );
		assertNull( messageStatusEventCallbackField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
		assertResource("/hook");
	}

	@Test
	public void constructor5() throws Exception {
		WebhookController webhook = new WebhookController( resourceConfig, messageStatusCallback );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( WebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertNull( messageEventCallbackField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
		assertResource(WebhookController.DEFAULT_PATH);
	}

	@Test
	public void constructor6() throws Exception {
		WebhookController webhook = new WebhookController( resourceConfig, messageStatusCallback, "/hook" );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( "/hook", pathField.get( webhook ) );
		assertNull( messageEventCallbackField.get( webhook ) );
		assertNull( clientField.get( webhook ) );
		assertNull( urlField.get( webhook ) );
		assertNull( channelField.get( webhook ) );
		assertResource("/hook");
	}
	
	@Test
	public void constructor7() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		WebhookController webhook = new WebhookController( resourceConfig, messageCallback, client, "http://localhost", ChannelType.whatsapp );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( WebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertEquals( client, clientField.get( webhook ) );
		assertEquals( "http://localhost", urlField.get( webhook ) );
		assertEquals( ChannelType.whatsapp, channelField.get( webhook ) );
		assertNull( messageStatusEventCallbackField.get( webhook ) );
		assertResource(WebhookController.DEFAULT_PATH);
	}

	@Test
	public void constructor8() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		WebhookController webhook = new WebhookController( resourceConfig, messageStatusCallback, client, "http://localhost", ChannelType.whatsapp );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( WebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertEquals( client, clientField.get( webhook ) );
		assertEquals( "http://localhost", urlField.get( webhook ) );
		assertEquals( ChannelType.whatsapp, channelField.get( webhook ) );
		assertNull( messageEventCallbackField.get( webhook ) );
		assertResource(WebhookController.DEFAULT_PATH);
	}

	@Test
	public void constructor9() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		WebhookController webhook = new WebhookController( resourceConfig, messageCallback, messageStatusCallback, client, "http://localhost", ChannelType.whatsapp );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( WebhookController.DEFAULT_PATH, pathField.get( webhook ) );
		assertEquals( client, clientField.get( webhook ) );
		assertEquals( "http://localhost", urlField.get( webhook ) );
		assertEquals( ChannelType.whatsapp, channelField.get( webhook ) );
		assertResource(WebhookController.DEFAULT_PATH);
	}

	@Test
	public void constructor10() throws Exception {
		AbstractClient client = new TestClient("TOKEN");
		WebhookController webhook = new WebhookController( resourceConfig, messageCallback, messageStatusCallback, "/hook", client, "http://localhost", ChannelType.whatsapp );
		assertEquals( messageCallback, messageEventCallbackField.get( webhook ) );
		assertEquals( messageStatusCallback, messageStatusEventCallbackField.get( webhook ) );
		assertEquals( "/hook", pathField.get( webhook ) );
		assertEquals( client, clientField.get( webhook ) );
		assertEquals( "http://localhost", urlField.get( webhook ) );
		assertEquals( ChannelType.whatsapp, channelField.get( webhook ) );
		assertResource("/hook");
	}

	private void assertResource(String expectedPath) {
		assertEquals( 1, resourceConfig.getResources().size() );
		Resource createdResource = resourceConfig.getResources().iterator().next();
		assertEquals( expectedPath, createdResource.getPath() );
		assertEquals( 1, createdResource.getResourceMethods().size() );
		assertEquals( "POST", createdResource.getResourceMethods().iterator().next().getHttpMethod() );
	}

	private static class TestClient extends AbstractClient {
		private TestClient( String apiToken ) {
			super( apiToken );
		}

		@Override
		@SuppressWarnings( "unchecked" )
		protected <RESPONSE> RESPONSE list( String url, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
			return (RESPONSE) new Subscription[] {};
		}

		@Override
		protected <RESPONSE> RESPONSE get( String url, String id, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
			return null;
		}

		@Override
		protected <REQUEST,RESPONSE> RESPONSE post( String url, REQUEST requestBody, Class<RESPONSE> responseBodyType )
			throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
			return null;
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
