import java.util.ArrayList;

public interface Accessor {

	public ArrayList<County> getLocationsInBound(Node root, Bound mybound);

	public boolean isWithinBound(Bound mybound, Bound nodebound);

	public ArrayList<County> getLocationsAtCounty(County County, double radius);

	public ArrayList<County> getNearestKLocationsAtCoord(double lon, double lat, int k);

	public ArrayList<County> getNearestKLocationsAtCounty(County county, int k);
}
