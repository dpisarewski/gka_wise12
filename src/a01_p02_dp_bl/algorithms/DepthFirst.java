package a01_p02_dp_bl.algorithms;

import a01_p02_dp_bl.GraphUtils;
import a01_p02_dp_bl.interfaces.CustomGraph;
import a01_p02_dp_bl.GraphVertex;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DepthFirst{

    public static GraphPath search(CustomGraph g, GraphVertex start, GraphVertex vertex, Map<GraphVertex, Integer> closed, int cost) {
        //Den Anfang in die Closed Liste einfügen
        closed.remove(start);
        closed.put(start, cost);
        //Beenden, wenn Anfang das Ziel ist
        if (start.equals(vertex)) return cost == 0 ? GraphUtils.createPath(g, start, vertex, closed) : null;
        //Nachbarknoten ermitteln
        List<GraphVertex> neighbours = new ArrayList<GraphVertex>();
        neighbours = g.getNeighbours(start, closed);

        //Iteration über Nachbarknoten
        for (GraphVertex v : neighbours){
            //Wenn aktueller Knoten nicht in der Closed Liste
            if (closed.get(v) == null || closed.get(v) > cost){
                //Rufe die Suche rekursiv auf, und wenn der Zielknoten gefunden ist
                search(g, v, vertex, closed, cost + 1);
            }
        }
        //Gebe einen leeren Pfad zurück, falls nichts gefunden
        return cost == 0 ? GraphUtils.createPath(g, start, vertex, closed) : null;
    }

}
