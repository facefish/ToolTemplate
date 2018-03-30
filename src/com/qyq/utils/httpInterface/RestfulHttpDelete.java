package com.qyq.utils.httpInterface;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class RestfulHttpDelete extends HttpEntityEnclosingRequestBase{

	    public static final String METHOD_NAME = "DELETE";
	    public String getMethod() {
	        return METHOD_NAME;
	    }
	    
	    public RestfulHttpDelete(final String uri) {
	        super();
	        setURI(URI.create(uri));
	    }
	    
	    public RestfulHttpDelete(final URI uri) {
	        super();
	        setURI(uri);
	    }
	    
	    public RestfulHttpDelete() {
	        super();
	    }

}
