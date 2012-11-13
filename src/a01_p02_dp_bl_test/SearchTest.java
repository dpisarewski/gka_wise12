package a01_p02_dp_bl_test;

import static org.junit.Assert.*;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.*;

import a01_p02_dp_bl.*;

public class SearchTest {
	GraphDirected 	gd;
	GraphUndirected gu;
	
	@Before
	public void setUp(){
		gd = new GraphDirected();
		gu = new GraphUndirected();
	}

	@Test
	public void testZeroLengthPath() {
		GraphVertex v1 = new GraphVertex("Bremen"); 
		gd.addVertex(v1);
		gu.addVertex(v1);
		assertEquals(0, ((GraphDefaultPath) gd.find(v1, v1, "BFS")).length());
		assertEquals(0, ((GraphDefaultPath) gu.find(v1, v1, "BFS")).length());
		assertEquals(0, ((GraphDefaultPath) gd.find(v1, v1, "DFS")).length());
		assertEquals(0, ((GraphDefaultPath) gu.find(v1, v1, "DFS")).length());
	}
	
	@Test
	public void testMinimalLengthPath(){
		GraphVertex v1 = new GraphVertex("Bremen");
		GraphVertex v2 = new GraphVertex("Hamburg");
		gd.addVertex(v1);
		gd.addVertex(v2);
		gd.addEdge(v1, v2);
		gu.addVertex(v1);
		gu.addVertex(v2);
		gu.addEdge(v1, v2);
		assertEquals(1, ((GraphDefaultPath) gd.find(v1, v2, "BFS")).length());
		assertEquals(1, ((GraphDefaultPath) gu.find(v1, v2, "BFS")).length());
		assertEquals(1, ((GraphDefaultPath) gd.find(v1, v2, "DFS")).length());
		assertEquals(1, ((GraphDefaultPath) gu.find(v1, v2, "DFS")).length());
	}
	
	@Test
	public void testNormalLengthPath(){
		GraphVertex v1 = new GraphVertex("Bremen");
		GraphVertex v2 = new GraphVertex("Hamburg");
		GraphVertex v3 = new GraphVertex("Berlin");
		gd.addVertex(v1);
		gd.addVertex(v2);
		gd.addVertex(v3);
		gd.addEdge(v1, v2);
		gd.addEdge(v2, v3);
		gu.addVertex(v1);
		gu.addVertex(v2);
		gu.addVertex(v3);
		gu.addEdge(v1, v2);
		gu.addEdge(v2, v3);
		assertEquals(2, ((GraphDefaultPath) gd.find(v1, v3, "BFS")).length());
		assertEquals(2, ((GraphDefaultPath) gu.find(v1, v3, "BFS")).length());
		assertEquals(2, ((GraphDefaultPath) gd.find(v1, v3, "DFS")).length());
		assertEquals(2, ((GraphDefaultPath) gu.find(v1, v3, "DFS")).length());
	}


	
	@Test
	public void testGraphDVL(){
		gd.load("files/graphDVL.gka");
		System.out.println(gd.toString());
		
        System.out.println("VL Directed BFS");
        GraphPath p = gd.find(new GraphVertex("s"), new GraphVertex("t"), "BFS");
        System.out.println(p.toString());
        System.out.println("Anzahl der Zugriffe: " + gd.getAccessCount());
        gd.clearAccessCount();
        System.out.println("\n\r");
        
        System.out.println("VL Directed DFS");
        GraphPath pd = gd.find(new GraphVertex("s"), new GraphVertex("t"), "DFS");
        System.out.println(pd.toString());
        System.out.println("Anzahl der Zugriffe: " + gd.getAccessCount());
        System.out.println("\n\r");
	}
	
