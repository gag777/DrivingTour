
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="format.css">

<title>Driving Tour</title>
</head>
<body onload=getLocation()>

	<h1>Explore Your Surroundings</h1>
	<p class = "para">This application will help you understand what is around you as you move from place to place. 
	To get started, ensure you have enabled location tracking on your browser, select what interval time you would like below  and click the ‘Go’ button.  
	If you are driving, remember to focus on the road, if you wish to set a route to a location you have heard, pull over first.
	Enjoy!.</p>
	
		<p class ="choose">Choose an interval time</p>
	<form action="ServletWeb" id="start" >
		<input type="hidden" name="lat" id="lat" value="" /> 
		<input type="hidden" name="lon" id="lon" value="" /> 
		<input type="hidden" name="demoStatus" id="demoStatus" value="false" /> 
		<div class="options">
		<select name ="interval" >
			<option value="20">20</option>
			<option value="30">30</option>
			<option value="40">40</option>
			<option value="50">50</option>
			<option value="60">60</option>
		</select>
		</div>
	</form>
	
	<script>
		
 		function getLocation() {
			
			if (navigator.geolocation) { 
				
				navigator.geolocation.getCurrentPosition(showPosition);

			} else {
				
				alert("Geolocation is not supported.");
			}
		}

		function showPosition(position) {
			
			document.getElementById("lat").value = position.coords.latitude;
			document.getElementById("lon").value = position.coords.longitude;
			revealButton(); 
		} 
		
		</script>

	<div>
		<button type="button" onClick="submit()" class="inputButton" id ="inputButton" style="display: none;" >Go</button>
		<button type="button" onClick="revealForm()" class="demoButton">Demo</button>
	</div>

	<div style="display: none;" id="demo">
		<form action="ServletWeb" id="startDemo">
			<input type="text" name="latDemo" id="latDemo"
				value="51.5106931040" /> <input type="text" name="lonDemo"
				id="lonDemo" value="-0.0897346436977" /> <input type="hidden"
				name="demoStatus" id="demoStatus" value="true" /> 
	
				<select name="interval" id="interval">
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
					<option value="50">50</option>
					<option value="60">60</option>
				</select>
			<input type="submit" value="Go" />
		</form>
	</div>

	<script>
		function submit(){
			document.getElementById('start').submit();
		}
	</script>

	<script>
		function revealForm() {
		   document.getElementById('demo').style.display = "block";
		}
		function revealButton() {
			   document.getElementById('inputButton').style.display = "block";
			}
		
	</script>

</body>
</html>




