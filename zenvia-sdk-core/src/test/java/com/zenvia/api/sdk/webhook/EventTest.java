package com.zenvia.api.sdk.webhook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.subscriptions.EventType;
import com.zenvia.api.sdk.contents.TextContent;
import com.zenvia.api.sdk.messages.MessageDirection;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class EventTest {
	private ObjectMapper jsonMapper = new ObjectMapper();


	@Test
	public void messageEventDeserialization() throws IOException {
		Event event = jsonMapper.readValue(
			"{\"id\":\"some-event-id\",\"timestamp\":\"2019-06-03T15:50:43-03:00\",\"type\":\"MESSAGE\",\"subscriptionId\":\"some-subs-id\",\"channel\":\"whatsapp\",\"direction\":\"IN\",\"message\":{\"from\":\"some-from\",\"to\":\"some-to\",\"direction\":\"IN\",\"channel\":\"whatsapp\",\"contents\":[{\"type\":\"text\",\"text\":\"This is a test!\"}]}}".getBytes( StandardCharsets.UTF_8 ),
			Event.class
		);

		assertNotNull( event );
		assertEquals( EventType.MESSAGE, event.type );
		assertEquals( "some-event-id", event.id );
		assertEquals( "some-subs-id", event.subscriptionId );
		assertEquals( ChannelType.whatsapp, event.channel );
		assertEquals( MessageEvent.class, event.getClass() );
		MessageEvent messageEvent = event.ofMessage();
		assertEquals( MessageDirection.IN, messageEvent.direction );
		assertEquals( "some-from", messageEvent.message.from );
		assertEquals( "some-to", messageEvent.message.to );
		assertEquals( ChannelType.whatsapp, messageEvent.message.channel );
		assertEquals( 1, messageEvent.message.contents.size() );
		TextContent textContent = messageEvent.message.contents.get(0).ofText();
		assertEquals( "This is a test!", textContent.text );
	}

	@Test
	public void messageStatusEventDeserialization() throws IOException {
		Event event = jsonMapper.readValue(
			"{\"id\":\"some-event-id\",\"timestamp\":\"2019-06-03T15:50:43-03:00\",\"type\":\"MESSAGE_STATUS\",\"subscriptionId\":\"some-subs-id\",\"channel\":\"whatsapp\",\"messageId\":\"some-message_id\",\"contentIndex\":0,\"messageStatus\":{\"timestamp\":\"2019-06-10T15:30:35-03:00\",\"code\":\"REJECTED\",\"description\":\"This is a error description\",\"causes\":[{\"channelErrorCode\":500,\"reason\":\"Template id acb94998-896b-11e9-96f7-cb86a027e9fc cannot be found or does not belongs to organizationId ORGANIZATION_ID_B\"}]}}".getBytes( StandardCharsets.UTF_8 ),
			Event.class
		);

		assertNotNull( event );
		assertEquals( EventType.MESSAGE_STATUS, event.type );
		assertEquals( "some-event-id", event.id );
		assertEquals( "some-subs-id", event.subscriptionId );
		assertEquals( ChannelType.whatsapp, event.channel );
		assertEquals( MessageStatusEvent.class, event.getClass() );
		MessageStatusEvent messageStatusEvent = event.ofMessageStatus();
		assertEquals( "some-message_id", messageStatusEvent.messageId );
		assertEquals( Integer.valueOf(0), messageStatusEvent.contentIndex );
		assertEquals( MessageStatusCode.REJECTED, messageStatusEvent.messageStatus.code );
		assertEquals( "This is a error description", messageStatusEvent.messageStatus.description );
		assertEquals( 1, messageStatusEvent.messageStatus.causes.size() );
		Cause cause = messageStatusEvent.messageStatus.causes.get(0);
		assertEquals( "500", cause.channelErrorCode );
		assertEquals( "Template id acb94998-896b-11e9-96f7-cb86a027e9fc cannot be found or does not belongs to organizationId ORGANIZATION_ID_B", cause.reason );
	}

	@Test
	public void unknowDeserialization() throws IOException {
		try {
			jsonMapper.readValue(
				"{\"type\":\"new\"}" .getBytes( StandardCharsets.UTF_8 ),
				Event.class );
			fail();
		} catch( JsonMappingException exception ) {
			assertEquals(
				"Could not resolve type id 'new' as a subtype of [simple type, class com.zenvia.api.sdk.webhook.Event]: known type ids = [MESSAGE, MESSAGE_STATUS]\n at [Source: (byte[])\"{\"type\":\"new\"}\"; line: 1, column: 9]",
				exception.getMessage()
			);
		}
	}
}
