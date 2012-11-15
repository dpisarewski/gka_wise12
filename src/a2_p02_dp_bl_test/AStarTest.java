package a2_p02_dp_bl_test;

import static org.junit.Assert.*;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Before;
import org.junit.Test;

import a1_p02_dp_bl.GraphDefaultPath;
import a1_p02_dp_bl.GraphUndirected;
import a1_p02_dp_bl.GraphVertex;
import a2_p02_dp_bl.GraphAlgoAstar;

public class AStarTest {
	GraphUndirected gu;

	@Before
	public void setUp(){
		gu = new GraphUndirected(DefaultWeightedEdge.class);
	}
	
	
	@Test
	public void testGraph3a(){
		gu.load("graph3.gka");
		System.out.println(gu.toString());
		GraphVertex startVertex = new GraphVertex("Husum");
		GraphVertex endVertex = new GraphVertex("Hamburg");
		
        System.out.println("Graph1 Directed BFS");
        GraphAlgoAstar astar = new GraphAlgoAstar(gu);
        GraphDefaultPath<GraphVertex, DefaultWeightedEdge> p = astar.astar(startVertex, endVertex);
        System.out.println(p.toString());
        System.out.println("Pfadl�nge gewichtet :" + p.getWeight());
        System.out.println("Anzahl der Zugriffe: " + astar.getAccessCounter());
        System.out.println("\n\r");
        
        assertEquals(518.0, p.getWeight(), 0.001);
		assertEquals(6, p.length());
		assertEquals(p.getStartVertex(), startVertex);
		assertEquals(p.getEndVertex(), endVertex);        
	}	

	
	@Test
	public void testGraph3b(){
		gu.load("graph3.gka");
		System.out.println(gu.toString());
		GraphVertex startVertex = new GraphVertex("Minden");
		GraphVertex endVertex = new GraphVertex("Hamburg");
		
        System.out.println("Graph1 Directed BFS");
        GraphAlgoAstar astar = new GraphAlgoAstar(gu);
        GraphDefaultPath<GraphVertex, DefaultWeightedEdge> p = astar.astar(startVertex, endVertex);
        System.out.println(p.toString());
        System.out.println("Pfadl�nge gewichtet :" + p.getWeight());
        System.out.println("Anzahl der Zugriffe: " + astar.getAccessCounter());
        System.out.println("\n\r");
        
        assertEquals(227.0, p.getWeight(), 0.001);
		assertEquals(2, p.length());
		assertEquals(p.getStartVertex(), startVertex);
		assertEquals(p.getEndVertex(), endVertex);        
	}	
	
	@Test
	public void testGraph3c(){
		gu.load("graph3.gka");
		System.out.println(gu.toString());
		GraphVertex startVertex = new GraphVertex("M�nster");
		GraphVertex endVertex = new GraphVertex("Hamburg");
		
        System.out.println("Graph1 Directed BFS");
        GraphAlgoAstar astar = new GraphAlgoAstar(gu);
        GraphDefaultPath<GraphVertex, DefaultWeightedEdge> p = astar.astar(startVertex, endVertex);
        System.out.println(p.toString());
        System.out.println("Pfadl�nge gewichtet :" + p.getWeight());
        System.out.println("Anzahl der Zugriffe: " + astar.getAccessCounter());
        System.out.println("\n\r");
        
        assertEquals(300.0, p.getWeight(), 0.001);
		assertEquals(2, p.length());
		assertEquals(p.getStartVertex(), startVertex);
		assertEquals(p.getEndVertex(), endVertex);        
	}	


}
	