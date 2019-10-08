package com.zenvia.api.sdk.contents;


/** Lists contents supported by the API/SDK.
 *
 *  @since 0.9.0 */
public enum ContentType {
	/** Maps to a {@link TextContent}.
	 *
	 *  @since 0.9.0 */
	text,

	/** Maps to a {@link FileContent}.
	 *
	 *  @since 0.9.0 */
	file,

	/** Maps to a {@link TemplateContent}.
	 *
	 *  @since 0.9.0 */
	template,

	/** Maps to a {@link JsonContent}.
	 *
	 *  @since 0.9.0 */
	json;
}
