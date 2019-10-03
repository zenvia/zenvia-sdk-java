package com.zenvia.api.sdk.contents;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class TemplateContent extends Content {
	public static final String TYPE = "template";
	
	public final String templateId;
	
	public final Map<String,String> fields;


	@JsonCreator
	public TemplateContent(
		@JsonProperty( "templateId" ) String templateId,
		@JsonProperty( "fields" ) Map<String,String> fields
	) {
		super( ContentType.template );
		this.templateId = templateId;
		this.fields = fields == null ? Collections.emptyMap() : Collections.unmodifiableMap( fields );
	}
}
