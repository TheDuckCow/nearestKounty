import java.util.ArrayList;



public class RTree implements Accessor{

    final int m = 2;
    final int M = 4;
    Node root;

    public class County {
        //Class representing an actual county
		double lon;
		double lat;
        String title;
    }

    private class Bound {
        double low_lon;
        double high_lon;
        double low_lat;
        double high_lat;

        public double area(){
            return (high_lon - low_lon) * (high_lat - low_lat);
        }
    }

    private abstract class Node {
        Bound bound;
        public abstract int size();
    }
	private class RNode extends Node{
        ArrayList<Node> nodes = new ArrayList<Node>();
        public int size(){
            return nodes.size();
        }
	}

    private class LeafNode extends Node{
        ArrayList<County> counties = new ArrayList<County>();
        public int size(){
            return counties.size();
        }
    }

    public void insertCounty(County county) {


    }

    private void insertCountyIntoNode(County county, Node node, Node parent) {
        if (node instanceof LeafNode) {
            LeafNode leaf = ((LeafNode)node);
            if (node.size() == M) {
                //If current leaf node is full
                //Split, add new node to parent


            } else {
                //If leaf node is not full, add to list of counties in leafnode
                leaf.counties.add(county);
                //Update bound for leafnode
                leaf.bound = newBoundWithCounty(county, leaf.bound);
            }
        } else {
            //It is a nav. node
            RNode leaf = ((RNode)node);
            //Update Bound, Fix itself

        }
    }

    private Bound newBoundWithCounty(County county, Bound oldBound) {
        //Updated bound with added county
        if (county.lon > oldBound.high_lon)
            oldBound.high_lon = county.lon;
        else if (county.lon < oldBound.low_lon)
            oldBound.low_lon = county.lon;
        if (county.lat > oldBound.high_lat)
            oldBound.high_lat = county.lat;
        else if (county.lat < oldBound.low_lat)
            oldBound.low_lat = county.lat;
        return oldBound;
    }

    private Node chooseSubtree(County county, RNode node) {
        ArrayList<Node> subNodes = node.nodes;
        if (subNodes.size() == 0){
            System.out.println("ERROR: Can't choose subtree from nothing");
            return null;
        }
        //Keeping track of the least initial area and the least amount of growth
        Node chosen = subNodes.get(0);
        double least_area = subNodes.get(0).bound.area();
        double least_growth = newBoundWithCounty(county, subNodes.get(0).bound).area() - least_area;

        for (int i = 1; i < subNodes.size(); i++) {
            //Update the chosen subtree with the least growth / least area if tie
            double area = subNodes.get(i).bound.area();
            double growth = newBoundWithCounty(county, subNodes.get(i).bound).area() - area;
            if (growth < least_growth || (growth == least_growth && area < least_area)) {
                chosen = subNodes.get(i);
                least_growth = growth;
                least_area = area;
            }
        }
        return chosen;
    }

    private Node splitNode(Node node) {
        //Returns the new node that is split out
        if (node.size() < M)
            return null;

        if (node instanceof LeafNode) {
            LeafNode leaf = ((LeafNode)node);
        } else {
            //It is a nav. node
            RNode leaf = ((RNode)node);

        }


    }

	@Override
	public ArrayList<County> getLocationsAtCoord(double lon, double lat,
			double radius) {
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

	public static void main(String [] args){


	}
}
