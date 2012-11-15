package a2_p02_dp_bl;

import java.util.Hashtable;

import javax.swing.JComponent;

import org.jgrapht.graph.DefaultWeightedEdge;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import a1_p02_dp_bl.GraphAlgoSearchBDFirst;
import a1_p02_dp_bl.GraphCommon;
import a1_p02_dp_bl.GraphDirected;
import a1_p02_dp_bl.GraphUndirected;
import a1_p02_dp_bl.GraphVertex;

public class VisualBDFirst implements VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge> {

	GraphAlgoState<GraphVertex, DefaultWeightedEdge> m_graphState;
	GraphAlgoSearchBDFirst<GraphVertex, DefaultWeightedEdge> m_algorithm;
	int m_selectedSubIndex = 0;
	

	@Override
	public int getNumSubs() {
		return 4;
	}

	@Override
	public String getTitle(int subIndex) {
		String title = null;
		switch(subIndex) {
		case 0:
			title = "Breadth first - Graph1: a -> f";
			break;
		case 1:
			title = "Depth first - Graph1: a -> f";
			break;
		case 2:
			title = "Breadth first - Graph2: a -> f";
			break;
		case 3:
			title = "Depth first - Graph2: a -> f";
			break;
		}
		return title;
	}

	@Override
	public Object getProperty(int subIndex, String propertyName) {		
		if (propertyName == propertyPositionsFileName) {
			String filename = null;
			switch(subIndex) {
			case 0:
			case 1:
				filename = "bdfs_graph1.gpos";
				break;
			case 2:
			case 3:
				filename = "bdfs_graph2.gpos";
				break;
			}
			return filename;			
		}
		return null;
	}
	
	@Override
	public GraphAlgoState<GraphVertex, DefaultWeightedEdge> init(int subIndex) {
		m_selectedSubIndex = subIndex;
		GraphCommon<GraphVertex, DefaultWeightedEdge> graph = null;
		// load the graph from disk
		switch(subIndex) {			
		case 0:
		case 1:
			graph = new GraphDirected(DefaultWeightedEdge.class);
			((GraphDirected)graph).load("graph1.gka");
			break;
		case 2:
		case 3:
			graph = new GraphUndirected(DefaultWeightedEdge.class);
			((GraphUndirected)graph).load("graph2.gka");
			break;
		}
			
		// create the algorithm state object which stores all the vertex and edge properties necessary for the algorithm (e.g. closed list, open list, calculated distances, predecessor etc)
		m_graphState = new GraphAlgoStateDefault<GraphVertex, DefaultWeightedEdge>(graph);
		// instantiate the algorithm object (sets graph and state object)
		m_algorithm = new GraphAlgoSearchBDFirst<GraphVertex, DefaultWeightedEdge>(m_graphState);
		
		return m_graphState;
	}
	
	@Override
	public void createStyles(int subIndex, mxGraph visualGraph, mxGraphComponent graphComponent) {
		visualGraph.getModel().beginUpdate();
		try
		{
			
			mxStylesheet stylesheet = visualGraph.getStylesheet();
			Hashtable<String, Object> style;
			
			style = new Hashtable<String, Object>();
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
			style.put(mxConstants.STYLE_ROUNDED, true);
			style.put(mxConstants.STYLE_OPACITY, 100);
			style.put(mxConstants.STYLE_FILLCOLOR, "#CCCCFF");
			style.put(mxConstants.STYLE_GRADIENTCOLOR, "#DDDDFF");
			style.put(mxConstants.STYLE_FONTSIZE, "16");
			style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
			stylesheet.putCellStyle("ASV", style);
			
			style = new Hashtable<String, Object>();
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
	 		style.put(mxConstants.STYLE_STARTARROW, mxConstants.NONE);
	 		style.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
			style.put(mxConstants.STYLE_OPACITY, 100);
			style.put(mxConstants.STYLE_STROKECOLOR, "#000000");
			style.put(mxConstants.STYLE_FILLCOLOR, "#000000");
			style.put(mxConstants.STYLE_FONTSIZE, "16");
			style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
			stylesheet.putCellStyle("ASE", style);
			
			
			//visualGraph.setCellStyle(style)
			// STYLE_FONTFAMILY, STYLE_FONTSTYLE, STYLE_STROKECOLOR, STYLE_ALIGN, STYLE_VERTICAL_ALIGN
			
			// STYLE_SHAPE, STYLE_ENDARROW, STYLE_STARTARROW
			
			
			// set background image
			//graphComponent.setBackgroundImage(m_mapIcon);
		}
		finally
		{
			visualGraph.getModel().endUpdate();
		}
		
	}


