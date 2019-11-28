package com.zenvia.api.sdk.webhook;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.zenvia.api.sdk.ZonedDateTimeDeserializer;


/** The status of a send message request.
 *
 * @since 1.1.0 */
public class MessageStatus {
	/** Timestamp of event occurrence.
	 *
	 *  @since 1.1.0 */
	@JsonDeserialize( using = ZonedDateTimeDeserializer.class )
	@JsonSerialize( using = ZonedDateTimeSerializer.class )
	@JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX" )
	public final ZonedDateTime timestamp;

	/** The status of a sent message.
	 *
	 *  @since 1.1.0 */
	public final MessageStatusCode code;

	/** General description of this status.
	 *
	 *  @since 1.1.0 */
	public final String description;
	
	/** List of error cause on failure of sending message.
	 *
	 *  @since 1.1.0 */
	public final List<Cause> causes;


	/** @param timestamp Timestamp of event occurrence.
	 *
	 *  @param code Status of a sent message.
	 *
	 *  @param description Description of this status.
	 *
	 *  @param causes List of error cause on failure of sending message.
	 *
	 *  @since 1.1.0 */
	@JsonCreator
	public MessageStatus(
		@JsonProperty( "timestamp" ) ZonedDateTime timestamp,
		@JsonProperty( "code" ) MessageStatusCode code,
		@JsonProperty( "description" ) String description,
		@JsonProperty( "causes" ) List<Cause> causes
	) {
		this.timestamp = timestamp;
		this.code = code;
		this.description = description;
		this.causes = causes;
	}
}
