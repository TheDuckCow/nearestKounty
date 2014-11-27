import java.util.ArrayList;


public class RTree implements Accessor{

    final int m = 2;
    final int M = 4;
    private RNode root = new RNode();;


    public void insertCounty(County county) {
        insertCountyIntoNode(county, root, null);
    }

    private void insertCountyIntoNode(County inCounty, Node inNode, RNode inParent) {
        assert (inNode instanceof RNode);
        RNode node = ((RNode)inNode);
        if (node.size() == 0){
            node.addNode(inCounty);
            return;
        }

        if (node.nodes.get(0) instanceof County) {
            //Leaf node
            if (node.size() == M) {
                //If current leaf node is full
                node.addNode(inCounty);
                inParent.nodes.add(splitNode(node));
            } else {
                //If leaf node is not full, add to list of counties in leafnode
                node.addNode(inCounty);
            }
        } else {
            //It is a nav. node
            //Update Bound, Fix itself
            Node chosen = chooseSubtree(inCounty, node);
            insertCountyIntoNode(inCounty, chosen, node);
            if (node.size() > M) {
                if (inParent == null) {
                    // ROOT NODE
                    RNode newNode = new RNode();
                    newNode.bound = new Bound(root.bound);
                    newNode.addNode(root);
                    newNode.addNode(splitNode(root));
                    root = newNode;
                } else {
                    inParent.addNode(splitNode(node));
                }
            }
        }
    }


    private Node chooseSubtree(County county, RNode node) {
        System.out.println("Choosing subtree");
        ArrayList<Node> subNodes = node.nodes;
        if (subNodes.size() == 0){
            System.out.println("ERROR: Can't choose subtree from nothing");
            return null;
        }
        //Keeping track of the least initial area and the least amount of growth
        Node chosen = subNodes.get(0);
        double least_area = subNodes.get(0).bound.area();
        double least_growth = Bound.newBoundWithNode(county, subNodes.get(0).bound).area() - least_area;

        for (int i = 1; i < subNodes.size(); i++) {
            //Update the chosen subtree with the least growth / least area if tie
            double area = subNodes.get(i).bound.area();
            double growth = Bound.newBoundWithNode(county, subNodes.get(i).bound).area() - area;
            if (growth < least_growth || (growth == least_growth && area < least_area)) {
                chosen = subNodes.get(i);
                least_growth = growth;
                least_area = area;
            }
        }
        return chosen;
    }

    private Node splitNode(Node node) {
        System.out.println("Splitting Node");
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
        Node newNode = new RNode();;

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
                double difference_from_group_1 = Bound.newBoundWithNode(n, node.bound).area() - node.bound.area();
                double difference_from_group_2 = Bound.newBoundWithNode(n, newNode.bound).area() - newNode.bound.area();
                double relative_diff = Math.abs(difference_from_group_1 - difference_from_group_2);

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
            groupToAddTo.bound = Bound.newBoundWithNode(nodeToAdd, groupToAddTo.bound);
            temp.remove(nodeToAdd);
        }
        if(node.size() == (M - m + 1)) {
            for(Node i : temp){
                newSubNodes.add(i);
                newNode.bound = Bound.newBoundWithNode(i, newNode.bound);
            }
        } else {
            for(Node i : temp){
                subNodes.add(i);
                node.bound = Bound.newBoundWithNode(i, node.bound);
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
		//ArrayList<County> nearbycounties = new ArrayList<County>();
		//Node current = root;

		//if (current instanceof LeafNode)
		//{
			//LeafNode leaf = (LeafNode)current;
			//for (County l : leaf.counties)
			//{
				//if (isWithinBound(mybound,l))
				//{
					//nearbycounties.add(l);
				//}
			//}
		//}
		//else
		//{
			//RNode rnode = (RNode)current;
			//ArrayList<Node> children = rnode.nodes;
			//for (int i = 0; i < children.size(); i++)
			//{
				//if (isWithinBound(mybound,children.get(i).bound))	//still gotta write the intersection function
				//{
					////nearbycounties = nearbycounties (INTERSECTION)
					////					getLocationsInBound(children.get(i),mybound)
				//}
			//}
		//}

		//return nearbycounties;
        return null;
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

    public void printTree() {
        printNode(root, 0);
    }

	public static void main(String [] args){

        RTree tree = new RTree();
        tree.printTree();
        int numberToAdd = 5;
        for( int i = 0; i < numberToAdd; i ++ ) {
            System.out.println();
            System.out.println("Adding: " + i);
            County c = new County(i ,i ,"" + i);
            tree.insertCounty(c);
            tree.printTree();

        }

	}


    public static void printNode(RNode root, int level) {
        String indent = "";
        for (int i = 0; i < level; i ++) {
            indent = indent + "\t";
        }
        System.out.println(indent + "Size: " + root.size());
        for (int i = 0 ; i < root.size(); i ++ ) {
            Node temp = root.nodes.get(i);
            if ( temp instanceof RNode) {
                printNode((RNode)temp, level + 1);
            } else if (temp instanceof County) {
                County county = ((County) temp);
                System.out.println(indent + "Point: " + county.lon + " " + county.lat + ": " + county.title);

            }
        }
    }
}
