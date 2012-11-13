package a01_p02_dp_bl;

import a01_p02_dp_bl.algorithms.BreadthFirst;
import a01_p02_dp_bl.algorithms.DepthFirst;
import a01_p02_dp_bl.interfaces.CustomGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class GraphUtils {

    public static GraphPath createPath(CustomGraph g, GraphVertex start, GraphVertex vertex, Map<GraphVertex, Integer> closed){
        GraphDefaultPath<GraphVertex, DefaultWeightedEdge> path = new GraphDefaultPath<GraphVertex, DefaultWeightedEdge>(g);
        //Anfangen mit dem Zielknoten
        GraphVertex v = vertex;
        //Vorgängerknoten ermitteln
        List<GraphVertex> predecessors = new ArrayList<GraphVertex>();
        //Solange es Knoten in der Closed Liste gibt
        while(v != null && closed.get(v) > 0){
            //Füge den aktuellen Knoten in den Pfad ein
            path.addVertex(v);
            //Vorgangerknoten ermitteln
            predecessors = g.filterByValue(v, closed, closed.get(v) - 1);
            //Beenden, falls keine Vorgängerknoten
            if(predecessors == null) break;
            //TODO Sortiere nach dem Gewicht der Kanten
            //Collections.sort(predecessors);
            v = predecessors.get(0);
        }
        //Den Anfangsknoten in den Pfad einfügen
        path.addVertex(start);
        //Pfad umkehren
        return path.reverse();
    }


    public static GraphPath find(CustomGraph g, GraphVertex start, GraphVertex vertex, String alg) {
        if (alg.equals("BFS")) {
            // Wenn Breadth-First Suche
            return BreadthFirst.search(g, start, vertex);
        } else if (alg.equals("DFS")) {
            // Wenn Depth-First Suche
            return DepthFirst.search(g, start, vertex, new HashMap<GraphVertex, Integer>(), 0);
        }
        return null;
    }

    /*//Lädt einen gespeicherten Graph aus einer Datei
    public static CustomGraph load(String filename){
        CustomGraph g = null;
        try {
            g = deserialize(new FileReader(filename));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return g;
    }*/
}
