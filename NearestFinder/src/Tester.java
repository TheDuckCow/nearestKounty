import java.io.IOException;
import java.util.ArrayList;

public class Tester {
    public static void main(String [] args){

        RTree tree = new RTree();
        //tree.printTree();
        //int numberToAdd = 100;
        //for( int i = 0; i < numberToAdd; i ++ ) {
            ////System.out.println();
            ////System.out.println("Adding: " + i);
            //County c = new County(i ,i ,"" + i, "" + i);
            //tree.insertCounty(c);
            ////if (i > 6)
        //}
        //tree.printTree();
        //ArrayList<County> result = tree.getLocationsInBound(new Bound(32, 37, 32, 37));
        //for(County c: result) {
            //System.out.println(c);
        //}

        String file = "/Users/timothychong/Documents/workspace/arduino/NearestFinder/src/counties.txt";
        try {
			tree.readCountyFromFile(file);
		} catch (NumberFormatException | IOException e) {
            System.out.println("Can't read file");
			e.printStackTrace();
		}

        //tree.printTree();

        System.out.println("Searching");
        ArrayList<County> result = tree.getLocationsInBound(new Bound(-114.7, -114.6, 45.198, 45.200));
        for(County c: result) {
            System.out.println(c);
        }
    }

}
