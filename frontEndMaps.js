// INITIAL SETUP/global variables
var countyMarkers = []

var tmpPoint = {
  center: new google.maps.LatLng(41.878113, -87.629798),
  zipcode: 2714856,
  name:"Chicago"
};
countyMarkers.push(tmpPoint);
tmpPoint = {
  center: new google.maps.LatLng(34.052234, -118.243684),
  zipcode: 3857799,
  name:"New York"
};
countyMarkers.push(tmpPoint);

tmpPoint = {
  center: new google.maps.LatLng(49.25, -123.1),
  zipcode: 603502,
  name:"Vancouver"
};
countyMarkers.push(tmpPoint);


var UICircle;
var genericMarker;
var centerMarker;
var markers = []; 	// reference container for markers

// key variables
var primaryCenter;	// long/lat of center of circle
var currentK;		// current "k" closest"
var radius;			// radius of the circle
var infowindow;

///////////////////////////////////////////////////////////////////////////
// UI button setup
function UIControls(map) {
  
  
	// Create a div to hold everything else
	//var outterDiv = document.createElement('DIV');
	var controlDiv = document.createElement('DIV');
	controlDiv.id = "controls";
	controlDiv.style.padding = '5px';
	controlDiv.style.backgroundColor = 'white';
	controlDiv.style.borderStyle = 'solid';
	controlDiv.style.borderWidth = '1px';
			   
	// Create an input field
	var controlInput = document.createElement('input');
	controlInput.id = "some-information";
	controlInput.name = "some-information";
	controlInput.value = 1;

	// Create a label
	var controlLabel = document.createElement('label');
	controlLabel.innerHTML = 'Find nearest k counties, k=';
	controlLabel.setAttribute("for","some-information");

	// Create a button to send the information
	var controlButton = document.createElement('a');
	controlButton.innerHTML = 'Send it!';

	// Append everything to the wrapper div
	controlDiv.appendChild(controlLabel);
	controlDiv.appendChild(controlInput);
	controlDiv.appendChild(controlButton);

	var onClick = function() {
		var inputvalue = $("#some-information").val();
		alert("You entered: '" + inputvalue + "', map center is at " + map.getCenter());
	};
	google.maps.event.addDomListener(controlButton, 'click', onClick);
	map.controls[google.maps.ControlPosition.TOP_RIGHT].push(controlDiv);
	
	
	
  
  /*  
  // DIV for text input, far right
  var controlDiv2 = document.createElement('DIV');
  controlDiv2.style.padding = '5px';

  var controlInput = document.createElement('INPUT');
  controlInput.name = "nearestKinput";
  controlInput.type = "text";
  controlInput.style.borderWidth = '2px';
  controlDiv2.appendChild(controlInput);

  //control javascript (e.g. any handlers that you need)
  var myControl = UInearestK(controlDiv2)
  
  map.controls[google.maps.ControlPosition.TOP_RIGHT].push(controlDiv2);
  
  
  
  
  
  // SETUP for the text "Find nearest {integer, 0-999}"
  controlDiv = document.createElement('div');
  controlDiv.style.padding = '5px';

  // Set CSS for the control border
  var controlUI = document.createElement('div');
  controlUI.style.backgroundColor = 'white';
  controlUI.style.borderStyle = 'solid';
  controlUI.style.borderWidth = '1px';
  controlUI.style.cursor = 'pointer';
  controlUI.style.textAlign = 'center';
  controlUI.title = 'Click to set the map to Home';
  controlDiv.appendChild(controlUI);

  // Set CSS for the control interior
  var controlText = document.createElement('div');
  controlText.style.fontFamily = 'Arial,sans-serif';
  controlText.style.fontSize = '12px';
  controlText.style.paddingLeft = '4px';
  controlText.style.paddingRight = '4px';
  controlText.innerHTML = 'Find nearest {integer, 0-999}:';
  controlUI.appendChild(controlText);
  
  map.controls[google.maps.ControlPosition.TOP_RIGHT].push(controlDiv);


  // Setup map for click listeners // NO listener for this, just text UI object
  //google.maps.event.addDomListener(controlUI, 'click', function() {
  //  map.setCenter(chicago)
  //});
  
  
  */
  

}

function UInearestK(div){
	console.log('HELLO?');
}


