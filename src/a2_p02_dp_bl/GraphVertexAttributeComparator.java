package a2_p02_dp_bl;

import java.util.Comparator;

import a1_p02_dp_bl.*;


public class GraphVertexAttributeComparator implements Comparator<GraphVertex>  { 
	  @Override public int compare(GraphVertex v1, GraphVertex v2) {
		  return v1.getNumericAttribute() - v2.getNumericAttribute();
	  }
} 