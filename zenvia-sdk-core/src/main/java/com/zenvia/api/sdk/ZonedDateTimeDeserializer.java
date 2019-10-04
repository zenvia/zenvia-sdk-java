package com.zenvia.api.sdk;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;


/** ZonedDateTime JSON deserializer, used on DTO with timestamps,
 *  just a wrapper for the standard JSR310 Jackson implementation.
 *
 *  @since 0.9.0 */
@SuppressWarnings( "serial" )
public class ZonedDateTimeDeserializer extends InstantDeserializer<ZonedDateTime> {
	/** @since 0.9.0 */
	public ZonedDateTimeDeserializer() {
		super( InstantDeserializer.ZONED_DATE_TIME, true );
	}
}
