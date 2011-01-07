package org.eksi.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;

public class BaslikSayfa {
	
	private static Pattern patternTitle = Pattern.compile("<h1>(.+?)</h1>",Pattern.DOTALL|Pattern.CASE_INSENSITIVE);

	private static Pattern patternOption = Pattern.compile("<option(.*?)>(\\d+)</option>",Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
	
	// eksisozluk iPhone tweaks
	private static Pattern pParag = Pattern.compile("<p\\s+id=\"d(\\d+)\">(.+?)</p>",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);		
	public static Pattern pTarget = Pattern.compile("\\&__target=[^&]+",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	
	private String strTitle;
	private int pageSelected, pageEnd;
	
	private String lastEntryID = null;
	private int serverResponse;
	private String redirectLocation;
	
	private String realURL;

	public String getHTML(String strURL) throws IOException{
		String html = "";
		realURL = strURL;
		
		URL sozluk = new URL(strURL);
		HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection eksi = (HttpURLConnection) sozluk.openConnection();
        
        serverResponse = eksi.getResponseCode();
        
        if(serverResponse == HttpURLConnection.HTTP_MOVED_PERM
        		|| serverResponse == HttpURLConnection.HTTP_MOVED_TEMP){
        	redirectLocation = eksi.getHeaderField("Location");
        	return getHTML("http://www.eksisozluk.com/"+redirectLocation+"&iphone=1");
        }
        
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
	
	public String getTitle(){
		return strTitle;
	}
	
	public int getPageSelected(){
		return pageSelected;
	}
	
	public int getPageEnd(){
		return pageEnd;
	}
	
	public int getPrevPage(){
		if(pageSelected > 1){
			return pageSelected-1;
		}
		// 0 for limit 
		return 0;
	}
	
	public int getNextPage(){
		if(pageSelected < pageEnd){
			return pageSelected+1;
		}
		// 0 for limit
		return 0;
	}
	
	public String getLastEntryID(){
		return lastEntryID;
	}
	
	public String getRealURL(){
		return realURL;
	}
	
	public ArrayList<String> entryList(String url) throws IOException{
		ArrayList<String> items = new ArrayList<String>(100);
		String strEntry;
		String strSel, strVal = null;

		String strHTML = this.getHTML(url);
		
		Matcher m = patternTitle.matcher(strHTML);
		m.find();
		strTitle = Html.fromHtml(m.group(1)).toString();
		
		pageSelected = pageEnd = 1;
		m = patternOption.matcher(strHTML);
		while(m.find()){
			strSel = m.group(1);
			strVal = m.group(2);
			if(!strSel.equals("")){
				pageSelected = Integer.valueOf(strVal);
			}
		}
		
		if(strVal != null){
			pageEnd = Integer.valueOf(strVal);
		}
		
		m = pParag.matcher(strHTML);
				
		while(m.find()){
			lastEntryID = m.group(1);
			strEntry = m.group(2);
			
			items.add(strEntry);
		}
				
		return items;
	}
	
	public static void main(String args[]) throws IOException{
		BaslikSayfa b = new BaslikSayfa();
		ArrayList<String> es = b.entryList("http://www.eksisozluk.com/show.asp?t=toefl+ibt");
		
		for(String e : es){
			System.out.println(e);
		}
	}
}
