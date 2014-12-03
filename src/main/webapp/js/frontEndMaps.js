$(document).on('ready', function(){
  // set the initial description
  kNearestDesciption();

  // tab styling switch
  $('.tab').on('click', function(){
    console.log('clicked');
    var $this = $(this);
    if (!$this.hasClass('selected')) {
      $('.selected').removeClass('selected');
      $this.addClass('selected');
    }
  });
});

// function to set the desciption for K Nearest tab
function kNearestDesciption(){
  var description;
  description = "Click the map to select the starting reference point";
  description += " or enter a longitude and latitude below.<br>";
  
  var $inputFields = $('<div>').addClass('inputFields');
  var $input = $('<div>').addClass('input').html("<span class='inputText'>Longitude:</span> <input class='lngInput'></input><div class='bounds'>[-59.70...-126.29]</div>"); 
  $inputFields.append($input);

  $input = $('<div>').addClass('input').html("<span class='inputText'>Latitude:</span> <input class='latInput'></input><br><div class='bounds'>[35.86...48.04]</div>"); 
  $inputFields.append($input);

  $input = $('<div>').addClass('input').html("<span class='inputText'>K Nearest:</span> <input class='k'></input><div class='bounds'>[1...1,000]</div>"); 
  $inputFields.append($input);

  var submit;
  submit = "<input class='submitButton' type='submit' value='Update'><hr>";

  $('.explanation').html(description).append($inputFields).append(submit);
}

// function to set the description for Rectangle Bound tab
function rectangleBoundDescription(){

}

// INITIAL SETUP/global variables

var countyMarkers = []
var adjustBoundSW;
var adjustBoundNE;

var UIRectangle;
//var genericMarker;
var markers = []; 	// reference container for markers

// key variables
var primaryCenter;	// long/lat of center of circle
var currentK = 20;		// current "k" closest"
var radius = 500000;			// radius of the circle

// function for refreshing the page with new markers, only one to drop markers
function drawCities(map){
  // first, delete ALL markers
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
  markers = [];
  console.log(countyMarkers);
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
    zoom: 13,
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
  //var UICont = new UIControls(map);
  
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
      draggable: false,
      bounds: new google.maps.LatLngBounds(
        new google.maps.LatLng(42.33809, -71.11075),
        new google.maps.LatLng(42.3693, -71.0490931)),
      editable: true  
  }
  adjustBoundNE = [42.3693 - 42.3581, 71.0490931 - 71.0636];
  adjustBoundSW = [42.33809 - 42.3581, 71.11075 - 71.0636];
  //42.3581,-71.0636
  UIRectangle = new google.maps.Rectangle(rectangleOptions);
  updateBounds();

  // Add an event listener on the rectangle.
  google.maps.event.addListener(UIRectangle, 'bounds_changed', updateBounds);
  
  
  // single click, change center coordinates/move the 
  google.maps.event.addListener(map, 'click', function(e) {
    primaryCenter = e.latLng
    var lat_low = primaryCenter.k + adjustBoundSW[0];
    var lat_high = primaryCenter.k + adjustBoundNE[0];
    var long_low = primaryCenter.B + adjustBoundSW[1];
    var long_high = primaryCenter.B + adjustBoundNE[1];
    UIRectangle.setBounds(new google.maps.LatLngBounds(
        new google.maps.LatLng(lat_high, long_high),
        new google.maps.LatLng(lat_low, long_low)));
  });

  function plot(point){
    tmpPoint = {
      center: new google.maps.LatLng(point['lat'], point['long']),
      zipcode: point.title,
      name: point.state
    };
    countyMarkers.push(tmpPoint);
  }

  function updateBounds(event) {
    var ne = UIRectangle.getBounds().getNorthEast();
    var sw = UIRectangle.getBounds().getSouthWest();

    $.ajax({
       url: "getLocationsInBound",
       type: "GET",
       data: {low_long : sw.lng(),
              low_lat : sw.lat(),
              high_long : ne.lng(),
              high_lat : ne.lat()}, 
       success: function(response){
          var point;
          countyMarkers = [];
          for (var i = 0; i < response.results.length; i++){
            point = response.results[i];
            plot(point);
          }
          drawCities(map);
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
