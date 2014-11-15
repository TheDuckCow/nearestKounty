import java.util.ArrayList;

public interface Accessor {

	public ArrayList<RTree.County> getLocationsInBound(RTree.Bound bound);

	public ArrayList<RTree.County> getLocationsAtCounty(RTree.County County, double radius);

	public ArrayList<RTree.County> getNearestKLocationsAtCoord(double lon, double lat, int k);

	public ArrayList<RTree.County> getNearestKLocationsAtCounty(RTree.County county, int k);
}
