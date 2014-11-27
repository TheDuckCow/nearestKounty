
public class Bound {
        double low_lon;
        double high_lon;
        double low_lat;
        double high_lat;
        public Bound(double low_lon, double high_lon, double low_lat, double high_lat) {
            this.low_lon = low_lon;
            this.high_lon = high_lon;
            this.low_lat = low_lat;
            this.high_lon = high_lon;
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
        
        public static Bound newBoundWithNode(Node node, Bound oldBound) {
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
    }