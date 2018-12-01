package WikipediaSearch;

import GoogleAPI.ControlSearch;
import Ranking.Ranker;
import se.walkercrou.places.Place;

public class Geosearch implements Comparable<Geosearch> {

	private String lon;
	private String title;
	private String lat;
	private String dist;
	private String pageid;
	private String extract;
	private Place googlePlace = null;
	private double rank;
	private int extractLength; 
	private int noOfRatings;
	private String source;
	private String noOfResults;
	private String oldTitle;

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.oldTitle=title; 
		this.title = title.replaceAll("\\(.*\\)", "");
	}


	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public String getPageid() {
		return pageid;
	}

	public void setPageid(String pageid) {
		this.pageid = pageid;
	}

	@Override
	public String toString() {
		double rating = -1.0;
		
		if (googlePlace != null) {
			//rating = googlePlace.getRating();
			rating = googlePlace.getRating();
		
		}
		return getNoOfResults() +" "+ hasGooglePlace() + "  title = " + title + " rank = " + getRank() + "  ratings = " + noOfRatings +" [lon = " + lon + ", lat = " + lat +  ", dist = " + dist
				+ "  rating = " + rating + "  extract = " + extract + "ExLength = " +extractLength + " ]";
		
	}

	public String getExtract() {
		return extract;
	}

	public void setExtract(String extract) {
		this.extract = extract;
	}

	public Place getGooglePlace() {
		return googlePlace;
	}

	public void setGooglePlace(Place googlePlace) {
		this.googlePlace = googlePlace;

	}
	
	public boolean hasGooglePlace() {
		
		if (this.googlePlace != null) {
			return true;
		}
		return false;
		
	}
	
	public void setNoOfRatings(int number){
		
		this.noOfRatings = number; 
	}
	
	public int getNoOfRatings() {
		
		return noOfRatings; 
	
	
	}

	public double getRank() {
		double rating = -1.0;
		if (googlePlace != null) {
			rating = googlePlace.getRating();
		}

		return Ranker.rank(this, dist, rating);
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	@Override
	public int compareTo(Geosearch o) {
		if (this.getRank() < o.getRank()) {
			return 1;
		} else if (this.getRank() == o.getRank()) {
			return 0;
		}
		return -1;
		
	}

	public int getExtractLength() {
		return extractLength;
	}

	public void setExtractLength(int extractLength) {
		this.extractLength = extractLength;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String souce) {
		this.source = souce;
	}

	public String getNoOfResults() {
		if( noOfResults != null) {
		return noOfResults; 
		} else
		return "0"; 
	}

	public void setNoOfResults(String noOfResults) {
		this.noOfResults = noOfResults;
	}

	public String getOldTitle() {
		return oldTitle;
	}

	public void setOldTitle(String oldTitle) {
		this.oldTitle = oldTitle;
	}
}
