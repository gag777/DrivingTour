package Ranking;

import WikipediaSearch.Geosearch;

public class Ranker {

	public static double rank(Geosearch obj, String dist, double rating) {

		if (rating < 0) {
			rating = 0;
		}

		int searchResults = Integer.parseInt(obj.getNoOfResults());
		int length = obj.getExtractLength();
		int noOfRankings = obj.getNoOfRatings();

		int points = 0;
		if (length > 1000) {

			points = points + 200;
		}

		if (obj.hasGooglePlace()) {
			points = points + 200;

		}

		if (noOfRankings != 0) {
			points = points + 200;

		}

		if (Double.parseDouble(dist) < 1000.0) {
			points = points + 200;
		}

		return (rating * 20) + searchResults + points;

	}

}



