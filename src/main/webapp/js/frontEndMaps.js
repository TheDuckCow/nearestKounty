// function to set the desciption for K Nearest tab
function kNearestDesciption(){
  var description;
  description = "Click the map to select the starting reference point";
  description += " or enter a longitude and latitude below.<br>";
  
  var $inputFields = $('<div>').addClass('inputFields');
  $input = $('<div>').addClass('input').html("<span class='inputText'>Latitude:</span> <input class='latInput'></input><br><div class='bounds'>[-90...90]</div>"); 
  $inputFields.append($input);

  var $input = $('<div>').addClass('input').html("<span class='inputText'>Longitude:</span> <input class='lngInput'></input><div class='bounds'>[-180...180]</div>"); 
  $inputFields.append($input);

  $input = $('<div>').addClass('input').html("<span class='inputText'>K Nearest:</span> <input class='k'></input><div class='bounds'>[1...1,000]</div>"); 
  $inputFields.append($input);

  var submit;
  submit = "<input class='submitButton' type='submit' value='Update'><hr>";

  var results;
  results = "<div class='kResults'></div>";

  $('.explanation').html(description).append($inputFields).append(submit).append(results);
}

// function to set the description for Rectangle Bound tab
function rectangleBoundDescription(){

  var description;
  description = "Click the map to select the center of the rectangle";
  description += " or enter a longitude and latitude below.<br>";
  
  var $inputFields = $('<div>').addClass('inputFields');
  $input = $('<div>').addClass('input').html("<span class='inputText'>Latitude:</span> <input class='latInput'></input><br><div class='bounds'>[-90...90]</div>"); 
  $inputFields.append($input);

  var $input = $('<div>').addClass('input').html("<span class='inputText'>Longitude:</span> <input class='lngInput'></input><div class='bounds'>[-180...180]</div>"); 
  $inputFields.append($input);

  var submit;
  submit = "<input class='submitButton' type='submit' value='Update'><hr>";
  submit += "<span class='nLocations'>0</span> locations within the bounds.";

  $('.explanation').html(description).append($inputFields).append(submit);
}

// INITIAL SETUP/global variables

