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

    public class Bound {
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
            RNode rnode = ((RNode)node);
            //Finding seed
            Node highest_low_lon = rnode.nodes.get(0);
            Node lowest_high_lon = rnode.nodes.get(0);
            Node highest_low_lat = rnode.nodes.get(0);
            Node lowest_high_lat = rnode.nodes.get(0);
            for (int i = 0; i < rnode.nodes.size(); i++ ) {
                Node temp = rnode.nodes.get(i);
                Bound bound = temp.bound;

                //along longitude
                if (bound.low_lon > highest_low_lon.bound.low_lon) {
                    highest_low_lon = temp;
                } else if (bound.high_lon < lowest_high_lon.bound.high_lon) {
                    lowest_high_lon = temp;
                }

                //along latitude
                if (bound.low_lat > highest_low_lat.bound.low_lat) {
                    highest_low_lat = temp;
                } else if (bound.high_lat < lowest_high_lat.bound.high_lat) {
                    lowest_high_lat = temp;
                }
            }
            //Find normalized separation
            double lon_sep_norm = (lowest_high_lon.bound.high_lon - highest_low_lon.bound.low_lon) / (rnode.bound.high_lon - rnode.bound.low_lon);
            double lat_sep_norm = (lowest_high_lat.bound.high_lat - highest_low_lat.bound.low_lat) / (rnode.bound.high_lat - rnode.bound.low_lat);

            //Get the two seeds
            Node seed1 = (lon_sep_norm > lat_sep_norm) ? highest_low_lon : highest_low_lat;
            Node seed2 = (lon_sep_norm > lat_sep_norm) ? lowest_high_lon : lowest_high_lat;
        }
    }

	@Override
	public ArrayList<County> getLocationsInBound(Bound bound) {
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
