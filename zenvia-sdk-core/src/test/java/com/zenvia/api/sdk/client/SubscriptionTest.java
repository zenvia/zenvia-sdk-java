package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.zenvia.api.sdk.client.messages.MessageDirection;
import com.zenvia.api.sdk.client.subscriptions.Criteria;
import com.zenvia.api.sdk.client.subscriptions.EventType;
import com.zenvia.api.sdk.client.subscriptions.MessageCriteria;
import com.zenvia.api.sdk.client.subscriptions.MessageStatusSubscription;
import com.zenvia.api.sdk.client.subscriptions.MessageSubscription;
import com.zenvia.api.sdk.client.subscriptions.Subscription;
import com.zenvia.api.sdk.client.subscriptions.SubscriptionStatus;
import com.zenvia.api.sdk.client.subscriptions.Webhook;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class SubscriptionTest {
	private ObjectMapper jsonMapper = new ObjectMapper();


	public void messageSubscriptionApplyReturnType() {
		@SuppressWarnings( "unused" )
		MessageSubscription subscription;
		subscription = new MessageSubscription( null, null, null, SubscriptionStatus.ACTIVE, null, null ).apply( null, null );
		subscription = new MessageSubscription( null, null, null, SubscriptionStatus.ACTIVE, null, null ).apply( null );
	}


	public void messageStatusSubscriptionApplyReturnType() {
		@SuppressWarnings( "unused" )
		MessageStatusSubscription subscription = new MessageStatusSubscription( null, null, null, SubscriptionStatus.ACTIVE, null, null ).apply( null, null );
		subscription = new MessageStatusSubscription( null, null, null, SubscriptionStatus.ACTIVE, null, null ).apply( null );
	}


	@Test
	public void messageStatusSubscriptionSerialization() throws IOException {
		Map<String,String> headers = new HashMap<>();
		headers.put( "name", "value" );
		MessageStatusSubscription subscription = new MessageStatusSubscription(
			"123",
			new Webhook( "http://localhost/", headers ),
			new Criteria( ChannelType.whatsapp ),
			SubscriptionStatus.ACTIVE,
			ZonedDateTime.of( 2019, 9, 24, 21, 1, 30, 500000000, ZoneId.of( "America/Sao_Paulo" ) ),
			ZonedDateTime.of( 2019, 9, 24, 21, 8, 0, 100000000, ZoneId.of( "America/Sao_Paulo" ) )
		);
		
		String json = jsonMapper.writeValueAsString( subscription );
		
		assertEquals( "{\"eventType\":\"MESSAGE_STATUS\",\"id\":\"123\",\"webhook\":{\"url\":\"http://localhost/\",\"headers\":{\"name\":\"value\"}},\"criteria\":{\"channel\":\"whatsapp\"},\"status\":\"ACTIVE\",\"createdAt\":\"2019-09-24T21:01:30.500-03:00\",\"updatedAt\":\"2019-09-24T21:08:00.100-03:00\"}", json );
	}


	@Test
	public void messageSubscriptionSerialization() throws IOException {
		Map<String,String> headers = new HashMap<>();
		headers.put( "name", "value" );
		MessageSubscription subscription = new MessageSubscription(
			"123",
			new Webhook( "http://localhost/", headers ),
			new MessageCriteria( ChannelType.whatsapp, MessageDirection.IN ),
			SubscriptionStatus.ACTIVE,
			ZonedDateTime.of( 2019, 9, 24, 21, 1, 30, 500000000, ZoneId.of( "America/Sao_Paulo" ) ),
			ZonedDateTime.of( 2019, 9, 24, 21, 8, 0, 100000000, ZoneId.of( "America/Sao_Paulo" ) )
		);
		
		String json = jsonMapper.writeValueAsString( subscription );
		
		assertEquals( "{\"eventType\":\"MESSAGE\",\"id\":\"123\",\"webhook\":{\"url\":\"http://localhost/\",\"headers\":{\"name\":\"value\"}},\"criteria\":{\"channel\":\"whatsapp\",\"direction\":\"IN\"},\"status\":\"ACTIVE\",\"createdAt\":\"2019-09-24T21:01:30.500-03:00\",\"updatedAt\":\"2019-09-24T21:08:00.100-03:00\"}", json );
	}


	@Test
	public void messageSubscriptionDeserialization() throws IOException {
		Subscription subscription = jsonMapper.readValue(
			"{\"eventType\":\"MESSAGE\",\"id\":\"123\",\"webhook\":{\"url\":\"http://localhost/\",\"headers\":{\"name\":\"value\"}},\"criteria\":{\"channel\":\"whatsapp\",\"direction\":\"IN\"},\"status\":\"ACTIVE\",\"createdAt\":\"2019-09-24T21:01:30.500-03:00\",\"updatedAt\":\"2019-09-24T21:08:00.100-03:00\"}".getBytes( StandardCharsets.UTF_8 ),
			Subscription.class
		);
		
		assertNotNull( subscription );
		assertEquals( EventType.MESSAGE, subscription.eventType );
		assertEquals( MessageSubscription.class, subscription.getClass() );
		MessageSubscription messageSubscription = (MessageSubscription) subscription;
		assertEquals( "123", messageSubscription.id );
		assertNotNull( messageSubscription.criteria );
		assertEquals( MessageCriteria.class, messageSubscription.criteria.getClass() );
		assertEquals( ChannelType.whatsapp, messageSubscription.criteria.channel );
		assertEquals( MessageDirection.IN, messageSubscription.criteria.direction );
		assertNotNull( messageSubscription.webhook );
		assertNotNull( messageSubscription.webhook.headers );
		assertEquals( 1, messageSubscription.webhook.headers.size() );
		assertEquals( "value", messageSubscription.webhook.headers.get( "name" ) );
	}


	@Test
	public void messageStatusSubscriptionDeserialization() throws IOException {
		Subscription subscription = jsonMapper.readValue(
			"{\"eventType\":\"MESSAGE_STATUS\",\"id\":\"123\",\"webhook\":{\"url\":\"http://localhost/\",\"headers\":{\"name\":\"value\"}},\"criteria\":{\"channel\":\"whatsapp\"},\"status\":\"ACTIVE\",\"createdAt\":\"2019-09-24T21:01:30.500-03:00\",\"updatedAt\":\"2019-09-24T21:08:00.100-03:00\"}".getBytes( StandardCharsets.UTF_8 ),
			Subscription.class
		);
		
		assertNotNull( subscription );
		assertEquals( EventType.MESSAGE_STATUS, subscription.eventType );
		assertEquals( MessageStatusSubscription.class, subscription.getClass() );
		MessageStatusSubscription messageStatusSubscription = (MessageStatusSubscription) subscription;
		assertEquals( "123", messageStatusSubscription.id );
		assertNotNull( messageStatusSubscription.criteria );
		assertEquals( Criteria.class, messageStatusSubscription.criteria.getClass() );
		assertEquals( ChannelType.whatsapp, messageStatusSubscription.criteria.channel );
		assertNotNull( messageStatusSubscription.webhook );
		assertNotNull( messageStatusSubscription.webhook.headers );
		assertEquals( 1, messageStatusSubscription.webhook.headers.size() );
		assertEquals( "value", messageStatusSubscription.webhook.headers.get( "name" ) );
	}


	@Test
	public void unknowDeserialization() throws IOException {
		try {
			jsonMapper.readValue(
				"{\"eventType\":\"new\"}" .getBytes( StandardCharsets.UTF_8 ),
				Subscription.class );
			fail();
		} catch( JsonMappingException exception ) {
			assertEquals(
				"Could not resolve type id 'new' as a subtype of [simple type, class com.zenvia.api.sdk.client.subscriptions.Subscription]: known type ids = [MESSAGE, MESSAGE_STATUS]\n at [Source: (byte[])\"{\"eventType\":\"new\"}\"; line: 1, column: 14]",
				exception.getMessage()
			);
		}
	}
}
