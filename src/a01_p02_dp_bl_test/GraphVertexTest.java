package a01_p02_dp_bl_test;

import static org.junit.Assert.*;

import org.junit.Test;

import a01_p02_dp_bl.GraphVertex;

public class GraphVertexTest {

	@Test
	public void testHashCode() {
		new GraphVertex<String>("v1", "attribute1");
		new GraphVertex<String>("v1", "attribute1");
	}

	@Test
	public void testCompareTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
