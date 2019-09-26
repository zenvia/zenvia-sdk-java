package com.zenvia.api.sdk;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;


@SuppressWarnings( "serial" )
public class ZonedDateTimeDeserializer extends InstantDeserializer<ZonedDateTime> {
	public ZonedDateTimeDeserializer() {
		super( InstantDeserializer.ZONED_DATE_TIME, true );
	}
}
