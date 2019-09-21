package com.zenvia.api.sdk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zenvia.api.sdk.client.exceptions.JsonException;


public class JsonMapper {
	protected static final Logger LOG = LoggerFactory.getLogger( JsonMapper.class );
	
	protected final ObjectMapper mapper;


	public JsonMapper() {
		mapper = new ObjectMapper();
		
		mapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
		mapper.disable( DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE );
		mapper.setDateFormat( new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ) );
		mapper.registerModule( new JavaTimeModule() );
		
		mapper.disable( JsonGenerator.Feature.AUTO_CLOSE_TARGET );
		
		mapper.setVisibility( PropertyAccessor.ALL, Visibility.NONE );
		mapper.setVisibility( PropertyAccessor.FIELD, Visibility.ANY );
		
		mapper.configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false );
	}


	public byte[] serialize( Object data ) throws IllegalArgumentException {
		try {
			byte[] serialized = mapper.writeValueAsBytes( data );
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "Serialized: {}", new String( serialized, StandardCharsets.UTF_8 ) );
			}
			return serialized;
		}
		catch( JsonProcessingException exception ) {
			LOG.error( "Exception serializing", exception );
			throw new IllegalArgumentException( "Exception serializing", exception );
		}
	}


	public <TYPE> TYPE deserialize( byte[] data, Class<TYPE> type ) throws JsonException, IOException {
		try {
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "Deserialing: {}", new String( data, StandardCharsets.UTF_8 ) );
			}
			
			if( data == null )
				return null;
			
			return mapper.readValue( data, type );
		} catch( JsonMappingException | JsonParseException exception ) {
			LOG.debug( "Exception deserializing: {}", exception.getMessage() );
			LOG.trace( "Exception deserializing", exception );
			throw new JsonException( data, exception );
		} catch( IOException exception ) {
			LOG.debug( "Exception deserializing: {}", exception.getMessage() );
			LOG.trace( "Exception deserializing", exception );
			throw new IOException( "Exception deserializing", exception );
		}
	}
}