var countyMarkers = []
var adjustBoundSW;
var adjustBoundNE;
var tab;
var globalK = 1;

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

  // then, draw all the new ones
  for (var county in countyMarkers){
  
  	data = "<b>"+countyMarkers[county].zipcode + ", " + countyMarkers[county].name + "</b><hr>Latitude: " + countyMarkers[county].center.k + "<br>Longitude: " + countyMarkers[county].center.B;
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
      //curl -d 'lat=31' -d 'long=-113' -d 'k=10' 0.0.0.0:8080/getNearestKLocationsAtCoord
  // SETUP of map and style
  //42.3581° N, 71.0636° W
  $(document).keyup('input', function(event){
    if(event.keyCode == 13){
        $(".submitButton").click();
    }
  });


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
  
  var rectangleOptions = {
      strokeColor: '#FF0000',
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: '#FF0000',
      fillOpacity: 0.35,
      map: null,
      draggable: false,
      bounds: new google.maps.LatLngBounds(
        new google.maps.LatLng(42.33809, -71.11075),
        new google.maps.LatLng(42.3693, -71.0490931)),
      editable: true  
  }
  adjustBoundNE = [42.3693 - 42.3581, 71.11075 - 71.0636];
  adjustBoundSW = [42.33809 - 42.3581, 71.0490931 - 71.0636];
  //42.3581,-71.0636
  UIRectangle = new google.maps.Rectangle(rectangleOptions);

  var kMarkerOptions = {
        map: map,
        position: new google.maps.LatLng(42.3581, -71.0636),
        clickable: false,
        icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
    };
  var kMarker = new google.maps.Marker(kMarkerOptions);

  // set the initial description
  kNearestDesciption();
  tab = 'nearest';
  $('.lngInput').val(-71.0636);
  $('.latInput').val(42.3581);
  $('.k').val(globalK);

  // tab styling switch
  $('.tab').on('click', function(){
    var $this = $(this);
    if (!$this.hasClass('selected')) {
      $('.selected').removeClass('selected');
      $this.addClass('selected');
      if ($this.hasClass('nearest')) {
        tab = 'nearest';
        kNearestDesciption();
        var position = kMarker.getPosition();
        $('.lngInput').val(position.lng());
        $('.latInput').val(position.lat());
        $('.k').val(globalK);
        getNearestK(position.lat(), position.lng(), globalK);
        countyMarkers = [];
        drawCities(map);
        UIRectangle.setMap(null);
        kMarker.setMap(map);
      } else {
        tab = 'bound';
        rectangleBoundDescription();
        var center = getCenter(UIRectangle);
        $('.lngInput').val(center[1]);
        $('.latInput').val(center[0]);
        UIRectangle.setMap(map);
        kMarker.setMap(null);
        updateBounds();
      }
    }
  });

  // clicking the update button
  $(document).on('click', '.submitButton', function(){
    //    map.setCenter(new google.maps.LatLng(primaryCenter.k, primaryCenter.B));
    var lng = parseFloat($('.lngInput').val());
    var lat = parseFloat($('.latInput').val());
    var k   = parseInt($('.k').val());

    // if the lat and lng are in the bounds of the US
    if (lng >= -180 && lng <= 180 && lat >= -90 && lat <= 90) {
      map.setCenter(new google.maps.LatLng(lat, lng));
      if (tab === 'nearest') {
        if (k >= 1 && k <= 1000 || k === 10000) {
          getNearestK(lat, lng, k);
          kMarker.setPosition(new google.maps.LatLng(lat, lng));
          globalK = k;
        }
      } else {
        var lat_low = lat + adjustBoundSW[0] + 0.005;
        var lat_high = lat + adjustBoundNE[0] + 0.005;
        var long_low = lng + adjustBoundSW[1] - 0.015;
        var long_high = lng + adjustBoundNE[1] - 0.015;
        UIRectangle.setBounds(new google.maps.LatLngBounds(
            new google.maps.LatLng(lat_low, long_low),
            new google.maps.LatLng(lat_high, long_high)));
        updateBounds();
      }
    }
  });

  // Add an event listener on the rectangle.
  google.maps.event.addListener(UIRectangle, 'bounds_changed', updateBounds);
  
  
  // single click, change center coordinates/move the 
  google.maps.event.addListener(map, 'click', function(e) {
    if (tab === 'bound') {
      primaryCenter = e.latLng
      var lat_low = primaryCenter.lat() + adjustBoundSW[0] + 0.005;
      var lat_high = primaryCenter.lat() + adjustBoundNE[0] + 0.005;
      var long_low = primaryCenter.lng() + adjustBoundSW[1] - 0.015;
      var long_high = primaryCenter.lng() + adjustBoundNE[1] - 0.015;
      UIRectangle.setBounds(new google.maps.LatLngBounds(
          new google.maps.LatLng(lat_low, long_low),
          new google.maps.LatLng(lat_high, long_high)));
      var center = getCenter(UIRectangle);
      $('.lngInput').val(center[1]);
      $('.latInput').val(center[0]);
    } else {
      kMarker.setPosition(e.latLng);
      console.log(e.latLng);
      $('.lngInput').val(e.latLng.lng());
      $('.latInput').val(e.latLng.lat());
      getNearestK(e.latLng.lat(), e.latLng.lng(), globalK);
      kMarker.setPosition(e.latLng);
    }
  });

  function majorityVoting(lat, long){
    $.ajax({
      url: "getNearestKLocationsAtCoord",
      type: "GET",
      data: {lat : lat,
             long : long,
             k : 5 },
      success: function(response){
        var foundCounties = {};
        for (var i = 0; i < response.results.length; i++){
          var index = response.results[i].title + ', ' + response.results[i].state;
          if (foundCounties[index] === undefined) {
            foundCounties[index] = 1;
          } else {
            foundCounties[index] = foundCounties[index] + 1;
          }
        }
        var majority = null;
        var max = 0;
        for (var key in foundCounties){
          if (foundCounties[key] > max){
            max = foundCounties[key];
            majority = key;
          }
        }
        $('.kResults').prepend('<b>Majority Voting</b><br>' + majority + '<br><br>');
      },
      error: function(err){
        console.log(err);
      }
    });
  }

  function getNearestK(lat, long, k){
    $.ajax({
      url: "getNearestKLocationsAtCoord",
      type: "GET",
      data: {lat : lat,
             long : long,
             k : k },
      success: function(response){
        console.log(response);
        countyMarkers = [];
        $('.kResults').html('<b>Nearest Points</b><br>');
        for (var i = 0; i < response.results.length; i++){
            point = response.results[i];
            addPointToResults(point, i+1);
            plot(point);
        }
        drawCities(map);
        majorityVoting(lat, long);
      },
      error: function(err){
        console.log(err);
      }
    });
  }

  function plot(point){
    tmpPoint = {
      center: new google.maps.LatLng(point['lat'], point['long']),
      zipcode: point.title,
      name: point.state
    };
    countyMarkers.push(tmpPoint);
  }

  function getDistance(lat1, lon1, lat2, lon2) {
      var R = 6371; // Radius of the earth in km
      var dLat = (lat2 - lat1) * Math.PI / 180;  // deg2rad below
      var dLon = (lon2 - lon1) * Math.PI / 180;
      var a = 
         0.5 - Math.cos(dLat)/2 + 
         Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * 
         (1 - Math.cos(dLon))/2;

      return (R * 2 * Math.asin(Math.sqrt(a))).toFixed(4);
  }

  function addPointToResults(point, num){
    var info = point.title + ', ' + point.state + '<br>';
    info += 'Latitude: ' + point.lat + '<br>Longitude: ' + point.long + '<br>';
    info  += 'Distance: ' + getDistance(point.lat, point.long, kMarker.getPosition().lat(), kMarker.getPosition().lng()) + 'km<br><br>';
    $('.kResults').append(num + '. ' + info);
  }

  function getCenter(rectangle) {
    var ne = UIRectangle.getBounds().getNorthEast();
    var sw = UIRectangle.getBounds().getSouthWest();
    return [(ne.lat() + sw.lat()) / 2, (ne.lng() + sw.lng()) / 2]
  }

  function updateBounds(event) {
    var ne = UIRectangle.getBounds().getNorthEast();
    var sw = UIRectangle.getBounds().getSouthWest();
    console.log('Bounds');
    console.log('SW Lat: ' + sw.lat());
    console.log('SW Lng: ' + sw.lng());
    console.log('NE Lat: ' + ne.lat());
    console.log('NE Lng: ' + ne.lng());
    console.log('');
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
          $('.nLocations').html(response.results.length);
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
