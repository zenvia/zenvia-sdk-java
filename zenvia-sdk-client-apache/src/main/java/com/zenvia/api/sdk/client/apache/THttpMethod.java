package com.zenvia.api.sdk.client.apache;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;


public enum THttpMethod {
	GET {
		@Override
		public HttpGet create( String url ) {
			return new HttpGet( url );
		}
	},
	POST {
		@Override
		public HttpPost create( String url ) {
			return new HttpPost( url );
		}
	},
	PATCH {
		@Override
		public HttpPatch create( String url ) {
			return new HttpPatch( url );
		}
	},
	DELETE {
		@Override
		public HttpDelete create( String url ) {
			return new HttpDelete( url );
		}
	};
	
	
	public abstract HttpUriRequest create( String url );
}
