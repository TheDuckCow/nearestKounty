package nearestFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CommandLine {
	
	//starts the command line interface
	public void start(String file)
	{
		RTree tree = new RTree();
        try {
            tree.readCountyFromFile(file);
        } catch (NumberFormatException | IOException e) {
            System.out.println("Can't read file");
            e.printStackTrace();
        }
    
        System.out.println("R-Tree Loading Complete.");
        Scanner in = new Scanner(System.in);
        String cmd;
        
        while (true)
        {
        	do
        	{
                System.out.println("----------------------COMMANDS---------------------------------");
                System.out.println("knearest: Searches for the k nearest neighbors of a query point");
                System.out.println("bound: Searches for all points within a specified bounding rectangle");
                System.out.println("quit: Exits the command line interface");
                System.out.println("---------------------------------------------------------------");
                System.out.println(" ");
                System.out.println("ENTER COMMAND...");
                cmd = in.nextLine();
        	} while (!cmd.equals("knearest") && !cmd.equals("bound") && !cmd.equals("quit"));
        	
            if (cmd.equals("knearest"))
            {
            	cmdKNearest(tree, in);
            }
            else if (cmd.equals("bound"))
            {
            	cmdBound(tree, in);
            }
            else if (cmd.equals("quit"))
            {
            	System.out.println("Exited.");
            	break;
            }
           
        }
        
    	return;
       
	}
	
	//executes the knearestsearch function and prints the results
	public static void cmdKNearest(RTree tree, Scanner inKN)
	{
  //      Scanner inKN = new Scanner(System.in);
		System.out.print("Enter latitude (double): ");
    	double lat = inKN.nextDouble();
    	System.out.print("Enter longitude (double): ");
    	double lon = inKN.nextDouble();
    	System.out.print("Enter number of neighbors (int): ");
    	int k = inKN.nextInt();
    	inKN.nextLine();
    	
    	ArrayList<County> result = tree.getNearestKLocationsAtPoint(lon, lat, k);
    	System.out.println(" ");
    	System.out.println("-----Printing " + k + " nearest locations from point " + lon +  ", " + lat + "-----");
    	System.out.println(" ");
    	for (County c: result)
    	{
    		System.out.println(c);
    	}
        System.out.println(" ");
    	System.out.println("-----Done-----");
        System.out.println(" ");
	}
	
	//executes the withinboundsearch function and prints the results
	public static void cmdBound(RTree tree, Scanner inB)
	{
	//	Scanner inB = new Scanner(System.in);
		System.out.print("Enter longitude for lower left bound: ");
    	double lon1 = inB.nextDouble();
    	System.out.print("Enter latitude for lower left bound: ");
    	double lat1 = inB.nextDouble();

    	System.out.print("Enter longitude for upper right bound: ");
    	double lon2 = inB.nextDouble();
    	System.out.print("Enter latitude for upper right bound: ");
    	double lat2 = inB.nextDouble();
    	inB.nextLine();
    	
        System.out.println(" ");
    	System.out.println("-----Printing all locations within LOWERLEFT bound ["+lon1+","+lat1+"] and UPPERRIGHT bound ["+lon2+","
    			+lat2+"]-----");
        System.out.println(" ");
    	Bound bound = new Bound(lon1,lon2,lat1,lat2);
    	ArrayList<County> result = tree.getLocationsInBound(bound);
    	
    	
    	if (result.size() == 0)
    	{
    		System.out.println("NO LOCATIONS WITHIN BOUND");
    	}
    	else
    	{
        	for (County c: result)
        	{
        		System.out.println(c);
        	}
        	

    	}
        System.out.println(" ");
    	System.out.println("-----Done-----");
        System.out.println(" ");
	}

}
