package com.reptilesoft.mediainventory;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class Poster {
	
	int http_timeout = com.reptilesoft.mediainventory.Constants.http_post_timeout;
	String http_post_url = com.reptilesoft.mediainventory.Constants.string_sync_url;
	
	public String postInventory(String inventory_xml)
	{
		String response="BAD";
		
		HttpClient httpclient=new DefaultHttpClient();
		try{
			HttpPost httppost=new HttpPost(http_post_url);
			HttpParams httpparams=new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpparams, http_timeout);
			HttpConnectionParams.setSoTimeout(httpparams, http_timeout);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			httpclient.getConnectionManager().shutdown();
		}
		return response;
	}
}
