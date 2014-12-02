
import java.util.ArrayList;

public class RNode extends Node{
    ArrayList<Node> nodes = new ArrayList<Node>();
    public int size(){
        return nodes.size();
    }
    public void addNode(Node inNode){
        nodes.add(inNode);
        updateBoundWithNewNode(inNode);
    }

    public void updateBoundWithNewNode(Node inNode) {
        if (bound == null) {
            bound = inNode.bound;
        } else
            bound = Bound.newBoundWithNode(inNode, bound);


    }
}
