package a01_p02_dp_bl.algorithms;

import a01_p02_dp_bl.GraphUtils;
import a01_p02_dp_bl.GraphVertex;
import a01_p02_dp_bl.interfaces.CustomGraph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BreadthFirst{

    public static GraphPath search(CustomGraph g, GraphVertex start, GraphVertex vertex){
        List<GraphVertex> open = new ArrayList<GraphVertex>();
        Map<GraphVertex, Integer> closed = new HashMap<GraphVertex, Integer>();
        List<GraphVertex> neighbours = new ArrayList<GraphVertex>();
        //Den Anfang in die Open Liste einfügen
        open.add(start);
        //Den Anfang in die Closed Liste einfügen
        closed.put(start, 0);
        //Iteration über Open Liste
        while(!open.isEmpty()){
            //Nehme den Nächsten Knoten aus der Open Liste
            GraphVertex v = open.remove(0);
            //Beende, wenn aktueller Knoten der ZielKnoten ist
            if (v.equals(vertex)) break;
            //Nachbarknoten ermitteln
            neighbours = g.getNeighbours(v, closed);
            //Nachbarknoten in die Closed Liste einfügen
            for(GraphVertex n : neighbours){
                if(closed.get(n) == null || closed.get(n) > closed.get(v) + 1) closed.put(n, closed.get(v) + 1);
            }
            open.addAll(neighbours);
        }
        //Erstelle einen Pfad
        return GraphUtils.createPath(g, start, vertex, closed);
    }




}
