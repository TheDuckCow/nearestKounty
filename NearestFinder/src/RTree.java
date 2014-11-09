import java.util.ArrayList;



public class RTree implements Accessor{

	public class Node {
		
		double lon;
		double lat;
		
	}
	
	public static void main(String [] args){
		
		System.out.println("Hello World");
	
	}

	@Override
	public ArrayList<Node> getLocationsAtCoord(double lon, double lat,
			double radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Node> getLocationsAtNode(Node node, double radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Node> getNearestKLocationsAtCoord(double lon, double lat,
			int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Node> getNearestKLocationsAtNode(Node node, int k) {
		// TODO Auto-generated method stub
		return null;
	}
}
