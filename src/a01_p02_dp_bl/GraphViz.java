package a01_p02_dp_bl;

import org.jgrapht.Graph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.*;

public class GraphViz {

    public static <V, E> void buildDotFile(String filePath, Graph<V, E> graph) throws IOException {
        DOTExporter<V, E> export =
                new DOTExporter<V, E>(new VertexNameProvider<V>() {

                    @Override
                    public String getVertexName(V vertex) {
                    return vertex.toString();
                    }
                }, new VertexNameProvider<V>() {

                    @Override
                    public String getVertexName(V vertex) {
                        return vertex.toString();
                    }
                }, null);

        Writer writer = new BufferedWriter(new FileWriter(filePath + ".dot"));
        export.export(writer, graph);
        writer.close();
        compileDotFile(filePath);
    }

    public static void compileDotFile(String dotFileName){
        String cmd = "dot -Tpng -o " + dotFileName + ".png "+ dotFileName +".dot";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
            buildDotFile("src/files/graph1",   new GraphDirected().load("src/files/graph1.gka"));
            buildDotFile("src/files/graph2",   new GraphUndirected().load("src/files/graph2.gka"));
            buildDotFile("src/files/graphDK5", new GraphDirected().load("src/files/graphDK5.gka"));
            buildDotFile("src/files/graphDVL", new GraphDirected().load("src/files/graphDVL.gka"));
            buildDotFile("src/files/graphUK5", new GraphUndirected().load("src/files/graphUK5.gka"));
            buildDotFile("src/files/graphUVL", new GraphUndirected().load("src/files/graphUVL.gka"));
    }

}