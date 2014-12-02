

public class NodeNN {
    RNode node;
    double minDist;
    double minmaxDist;

    public NodeNN (RNode node, double minDist, double minmaxDist)
    {
        this.node = node;
        this.minDist = minDist;
        this.minmaxDist = minmaxDist;
    }

}
    //public class MinDistSorter implements Comparator<NodeNN>
    //{
        //public int compare(NodeNN nodenn1, NodeNN nodenn2)
        //{
            //Double num1 = nodenn1.minDist;
            //Double num2 = nodenn2.minDist;
            //return num1.compareTo(num2);
        //}
    //}


//public class MinMaxDistSorter implements Comparator<NodeNN>
//{
    //public int compare(NodeNN nodenn1, NodeNN nodenn2)
    //{
        //Double num1 = nodenn1.minmaxDist;
        //Double num2 = nodenn2.minmaxDist;
        //return num1.compareTo(num2);
    //}
//}


