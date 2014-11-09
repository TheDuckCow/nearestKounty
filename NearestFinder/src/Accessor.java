import java.util.ArrayList;

public interface Accessor {
	
	public ArrayList<RTree.Node> getLocationsAtCoord(double lon, double lat, double radius);

	public ArrayList<RTree.Node> getLocationsAtNode(RTree.Node node, double radius);
	
	public ArrayList<RTree.Node> getNearestKLocationsAtCoord(double lon, double lat, int k);
	
	public ArrayList<RTree.Node> getNearestKLocationsAtNode(RTree.Node node, int k);
}
