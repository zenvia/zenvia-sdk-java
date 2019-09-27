package com.zenvia.api.sdk.contents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import com.zenvia.api.sdk.Json;


@JsonIgnoreProperties( ignoreUnknown = true )
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, property = "type", include = As.EXISTING_PROPERTY )
@JsonSubTypes( {
	@JsonSubTypes.Type( value = TextContent.class, name = TextContent.TYPE ),
	@JsonSubTypes.Type( value = FileContent.class, name = FileContent.TYPE ),
	@JsonSubTypes.Type( value = TemplateContent.class, name = TemplateContent.TYPE )
} )
public abstract class Content {
	public final ContentType type;


	protected Content( ContentType type ) {
		this.type = type;
	}


	public TextContent ofText() {
		return (TextContent) this;
	}


	public FileContent ofFile() {
		return (FileContent) this;
	}


	public TemplateContent ofTemplate() {
		return (TemplateContent) this;
	}


	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
