package com.zenvia.api.sdk.contents;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/** Content used for sending pre-defined messages. Currently only available for
 *  WhatsApp channel to send notification messages.
 *
 * @since 0.9.0 */
public class TemplateContent extends Content {
	/** @since 0.9.0 */
	public static final String TYPE = "template";

	/** Id used to select the pre-defined message to be sent.
	 *
	 *  @since 0.9.0 */
	public final String templateId;

	/** Sets the values of variables in the pre-defined message.
	 *  The map key is the name of the variable.
	 *
	 *  @since 0.9.0 */
	public final Map<String,String> fields;


	/** @param templateId Id used to select the pre-defined message to be sent 
	 *
	 *  @param fields Sets the values of variables in the pre-defined message.
	 *  The map key is the name of the variable.
	 *
	 *  @since 0.9.0 */
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
