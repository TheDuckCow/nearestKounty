import java.util.ArrayList;

public interface Accessor {

	public ArrayList<County> getLocationsInBound(Bound mybound);

	public ArrayList<County> getLocationsAtCounty(County County, double radius);

	public ArrayList<County> getNearestKLocationsAtCoord(double lon, double lat, int k);

	public ArrayList<County> getNearestKLocationsAtCounty(County county, int k);
}
