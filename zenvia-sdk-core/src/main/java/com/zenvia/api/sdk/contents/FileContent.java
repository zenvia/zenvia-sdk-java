package com.zenvia.api.sdk.contents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/** Content used for sending and receiving multimedia/file messages.
*
*  @since 0.9.0 */
public class FileContent extends Content {
	/** @since 0.9.0 */
	public static final String TYPE = "file";

	/** URL hosting the media content.
	 *
	 *  @since 0.9.0 */
	public final String fileUrl;

	/** The <a href="https://en.wikipedia.org/wiki/Media_type" target="_blank"><i>media type</i></a> of media. 
	 *
	 *  @since 0.9.0 */
	public final String fileMimeType;

	/** The media caption.
	 *
	 *  @since 0.9.0 */
	public final String fileCaption;


	/** @param fileUrl
	 *  URL hosting the media content. While sending this type of content, the URL must be accessible
	 *  by at least Zenvia's platform. This URL reply must contain the <i>Content-Type</i> header. 
	 *
	 *  @since 0.9.0 */
	public FileContent( String fileUrl ){
		this( fileUrl, null, null );
	}


	/** @param fileUrl
	 *  URL hosting the media content. While sending this type of content, the URL must be accessible
	 *  by at least Zenvia's platform.
	 *
	 *  @param fileCaption
	 *  The media caption. This field is optional. While sending this type of content, it will only be
	 *  used if the channel supports media with caption.
	 *
	 *  @since 0.9.0 */
	public FileContent( String fileUrl, String fileCaption ) {
		this( fileUrl, null, fileCaption );
	}


	/** @param fileUrl
	 *  URL hosting the media content. While sending this type of content, the URL must be accessible
	 *  by at least Zenvia's platform.
	 *
	 *  @param fileMimeType
	 *  The <a href="https://en.wikipedia.org/wiki/Media_type" target="_blank"><i>media type</i></a> of media.
	 *  While sending this type of content, this field is optional. If not provided, the
	 *  <i>media type</i> in the <i>Content-Type</i> header of the URL reply will be used.
	 *
	 *  @param fileCaption
	 *  The media caption. This field is optional. While sending this type of content, it will only be
	 *  used if the channel supports media with caption.
	 *
	 *  @since 0.9.0 */
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
