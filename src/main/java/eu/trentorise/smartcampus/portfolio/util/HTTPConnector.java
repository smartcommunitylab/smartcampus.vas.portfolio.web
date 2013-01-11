/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.portfolio.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HTTPConnector {
	
	public static String doGet(String address, String req, String accept, String contentType, String token) throws Exception {

		StringBuffer response = new StringBuffer();

		URL url = new URL(address + ((req != null)?("?" + req):""));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
	  conn.setDoOutput(false);
	  conn.setDoInput(true);
		
		if (accept != null) {
			conn.setRequestProperty("Accept", accept);
		}
		if (contentType != null) {
			conn.setRequestProperty("Content-Type", contentType);
		}
		conn.setRequestProperty("AUTH_TOKEN", token);
		
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output = null;
		while ((output = br.readLine()) != null) {
			response.append(output);
		}

		conn.disconnect();

		return response.toString();
	}

	
	public static String doPost(String address, String req, String accept, String contentType, String token) throws Exception {

		StringBuffer response = new StringBuffer();

		URL url = new URL(address);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			((HttpsURLConnection)conn).setHostnameVerifier(new CustomizedHostnameVerifier());
		}
		
		conn.setRequestMethod("POST");
	  conn.setDoOutput(true);
	  conn.setDoInput(true);
		
		if (accept != null) {
			conn.setRequestProperty("Accept", accept);
		}
		if (contentType != null) {
			conn.setRequestProperty("Content-Type", contentType);
		}
		if (token != null) conn.setRequestProperty("AUTH_TOKEN", token);
		
		OutputStream out = conn.getOutputStream();
	  Writer writer = new OutputStreamWriter(out, "UTF-8");
	  if (req != null) {
		  writer.write(req);
	  }
	  writer.close();
	  out.close();		
		
		
		if (conn.getResponseCode() < 200 || conn.getResponseCode() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output = null;
		while ((output = br.readLine()) != null) {
			response.append(output);
		}

		conn.disconnect();

		return response.toString();
	}	
	
	private static class CustomizedHostnameVerifier implements HostnameVerifier {
		 public boolean verify(String hostname, SSLSession session) {
		  return true;
		 }
	}
	public static void main(String[] args) throws Exception {
		String post = doPost("https://213.21.154.86/ac-service-provider-web-dev/ac/validateCode/d9ff54ec-98b9-4fb8-9adf-f46ce40717b5", null, null, null, null);
		System.err.println(post);
	}
}
