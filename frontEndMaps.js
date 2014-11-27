// INITIAL SETUP/global variables
var countyMarkers = {};
countyMarkers['chicago'] = {
  center: new google.maps.LatLng(41.878113, -87.629798),
  population: 2714856
};
countyMarkers['newyork'] = {
  center: new google.maps.LatLng(40.714352, -74.005973),
  population: 8405837
};
countyMarkers['losangeles'] = {
  center: new google.maps.LatLng(34.052234, -118.243684),
  population: 3857799
};
countyMarkers['vancouver'] = {
  center: new google.maps.LatLng(49.25, -123.1),
  population: 603502
};

var UICircle;
var genericMarker;

///////////////////////////////////////////////////////////////////////////
// UI button setup
function UIControls(map) {

    
  // DIV for text input, far right
  var controlDiv2 = document.createElement('DIV');
  controlDiv2.style.padding = '5px';

  var controlInput = document.createElement('INPUT');
  controlInput.name = "nearestKinput";
  controlInput.type = "text";
  controlInput.style.borderWidth = '2px';
  controlDiv2.appendChild(controlInput);

  //control javascript (e.g. any handlers that you need)
  //var myControl = MyControl(controlDiv2)
  
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
  

}


///////////////////////////////////////////////////////////////////////////
// INITIALIZE FUNCTIOn (contains all)
function initialize() {
  
  // SETUP of map and style
  var myLatlng = new google.maps.LatLng(41.070719,-95.4165768);
  var mapOptions = {
    zoom: 5,
    center: myLatlng,
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
  
  
  
  var image = 'images/markerBlue.png';
  var myLatLng = new google.maps.LatLng(41.070719,-95.4165768);
  
  for (var county in countyMarkers){
  	var markerOptions = {
  		map: map,
      	icon: image,
      	position: countyMarkers[county].center
    };
    genericMarker = new google.maps.Marker(markerOptions);
    
  }
  
  var circleOptions = {
      strokeColor: '#FF0000',
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: '#FF0000',
      fillOpacity: 0.35,
      map: map,
      center: myLatLng, //LatLng object
      radius: 500000, // in meters
      editable: true
      
    };
  UICircle = new google.maps.Circle(circleOptions)
  
  
  
  
  // LISTENERS
  
  google.maps.event.addListener(UICircle, 'radius_changed', function() {
  	console.log(UICircle.getRadius());
  	// pass to back-end here?
  });
  
   google.maps.event.addListener(UICircle, 'center_changed', function() {
  	console.log(UICircle.getCenter());
  	
  	// pass to back-end stuff here?
  });
  
  
  google.maps.event.addListener(map, 'center_changed', function() {
    // 3 seconds after the center of the map has changed, pan back to the
    // marker.
    window.setTimeout(function() {
      map.panTo(marker.getPosition());
    }, 3000);
  });
  
}


///////////////////////////////////////////////////////////////////////////
// Final call, to initialize after the page has loaded
google.maps.event.addDomListener(window, 'load', initialize);
