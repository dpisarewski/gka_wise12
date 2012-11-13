package a01_p02_dp_bl.interfaces;

import a01_p02_dp_bl.GraphVertex;
import org.jgrapht.Graph;

import java.util.List;
import java.util.Map;

public interface CustomGraph{

    public List<GraphVertex> filterByValue(GraphVertex vertex, Map<GraphVertex, Integer> map, Integer value);
    public List<GraphVertex> getNeighbours(GraphVertex vertex, Map<GraphVertex, Integer> closed);
}