package bearmaps.proj2c.integerhoppuzzle;

import bearmaps.proj2c.AStarGraph;
import bearmaps.proj2c.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * The Integer Hop puzzle implemented as a graph.
 * Created by hug.
 */
public class IntegerHopGraph implements AStarGraph<Integer> {
    @Override
    public List<WeightedEdge<Integer>> neighbors(Integer v) {
        ArrayList<WeightedEdge<Integer>> neighbors = new ArrayList<>();
        neighbors.add(new WeightedEdge<>(v, v * v, 10));
        neighbors.add(new WeightedEdge<>(v, v * 2, 5));
        neighbors.add(new WeightedEdge<>(v, v / 2, 5));
        neighbors.add(new WeightedEdge<>(v, v - 1, 1));
        neighbors.add(new WeightedEdge<>(v, v + 1, 1));
        return neighbors;
    }

    @Override
    public double estimatedDistanceToGoal(Integer s, Integer goal) {
        // possibly fun challenge: Try to find an admissible heuristic that
        // speeds up your search. This is tough!

//        return 0; (Djikstra's)

        int res;

        if (goal > s*s) {
            res = (goal - s*s)/10;
        } else if (goal >= s*2) {
            res = (goal - s*2)/5;
        } else if (goal <= s/2) {
            res = (goal - s/2)/5;
        } else {
            res = goal - s;
        }

        return res < 0? -res : res;
    }
}
