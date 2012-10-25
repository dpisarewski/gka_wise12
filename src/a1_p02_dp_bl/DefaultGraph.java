package a1_p02_dp_bl;

import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;

public class DefaultGraph<Attribute> extends AbstractBaseGraph<GraphVertex<Attribute>,GraphEdge> {

	private static final long serialVersionUID = 6379277439323618776L;

	public DefaultGraph(boolean directed, boolean weighted) {
		super(new ClassBasedEdgeFactory<GraphVertex<Attribute>, GraphEdge>(GraphEdge.class), false, false);
	}

	

}
