package a01_p02_dp_bl;

import org.jgrapht.Graph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;

import java.io.*;

import a01_p02_dp_bl.interfaces.*;

public class GraphViz {

    public static <V, E> void buildDotFile(String filePath, CustomGraph graph) throws IOException {
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
        export.export(writer, (Graph<V, E>)graph);
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
            buildDotFile("files/graph1",   new GraphDirected().load("files/graph1.gka"));
            buildDotFile("files/graph2",   new GraphUndirected().load("files/graph2.gka"));
            buildDotFile("files/graphDK5", new GraphDirected().load("files/graphDK5.gka"));
            buildDotFile("files/graphDVL", new GraphDirected().load("files/graphDVL.gka"));
            buildDotFile("files/graphUK5", new GraphUndirected().load("files/graphUK5.gka"));
            buildDotFile("files/graphUVL", new GraphUndirected().load("files/graphUVL.gka"));
    }

}