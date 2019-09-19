package com.zenvia.api.sdk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class JsonMapper {
	private static final Logger LOG = LoggerFactory.getLogger( JsonMapper.class );
	
	
	private static final ObjectMapper mapper = new ObjectMapper();
	{
		mapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
		mapper.disable( DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE );
		mapper.setDateFormat( new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ) );
		mapper.registerModule( new JavaTimeModule() );
		
		mapper.disable( JsonGenerator.Feature.AUTO_CLOSE_TARGET );
		
		mapper.setVisibility( PropertyAccessor.ALL, Visibility.NONE );
		mapper.setVisibility( PropertyAccessor.FIELD, Visibility.ANY );
		
		mapper.configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false );
	}


	public static byte[] serialize( Object data )
	{
		try
		{
			byte[] serialized = mapper.writeValueAsBytes( data );
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "Serialized: {}", new String( serialized, StandardCharsets.UTF_8 ) );
			}
			return serialized;
		}
		catch( JsonProcessingException exception )
		{
			LOG.error( "Exception serializing: ", exception );
			throw new IllegalArgumentException( "Exception serializing", exception );
		}
	}


	public static <TYPE> TYPE deserialize( byte[] data, Class<TYPE> type )
	{
		try
		{
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "Deserialing: {}", new String( data, StandardCharsets.UTF_8 ) );
			}
			
			if( data == null )
				return null;
			
			return mapper.readValue( data, type );
		}
		catch( IOException exception )
		{
			LOG.error( "Exception deserializing: ", exception );
			throw new IllegalArgumentException( "Exception deserializing", exception );
		}
	}


	private JsonMapper() {
		super();
	}
}
