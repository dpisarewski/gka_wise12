package a01_p02_bl_dp_test;

import static org.junit.Assert.*;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.*;

import a01_p02_bl_dp.*;

public class SearchTest {
	GraphDirected 	gd;
	GraphUndirected gu;
	
	@Before
	public void setUp(){
		gd = new GraphDirected(DefaultWeightedEdge.class);
		gu = new GraphUndirected(DefaultWeightedEdge.class);
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
		//assertEquals(2, ((GraphDefaultPath) gd.find(v1, v3, "BFS")).length());
		assertEquals(2, ((GraphDefaultPath) gu.find(v1, v3, "BFS")).length());
		assertEquals(2, ((GraphDefaultPath) gd.find(v1, v3, "DFS")).length());
		assertEquals(2, ((GraphDefaultPath) gu.find(v1, v3, "DFS")).length());
	}

}
