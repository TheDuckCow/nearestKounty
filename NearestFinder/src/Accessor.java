import java.util.ArrayList;

public interface Accessor {

	public ArrayList<County> getLocationsInBound(Bound mybound);

	public ArrayList<County> getLocationsAtCounty(County County, double halfSquare);

	public ArrayList<County> getNearestKLocationsAtCounty(County county, int k);
}
