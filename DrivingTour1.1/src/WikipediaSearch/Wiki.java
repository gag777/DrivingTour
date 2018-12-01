 package WikipediaSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;

/**
 * This class is where the call to the Wikipedia API will be complete and its return processed and dealt with
 * @author GeorgeGreenwood
 *
 */
public class Wiki {

	private static String address = "https://en.wikipedia.org/w/";
	private static String query = "api.php?action=query&format=json&list=geosearch&gscoord=";
	private static String radiusString ="&gsradius=";
	private static String limitString = "&gslimit=";
	private static HttpURLConnection connection; 
	private static int wikiLength;
	public static String imageUrl;

	/**
	 * Method to build the URL for API call
	 * @param lat, latitude of searched position 
	 * @param lon, longitude of searched position 
	 * @param radius, radios of the search in metres
	 * @return A string for the URL to call API
	 */
	public static String buildUrl (double lat, double lon, int radius) {
		
		StringBuilder sb = new StringBuilder(); 
		sb.append(address); 
		sb.append(query); 
		sb.append(String.valueOf(lat));
		sb.append("|");
		sb.append(String.valueOf(lon));
		sb.append(radiusString);
		sb.append(String.valueOf(radius));
		sb.append(limitString);
		sb.append("20"); 
		
		return sb.toString();
	}
	
	
	
	/**
	 * A method to call the API
	 * @param urlAddress, the URL used to call API with correct parameters
	 * @return a string of the API return, in JOSN format
	 * @throws IOException, when request fails
	 */
	public static String makeRequest (String urlAddress) throws IOException  {
		
		URL url = null; 
		
		try {
			url = new URL(urlAddress);
					
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
	
	/**
	 * A method to convert the API return into a java object 
	 * @param json, the API return in JSON format
	 * @return an array list of objects featuring data of API return
	 */
	public static ArrayList<Geosearch> JSONtoPOJO (String json ) {
		if (json.substring(0, 7) .equals("{\"error")) {
			return null;
		}
		ArrayList<Geosearch> geoArray = new ArrayList<Geosearch>(); 
		
		int count = 0;
		int i = 0;
		
		while (!json.substring(i, i+1).equals("[") ) {
			i ++; 
			count ++;
			
		}
		json = json.substring(count);
		
		
		JSONArray obj = new JSONArray(json);
		
		for (int j = 0; j< obj.length(); j++) {
			Geosearch newObj = new Geosearch(); 
			newObj.setDist( obj.getJSONObject(j).get("dist").toString() );
			newObj.setTitle( obj.getJSONObject(j).get("title").toString() );
			newObj.setPageid(obj.getJSONObject(j).get("pageid").toString() );
			newObj.setLat(obj.getJSONObject(j).get("lat").toString() );
			newObj.setLon(obj.getJSONObject(j).get("lon").toString() );
			
			geoArray.add(newObj); 
		}
		
		
		return geoArray;
		
	}
	
	
	/**
	 * Method to make request to Wikpedia APi to access page extract
	 * @param pageName, the name of the page 
	 * @return a string JSON object the API returns
	 * @throws IOException if connection to API fails
	 */
	public static String makeRequestExtract (String pageName) throws IOException {
		
		pageName= pageName.replace(" ", "_");
		
		URL url = null; 
		
		try {
			url = new URL("https://en.wikipedia.org/w/api.php?action=query&titles=" +pageName+ "&prop=pageimages|extracts&format=json&pithumbsize=500&exintro=&explaintext=");
					
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
			
				storeExtractLength(sb.toString().length()); 
			
			return sb.toString();
		
	}
	
	/**
	 * Method to get the extract from the data returned by API
	 * 
	 * @param stringJson, the JSON object returned by the API
	 * @return the extract as a string
	 */
	public static String getExtract(String stringJson) {

		String[] splitString = stringJson.split("\"extract\":");

		if (splitString.length > 1) {
			String extract = splitString[1];
			findImage(splitString[0]);
		
			return extract.replace("\"", " ");

		} else {
			
			return "";
		}
	}

	public static void findImage(String input) {

		String[] splitString = input.split("\"source\":\"");
		if (splitString.length > 1) {

			String photoUrl = splitString[1];
			
			storeImageUrl(photoUrl.substring(0, photoUrl.length() - 2));

		} else {
			storeImageUrl("no image");
	
		}
	}

	/**
	 * Method to reduce extract to only 2 sentences
	 * 
	 * @param extract,
	 * @return a shortened extract
	 */
	public static String reduceExtract (String extract) {
		
		int count = 0;
		
		for (int i = 0; i< extract.length(); i++) {
		
			if( extract.charAt(i) == '.' && (extract.charAt(i +1) == ' ' || extract.charAt(i +1) == '\\') ) {
				count++;
				
				if(count== 2) {
					return extract.substring(0, i); 
				}
			}
			
		}
		
	
		return extract.replace('}', ' '); 
		
	}
	/**
	 * Method to remove brackets
	 * @param extract
	 * @return
	 */
	public static String removeBrack (String extract) {
		return extract.replaceAll("\\(.*\\)", ""); 
	}
	
	/**
	 * Method to set the extract to the geosearch object
	 * @param geoArray
	 * @throws IOException
	 */
	public static void setWikiExtract (ArrayList<Geosearch> geoArray) throws IOException {
		if (geoArray !=null) {
		for(int i = 0; i<geoArray.size(); i++) {
			geoArray.get(i).setExtract(
					removeBrack(
					reduceExtract(
					    getExtract(
							makeRequestExtract(geoArray.get(i).getOldTitle()))
							) 	
					)
				);
			geoArray.get(i).setExtractLength(wikiLength);
		
			geoArray.get(i).setSource(imageUrl);
			
		}
		}
	}
	
	public static void storeExtractLength(int length) {
	
		if (length != 0) {
			wikiLength = length; 
		
		}
		
	}
	
	public static void storeImageUrl(String imageUrlPara) {
		if (imageUrlPara != null) {
			
			imageUrl = imageUrlPara; 
			
		}
	}
	
	/**
	 * Method to make all the method calls in the correct order 
	 * @param lat, latitude of searched position 
	 * @param lon, longitude of searched position 
	 * @param radius, radios of the search in metres
	 * @return an array list of nearby places with correct extracts 
	 */
	public static ArrayList<Geosearch> gather (double lat, double lon, int radius ) {
		
		String stringURL = buildUrl(lat, lon, radius); 
		ArrayList<Geosearch> geoArray = new ArrayList<Geosearch>();
		
		try {
			
			geoArray= JSONtoPOJO(makeRequest(stringURL));
			setWikiExtract(geoArray);
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		return geoArray; 
		
	}
	
	public static void main (String[] args) {
		
		ArrayList<Geosearch> gath = gather(43.76964697468,11.25519007444 , 2000);
		for(int i = 0 ; i <gath.size(); i++) {
			System.out.println(gath.get(i).toString());
			
		}
		//System.out.println(reduceExtract(" Piazza della Signoria (Italian pronunciation: [\\u02c8pjattsa della si\\u0272\\u0272o\\u02c8ri\\u02d0a]) is an L-shaped square in front of the Palazzo Vecchio in Florence, Italy. It was named after the Palazzo della Signoria, also called Palazzo "));
		
	}
}


