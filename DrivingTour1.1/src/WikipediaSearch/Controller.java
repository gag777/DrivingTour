package WikipediaSearch;
/**
 * Class to collect and control all the information collected from other classes.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import GoogleAPI.ControlSearch;
import GoogleAPI.FindNearby;
import se.walkercrou.places.Place;

public class Controller {
	
	private ArrayList<Geosearch> WikiPlaces= new ArrayList<Geosearch>(); 
	private List<Place> Googleplaces = new ArrayList<Place>(); 
	
	/**
	 * Constructor for controller
	 * @param wikiPlaces2, arrayList of geosearch objects
	 * @param Googleplaces, list of googlePlace objects
	 */
	public Controller (ArrayList<Geosearch> wikiPlaces2, List<Place> Googleplaces) {
		this.WikiPlaces = wikiPlaces2;
		this.Googleplaces = Googleplaces; 
	}
	
	/**
	 * Setter for wikiPlaces
	 * @param lat the locations latitude
	 * @param lon, the locations longitude
	 * @param radius, the radius of the search.
	 */
	public void setWikiPlaces (double lat, double lon, int radius ) {
		
		this.WikiPlaces = Wiki.gather(lat, lon, radius);
		
	}
	
	/**
	 * Setter for GooglePlaces
	 * @param lat the locations latitude
	 * @param lon, the locations longitude
	 * @param radius, the radius of the search.
	 */
	public void setGooglePlaces (double lat, double lon, int radius ) {
		this.Googleplaces =  FindNearby.getNearby(lat, lon, radius);
		
	}
	
	/**
	 * Getter for wikiPlaces
	 * @return arrayList of Geosearch objects
	 */
	public ArrayList<Geosearch> getWikiPlaces(){
		return this.WikiPlaces;
		
	}
	
	/**
	 * Method to merge together the information in both the wikiPlaces and
	 * GooglePlaces
	 */
	public void merger() {

		for (int i = 0; i < WikiPlaces.size(); i++) {
			String wikiTitle = WikiPlaces.get(i).getTitle();

			for (int j = 0; j < Googleplaces.size(); j++) {

				String googleTitle = Googleplaces.get(j).getName();

				if (wikiTitle.equals(googleTitle) || wikiTitle.contains(googleTitle)
						|| googleTitle.contains(wikiTitle)) {

					if (matchCoordinates(WikiPlaces.get(i).getLon(), WikiPlaces.get(i).getLat(),
							String.valueOf(Googleplaces.get(j).getLongitude()),
							String.valueOf(Googleplaces.get(j).getLatitude()))) {

						WikiPlaces.get(i).setGooglePlace(Googleplaces.get(j));
						Place detailed = Googleplaces.get(j).getDetails();
						WikiPlaces.get(i).setNoOfRatings(detailed.getReviews().size());
						
			
					}
				}
			}
		}

	}

	/**
	 * Method to match the coordinates of wikiPlaces locations and googlePlace locations
	 * @param wikiCoordLon, longitude of wikiPlace
	 * @param wikiCoordLat, latitude of wikiPlace
	 * @param googleCoordLon, longitude of GooglePlace
	 * @param googleCoordLat, Latitude of GooglePlace
	 * @return, true if locations are a near (not entirely accurate match)
	 */
	public boolean matchCoordinates(String wikiCoordLon, String wikiCoordLat, String googleCoordLon, String googleCoordLat) {
	
		if ( wikiCoordLon.isEmpty() || googleCoordLon.isEmpty() || wikiCoordLat.isEmpty() || googleCoordLat.isEmpty() ) {
			throw new IllegalArgumentException("One place doesnt have coordinates available");
		}
		
		if (wikiCoordLon.substring(0, 4).equals(googleCoordLon.substring(0, 4)) && 
				wikiCoordLat.substring(0, 4).equals(googleCoordLat.substring(0, 4)) ) {
			
			return true;
		}
		
		else return false; 
		
	}
	
	public void searchGoogle () {
		
		if (WikiPlaces !=null) {
		for (int i = 0; i< WikiPlaces.size(); i++) {
			List<Place> search = FindNearby.googleSearch(WikiPlaces.get(i).getOldTitle());
			if ( search !=null) {
				WikiPlaces.get(i).setGooglePlace(search.get(0));
			}
		}
		}
	}
	
	/**
	 * Method to rid list of places with no google place match 
	 * @param WikiPlaces
	 * @return a reduced list
	 */
	public static ArrayList<Geosearch> reduce (ArrayList<Geosearch> WikiPlaces){
		for (int i = 0; i< WikiPlaces.size(); i++) {
			if (!WikiPlaces.get(i).hasGooglePlace()) {
				WikiPlaces.remove(i);
			}
		}
			return WikiPlaces; 
	}
	
	/**
	 * method to search for result number as set to object
	 * @param wiki, geosearch arraylist 
	 */
	public static void resultSearch(ArrayList<Geosearch> wiki) {
		
		for(int i = 0; i < wiki.size(); i ++ ) {
			String results = ControlSearch.makeAndSet(wiki.get(i).getOldTitle());
			wiki.get(i).setNoOfResults(results);
		}
		
	}
	
	/**
	 * Method to call other methods
	 * 
	 * @param lat the locations latitude
	 * @param     lon, the locations longitude
	 * @return
	 */
	public static ArrayList<Geosearch> call(String lat, String lon) {

		ArrayList<Geosearch> WikiPlaces = new ArrayList<Geosearch>();
		List<Place> Googleplaces = new ArrayList<Place>();

		Controller a = new Controller(WikiPlaces, Googleplaces);
		
		try {
			a.setWikiPlaces(Double.valueOf(lat), Double.valueOf(lon), 2000);
			
		} catch (NumberFormatException e) {
			WikiPlaces = null;
		}
		
		if (WikiPlaces != null) {
			a.searchGoogle();
			WikiPlaces = a.getWikiPlaces();
			resultSearch(WikiPlaces); 

			Collections.sort(WikiPlaces);
			
			Iterator<Geosearch> iter = WikiPlaces.iterator();
			while (iter.hasNext()) {
				Geosearch obj = iter.next();
				if (!obj.hasGooglePlace() || obj.getTitle().contains("Timeline")) {
					iter.remove();
				}

			}

			return WikiPlaces;
			
		} else
			return null;
	}
	

	
	public static void main (String[] args) {
		ArrayList<Geosearch> WikiPlaces= new ArrayList<Geosearch>(); 
		List<Place> Googleplaces = new ArrayList<Place>(); 
		
		Controller a = new Controller(WikiPlaces, Googleplaces );
		//a.setWikiPlaces(52.449830656323, -1.933765408876, 2000);
	   // a.setWikiPlaces(52.20334691487669, 0.1219821721315384, 2000); // cambridge
		//a.setWikiPlaces(53.56177406597971, -1.765713952481747, 2000);
	   // a.setWikiPlaces (51.275, 1.087, 3000); //cants 
	    a.setWikiPlaces (51.75004242951, -1.2623709440231, 3000); // oxford

		WikiPlaces = a.getWikiPlaces(); 
	
		a.searchGoogle(); 
		resultSearch(WikiPlaces); 
		
		if (WikiPlaces !=null) {
		Collections.sort(WikiPlaces);
		
		
		Iterator<Geosearch> iter = WikiPlaces.iterator();
		while(iter.hasNext()) {
			Geosearch obj = iter.next(); 
			if (!obj.hasGooglePlace() || obj.getTitle().contains("Timeline")) {
				iter.remove();
			}
		
		}

		
		for (int i = 0; i< WikiPlaces.size(); i++) {

		System.out.println(WikiPlaces.get(i).toString());
		
		}
		
	}
	}
}
