import java.util.ArrayList;



public class RTree implements Accessor{

    final int M = 3;

	public class Node {

		double lon;
		double lat;

        boolean isLeafNode = true;

	}

    public class County {


    }

	public static void main(String [] args){

		System.out.println("Hello World");

	}

	@Override
	public ArrayList<County> getLocationsAtCoord(double lon, double lat,
			double radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<County> getLocationsAtCounty(County county, double radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<County> getNearestKLocationsAtCoord(double lon, double lat,
			int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<County> getNearestKLocationsAtCounty(County node, int k) {
		// TODO Auto-generated method stub
		return null;
	}

}
