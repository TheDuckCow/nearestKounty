

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.text.html.HTMLDocument.Iterator;

public class RTree implements Accessor{

    final int M = 100;
    final int m = M/2;
    private RNode root = new RNode();;

    //For searching
    double max_dist;

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
                node.addNode(inCounty);
                //If current leaf node is full

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
            } else {

                //If leaf node is not full, add to list of counties in leafnode
                node.addNode(inCounty);
            }
        } else {
            //It is a nav. node
            //Update Bound, Fix itself
            node.updateBoundWithNewNode(inCounty);
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
        //System.out.println("Choosing subtree");
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

    private Node splitNode(RNode node) {
        //System.out.println("Splitting Node");

        //Returns the new node that is split out
        if (node.size() < M)
            return null;

        //System.out.println(node.nodes);
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
        RNode newNode = new RNode();

        ArrayList<Node> newSubNodes = newNode.nodes;

        //Remove seeds from temp
        temp.remove(seed1);
        temp.remove(seed2);

        node.bound = null;

        //Adding seeds
        node.addNode(seed1);
        newNode.addNode(seed2);

        //Adding remaining nodes to the split
        while (temp.size() > 0 && (subNodes.size() < (M - m + 1) || newSubNodes.size() < (M - m +1) )) {
            Node nodeToAdd = temp.get(0);
            RNode groupToAddTo = node;
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
            groupToAddTo.addNode(nodeToAdd);

            temp.remove(nodeToAdd);
        }
        if(node.size() == (M - m + 1)) {
            for(Node i : temp){
                newSubNodes.add(i);
            }
        } else {
            for(Node i : temp){
                subNodes.add(i);
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
	public ArrayList<County> getLocationsInBound(Bound myBound)
	{
        ArrayList<County> nearbyCounties = new ArrayList<County>();
        getInnerLocations(root, myBound, nearbyCounties);
        return nearbyCounties;
	}

    private void getInnerLocations(Node inNode, Bound myBound, ArrayList<County> nearbyCounties){
        RNode node = ((RNode) inNode);
        if (node.nodes.size() == 0) {
            return;
        }

        if (node.nodes.get(0) instanceof County)
        {
            for (Node n : node.nodes){
                County county = (County) n;
                if (isWithinBound(myBound,county)){
                    //System.out.print("Got");
                    //System.out.println(county);
                    nearbyCounties.add(county);
                }
            }
        }
        else
        {
            for(Node n: node.nodes) {
                RNode subNode = (RNode) n;
                if (interceptWithBound(myBound, subNode.bound)){
                    //System.out.println("Getting into" + subNode.bound + "With minDist: " + knnMinDist(new Point2D.Double(-71.09387040138245,  42.34990180474362), subNode.bound));
                    getInnerLocations(subNode, myBound, nearbyCounties);
                }
            }
        }
    }

	private boolean interceptWithBound(Bound myBound, Node node)
    {
        return interceptWithBound(myBound, node.bound);
    }
	//checks if bounding rectangle is overlapping with rectangle mybound
	private boolean interceptWithBound(Bound myBound, Bound nodeBound)
	{
        //See if bounds in mybound are within bounds in nodebound

        //If one rectangle is to the left of the other
        if( myBound.low_lon > nodeBound.high_lon || nodeBound.low_lon > myBound.high_lon)
            return false;

        //If one rectangle is above the other
        if( myBound.low_lat > nodeBound.high_lat || nodeBound.low_lat > myBound.high_lat)
            return false;

        return true;
	}

    //checks if a county/node coordinate is within rectangle mybound
    private boolean isWithinBound(Bound mybound, County county)
    {
        if (county.lon <= mybound.high_lon && county.lon >= mybound.low_lon &&
            county.lat <= mybound.high_lat && county.lat >= mybound.low_lat)
        {
            return true;
        }
        return false;
    }

    private class NodeNNL {
        County county;
        double dist;

        NodeNNL (County c, double distance){
            this.county = c;
            this.dist = distance;
        }
    }

    public void nearestKNeighborSearch (RNode node, Point2D.Double p, ArrayList<NodeNNL> counties, int k, double minMaxBranchDist)
    {

        if (node.nodes.get(0) instanceof County) {
            //Leaf Level
			for (Node n: node.nodes)
			{
				County c = (County) n;
				double dist = (p.x - c.lon) * (p.x - c.lon) + (p.y - c.lat) * (p.y - c.lat);

                if (dist > minMaxBranchDist)
                    continue;

                if(counties.size() == 0) {
                    counties.add(new NodeNNL(c, dist));
                } else if(counties.size() >= k) {
                    //If we already have enough
                    if (dist < max_dist){
                        //Adding to list, keeping it sorted
                        for(int i = counties.size() - 1; i >= 0; i--) {
                            if(counties.get(i).dist <= dist){
                                //Only add if it's not at the end
                                if (i != counties.size() - 1){
                                    //Add it and then remove from end
                                    counties.add(i + 1, new NodeNNL(c, dist));
                                    counties.remove(counties.size() - 1);
                                }
                                break;
                            } else if (i == 0) {
                                //Add to beginning
                                counties.add(0 , new NodeNNL(c, dist));
                                counties.remove(counties.size() - 1);
                            }
                        }
                        //Removing extra counties at the end
                    }
                } else {
                    //Add to list no matter what
                    for(int i = counties.size() - 1; i >= 0; i--) {
                        if(counties.get(i).dist <= dist){
                            //Add to after i
                            counties.add(i + 1, new NodeNNL(c, dist));
                            break;
                        } else if (i == 0) {
                            //Add to beginning
                            counties.add(0 , new NodeNNL(c, dist));
                        }
                    }
                }
                max_dist = counties.get(counties.size() - 1).dist;
			}
            //TODO REMOVE IF STATEMENT BELOW
            if(counties.size() > k)
                System.out.println("ERROR. counties size more than needed");
        } else {

            ArrayList<NodeNN> branchlist = new ArrayList<NodeNN>();

			//----------------------generate branchlist------------------------------------
			for (int i = 0; i < node.nodes.size(); i++)
			{
				double mindist = knnMinDist(p, node.nodes.get(i).bound);
				double minmaxdist = knnMinMaxDist(p, node.nodes.get(i).bound);
				NodeNN branch = new NodeNN((RNode)(node.nodes.get(i)), mindist, minmaxdist);
				branchlist.add(branch);
			}

			//sort branchlist
            Collections.sort(branchlist, new MinMaxDistSorter());

            //downwoard pruning
            for( int i = branchlist.size() - 1; i >= k; i--){
                if (branchlist.get(i).minDist > branchlist.get(k - 1).minmaxDist) {
                    branchlist.remove(i);
                }
            }

            for( int i = 0; i < branchlist.size(); i++){

                NodeNN n = branchlist.get(i);
                double branch_max_dist = (branchlist.size() > k) ? branchlist.get(k).minmaxDist : Double.MAX_VALUE;
                nearestKNeighborSearch(n.node, p, counties, k, branch_max_dist);

                //Upward pruning
                if( counties.size() >= k) {
                    for( int j = 0; j < branchlist.size(); j++) {
                        NodeNN toDelete = branchlist.get(j);
                        if (toDelete.minDist > max_dist) {
                            if (j <= i)
                                i--;
                            branchlist.remove(j);
                            j--;
                        }
                    }
                }
            }
        }
    }

    public class MinMaxDistSorter implements Comparator<NodeNN>
    {
        public int compare(NodeNN nodenn1, NodeNN nodenn2)
        {
            Double num1 = nodenn1.minmaxDist;
            Double num2 = nodenn2.minmaxDist;
            return num1.compareTo(num2);
        }
    }

	public static double knnMinMaxDist(Point2D.Double p, Bound bound)
	{
		double xnum = 0, ynum = 0;

        //pi-rMi
		if (p.x >= (bound.low_lon + bound.high_lon) / 2)
			ynum += (p.x - bound.low_lon) * (p.x - bound.low_lon);
		else
			ynum += (p.x - bound.high_lon) * (p.x - bound.high_lon);

		if (p.y >= (bound.low_lat + bound.high_lat)/2)
			xnum += (p.y - bound.low_lat) * (p.y - bound.low_lat);
		else
			xnum += (p.y - bound.high_lat) * (p.y - bound.high_lat);

        //pk-rmk
		if (p.x <= (bound.low_lon + bound.high_lon) / 2)
			xnum += (p.x - bound.low_lon) * (p.x - bound.low_lon);
		else
			xnum += (p.x - bound.high_lon) * (p.x - bound.high_lon);

		if (p.y <= (bound.low_lat + bound.high_lat)/2)
			ynum += (p.y - bound.low_lat) * (p.y - bound.low_lat);
		else
			ynum += (p.y - bound.high_lat) * (p.y - bound.high_lat);

		if (xnum < ynum)
			return xnum;

		return ynum;

	}

	//calculates the square of the Euclidian distance from a point to a bounding box
	public static double knnMinDist(Point2D.Double p, Bound bound)
	{
		double dist = 0;

		if (p.x < bound.low_lon)
			dist += (bound.low_lon - p.x) * (bound.low_lon - p.x);
		else if (p.x > bound.high_lon)
			dist += (bound.high_lon - p.x) * (bound.high_lon - p.x);

		if (p.y < bound.low_lat)
			dist += (bound.low_lat - p.y) * (bound.low_lat - p.y);
		else if (p.y > bound.high_lat)
			dist += (p.y - bound.high_lat) * (p.y - bound.high_lat);

		return dist;
	}

    public void printTree() {
        printNode(root, 0);
    }

    public static void printNode(RNode root, int level) {
        String indent = "";
        for (int i = 0; i < level; i ++) {
            indent = indent + "\t";
        }
        System.out.println(indent + "Size: " + root.size() + " Bound: " + root.bound);
        for (int i = 0 ; i < root.size(); i ++ ) {
            Node temp = root.nodes.get(i);
            if ( temp instanceof RNode) {
                printNode((RNode)temp, level + 1);
            } else if (temp instanceof County) {
                County county = ((County) temp);
                System.out.println(indent + county);
            }
        }
    }

    public void readCountyFromFile(String dataFileName) throws NumberFormatException, IOException {


        /**
         * Creating a buffered reader to read the file
         */
        BufferedReader bReader = new BufferedReader(
                new FileReader(dataFileName));

        String line;

        /**
         * Looping the read block until all lines in the file are read.
         */
        boolean first = true;
        while ((line = bReader.readLine()) != null) {
            if (first) {
                first = false;
                continue;
            }

            /**
             * Splitting the content of tabbed separated line
             */
            String datavalue[] = line.split("\t");
            String state = datavalue[0];
            String title = datavalue[1];
            double lat = Double.parseDouble(datavalue[2]);
            double lon = Double.parseDouble(datavalue[3]);

            insertCounty(new County(lon, lat, title, state));

        }
        bReader.close();
    }

	public ArrayList<County> getNearestKLocationsAtPoint(double lon, double lat, int k){
        Point2D.Double x = new Point2D.Double(lon, lat);
        ArrayList<NodeNNL> result = new ArrayList<NodeNNL>();
        ArrayList<NodeNN> branchlist = new ArrayList<NodeNN>();
        max_dist = Double.MAX_VALUE;
        nearestKNeighborSearch(root, x, result, k, Double.MAX_VALUE);
        ArrayList<County> counties = new ArrayList<County>();
        for (int i= 0; i < result.size(); i++ ) {
            counties.add(result.get(i).county);
        }
		return counties;
	}

}
