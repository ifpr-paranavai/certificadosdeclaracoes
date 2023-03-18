package br.com.cedi.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Teste {
	
	public static void main(String[] args) {
System.out.println(netIsAvailable());
		
		
	}
	
	private static boolean netIsAvailable() {
	    try {
	        final URL url = new URL("http://www.google.com");
	        final URLConnection conn = url.openConnection();
	        conn.connect();
	        conn.getInputStream().close();
	        return true;
	    } catch (MalformedURLException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	        return false;
	    }
	}

}
