package Server;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import GoogleAPI.FindNearby;
import WikipediaSearch.Controller;
import WikipediaSearch.Geosearch;
import WikipediaSearch.Wiki;
import se.walkercrou.places.Place;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/Servlet")
public class ServletWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ArrayList<Geosearch> completed = new ArrayList<Geosearch>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletWeb() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		boolean demoMode;
		String demoStatus = request.getParameter("demoStatus");
		if (demoStatus.equals("true")) {
			demoMode = true;
		} else {
			demoMode = false;
		}

		String intervalTime = request.getParameter("interval");
		int timeNumber = Integer.parseInt(intervalTime) * 1000;

		String lat;
		String lon;

		if (demoMode) {
			lat = request.getParameter("latDemo");
			lon = request.getParameter("lonDemo");

		} else {
			lat = request.getParameter("lat");
			lon = request.getParameter("lon");
		}

		String completedString = request.getParameter("completed");
		String[] completed = null;
		if (completedString != null) {
			completed = completedString.split("_");
		}

		ArrayList<Geosearch> geo = new ArrayList<Geosearch>();

		try {
			geo = Controller.call(lat, lon);
		} catch (NullPointerException e) {
			System.out.print("search returns null");
			;
		}

		if (geo == null) {
			geo = new ArrayList<Geosearch>();
		}

		if (completed != null) {
			for (int i = 0; i < completed.length; i++) {
				for (int j = 0; j < geo.size(); j++) {
					if (geo.get(j).getTitle().equals(completed[i])) {
						geo.remove(j);
					}
				}
			}
		}

		if (!geo.isEmpty()) {
			String b = geo.get(0).getTitle();
			String c = geo.get(0).getExtract();

			String imgUrl;
			if (geo.get(0).getSource().equals("no image")) {
				imgUrl = " pinImage.png\"  style=\"width:300px;height:400px;"; // Default image

			} else {
				imgUrl = geo.get(0).getSource();
			}

			PrintWriter out = response.getWriter();

			try {
				out.println("<!DOCTYPE html>");
				out.println("<html><head>");
				out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
				out.println(" <link rel=\"stylesheet\" type=\"text/css\" href=\"PlaceStyle.css\"> ");
				out.println("<title> Driving Tour </title></head>");
				out.println("<body onload=\"speak(); getLocation()\">");
				out.println("<a href=\"index.jsp\">Home</a>");
				out.println("<h1 > " + b + " </h1>");
				out.println("<p> " + geo.get(0).getDist() + "m away</p>");

				out.println("<input type=\"hidden\" name=\"javaString\" id=\"result\" value=\" " + b + " \" />");
				out.println("<input type=\"hidden\" name=\"javaString2\" id=\"result2\" value=\" " + c + " \" />");
				out.println(" <img src =\"" + imgUrl + " \">");

				out.println(" <script >");
				out.println("function speak(){ ");
				out.println("var message = new SpeechSynthesisUtterance(document.getElementById(\"result\").value); ");
				out.println("	window.speechSynthesis.speak(message);");
				out.println(
						"var message2 = new SpeechSynthesisUtterance(document.getElementById(\"result2\").value); ");
				out.println("	window.speechSynthesis.speak(message2);} ");
				out.println(" </script >");

				if (!demoMode) {
					out.println("  <form id = \"searchForm\" action=\"ServletWeb\" >\n"
							+ "	 		<input type=\"hidden\" name=\"lat\" id=\"lat\" value=\"\" /> \n"
							+ "	 		<input	type=\"hidden\" name=\"lon\" id=\"lon\" value=\"\" />");
					out.println("<input type=\"hidden\" name=\"completed\" id=\"completed\" value=\"" + b + "_"
							+ getCompleted(completedString) + "\" />");
					out.println(" <input type=\"hidden\" name=\"demoStatus\" id=\"demoStatus\" value=\"false\" />");
					out.println(" <input type=\"hidden\" name=\"interval\" id=\"interval\" value=\"" + intervalTime
							+ "\" />");
					out.println("</form> ");

					out.println(" <script >");
					out.println(" function getLocation() {\n" + "			if (navigator.geolocation) { \n"
							+ "				navigator.geolocation.getCurrentPosition(showPosition);\n"
							+ "			} else {\n" + "				x.innerHTML = \"Geolocation is not supported.\";\n"
							+ "			}\n" + "		}");
					out.println("	function showPosition(position) {\n"
							+ "			document.getElementById(\"lat\").value = position.coords.latitude;\n"
							+ "			document.getElementById(\"lon\").value = position.coords.longitude;\n"
							+ "		} ");
					out.println(" </script >");

				} else {

					out.println("  <form id = \"searchFormDemo\" action=\"ServletWeb\" >\n"
							+ "	 		<input type=\"hidden\" name=\"latDemo\" id=\"latDemo\" value=\"" + lat
							+ "\" /> \n"
							+ "	 		<input	type=\"hidden\" name=\"lonDemo\" id=\"lonDemo\" value=\"" + lon
							+ "\" />");
					out.println("<input type=\"hidden\" name=\"completed\" id=\"completed\" value=\"" + b + "_"
							+ getCompleted(completedString) + "\" />");
					out.println(" <input type=\"hidden\" name=\"demoStatus\" id=\"demoStatus\" value=\"true\" />");
					out.println(" <input type=\"hidden\" name=\"interval\" id=\"interval\" value=\"" + intervalTime
							+ "\" />");
					out.println("</form> ");
				}

				if (!demoMode) {
					out.println(" <script >");
					out.println("setTimeout( function submit() { ");
					out.println("document.getElementById('searchForm').submit();} , " + timeNumber + ");");
					out.println("window.onload = submit;");
					out.println("");
					out.println(" </script >");

				} else {
					out.println(" <script >");
					out.println("setTimeout( function submit() { ");
					out.println("document.getElementById('searchFormDemo').submit();} ," + timeNumber + ");");
					out.println("window.onload = submit;");
					out.println(" </script >");

				}

				String name = "";
				if (geo.get(0).hasGooglePlace()) {
					name = geo.get(0).getGooglePlace().getName();
				} else {
					name = geo.get(0).getLat().toString() + "," + geo.get(0).getLon().toString();
				}

				out.println("	<form action=\"https://www.google.com/maps/place/" + name + "\">");
				out.println(" <input type=\"submit\" value=\"Go to Google\" class= \"googleButton\" />");
				out.println(" </form>");
				out.println("");

				out.println("</body>");
				out.println("</html>");
			} finally {
				out.close();

			}

		} else {

			// When there are no results...
			PrintWriter out = response.getWriter();

			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println(" <link rel=\"stylesheet\" type=\"text/css\" href=\"PlaceStyle.css\"> ");
			out.println("<title> Driving Tour </title></head>");
			out.println("<body onload=\"getLocation()\">");
			out.println("<a href=\"index.jsp\">Home</a>");
			out.println("<h1 > " + "Searching for locations near you" + " </h1>");

			if (!demoMode) {
				out.println("  <form id = \"searchForm\" action=\"ServletWeb\" >\n"
						+ "	 		<input type=\"hidden\" name=\"lat\" id=\"lat\" value=\"\" /> \n"
						+ "	 		<input	type=\"hidden\" name=\"lon\" id=\"lon\" value=\"\" />");
				out.println("<input type=\"hidden\" name=\"completed\" id=\"completed\" value=\""
						+ getCompleted(completedString) + "\" />");
				out.println(" <input type=\"hidden\" name=\"demoStatus\" id=\"demoStatus\" value=\"false\" />");
				out.println(
						" <input type=\"hidden\" name=\"interval\" id=\"interval\" value=\"" + intervalTime + "\" />");
				out.println("</form> ");

				out.println(" <script >");
				out.println(" function getLocation() {\n" + "			if (navigator.geolocation) { \n"
						+ "				navigator.geolocation.getCurrentPosition(showPosition);\n"
						+ "			} else {\n" + "				x.innerHTML = \"Geolocation is not supported.\";\n"
						+ "			}\n" + "		}");
				out.println("	function showPosition(position) {\n"
						+ "			document.getElementById(\"lat\").value = position.coords.latitude;\n"
						+ "			document.getElementById(\"lon\").value = position.coords.longitude;\n"
						+ "		} ");
				out.println(" </script >");

			} else {

				out.println("  <form id = \"searchFormDemo\" action=\"ServletWeb\" >\n"
						+ "	 		<input type=\"hidden\" name=\"latDemo\" id=\"latDemo\" value=\"" + lat + "\" /> \n"
						+ "	 		<input	type=\"hidden\" name=\"lonDemo\" id=\"lonDemo\" value=\"" + lon + "\" />");
				out.println("<input type=\"hidden\" name=\"completed\" id=\"completed\" value=\""
						+ getCompleted(completedString) + "\" />");
				out.println(" <input type=\"hidden\" name=\"demoStatus\" id=\"demoStatus\" value=\"true\" />");
				out.println(
						" <input type=\"hidden\" name=\"interval\" id=\"interval\" value=\"" + intervalTime + "\" />");
				out.println("</form> ");
			}

			if (!demoMode) {
				out.println(" <script >");
				out.println("setTimeout( function submit() { ");
				out.println("document.getElementById('searchForm').submit();} , " + timeNumber + ");");
				out.println("window.onload = submit;");
				out.println("");
				out.println(" </script >");

			} else {
				out.println(" <script >");
				out.println("setTimeout( function submit() { ");
				out.println("document.getElementById('searchFormDemo').submit();} ," + timeNumber + ");");
				out.println("window.onload = submit;");
				out.println(" </script >");

			}
			out.println("</body>");
			out.println("</html>");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public String getCompleted(String comp) {
		if (comp != null) {
			return comp;
		} else
			return "";
	}

}
