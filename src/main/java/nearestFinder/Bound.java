package nearestFinder;

public class Bound {
    double low_lon;
    double high_lon;
    double low_lat;
    double high_lat;
    public Bound(double low_lon, double high_lon, double low_lat, double high_lat) {
        this.low_lon = low_lon;
        this.high_lon = high_lon;
        this.low_lat = low_lat;
        this.high_lat = high_lat;
    }
    public Bound(Bound b) {
        this.low_lon = b.low_lon;
        this.high_lon = b.high_lon;
        this.low_lat = b.low_lat;
        this.high_lat = b.high_lat;
    }
    public double area(){
        return (high_lon - low_lon) * (high_lat - low_lat);
    }

    public double intersectarea(Bound bound2)
    {
    	//calculates the area of intersection for two MBRs
    	return Math.max(0,Math.max(high_lon,bound2.high_lon) - Math.min(low_lon, bound2.low_lon)) *
    		   Math.max(0,Math.max(bound2.high_lat,bound2.high_lat)- Math.min(low_lat,bound2.low_lat));
    }
    public double unionarea(Bound bound2)
    {
    	double intersect = this.intersectarea(bound2);
    	return this.area() + bound2.area() - intersect;
    }

    public String toString() {
        return "lon: (" + low_lon + ", " + high_lon + "). lat: (" + low_lat + ", " + high_lat +')';
    }

    public static Bound newBoundWithNode(Node node, Bound oldBound) {
        Bound bound = node.bound;


        //Updated bound with added bound
        Bound result = new Bound(oldBound);

        if (bound.high_lon > result.high_lon)
            result.high_lon = bound.high_lon;
        else if (bound.low_lon < result.low_lon)
            result.low_lon = bound.low_lon;

        if (bound.high_lat > result.high_lat)
            result.high_lat = bound.high_lat;

        else if (bound.low_lat < result.low_lat)
            result.low_lat = bound.low_lat;

        return result;
    }
}
