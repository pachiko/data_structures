package bearmaps.proj2c;

import bearmaps.proj2ab.DoubleMapPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    int numStatesExplored;
    double explorationTime;
    SolverOutcome outcome;
    LinkedList<Vertex> result;
    double pathWeight;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch time = new Stopwatch();
        pathWeight = 0.;
        result = new LinkedList<>();
        outcome = SolverOutcome.UNSOLVABLE;

        ExtrinsicMinPQ<Vertex> pq = new DoubleMapPQ<>();
        pq.add(start, input.estimatedDistanceToGoal(start, end));

        HashMap<Vertex, Double> distance = new HashMap<>();
        distance.put(start, 0.);
        HashMap<Vertex, Vertex> jump = new HashMap<>();
        HashSet<Vertex> visited = new HashSet<>();

        while (pq.size() > 0 && time.elapsedTime() < timeout && outcome != SolverOutcome.SOLVED) {
            Vertex v = pq.removeSmallest();
            if (v.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                break;
            }

            if (visited.contains(v)) continue;
            numStatesExplored++;
            visited.add(v);

            List<WeightedEdge<Vertex>> vn = input.neighbors(v);
            for (WeightedEdge<Vertex> e : vn) {
                Vertex b = e.to();
                double heu = input.estimatedDistanceToGoal(b, end);
                double dist = e.weight();

                if (!distance.containsKey(b) || distance.get(v) + dist < distance.get(b)) {
                    distance.put(b, distance.get(v) + dist);
                    jump.put(b, v);

                    if (pq.contains(b)) {
                        pq.changePriority(b, distance.get(b) + heu);
                    } else {
                        pq.add(b, distance.get(v) + dist + heu);
                    }
                }
            }
        }

        if (outcome == SolverOutcome.SOLVED) {
            pathWeight = distance.get(end);
            Vertex v = end;
            while (true) {
                result.addFirst(v);
                if (v.equals(start)) break;
                v = jump.get(v);
            }
        }

        explorationTime = time.elapsedTime();
        if (explorationTime >= timeout) outcome = SolverOutcome.TIMEOUT;
    }

    public SolverOutcome outcome() {
        return outcome;
    }

    public List<Vertex> solution() { return result; }

    public double solutionWeight() { return pathWeight; }

    public int numStatesExplored() {
        return numStatesExplored;
    }

    public double explorationTime() {
        return explorationTime;
    }
}
