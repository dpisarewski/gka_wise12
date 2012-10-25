package a1_p02_dp_bl;

import org.jgrapht.graph.DefaultWeightedEdge;

public class GraphEdge extends DefaultWeightedEdge{
	
	String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private static final long serialVersionUID = -8730113215552552200L;

	public String toString(){
		return getSource().toString() + "," + getTarget().toString();
	}
}
