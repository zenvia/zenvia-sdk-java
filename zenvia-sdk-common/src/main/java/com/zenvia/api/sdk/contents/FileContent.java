package com.zenvia.api.sdk.contents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class FileContent extends Content {
	public static final String TYPE = "file";
	
	public final String fileUrl;
	
	public final String fileMimeType;
	
	public final String fileCaption;


	@JsonCreator
	public FileContent(
		@JsonProperty( "fileUrl" ) String fileUrl,
		@JsonProperty( "fileMimeType" ) String fileMimeType,
		@JsonProperty( "fileCaption" ) String fileCaption
	) {
		super( ContentType.file );
		this.fileUrl = fileUrl;
		this.fileMimeType = fileMimeType;
		this.fileCaption = fileCaption;
	}
}
