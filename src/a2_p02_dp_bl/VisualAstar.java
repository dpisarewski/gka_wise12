package a2_p02_dp_bl;

import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.jgrapht.graph.DefaultWeightedEdge;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import a1_p02_dp_bl.GraphUndirected;
import a1_p02_dp_bl.GraphVertex;

public class VisualAstar implements VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge> {

	GraphAlgoState<GraphVertex, DefaultWeightedEdge> m_graphState;
	GraphAlgoAstar m_algorithm;
	ImageIcon m_mapIcon;
	int m_selectedSubIndex = 0;
	

	@Override
	public int getNumSubs() {
		return 5;
	}

	@Override
	public String getTitle(int subIndex) {
		String title = "";
		switch(subIndex) {
		case 0:
			title = "A* Graph3 : Husum -> Hamburg";
			break;
		case 1:
			title = "A* Graph3 : Minden  -> Hamburg";
			break;
		case 2:
			title = "A* Graph3 : M�nster -> Hamburg";
			break;
		case 3:
			title = "A* Graph3 : Zuf�lliger Graph mit 20 Knoten";
			break;
		case 4:
			title = "A* Graph3 : Zuf�lliger Graph mit 30 Knoten";
			break;
		}
		return title;
	}

	@Override
	public Object getProperty(int subIndex, String propertyName) {
		if (propertyName == propertyPositionsFileName) {
			if (subIndex < 3)
				return "astar_graph3.gpos";
		}
		return null;
	}
	
	

	@Override
	public GraphAlgoState<GraphVertex, DefaultWeightedEdge> init(int subIndex) {
		m_selectedSubIndex = subIndex;
		GraphUndirected graph = null;
		switch(subIndex) {
		case 0:
		case 1:
		case 2:
		// load the graph from disk
			graph = new GraphUndirected(DefaultWeightedEdge.class);
			graph.load("graph3.gka");
			break;
		case 3:
			graph = (GraphUndirected)CreateHeuristicGraph.create(20, 900, 900);
			break;
		case 4:
			graph = (GraphUndirected)CreateHeuristicGraph.create(30, 900, 900);
			break;
		}


		// create the algorithm state object which stores all the vertex and edge properties necessary for the algorithm (e.g. closed list, open list, calculated distances, predecessor etc)
		m_graphState = new GraphAlgoStateDefault<GraphVertex, DefaultWeightedEdge>(graph);
		// instantiate the algorithm object (sets graph and state object)
		m_algorithm = new GraphAlgoAstar(graph, m_graphState);
        
        // create graphical representation of the graph
        //createVisualGraph(m_visualState, "astar_graph3.gpos");
		switch(subIndex) {
		case 0:
		case 1:
		case 2:
			m_mapIcon = new ImageIcon("KarteNorddeutschland.png");
			break;
		default:
			m_mapIcon = null;
			break;
		}
		//visualGraph.setMinimumGraphSize(new mxRectangle(0,0,m_mapIcon.getIconWidth(), m_mapIcon.getIconHeight()));
		
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
			if (m_mapIcon != null)
				graphComponent.setBackgroundImage(m_mapIcon);
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
			m_algorithm.astar(new GraphVertex("Husum"), new GraphVertex("Hamburg"));
			break;
		case 1:
			m_algorithm.astar(new GraphVertex("Minden"), new GraphVertex("Hamburg"));
			break;
		case 2:
			m_algorithm.astar(new GraphVertex("M�nster"), new GraphVertex("Hamburg"));
			break;
		default:
			m_algorithm.astar(new GraphVertex("s"), new GraphVertex("z"));
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
		Integer g = m_graphState.getVertexProperty(vertex, GraphAlgoAstar.distancePropertyName);		
		Boolean c = m_graphState.getVertexProperty(vertex, GraphAlgoAstar.closedPropertyName);
		Boolean o = m_graphState.getVertexProperty(vertex, GraphAlgoAstar.openPropertyName);
		Boolean p = m_graphState.getVertexProperty(vertex, GraphAlgoAstar.pathPropertyName);
		String style = "ASV";

		
		String label = vertex.getName() + "\nh:" + vertex.getNumericAttribute() + "\n";
		if (g!=null) {
			// wenn g gesetzt ist, dann als zweite Zeile anzeigen
			label = label + "g:" + g.toString();
		}
		
		
		if (m_algorithm.getCurrentVertex() == vertex) {
			style += ";strokeWidth=3;strokeColor=#FF0000";
			if (p != null)
				style += ";shape=ellipse";
		} else if (p != null) {
			style += ";shape=ellipse;strokeWidth=3;strokeColor=#0000FF";
		}
		

		if (c!=null) {
			style += ";fillColor=#CC0000;gradientColor=#DDDDDD";
		} else if (o != null) {
			style += ";fillColor=#00CC00;gradientColor=#DDDDDD";
		}
		
		return new VertexStyle(label, style, 80, 80);
	}
	

	@Override
	public String getEdgeLabel(DefaultWeightedEdge edge) {
		String label = Integer.toString((int)m_graphState.getGraph().getEdgeWeight(edge));
		return label;
	}

	@Override
	public String getEdgeStyle(DefaultWeightedEdge edge) {
		String style = "ASE";
		GraphVertex vsource = m_graphState.getGraph().getEdgeSource(edge);
		GraphVertex vtarget = m_graphState.getGraph().getEdgeTarget(edge);
		GraphVertex vsourcePredec = m_graphState.getVertexProperty(vsource, GraphAlgoAstar.predecessorPropertyName);
		GraphVertex vtargetPredec = m_graphState.getVertexProperty(vtarget, GraphAlgoAstar.predecessorPropertyName);
		
		Boolean srcPath = m_graphState.getVertexProperty(vsource, GraphAlgoAstar.pathPropertyName);
		Boolean dstPath = m_graphState.getVertexProperty(vtarget, GraphAlgoAstar.pathPropertyName);
		
		if (srcPath != null && dstPath != null) {
			style += ";strokeColor=#0000FF;strokeWidth=3;fontColor=#0000FF;fontSize=24";
		}
		
		if (vtarget.equals(vsourcePredec)) {
			style += ";endArrow=classic";
		}
		if (vsource.equals(vtargetPredec)) {
			style += ";startArrow=classic";
		}
		
		return style;
	}




}
