package nearestFinder;

public class County extends Node{
    //Class representing an actual county
    public double lon;
    public double lat;
    public String title, state;
    public County (double lon, double lat, String title, String state) {
        this.lon = lon;
        this.lat = lat;
        bound = new Bound(lon, lon, lat, lat);
        this.title = title;
        this.state = state;
    }
    public int size() { return 0;}

    public String toString() {
        return "County: " + lon + " " + lat + ": " + title + ", " + state;

    }
}

