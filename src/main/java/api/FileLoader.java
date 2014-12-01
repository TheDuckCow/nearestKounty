package api;

import java.io.IOException;

import nearestFinder.RTree;

public class FileLoader implements Runnable{
	public static RTree tree;
	
	@Override
	public void run() {
		System.out.println("Reading the counties file.");
		// Read in the file that will be used
		tree = new RTree();
		String file = "counties.txt";
		try {
			tree.readCountyFromFile(file);
		} catch (NumberFormatException | IOException e) {
			System.out.println("Can't read file");
			e.printStackTrace();
		}
		System.out.println("Done reading file.");
		
	}
	
    public static void main(String args[]) {
        (new Thread(new FileLoader())).start();
    }

}