	@Test
	public void testGraphUVL(){
		gu.load("files/graphUVL.gka");
		System.out.println(gu.toString());
		
        System.out.println("VL Undirected BFS");
        GraphPath p = gu.find(new GraphVertex("s"), new GraphVertex("t"), "BFS");
        System.out.println(p.toString());
        System.out.println("Anzahl der Zugriffe: " + gu.getAccessCount());
        gu.clearAccessCount();
        System.out.println("\n\r");

        System.out.println("VL Undirected DFS");
        GraphPath pd = gu.find(new GraphVertex("s"), new GraphVertex("t"), "DFS");
        System.out.println(pd.toString());
        System.out.println("Anzahl der Zugriffe: " + gu.getAccessCount());
        System.out.println("\n\r");
	}

	
	@Test
	public void testGraph1(){
		gd.load("files/graph1.gka");
		System.out.println(gd.toString());
		
        System.out.println("Graph1 Directed BFS");
        GraphPath p = gd.find(new GraphVertex("a"), new GraphVertex("f"), "BFS");
        System.out.println(p.toString());
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(0)).getName().equals("a"));
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(1)).getName().equals("k"));
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(2)).getName().equals("g"));
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(3)).getName().equals("e"));
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(4)).getName().equals("f"));
        System.out.println("Anzahl der Zugriffe: " + gd.getAccessCount());
        gd.clearAccessCount();
        System.out.println("\n\r");
        	        
        System.out.println("Graph1 Directed DFS");
        GraphPath pd = gd.find(new GraphVertex("a"), new GraphVertex("f"), "DFS");
        System.out.println(pd.toString());
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(0)).getName().equals("a"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(1)).getName().equals("k"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(2)).getName().equals("g"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(3)).getName().equals("e"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(4)).getName().equals("f"));
        System.out.println("Anzahl der Zugriffe: " + gd.getAccessCount());
        System.out.println("\n\r");
	}
	
	@Test
	public void testGraph2(){
		gu.load("files/graph2.gka");
		System.out.println(gu.toString());
        
        System.out.println("Graph2 Undirected BFS");
        GraphPath p = gu.find(new GraphVertex("a"), new GraphVertex("f"), "BFS");
        System.out.println(p.toString());
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(0)).getName().equals("a"));
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(1)).getName().equals("c"));
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(2)).getName().equals("f"));
        System.out.println("Anzahl der Zugriffe: " + gu.getAccessCount());
        gu.clearAccessCount();
        System.out.println("\n\r");
        	        
        System.out.println("Graph2 Undirected DFS");
        GraphPath pd = gu.find(new GraphVertex("a"), new GraphVertex("f"), "DFS");
        System.out.println(pd.toString());
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(0)).getName().equals("a"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(1)).getName().equals("c"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(2)).getName().equals("f"));
        System.out.println("Anzahl der Zugriffe: " + gu.getAccessCount());
        System.out.println("\n\r");
	}
		
	
	@Test
	public void testGraphUK5(){
		gu.load("files/graphUK5.gka");
        System.out.println(gu.toString());

        String vertices[] = new String[] {"a","b","c","d"};
        for(String vs : vertices) {
	        System.out.println("K5 Undirected BFS");
	        GraphPath p = gu.find(new GraphVertex(vs), new GraphVertex("e"), "BFS");
	        System.out.println(p.toString());
	        System.out.println("Anzahl der Zugriffe: " + gu.getAccessCount());
	        gu.clearAccessCount();
	        System.out.println("\n\r");
        }
        
        for(String vs : vertices) {
	        System.out.println("K5 Undirected DFS");
	        GraphPath pd = gu.find(new GraphVertex(vs), new GraphVertex("e"), "DFS");
	        System.out.println(pd.toString());
	        System.out.println("Anzahl der Zugriffe: " + gu.getAccessCount());
	        gu.clearAccessCount();
	        System.out.println("\n\r");
        }
	}

	@Test
	public void testGraphDK5(){
		gd.load("files/graphDK5.gka");
        System.out.println(gd.toString());

        String vertices[] = new String[] {"a","b","c","d"};
        for(String vs : vertices) {
	        System.out.println("K5 Directed BFS");
	        GraphPath p = gd.find(new GraphVertex(vs), new GraphVertex("e"), "BFS");
	        System.out.println(p.toString());
	        System.out.println("Anzahl der Zugriffe: " + gd.getAccessCount());
	        gd.clearAccessCount();
	        System.out.println("\n\r");
        }
        
        for(String vs : vertices) {
	        System.out.println("K5 Directed DFS");
	        GraphPath pd = gd.find(new GraphVertex(vs), new GraphVertex("e"), "DFS");
	        System.out.println(pd.toString());
	        System.out.println("Anzahl der Zugriffe: " + gd.getAccessCount());
	        gd.clearAccessCount();
	        System.out.println("\n\r");
        }
        
	}
}
