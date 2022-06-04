package bearmaps.proj2c;

import bearmaps.proj2ab.DoubleMapPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.*;

/**
 * Obfuscated implementation of a solver for a shortest paths problem.
 * Created by hug.
 */
public class WeirdSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private AStarGraph<Vertex> graph;
    private List<Vertex> vertexList;
    private Map<Vertex, WeightedEdge<Vertex>> edgeMap = new HashMap<>();
    private Map<Vertex, Double> distance = new HashMap<>();

    private Vertex goal;
    private SolverOutcome outcome;
    private int statesExplored = 0;
    private double elapsedTime;

    public WeirdSolver(AStarGraph<Vertex> graph1, Vertex start, Vertex end, double timeout) {
        graph = graph1;
        this.goal = end;
        ExtrinsicMinPQ<Vertex> pq = new DoubleMapPQ<>();

        pq.add(start, graph.estimatedDistanceToGoal(start, end));
        edgeMap.put(start, null);
        distance.put(start, 0.0);

        Stopwatch timer = new Stopwatch();

        while (pq.size() > 0 && !foundGoal(pq, end) && enoughTime(timer, timeout)) {
            Vertex vertex = pq.removeSmallest();
            statesExplored += 1;
            for (WeightedEdge<Vertex> edge : graph.neighbors(vertex)) {
                Vertex to = edge.to();

                double distance = distanceTo(to);
                double newDist = distanceTo(vertex) + edge.weight();

                if (newDist < distance) {
                    edgeMap.put(to, edge);
                    this.distance.put(to, newDist);
                    double priority = graph.estimatedDistanceToGoal(to, end) + distanceTo(to);

                    if (pq.contains(to)) pq.changePriority(to, priority);
                    else pq.add(to, priority);
                }
            }
        }

        elapsedTime = timer.elapsedTime();

        if (pq.size() == 0) {
            this.outcome = SolverOutcome.UNSOLVABLE;
            vertexList = new ArrayList<>();
            return;
        }

        vertexList = constructPath(start, pq.getSmallest());

        if (pq.getSmallest().equals(end)) {
            this.outcome = SolverOutcome.SOLVED;
        } else {
            this.outcome = SolverOutcome.TIMEOUT;
        }
    }

    private boolean foundGoal(ExtrinsicMinPQ<Vertex> pq, Vertex end) {
        return pq.getSmallest().equals(end);
    }


    private boolean enoughTime(Stopwatch timer, double timeout) {
        return timer.elapsedTime() < timeout;
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    private List<Vertex> constructPath(Vertex start, Vertex end) {
        List<Vertex> res = new ArrayList<>();

        res.add(end);
        while (edgeMap.get(end) != null) {
            WeightedEdge<Vertex> e = edgeMap.get(end);
            res.add(e.from());
            end = e.from();
        }
        Collections.reverse(res);
        return res;
    }


    private double distanceTo(Vertex v) {
        return distance.getOrDefault(v, Double.POSITIVE_INFINITY);
    }

    @Override
    public double solutionWeight() {
        return distanceTo(goal);
    }

    @Override
    public List<Vertex> solution() {
        return vertexList;
    }

    @Override
    public int numStatesExplored() {
        return statesExplored;
    }

    @Override
    public double explorationTime() {
        return elapsedTime;
    }
}
