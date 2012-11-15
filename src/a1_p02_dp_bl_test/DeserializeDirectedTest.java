package a1_p02_dp_bl_test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Test;

import a1_p02_dp_bl.GraphDirected;

public class DeserializeDirectedTest {

	@Test
	public void testDeserialize() {
		boolean exception = false;
		GraphDirected g = new GraphDirected(DefaultWeightedEdge.class);
		assertTrue(g != null);
		
		StringReader sr;
		
		sr = new StringReader("#gerichtet\na,b\na,c\nb,d\nd,b\n");
		try {
			g.deserialize(sr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		sr.close();
		System.out.println(g.toString());
		assertTrue(g.toString().equals("([a, b, c, d], [(a,b), (a,c), (b,d), (d,b)])"));
		
		
		sr = new StringReader("#gerichtet\nb\na,c\nb,d\n");
		try {
			g.deserialize(sr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exception = true;
		} 
		sr.close();
		System.out.println(g.toString());
		assertTrue(exception);
		assertTrue(g.toString().equals("([], [])"));
		
		
	}

}
