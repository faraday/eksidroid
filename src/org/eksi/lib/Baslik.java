package org.eksi.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Baslik {
	
	private static String URL_bugun = "http://www.eksisozluk.com/index.asp?a=td&iphone=1";
	private static String URL_dun = "http://www.eksisozluk.com/index.asp?a=yd&iphone=1";
	
	public static Pattern pTarget = Pattern.compile("\\&__target=[^&\"]+",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	//private static Pattern pTarget = Pattern.compile("\\&__target=.+?\"",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	
	private static Pattern pLi = Pattern.compile("<li>(.+?)</li>",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);

	
	public String getHTML(String strURL) throws IOException{
		String html = "";
		
		URL sozluk = new URL(strURL);
        URLConnection eksi = sozluk.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                		eksi.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null){ 
            html += inputLine;
        }
        in.close();
        
        return html;
	}
	
	public ArrayList<String> baslikList(String url) throws IOException{
		ArrayList<String> items = new ArrayList<String>(100);
		String strLink;
		
		String strBugun = this.getHTML(url);
		Matcher m = pLi.matcher(strBugun);
		
		Matcher mTarget;
		
		while(m.find()){
			strLink = m.group(1);
			strLink = strLink.replace("&amp;", "&");
			strLink = strLink.replace("show.asp", "http://www.eksisozluk.com/show.asp");
			
			strLink = strLink.replace("&iphone=1", "");
                			
			// "eksisozluk iPhone" tweak for links 
			mTarget = pTarget.matcher(strLink);
			strLink = mTarget.replaceAll("&iphone=1");
			mTarget.reset();
			
			items.add(strLink);
		}
		
		return items;
	}
	
	public ArrayList<String> bugun() throws IOException{
		return baslikList(URL_bugun);
	}
	
	public ArrayList<String> dun() throws IOException{
		return baslikList(URL_dun);
	}

}
