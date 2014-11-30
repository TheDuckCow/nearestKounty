// INITIAL SETUP/global variables
var countyMarkers = []

var tmpPoint = {
  center: new google.maps.LatLng(41.878113, -87.629798),
  zipcode: 2714856,
  name:"Chicago"
};
countyMarkers.push(tmpPoint);
tmpPoint = {
  center: new google.maps.LatLng(40.7127, -74.0059),
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
//var genericMarker;
var markers = []; 	// reference container for markers

// key variables
var primaryCenter;	// long/lat of center of circle
var currentK = 20;		// current "k" closest"
var radius = 500000;			// radius of the circle
//var infowindow;


///////////////////////////////////////////////////////////////////////////
// BACKEND stuff
function backendSendK(){
	console.log("Sending K: "+currentK+", at "+primaryCenter);
	
	// clear all values of "countyMarkers" and load in new values from backend
	
	// radius = //get updated radius from backend
	
	UICircle.setRadius(radius);
}

function backendSendRadius(){
	console.log("Sending current radius: "+radius+", at "+primaryCenter);
	
	UICircle.setRadius(radius); // creates a loop!
	//UICircle.set ('radius',radius);
	
	// clear all values of "countyMarkers" and load in new values from backend
	// currentK = //get updated K from backend
	
}

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
	
	
	// Create a label
	var infoLabel = document.createElement('label');
	infoLabel.innerHTML = '<b>County Finder</b><br>Click the map to select the starting reference point. <br>Set the number of counties you would like returned below.<br>K reference points: ';
	infoLabel.setAttribute("for","infoLable");
	
		   
	// Create an dropdown list
  var dropDownList = document.createElement('select');
  dropDownList.setAttribute('class', 'k');

  for(var i = 1; i < 11; i++) {
    var opt = document.createElement("option");
    opt.value= i;
    opt.innerHTML = i; // whatever property it has

    // then append it to the select element
    dropDownList.appendChild(opt);
  }


	// // Create a label
	// var controlLabel = document.createElement('label');
	// controlLabel.innerHTML = '<br>Find nearest k counties, k=';
	// controlLabel.setAttribute("for","some-information");

	// // Create a button to send the information
	// var controlButton = document.createElement('a');
	// controlButton.innerHTML = ' Press to set K!';
	
	
	// // second row!
	// // Create an input field
	// var controlInputR = document.createElement('input');
	// controlInputR.id = "radius-information";
	// controlInputR.name = "radius-information";
	// controlInputR.value = radius;

	// // Create a label
	// var controlLabelR = document.createElement('label');
	// controlLabelR.innerHTML = '<br>Radius, r(meters)=';
	// controlLabelR.setAttribute("for","radius-information");

	// // Create a button to send the information
	// var controlButtonR = document.createElement('a');
	// controlButtonR.innerHTML = ' Press to set Radius!';


	// Append everything to the wrapper div
	controlDiv.appendChild(infoLabel);
	controlDiv.appendChild(dropDownList);
	// controlDiv.appendChild(controlInput);
	// controlDiv.appendChild(controlButton);
	// controlDiv.appendChild(controlLabelR);
	// controlDiv.appendChild(controlInputR);
	// controlDiv.appendChild(controlButtonR);
	
	// ie when you click on "send it" in the top right corner
	var onClickK = function() {
		currentK=controlInput.value;
		console.log('BUTTON: Setting new k value of '+currentK);
		
		// pass to back-end stuff here?
		backendSendK();
  		drawCities(map);
		
	};
	//google.maps.event.addDomListener(controlButton, 'click', onClickK);
	
	// ie when you click on "send it" in the top right corner
	var onClickR = function() {
		radius=Number(controlInputR.value);
		console.log('BUTTON: Setting new radius value: '+radius);
		
		// pass to back-end stuff here?
		backendSendRadius();
  		drawCities(map);
		
	};
	
	//google.maps.event.addDomListener(controlButtonR, 'click', onClickR);
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


// function for refreshing the page with new markers, only one to drop markers
function drawCities(map){
  // first, delete ALL markers
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
  markers = [];
  
  // then, draw all the new ones
  for (var county in countyMarkers){
  
  	data = "<b>"+countyMarkers[county].zipcode + "</b><hr/>Coordinate: " + countyMarkers[county].center+"<br>Name: "+countyMarkers[county].name;
  	var markerOptions = {
  		map: map,
      	//icon: 'images/markerGreen.png',
      	//draggable:true,
      	position: countyMarkers[county].center,
      	data: data,
      	clickable: true
    };
    var genericMarker = new google.maps.Marker(markerOptions);
    
    genericMarker.info = new google.maps.InfoWindow({
	  content: data
    });
    
    markers.push(genericMarker);
    google.maps.event.addListener(genericMarker, 'click', function() {
	this.info.open(map, this);	  
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
  //42.3581° N, 71.0636° W
  primaryCenter = new google.maps.LatLng(42.3581,-71.0636);
  var mapOptions = {
    zoom: 8,
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
      radius: radius, // in meters
      editable: true
    };
    
  var rectangleOptions = {
      strokeColor: '#FF0000',
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: '#FF0000',
      fillOpacity: 0.35,
      map: map,
      draggable: true,
      bounds: new google.maps.LatLngBounds(
        new google.maps.LatLng(42.1581, -72.1636),
        new google.maps.LatLng(42.4581, -71.0136)),
      editable: true  
  }
  //42.3581° N, 71.0636° W
  UICircle = new google.maps.Rectangle(rectangleOptions);
  updateBounds();

  // Add an event listener on the rectangle.
  google.maps.event.addListener(UICircle, 'bounds_changed', updateBounds);
  
  
  // LISTENERS
  
  google.maps.event.addListener(UICircle, 'radius_changed', function() {
  	if (radius != UICircle.getRadius()){
  		radius = UICircle.getRadius();
  	
  		// pass to back-end stuff here?
  		backendSendRadius();
  		drawCities(map);
  	}
  });
  
  
	google.maps.event.addListener(UICircle, 'center_changed', function() {
	primaryCenter = UICircle.center;
  	
  	// pass to back-end stuff here?
  	backendSendRadius();
  	drawCities(map);
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

  function updateBounds(event) {
    var ne = UICircle.getBounds().getNorthEast();
    var sw = UICircle.getBounds().getSouthWest();

    $.ajax({
       url: "getLocationsInBound",
       type: "GET",
       data: {low_long : sw.lng(),
              low_lat : sw.lat(),
              high_long : ne.lng(),
              high_lat : ne.lat()}, 
       success: function(response){
           console.log(response);
       },
       error: function(err){
           console.log(err);
       }
    });
  }
  
}



///////////////////////////////////////////////////////////////////////////
// Final call, to initialize after the page has loaded
google.maps.event.addDomListener(window, 'load', initialize);
