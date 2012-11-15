package a1_p02_dp_bl_test;

import static org.junit.Assert.*;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.*;

import a1_p02_dp_bl.*;

public class SearchTest {
	GraphDirected 	gd;
	GraphUndirected gu;
	
	@Before
	public void setUp(){
		gd = new GraphDirected(DefaultWeightedEdge.class);
		gu = new GraphUndirected(DefaultWeightedEdge.class);
	}

	/*
	@Test
	public void testZeroLengthPath() {		
		GraphVertex v1 = new GraphVertex("Bremen"); 
		gd.addVertex(v1);
		gu.addVertex(v1);		
		assertEquals(0, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd)).breadthFirst(v1, v1).length());
		assertEquals(0, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu)).breadthFirst(v1, v1).length());
		assertEquals(0, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd)).depthFirst(v1, v1).length());
		assertEquals(0, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu)).depthFirst(v1, v1).length());
		
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
		
		assertEquals(1, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd)).breadthFirst(v1, v2).length());
		assertEquals(1, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu)).breadthFirst(v1, v2).length());
		assertEquals(1, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd)).depthFirst(v1, v2).length());
		assertEquals(1, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu)).depthFirst(v1, v2).length());
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
		
		assertEquals(2, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd)).breadthFirst(v1, v3).length());
		assertEquals(2, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu)).breadthFirst(v1, v3).length());
		assertEquals(2, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd)).depthFirst(v1, v3).length());
		assertEquals(2, (new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu)).depthFirst(v1, v3).length());		
	}
	
	
	@Test
	public void testGraphDVL(){
		gd.load("graphDVL.gka");
		System.out.println(gd.toString());

        System.out.println("VL Directed BFS");
        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> breadth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd);
        GraphDefaultPath<GraphVertex,DefaultWeightedEdge> p = breadth_first.breadthFirst(new GraphVertex("s"), new GraphVertex("t"));
        System.out.println(p.toString());
        System.out.println("Anzahl der Zugriffe: " + breadth_first.getAccessCounter());        
        System.out.println("\n\r");
		assertEquals(3, p.length());

        System.out.println("VL Directed DFS");
        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> depth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd);
        GraphDefaultPath<GraphVertex,DefaultWeightedEdge> pd = depth_first.depthFirst(new GraphVertex("s"), new GraphVertex("t"));
        System.out.println(pd.toString());
        System.out.println("Anzahl der Zugriffe: " + depth_first.getAccessCounter());
        System.out.println("\n\r");
		assertEquals(3, pd.length());
	}
	
	@Test
	public void testGraphUVL(){
		gu.load("graphUVL.gka");
		System.out.println(gu.toString());
		
        System.out.println("VL Undirected BFS");
        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> breadth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu);
        GraphDefaultPath<GraphVertex,DefaultWeightedEdge> p = breadth_first.breadthFirst(new GraphVertex("s"), new GraphVertex("t"));
        System.out.println(p.toString());
        System.out.println("Anzahl der Zugriffe: " + breadth_first.getAccessCounter());
        System.out.println("\n\r");
		assertEquals(3, p.length());

        System.out.println("VL Undirected DFS");
        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> depth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu);
        GraphDefaultPath<GraphVertex,DefaultWeightedEdge> pd = depth_first.depthFirst(new GraphVertex("s"), new GraphVertex("t"));
        System.out.println(pd.toString());
        System.out.println("Anzahl der Zugriffe: " + depth_first.getAccessCounter());
        System.out.println("\n\r");
		assertEquals(3, pd.length());
	}
	*/
	
	@Test
	public void testGraph1(){
		gd.load("graph1.gka");
		System.out.println(gd.toString());
		
        System.out.println("Graph1 Directed BFS");
        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> breadth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd);
        GraphDefaultPath p = breadth_first.breadthFirst(new GraphVertex("a"), new GraphVertex("f"));
        System.out.println(p.toString());
        //assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(0)).getName().equals("a"));
        //assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(1)).getName().equals("k"));
        //assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(2)).getName().equals("g"));
        //assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(3)).getName().equals("e"));
        //assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(4)).getName().equals("f"));
        System.out.println("Anzahl der Zugriffe: " + breadth_first.getAccessCounter());
        System.out.println("\n\r");
        	        
