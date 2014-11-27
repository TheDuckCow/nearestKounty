// First, grab the set of points
var citymap = {};
citymap['chicago'] = {
  center: new google.maps.LatLng(41.878113, -87.629798),
  population: 2714856
};
citymap['newyork'] = {
  center: new google.maps.LatLng(40.714352, -74.005973),
  population: 8405837
};
citymap['losangeles'] = {
  center: new google.maps.LatLng(34.052234, -118.243684),
  population: 3857799
};
citymap['vancouver'] = {
  center: new google.maps.LatLng(49.25, -123.1),
  population: 603502
};

var cityCircle;

function initialize() {
  //var myLatlng = new google.maps.LatLng(-25.363882,131.044922);
  var myLatlng = new google.maps.LatLng(41.070719,-95.4165768);
  var mapOptions = {
    zoom: 5,
    center: myLatlng
  }
  var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
  
  map.set('styles', [
  {
	featureType: 'road',
	elementType: 'geometry',
	stylers: [
	  { visibility: 'off'}
	]
  }, {
	featureType: 'road',
	elementType: 'labels',
	stylers: [
	  { visibility: 'off' }
	]
  }, {
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
  
  var image = 'images/county.png';
  var myLatLng = new google.maps.LatLng(41.070719,-95.4165768);
  var beachMarker = new google.maps.Marker({
      position: myLatLng,
      map: map,
      icon: image
  });
  
  var circleOptions = {
      strokeColor: '#FF0000',
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: '#FF0000',
      fillOpacity: 0.35,
      map: map,
      center: myLatLng,
      radius: 500000
    };
  cityCircle = new google.maps.Circle(circleOptions)
  
  // example just to show multiple circles...
  for (var city in citymap) {
    var markers = {
      // add markers
    };
    // Add the circle for this city to the map.
  }
  
}

google.maps.event.addDomListener(window, 'load', initialize);
