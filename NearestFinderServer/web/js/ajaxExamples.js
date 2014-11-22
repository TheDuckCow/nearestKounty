$(document).on('ready', function(){
    
    //The following AJAX requests fire once the page is loaded
    $.ajax({
       url: "getLocationsInBound",
       type: "GET",
       data: {dataName : "value"}, // this is where you will pass in data
       success: function(response){
           console.log(response);
       },
       error: function(err){
           console.log(err);
       }
    });
    
    $.ajax({
       url: "getLocationsAtCounty",
       type: "GET",
       data: {dataName : "value"}, // this is where you will pass in data
       success: function(response){
           console.log(response);
       },
       error: function(err){
           console.log(err);
       }
    });

    $.ajax({
       url: "getNearestKLocationsAtCoord",
       type: "GET",
       data: {dataName : "value"}, // this is where you will pass in data
       success: function(response){
           console.log(response);
       },
       error: function(err){
           console.log(err);
       }
    });
    
    $.ajax({
       url: "getNearestKLocationsAtCounty",
       type: "GET",
       data: {dataName : "value"}, // this is where you will pass in data
       success: function(response){
           console.log(response);
       },
       error: function(err){
           console.log(err);
       }
    });
    
    //This is an example of an AJAX request that is bound to an event handler
    //Clicking anywhere on the page causes the event to fire
    $(document).on('click', function(e){
        var mouseX = e.pageY;
        var mouseY = e.pageX;
        
        $.ajax({
            url: "isWithinBound",
            type: "GET",
            data: {locationX: mouseX,
                   locationY: mouseY},
            success: function(response){
                console.log(response);
            },
            error: function(err){
                console.log(err);
            }
        });     
    });
});