function drawCities(map){
  // first, delete ALL markets
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
  markers = [];
  
  
  // then, draw all the new ones
  for (var county in countyMarkers){
  
  	data = countyMarkers[county].zipcode + "<br /><br /><hr />Coordinate: " + countyMarkers[county].center;
  	var markerOptions = {
  		map: map,
      	//icon: 'images/markerGreen.png',
      	//draggable:true,
      	position: countyMarkers[county].center,
      	data: data
    };
    genericMarker = new google.maps.Marker(markerOptions);
    markers.push(genericMarker);
    
    // add listener for each pin
    
    // Listen for click event   // currently just does on the first one..
	google.maps.event.addListener(genericMarker, 'click', function() { 
		onItemClick(event, genericMarker, map); 
	});
    
  }

}


// for the info window popup
function onItemClick(event, pin, map) { 
  // Create content  
  var contentString = pin.data.text + "<br /><br /><hr />Coordinate: " + pin.data.lng +"," + pin.data.lat; 
  
  infowindow = new google.maps.InfoWindow({
      content: contentString
  });
    
  // Replace our Info Window's content and position 
  infowindow.setContent(contentString); 
  infowindow.setPosition(pin.position); 
  infowindow.open(map) 
} 

///////////////////////////////////////////////////////////////////////////
// INITIALIZE FUNCTIOn (contains all)
function initialize() {
  
  // SETUP of map and style
  primaryCenter = new google.maps.LatLng(41.070719,-95.4165768);
  var mapOptions = {
    zoom: 5,
    center: primaryCenter,
    disableDefaultUI: true,
    zoomControl: true,
    zoomControlOptions: {
      style: google.maps.ZoomControlStyle.SMALL
    },
    scaleControl: true
  }
  
  // limit view bounds and regions, etc
  // NE: 48.0426795,-59.6880584
  // SW: 35.8640672,-126.2805807
  var strictBounds = new google.maps.LatLngBounds(
	  new google.maps.LatLng(48.04, -59.70),
	  new google.maps.LatLng(35.86, -126.29) 
  );
  
  var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
  //infowindow = new google.map.InfoWindow();  // for showing pin info
  
  // STYLES and UI SETUP 
  map.set('styles', [
  {
	featureType: 'road',
	elementType: 'geometry',
	stylers: [
	  { visibility: 'off'}
	]
  },{
	featureType: 'landscape',
	elementType: 'geometry',
	stylers: [
	  { hue: '#ffff00' },
	  { gamma: 1.4 },
	  { saturation: 82 },
	  { lightness: 96 }
	]
  },{
	  featureType: 'all',
	  elementType: 'labels',
	  stylers: [
		{ visibility: 'off' }
	  ]
	}
  ]);
  
  // Setup of the UI controls
  var UICont = new UIControls(map);

  
  // OTHER STUFF
  drawCities(map)
  
  var circleOptions = {
      strokeColor: '#FF0000',
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: '#FF0000',
      fillOpacity: 0.35,
      map: map,
      center: primaryCenter, //LatLng object
      radius: 500000, // in meters
      editable: true
    };
  UICircle = new google.maps.Circle(circleOptions)
  
  
  // LISTENERS
  
  google.maps.event.addListener(UICircle, 'radius_changed', function() {
  	console.log(UICircle.getRadius());
  	// pass to back-end stuff here?
  	drawCities(map)
  });
  
	google.maps.event.addListener(UICircle, 'center_changed', function() {
	primaryCenter = UICircle.center;
  	console.log(UICircle.getCenter());
  	
  	// pass to back-end stuff here?
  	drawCities(map)
  });
  
  
  // single click, change center coordinates/move the 
  google.maps.event.addListener(map, 'click', function(e) {
    primaryCenter = e.latLng
    UICircle.setCenter(primaryCenter);
    // pass to back-end stuff here?
    drawCities(map)
    
  });
  
  
//   google.maps.event.addListener(map, 'center_changed', function() {
//     // 3 seconds after the center of the map has changed, pan back to the
//     // marker.
//     window.setTimeout(function() {
//       map.panTo(marker.getPosition());
//     }, 3000);
//   });
  
}


///////////////////////////////////////////////////////////////////////////
// Final call, to initialize after the page has loaded
google.maps.event.addDomListener(window, 'load', initialize);
