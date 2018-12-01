package GoogleAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

public class ControlSearch {

	private static String API_KEY = "AIzaSyB-cIbWkT2WS-_doRwmBl7xFJ3_ffN5qmc"; 
	private static HttpURLConnection connection; 
	
	public static String googleQuery (String searchName) throws IOException {

		searchName = searchName.replace (" ", "%20");
		
		URL url = null; 
		
		try {
			url = new URL("https://www.googleapis.com/customsearch/v1?key=" +API_KEY+ "&cx=012290402059180647028:jaqij4f6yos&q="+searchName+"&alt=json");
				
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer sb = new StringBuffer();
			
		while ((inputLine = in.readLine()) != null) {
				    sb.append(inputLine);
				}
				in.close();
			
			
			return sb.toString();
		
	}
	
	public static String findNoOfResults(String json) {
		JSONObject obj = new JSONObject(json);
	
		String a = obj.toString();
		String[] splitString = a.split("totalResults\":\"");
		
		int count = 0;
		for (int i = 0; i < splitString[1].length(); i++) {
			if (splitString[1].charAt(i)==  '\"') {
				count ++;
			}
			if (count ==1) {
				return splitString[1].substring(0, i);
			}
		}
		return "";
		
	}
	
	public static String makeAndSet(String name) {
		String noOfResults = null; 
		
		try {
			String query = googleQuery(name);
			noOfResults = findNoOfResults(query); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return noOfResults; 
	}
	

	}

