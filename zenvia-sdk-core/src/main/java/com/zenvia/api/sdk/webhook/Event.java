package com.zenvia.api.sdk.webhook;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.zenvia.api.sdk.ZonedDateTimeDeserializer;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.subscriptions.EventType;


/** Base class for all event types.
 *
 *  @since 1.1.0 */
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, property = "type", include = As.EXISTING_PROPERTY )
@JsonSubTypes( {
	@JsonSubTypes.Type( value = MessageStatusEvent.class, name = MessageStatusEvent.TYPE ),
	@JsonSubTypes.Type( value = MessageEvent.class, name = MessageEvent.TYPE )
} )
public abstract class Event {
	/** Event type.
	 * 
	 *  @since 1.1.0 */
	public final EventType type;
	
	/** Event identifier.
	 *
	 *  @since 1.1.0 */
	public final String id;

	/** Timestamp of event occurrence. 
	 *
	 *  @since 1.1.0 */
	@JsonDeserialize( using = ZonedDateTimeDeserializer.class )
	@JsonSerialize( using = ZonedDateTimeSerializer.class )
	@JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX" )
	public final ZonedDateTime timestamp;

	/** Subscription identifier.
	 *
	 *  @since 1.1.0 */
	public final String subscriptionId;
	
	/** Channel where the message will go through while sending a message, the source channel when
	 *  receiving a message.
	 *
	 *  @since 1.1.0 */
	public final ChannelType channel;


	protected Event( EventType type, String id, ZonedDateTime timestamp, String subscriptionId, ChannelType channel ) {
		this.type = type;
		this.id = id;
		this.timestamp = timestamp;
		this.subscriptionId = subscriptionId;
		this.channel = channel;
	}


	/** Casts the Event to MessageEvent. Usuful when handling callbacks.
	 *
	 *  @since 1.1.0 */
	public MessageEvent ofMessage() {
		return (MessageEvent) this;
	}


	/** Casts the Event to MessageStatusEvent. Usuful when handling callbacks.
	 *
	 *  @since 1.1.0 */
	public MessageStatusEvent ofMessageStatus() {
		return (MessageStatusEvent) this;
	}
}
