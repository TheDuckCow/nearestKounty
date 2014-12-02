
import java.io.IOException;
import java.util.ArrayList;

public class Tester {
    public static void main(String [] args){

        RTree tree = new RTree();
        //tree.printTree();
        //int numberToAdd = 100;
        //for( int i = 0; i < numberToAdd; i ++ ) {
            //System.out.println();
            //System.out.println("Adding: " + i);
            //County c = new County(i ,i ,"" + i, "" + i);
            //tree.insertCounty(c);
        //}

        //tree.printTree();
        //ArrayList<County> result = tree.getLocationsInBound(new Bound(32, 37, 32, 37));
        //for(County c: result) {
            //System.out.println(c);
        //}

        String file = "/Users/timothychong/Documents/workspace/arduino/counties.txt";
        try {
            tree.readCountyFromFile(file);
        } catch (NumberFormatException | IOException e) {
            System.out.println("Can't read file");
            e.printStackTrace();
        }

        //tree.printTree();
        //33.6167° N, 117.8975° W
        //33.9481° N, 117.3961° Wkj
        //System.out.println("Searching");
        //ArrayList<County> result = tree.getLocationsInBound(new Bound(-117.4975, -117.3961, 33.6167, 33.9481));
        //for(County c: result) {
            //System.out.println(c);
        //}

        System.out.println("Searching");
        ArrayList<County> result = tree.getNearestKLocationsAtPoint(-117, 33 ,10);
        for(County c: result) {
            System.out.println(c);
        }

    }

}