        System.out.println("Graph1 Directed DFS");
        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> depth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd);
        GraphDefaultPath pd = depth_first.depthFirst(new GraphVertex("a"), new GraphVertex("f"));
        System.out.println(pd.toString());
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(0)).getName().equals("a"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(1)).getName().equals("k"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(2)).getName().equals("g"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(3)).getName().equals("e"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(4)).getName().equals("f"));
        System.out.println("Anzahl der Zugriffe: " + breadth_first.getAccessCounter());
        System.out.println("\n\r");
	}	

	@Test
	public void testGraph2() {
		gu.load("graph2.gka");
		System.out.println(gu.toString());
        
        System.out.println("Graph2 Undirected BFS");
        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> breadth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu);
        GraphDefaultPath p = breadth_first.breadthFirst(new GraphVertex("a"), new GraphVertex("f"));
        System.out.println(p.toString());
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(0)).getName().equals("a"));
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(1)).getName().equals("c"));
        assertTrue(((GraphVertex)((GraphDefaultPath)p).getVertexList().get(2)).getName().equals("f"));
        System.out.println("Anzahl der Zugriffe: " + breadth_first.getAccessCounter());
        System.out.println("\n\r");
        	        
        System.out.println("Graph2 Undirected DFS");
        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> depth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu);
        GraphDefaultPath pd = depth_first.breadthFirst(new GraphVertex("a"), new GraphVertex("f"));
        System.out.println(pd.toString());
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(0)).getName().equals("a"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(1)).getName().equals("c"));
        assertTrue(((GraphVertex)((GraphDefaultPath)pd).getVertexList().get(2)).getName().equals("f"));
        System.out.println("Anzahl der Zugriffe: " + depth_first.getAccessCounter());
        System.out.println("\n\r");
	}
	
	@Test
	public void testGraphUK5() {
		gu.load("graphUK5.gka");
        System.out.println(gu.toString());

        String vertices[] = new String[] {"a","b","c","d"};
        for(String vs : vertices) {
	        System.out.println("K5 Undirected BFS");
	        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> breadth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu);
	        GraphDefaultPath<GraphVertex,DefaultWeightedEdge> p = breadth_first.breadthFirst(new GraphVertex(vs), new GraphVertex("e"));	        
	        System.out.println(p.toString());
	        System.out.println("Anzahl der Zugriffe: " + breadth_first.getAccessCounter());
	        System.out.println("\n\r");
			assertEquals(1, p.length());
        }
        
        for(String vs : vertices) {
	        System.out.println("K5 Undirected DFS");
	        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> depth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gu);
	        GraphDefaultPath<GraphVertex,DefaultWeightedEdge> pd = depth_first.depthFirst(new GraphVertex(vs), new GraphVertex("e"));
	        System.out.println(pd.toString());
	        //System.out.println("Anzahl der Zugriffe: " + depth_first.anzahlZugriffe());
	        System.out.println("\n\r");
			assertEquals(1, pd.length());
        }
	}
	
	
	@Test
	public void testGraphDK5(){
		gd.load("graphDK5.gka");
        System.out.println(gd.toString());

        String vertices[] = new String[] {"a","b","c","d"};
        for(String vs : vertices) {
	        System.out.println("K5 Directed BFS");
	        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> breadth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd);
	        GraphDefaultPath<GraphVertex,DefaultWeightedEdge> p = breadth_first.breadthFirst(new GraphVertex(vs), new GraphVertex("e"));	        
	        System.out.println(p.toString());
	        System.out.println("Anzahl der Zugriffe: " + breadth_first.getAccessCounter());
	        System.out.println("\n\r");
			assertEquals(1, p.length());	        
        }
        
        for(String vs : vertices) {
	        System.out.println("K5 Directed DFS");
	        System.out.println("K5 Undirected DFS");
	        GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge> depth_first = new GraphAlgoSearchBDFirst<GraphVertex,DefaultWeightedEdge>(gd);
	        GraphDefaultPath<GraphVertex,DefaultWeightedEdge> pd = depth_first.depthFirst(new GraphVertex(vs), new GraphVertex("e"));
	        System.out.println(pd.toString());
	        System.out.println("Anzahl der Zugriffe: " + depth_first.getAccessCounter());
	        System.out.println("\n\r");
			assertEquals(1, pd.length());
        }
        
	}	
	
}
