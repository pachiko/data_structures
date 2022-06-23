package bearmaps.proj2d;

import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    PointSet tree;
    HashMap<Point, Long> pointToNode;
    MyTrieMap<Node> trie;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);

        List<Node> nodes = this.getNodes();
        ArrayList<Point> pts = new ArrayList<>(nodes.size());
        pointToNode = new HashMap<>(nodes.size());
        trie = new MyTrieMap<>();

        for (Node n: nodes) {
            Point p = new Point(n.lon(), n.lat());
            pts.add(p);
            pointToNode.put(p, n.id());
            String name = n.name();
            if (name != null) {
                trie.add(cleanString(name), n);
            }
        }
        tree = new KDTree(pts);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point nearest = tree.nearest(lon, lat);
        return pointToNode.get(nearest);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String clean = cleanString(prefix);
        List<Node> found = trie.valuesWithPrefix(clean);
        ArrayList<String> res = new ArrayList<>(found.size());
        for (Node n: found) {
            res.add(n.name());
        }
        return res;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String clean = cleanString(locationName);
        List<Node> found = trie.get(clean);
        List<Map<String, Object>> res = new ArrayList<>();
        for (Node n : found) {
            HashMap<String, Object> nodeInfo = new HashMap<>();
            nodeInfo.put("lat", n.lat());
            nodeInfo.put("lon", n.lon());
            nodeInfo.put("name", n.name());
            nodeInfo.put("id", n.id());
            res.add(nodeInfo);
        }
        return res;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
