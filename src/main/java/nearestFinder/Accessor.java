
import java.util.ArrayList;

public interface Accessor {

	public ArrayList<County> getLocationsInBound(Bound mybound);

	public ArrayList<County> getNearestKLocationsAtPoint(double lon, double lat, int k);
}
