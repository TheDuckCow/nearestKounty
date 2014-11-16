import java.util.ArrayList;



public class RTree implements Accessor{

    final int m = 2;
    final int M = 4;
    Node root;


    public class Bound {
        double low_lon;
        double high_lon;
        double low_lat;
        double high_lat;
        public Bound(double low_lon, double high_lon, double low_lat, double high_lat) {
            low_lon = low_lon;
            high_lon = high_lon;
            low_lat = low_lat;
            high_lon = high_lon;
        }
        public Bound(Bound b) {
            low_lon = b.low_lon;
            high_lon = b.high_lon;
            low_lat = b.low_lat;
            high_lon = b.high_lon;
        }
        public double area(){
            return (high_lon - low_lon) * (high_lat - low_lat);
        }
    }

    private abstract class Node {
        Bound bound;
        public abstract int size();
        public ArrayList<Node> nodes;
    }
	private class RNode extends Node{
        ArrayList<Node> nodes = new ArrayList<Node>();
        public int size(){
            return nodes.size();
        }
	}

    private class LeafNode extends Node{
        ArrayList<Node> nodes = new ArrayList<Node>();
        public int size(){
            return nodes.size();
        }
    }

    public class County extends Node{
        //Class representing an actual county
		double lon;
		double lat;
        String title;
        public County (double lon, double lat, String title) {
            bound = new Bound(lon, lon, lat, lat);
            title = title;
        }
        public int size() { return 0;}
    }

    public void insertCounty(County county) {
        //TODO
    }

    private void insertCountyIntoNode(County county, Node node, Node parent) {
        if (node instanceof LeafNode) {
            LeafNode leaf = ((LeafNode)node);
            if (node.size() == M) {
                //If current leaf node is full
                leaf.nodes.add(county);
                parent.nodes.add(splitNode(node));
            } else {
                //If leaf node is not full, add to list of counties in leafnode
                leaf.nodes.add(county);
                //Update bound for leafnode
                leaf.bound = newBoundWithNode(county, leaf.bound);
            }
        } else {
            //It is a nav. node
            RNode rnode = ((RNode)node);
            //Update Bound, Fix itself
            Node chosen = chooseSubtree(county, rnode);
            insertCountyIntoNode(county, chosen, chosen);
            if (node.size() > M) {
                if (parent == null) {
                    // ROOT NODE
                    //TODO

                } else {
                    parent.nodes.add(splitNode(node));
                }
            }
        }
    }


