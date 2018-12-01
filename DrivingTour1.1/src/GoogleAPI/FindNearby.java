package GoogleAPI;

import java.util.List;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.Prediction;
import se.walkercrou.places.Review;
import se.walkercrou.places.exception.NoResultsFoundException;
import se.walkercrou.places.exception.OverQueryLimitException;

public class FindNearby {
	
	private static String API_KEY = "AIzaSyDncfi2KQddGxmwut69cWB_O5T0DEROZms"; 
	static GooglePlaces client = new GooglePlaces(API_KEY); 
	
	public static List<Place> getNearby (double lat, double lon, int radius ){
		
		List<Place> nbPlaces = client.getNearbyPlaces(lat, lon, radius, 30);
		
		return nbPlaces;
		
	}
	
	public static List<Place> googleSearch(String placeName) {

		List<Place> places = null;

		try {
			places = client.getPlacesByQuery(placeName, GooglePlaces.MAXIMUM_RESULTS);

		} catch (Exception e) {
			System.out.println("No google place");
		

		}
		
		
	
		return places;
		
	}
	
	public static String placeToString (List<Place> places){

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<places.size(); i++){

            sb.append(places.get(i).getName());
            sb.append("  " + places.get(i).toString());
       
            sb.append("    \n " );

        }

        return sb.toString();

    }
	
	
}
