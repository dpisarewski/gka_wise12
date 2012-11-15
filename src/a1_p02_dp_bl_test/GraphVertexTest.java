package a1_p02_dp_bl_test;

import static org.junit.Assert.*;

import org.junit.Test;

import a1_p02_dp_bl.GraphVertex;

public class GraphVertexTest {

	
	@Test
	public void testHashCode() {
		 GraphVertex v1a = new GraphVertex("v1", 100);
		 GraphVertex v1b = new GraphVertex("v1", -99);
		 GraphVertex v2 = new GraphVertex("v2", -666);
		 assertTrue(v1a == v1b);
		 assertTrue(v1a == v2);
	}

	@Test
	public void testEqualsObject() {
		 GraphVertex v1a = new GraphVertex("v1", 100);
		 GraphVertex v1b = new GraphVertex("v1", -99);
		 GraphVertex v2 = new GraphVertex("v2", -666);
		 assertTrue(v1a == v1b);
		 assertTrue(v1a == v2);
	}

}
