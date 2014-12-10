## Live application

http://ec2-54-149-48-66.us-west-2.compute.amazonaws.com:8080

## Running the server locally

First build with:

    $mvn clean install

Then run it with:

    $java -cp target/classes:target/dependency/* com.example.Main
    
## Running the command line interface

To run the command line interface run the Tester.java class within the nearestFinder package.
The java files are located in "./src/main/java/nearestFinder"
Example:


knearest: Searches for the k nearest neighbors of a query point
bound: Searches for all points within a specified bounding rectangle
quit: Exits the command line interface

ENTER COMMAND...

Enter latitude (double): 42.34414502258788
Enter longitude (double): -71.08720779418945
Enter number of neighbors (positive int): 34
 
-----Printing 34 nearest locations from point -71.08720779418945, 42.34414502258788-----
 
County: -71.0872738 42.3442642: Suffolk, MA
County: -71.0868341 42.3448776: Suffolk, MA
County: -71.0867 42.3453: Suffolk, MA
County: -71.0856071 42.3442642: Suffolk, MA
County: -71.0850516 42.3442642: Suffolk, MA
County: -71.0847738 42.3437087: Suffolk, MA
County: -71.0869961 42.3467642: Suffolk, MA
County: -71.0864404 42.3467642: Suffolk, MA
County: -71.0856071 42.3417643: Suffolk, MA
County: -71.0900517 42.3445419: Middlesex, MA
County: -71.0900517 42.3445419: Suffolk, MA
County: -71.0894961 42.3459308: Suffolk, MA
...
