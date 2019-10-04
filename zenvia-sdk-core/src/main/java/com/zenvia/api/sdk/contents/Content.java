package com.zenvia.api.sdk.contents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import com.zenvia.api.sdk.Json;


/** Base class for all content types.
 *
 *  @since 0.9.0 */
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, property = "type", include = As.EXISTING_PROPERTY )
@JsonSubTypes( {
	@JsonSubTypes.Type( value = TextContent.class, name = TextContent.TYPE ),
	@JsonSubTypes.Type( value = FileContent.class, name = FileContent.TYPE ),
	@JsonSubTypes.Type( value = TemplateContent.class, name = TemplateContent.TYPE ),
	@JsonSubTypes.Type( value = JsonContent.class, name = JsonContent.TYPE )
} )
public abstract class Content {
	/** @since 0.9.0 */
	public final ContentType type;


	protected Content( ContentType type ) {
		this.type = type;
	}


	/** Casts the Content to TextContent. Usuful when handling callbacks.
	 *
	 *  @since 0.9.0 */
	public TextContent ofText() {
		return (TextContent) this;
	}


	/** Casts the Content to FileContent. Usuful when handling callbacks.
	 *
	 *  @since 0.9.0 */
	public FileContent ofFile() {
		return (FileContent) this;
	}


	/** Casts the Content to TemplateContent. Usuful when handling callbacks.
	 *
	 *  @since 0.9.0 */
	public TemplateContent ofTemplate() {
		return (TemplateContent) this;
	}


	/** Casts the Content to JsonContent. Usuful when handling callbacks.
	 *
	 *  @since 1.0.0 */
	public JsonContent ofJson() {
		return (JsonContent) this;
	}


	/** String containg the object as an indented JSON. */
	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
