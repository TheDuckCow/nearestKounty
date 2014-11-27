
public class County extends Node{
    //Class representing an actual county
    double lon;
    double lat;
    String title;
    public County (double lon, double lat, String title) {
        this.lon = lon;
        this.lat = lat;
        bound = new Bound(lon, lon, lat, lat);
        this.title = title;
    }
    public int size() { return 0;}

    public String toString() {
        return "County: " + lon + " " + lat + ": " + title;

    }
}