	@Override
	public void addControls(int subIndex, JComponent controlParent, mxGraphComponent graphComponent) {
	}
	
		
	@Override
	public void start() {

		switch(m_selectedSubIndex) {
		case 0:
			m_algorithm.breadthFirst(new GraphVertex("a"), new GraphVertex("f"));
			break;
		case 1:
			m_algorithm.depthFirst(new GraphVertex("a"), new GraphVertex("f"));
			break;
		case 2:
			m_algorithm.breadthFirst(new GraphVertex("a"), new GraphVertex("f"));
			break;
		case 3:
			m_algorithm.depthFirst(new GraphVertex("a"), new GraphVertex("f"));
			break;
		}
	}

	
	@Override
	public void removeControls(int subIndex, JComponent controlParent, mxGraphComponent graphComponent) {
		// remove background image
		graphComponent.setBackgroundImage(null);
	}

	
	
	@Override
	public void cleanup() {
		m_algorithm = null;
		m_graphState = null;		
	}
	
	

	@Override
	public VertexStyle getVertexStyle(GraphVertex vertex) {
		//Integer g = m_graphState.getVertexProperty(vertex, GraphAlgoAstar.distancePropertyName);		
		Integer c = m_graphState.getVertexProperty(vertex, GraphAlgoAstar.closedPropertyName);
		Boolean o = m_graphState.getVertexProperty(vertex, GraphAlgoAstar.openPropertyName);
		Boolean p = m_graphState.getVertexProperty(vertex, GraphAlgoAstar.pathPropertyName);
		String style = "ASV";

		
		String label = vertex.getName() + "\n";
		if (c!=null) {
			// wenn g gesetzt ist, dann als zweite Zeile anzeigen
			label = label + "d:" + c.toString();
		}
		
		
		if (m_algorithm.getCurrentVertex() == vertex) {
			style += ";strokeWidth=3;strokeColor=#FF0000";
			if (p != null)
				style += ";shape=ellipse";
		} else if (p != null) {
			style += ";shape=ellipse;strokeWidth=3;strokeColor=#0000FF";
		}
		

		if (o!=null) {
			style += ";fillColor=#00CC00;gradientColor=#DDDDDD";
		} else if (c != null) {
			switch(m_selectedSubIndex) {
			case 1:
			case 3:
				style += ";fillColor=#00CC00;gradientColor=#DDDDDD";
				break;
			default:
				style += ";fillColor=#CC0000;gradientColor=#DDDDDD";
				break;
			}
		}
		
		return new VertexStyle(label, style, 80, 80);
	}
	

	@Override
	public String getEdgeLabel(DefaultWeightedEdge edge) {
		return "";
	}

	@Override
	public String getEdgeStyle(DefaultWeightedEdge edge) {
		String style = "ASE";
		GraphVertex vsource = m_graphState.getGraph().getEdgeSource(edge);
		GraphVertex vtarget = m_graphState.getGraph().getEdgeTarget(edge);
		
		Boolean srcPath = m_graphState.getVertexProperty(vsource, GraphAlgoAstar.pathPropertyName);
		Boolean dstPath = m_graphState.getVertexProperty(vtarget, GraphAlgoAstar.pathPropertyName);
		
		if (srcPath != null && dstPath != null) {
			style += ";strokeColor=#0000FF;strokeWidth=3;fontColor=#0000FF;fontSize=24";
		}
		if (m_graphState.getGraph().isDirected()) {
			style += ";endArrow=classic";
		}
		
		return style;
	}


}