    private Bound newBoundWithNode(Node node, Bound oldBound) {
        Bound bound = node.bound;
        //Updated bound with added bound
        Bound result = new Bound(oldBound.low_lon, oldBound.high_lon, oldBound.low_lat, oldBound.high_lat);
        if (bound.high_lon > result.high_lon)
            result.high_lon = bound.high_lon;
        else if (bound.low_lon < result.low_lon)
            result.low_lon = bound.low_lon;
        if (bound.high_lat > result.high_lat)
            result.high_lat = bound.high_lat;
        else if (bound.low_lat < result.low_lat)
            result.low_lat = bound.low_lat;
        return result;
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
        double least_growth = newBoundWithNode(county, subNodes.get(0).bound).area() - least_area;

        for (int i = 1; i < subNodes.size(); i++) {
            //Update the chosen subtree with the least growth / least area if tie
            double area = subNodes.get(i).bound.area();
            double growth = newBoundWithNode(county, subNodes.get(i).bound).area() - area;
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

        ArrayList<Node> subNodes = node.nodes;

        //Temporary storing the arrays
        ArrayList<Node> temp = new ArrayList<Node>();
        //Moving everything to temp array
        for (Node n : subNodes) {
            temp.add(n);
        }
        subNodes.clear();

        ArrayList<Node> seeds = pickLinearSeed(temp, node.bound.high_lon - node.bound.low_lon, node.bound.high_lat - node.bound.low_lat);
        Node seed1 = seeds.get(0);
        Node seed2 = seeds.get(1);

        //New array to be returned
        Node newNode;
        if (node instanceof LeafNode){
            newNode = new LeafNode();
        } else if (node instanceof RNode) {
            newNode = new RNode();
        } else {
            System.out.println("ERROR: Split node is given non leafnode or rnode");
        }

        ArrayList<Node> newSubNodes = newNode.nodes;

        //Remove seeds from temp
        temp.remove(seed1);
        temp.remove(seed2);

        //Adding seeds
        subNodes.add(seed1);
        node.bound = new Bound(seed1.bound);
        newSubNodes.add(seed2);
        newNode.bound = new Bound(seed2.bound);

        //Adding remaining nodes to the split
        while (subNodes.size() < (M - m + 1) || newSubNodes.size() < (M - m +1) ) {
            Node nodeToAdd = temp.get(0);
            Node groupToAddTo = node;
            double maximum_diff = 0;
            //Find which one to add
            for(Node n: temp) {
                double difference_from_group_1 = newBoundWithNode(n, node.bound).area() - node.bound.area();
                double difference_from_group_2 = newBoundWithNode(n, newNode.bound).area() - newNode.bound.area();
                double relative_diff = abs(difference_from_group_1 - difference_from_group_2);

                if (relative_diff > maximum_diff) {
                    maximum_diff = relative_diff;
                    nodeToAdd = n;
                    if (difference_from_group_1 > difference_from_group_2) {
                        groupToAddTo = newNode;
                    } else {
                        groupToAddTo = node;
                    }
                }
            }
            //Actually adding
            groupToAddTo.nodes.add(nodeToAdd);
            groupToAddTo.bound = newBoundWithNode(nodeToAdd, groupToAddTo.bound);
            temp.remove(nodeToAdd);
        }
        if(node.size() == (M - m + 1)) {
            for(Node i : temp){
                newSubNodes.add(i);
                newNode.bound = newBoundWithNode(i, newNode.bound);
            }
        } else {
            for(Node i : temp){
                subNodes.add(i);
                node.bound = newBoundWithNode(i, node.bound);
            }
        }
        return newNode;
    }

    private ArrayList<Node> pickLinearSeed(ArrayList<Node> subNodes, double width_lon, double width_lat)
    {
        //Return two seeds
        ArrayList<Node> result = new ArrayList<Node>();

        Node highest_low_lon = subNodes.get(0);
        Node lowest_high_lon = subNodes.get(0);
        Node highest_low_lat = subNodes.get(0);
        Node lowest_high_lat = subNodes.get(0);
        for (int i = 0; i < subNodes.size(); i++ ) {
            Node temp = subNodes.get(i);
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
        double lon_sep_norm;
        double lat_sep_norm;

        if (subNodes.get(0) instanceof County){
            lon_sep_norm = (lowest_high_lon.bound.high_lon - highest_low_lon.bound.low_lon);
            lat_sep_norm = (lowest_high_lat.bound.high_lat - highest_low_lat.bound.low_lat);
        } else {
            lon_sep_norm = (lowest_high_lon.bound.high_lon - highest_low_lon.bound.low_lon) / (width_lon);
            lat_sep_norm = (lowest_high_lat.bound.high_lat - highest_low_lat.bound.low_lat) / (width_lat);
        }

        //Get the two seeds
        Node seed1 = (lon_sep_norm > lat_sep_norm) ? highest_low_lon : highest_low_lat;
        Node seed2 = (lon_sep_norm > lat_sep_norm) ? lowest_high_lon : lowest_high_lat;

        result.add(seed1);
        result.add(seed2);
        return result;
    }

	@Override
	public ArrayList<County> getLocationsInBound(Node root, Bound mybound) 
	{
		ArrayList<County> nearbycounties = new ArrayList<County>();
		Node current = root;

		if (current instanceof LeafNode)
		{
			LeafNode leaf = (LeafNode)current;
			for (County l : leaf.counties)
			{
				if (isWithinBound(mybound,l))
				{
					nearbycounties.add(l);
				}
			}
		}
		else
		{
			RNode rnode = (RNode)current;
			ArrayList<Node> children = rnode.nodes;
			for (int i = 0; i < children.size(); i++)
			{
				if (isWithinBound(mybound,children.get(i).bound))	//still gotta write the intersection function
				{
					//nearbycounties = nearbycounties (INTERSECTION) 
					//					getLocationsInBound(children.get(i),mybound)
				}
			}
		}
		
		return nearbycounties;
	}
	
	//checks if bounding rectangle is overlapping with rectangle mybound
	public boolean isWithinBound(Bound mybound, Bound nodebound)
	{
		if (mybound.low_lon < nodebound.high_lon && mybound.high_lon > nodebound.low_lon &&
			mybound.high_lat > nodebound.low_lat && mybound.low_lat < nodebound.high_lat)
		{
			return true;
		}
		return false;
	}
	
	//checks if a county coordinate is within rectangle mybound
	public boolean isWithinBound(Bound mybound, County county)
	{
		if (county.lon < mybound.high_lon && county.lon > mybound.low_lon &&
			county.lat < mybound.high_lat && county.lat > mybound.low_lat)
		{
			return true;
		}
		return false;
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
