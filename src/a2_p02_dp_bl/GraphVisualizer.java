package a2_p02_dp_bl;


import java.util.*;
import java.util.concurrent.Semaphore;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import javax.swing.*;

import org.jgrapht.graph.DefaultWeightedEdge;


import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import a1_p02_dp_bl.*;

public class GraphVisualizer extends JFrame {

	class AlgorithmListEntry {
		VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge> m_algo;
		int m_subIndex;
		
		public AlgorithmListEntry(VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge> algo, int subIndex) {
			m_algo = algo;
			m_subIndex = subIndex;
		}
		public String toString() {
			return m_algo.getTitle(m_subIndex);
		}
		public VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge> getAlgorithm() {
			return m_algo;
		}
		public int getSubIndex() {
			return m_subIndex;
		}
		
	}
	
	class GraphResizer implements ComponentListener {

		@Override
		public void componentHidden(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentResized(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			Dimension dim = GraphVisualizer.this.getSize();
			System.out.println("RESIZE: " + dim.toString() );
			if (m_graphComponent != null) {
				m_graphComponent.getGraphControl().updatePreferredSize();
				// resize graph component
				//m_graphComponent.setLocation(0, 0);
				//m_graphComponent.setSize(dim);
				//m_graphComponent.invalidate();
				//m_vgraph.setMinimumGraphSize(new mxRectangle(0,0,dim.getWidth(), dim.getHeight()));
				//m_graphComponent.setSize(GraphicVisualizer.this.getSize());
				//m_graphComponent.validate();
			}
			
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			if (m_graphComponent != null) {
				// resize graph component
				m_graphComponent.invalidate();
				//m_graphComponent.setSize(GraphicVisualizer.this.getSize());
				m_graphComponent.validate();
			}
			
		}
		
	}	
	
	private static final long serialVersionUID = -5426350969837103570L;

	public final static String g_accessLabel = "Anzahl Zugriffe: ";
	public final static String g_phaseLabel = "Phase: ";
	public final static String g_stepLabel = "Aktion: ";
	
	java.util.ArrayList<VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge>> m_algorithmList = new java.util.ArrayList();
	VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge> m_currentAlgorithm = null;
	int m_currentSubAlgorithm = -1;
	
	GraphAlgoState<GraphVertex, DefaultWeightedEdge> m_algoState = null;
	GraphAlgoStepListener<GraphVertex, DefaultWeightedEdge> m_stepListener = null;
	Set<GraphVertex> m_dirtyVertices = new HashSet<GraphVertex>();
	Set<DefaultWeightedEdge> m_dirtyEdges = new HashSet<DefaultWeightedEdge>();
	
	mxGraph m_vgraph;
	mxGraphComponent m_graphComponent;
	Map<GraphVertex, mxCell> m_vertexToVisual = new HashMap<GraphVertex, mxCell>();
	Map<DefaultWeightedEdge, mxCell> m_edgeToVisual = new HashMap<DefaultWeightedEdge, mxCell>();
	
	
	
	JPanel m_controlPane;
	JComboBox m_algoSelection;
	JButton m_stepButton;
	JLabel m_accessCounterValue;
	JLabel m_phaseName;
	JLabel m_stepName;
	JButton m_savePositionsButton;

	
	// semaphore used for halting algorithm thread until step button is pressed
	private final Semaphore m_singleStepLock = new Semaphore(1, true);
	private Thread m_algorithmThread = null;


	

	
	public GraphVisualizer(java.util.List<VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge>> algorithms)
	{
		super("Graphic Visualizer");
		//this.addComponentListener(new GraphResizer());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m_algorithmList.addAll(algorithms);
		
		m_currentAlgorithm = null;
		m_currentSubAlgorithm = -1;
		
		//m_controlPane = new JPanel(new GridBagLayout());
		//GridBagConstraints c = new GridBagConstraints();
		
		m_controlPane = new JPanel();
		m_controlPane.setPreferredSize(new Dimension(250,800));
		m_controlPane.setLayout(new BoxLayout(m_controlPane, BoxLayout.Y_AXIS));
		
		//Create a split pane with the two scroll panes in it.
		//JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this, m_controlPane);
		//splitPane.setOneTouchExpandable(true);
		//splitPane.setDividerLocation(1024);
		//splitPane.validate();

		
		//m_controlFrame = new JFrame();
	
		this.getContentPane().add(m_controlPane, BorderLayout.EAST);
		
		//parent.add(splitPane);

		//For each component to be added to this container:
		//...Create the component...
		//...Set instance variables in the GridBagConstraints instance...
		//pane.add(theComponent, c);
		
		//graphFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//graphFrame.setSize(1024, 768);
		//graphFrame.setVisible(true);

		//setBackground(new Color(255,0,0));
		
		
		

		//Provide minimum sizes for the two components in the split pane
		//Dimension minimumSize = new Dimension(100, 50);
		//m_controlFrame.setMinimumSize(minimumSize);
		
		
		//visualizer.visualizeGraph3();
		//visualizer.setMinimumSize(minimumSize);

		ArrayList<AlgorithmListEntry> algoListEntries = new ArrayList<AlgorithmListEntry>();
		for(VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge> algo : m_algorithmList) {
			for(int i=0; i<algo.getNumSubs(); i++) {
				algoListEntries.add(new AlgorithmListEntry(algo, i));
				
			}
		}
		
		m_algoSelection = new JComboBox(algoListEntries.toArray());
		m_algoSelection.setMaximumSize(new Dimension(800, 30));
		m_algoSelection.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		
		m_stepButton = new JButton("Next Step");
		//m_stepButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		m_stepButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		
		m_accessCounterValue = new JLabel(g_accessLabel + "0");
		m_accessCounterValue.setAlignmentX(Component.LEFT_ALIGNMENT);
		m_phaseName = new JLabel(g_phaseLabel);
		m_phaseName.setAlignmentX(Component.LEFT_ALIGNMENT);
		m_stepName = new JLabel(g_stepLabel);
		m_stepName.setAlignmentX(Component.LEFT_ALIGNMENT);

		m_savePositionsButton = new JButton("Save Vertex Positions");
		m_savePositionsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		
		
		// create control pane
		m_controlPane.add(new JLabel("Alorithmus:"));
		m_controlPane.add(m_algoSelection);
		m_controlPane.add(Box.createVerticalStrut(20));		
		m_controlPane.add(m_phaseName);
		m_controlPane.add(m_stepName);
		m_controlPane.add(Box.createVerticalStrut(20));
		m_controlPane.add(m_accessCounterValue);
		m_controlPane.add(Box.createVerticalStrut(40));		
		m_controlPane.add(m_stepButton);
		m_controlPane.add(Box.createVerticalStrut(80));		
		m_controlPane.add(m_savePositionsButton);
		
		
		this.addComponentListener(new GraphResizer());
		
		
		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					// new algo selected
					AlgorithmListEntry entry = ((AlgorithmListEntry)itemEvent.getItem());
					m_currentAlgorithm = entry.getAlgorithm();
					m_currentSubAlgorithm = entry.getSubIndex();
					initializeAlgorithm();
					startAlgorithm();
				} else {
					// remove selection (cleanup and stop execution ?)
					AlgorithmListEntry entry = ((AlgorithmListEntry)itemEvent.getItem());
					if (m_currentAlgorithm != null) {
						stopAlgorithm();
						cleanupAlgorithm();
						m_currentAlgorithm = null;
						m_currentSubAlgorithm = 0;
					}
				}
			}
		};
	    m_algoSelection.addItemListener(itemListener);
	    m_algoSelection.setSelectedIndex(-1);
	    
		m_stepButton.addActionListener(new ActionListener() {
		   	 
	        public void actionPerformed(ActionEvent e)
	        {
	        	if (m_algorithmThread != null) {
	                // release the sempahore to let the algorithm thread continue execution
	        		m_singleStepLock.release();
	        	}
	        }
	    });
	    

		m_savePositionsButton.addActionListener(new ActionListener() {
		   	 
	        public void actionPerformed(ActionEvent e)
	        {
	        	if (m_vgraph != null && m_currentAlgorithm != null && m_currentAlgorithm.getProperty(m_currentSubAlgorithm, VisualizedGraphAlgo.propertyPositionsFileName) != null) {
	        		String filename = (String)m_currentAlgorithm.getProperty(m_currentSubAlgorithm, VisualizedGraphAlgo.propertyPositionsFileName);
		        	saveGraphPositions(filename);
	        	}	        	
	        }
	    });
		
		
	    	
	    	
		
		
		this.setVisible(true);
		
	}
	
	

	
	
	
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1024,768);
	}
	

	
	protected void saveGraphPositions(String filename) {		
		FileWriter fw = null;
		try {

			fw = new FileWriter(filename);
			BufferedWriter writer = new BufferedWriter(fw);
			
			for (GraphVertex v : m_vertexToVisual.keySet()) {
				mxCell graphCell = m_vertexToVisual.get(v);
				mxCellState cellState = m_vgraph.getView().getState(graphCell);
				mxPoint position = cellState.getOrigin();
				String posString = String.format("%s;%s;%s", v.getName(), new Double(position.getX()).toString(), new Double(position.getY()).toString());
				writer.write(posString);
				writer.newLine();
				//System.out.println(posString);
			}
			writer.flush();
		}
		catch(IOException ex) {
			System.out.println(ex);			
		}
		finally {
			try {
				if (fw != null)
					fw.close();
			}
			catch(IOException ex) {
				System.out.println(ex);			
			}
		}
	}
	
	protected Map<String, mxPoint> loadGraphPositions(String filename) {
		Map<String, mxPoint> map = new HashMap<String, mxPoint>();
		FileReader fr = null;
		try {

			fr = new FileReader(filename);
			LineNumberReader lineReader =  new LineNumberReader(fr);
			lineReader.setLineNumber(0);
			
			String line = lineReader.readLine();
			while(line != null) {
				line = line.trim();
				// skip empty lines
				if (!line.isEmpty()) {
					String[] elements = line.split(";");
					String vname = elements[0].trim();
					double x = Double.parseDouble(elements[1]);
					double y = Double.parseDouble(elements[2]);
					map.put(vname, new mxPoint(x,y));
					
					//mxCell graphCell = m_vertexToVisual.get(new GraphVertex(vname));
					//mxCellState cellState = m_vgraph.getView().getState(graphCell);
					//cellState.setX(x);
					//cellState.setY(y);					
				}
				line = lineReader.readLine();
			}
			return map;
		}
		catch(IOException ex) {
			System.out.println(ex);
		}
		catch(Exception ex) {
			System.out.println(ex);
		}
		finally {
			try {
				if (fr != null)
					fr.close();
			}
			catch(IOException ex) {
				System.out.println(ex);
			}
		}
		return null;
	}
	
	
	
	
	
	public void createVisualGraph() {
		

		m_vgraph = new mxGraph();
		m_vgraph.setCellsDisconnectable(false);
		m_vgraph.setCellsDeletable(false);
		m_vgraph.setCellsEditable(false);
		m_vgraph.setCellsLocked(false);
		m_vgraph.setCellsMovable(true);
		m_vgraph.setCellsDisconnectable(false);
		m_vgraph.setCellsResizable(false);
		m_vgraph.setCellsSelectable(true);
		m_vgraph.setConnectableEdges(false);
		//vgraph.setConnectableEdges(false);
		//mxGraphComponent.setConnectable(false);
		
		/*
		setCellsBendable
	 	setCellsCloneable
	 	setCloneInvalidEdges
	 	setDropEnabled
	 	setEdgeLabelsMovable	 	
	 	setLabelsVisible
	 	*/
		m_vgraph.setResetEdgesOnMove(true);
	 	//setAllowDanglingEdges
	 	
	 	//setEnabled(false);
		
		m_vgraph.setDisconnectOnMove(false);
		m_vgraph.setConnectableEdges(false);
		
		
	}
		
	public void populateVisualGraph(GraphAlgoState<GraphVertex, DefaultWeightedEdge> algoState, String positionFilename) {
		Map<String, mxPoint> positionMap = null;
		GraphCommon<GraphVertex, DefaultWeightedEdge> graph = algoState.getGraph();		
		Object parent = m_vgraph.getDefaultParent();

		
		// load graph vertex positions (if any)
		if (positionFilename != null) {
			positionMap = loadGraphPositions(positionFilename);
		}
				
		m_vgraph.getModel().beginUpdate();
		try
		{
			
			Set<GraphVertex> vertices = graph.vertexSet();
			for(GraphVertex v : vertices) {
				double xPos = 20.0;
				double yPos = 20.0;
				if (positionMap != null && positionMap.containsKey(v.getName())) {
					mxPoint vpos = positionMap.get(v.getName());
					xPos = vpos.getX();
					yPos = vpos.getY();
				} else if (v.getX() != Double.NaN && v.getY() != Double.NaN) {
					xPos = v.getX();
					yPos = v.getY();
				}
				VertexStyle style = m_currentAlgorithm.getVertexStyle(v);
				mxCell gv = (mxCell)m_vgraph.insertVertex(parent, null, style.getLabel(), xPos, yPos, style.getWidth(), style.getWidth(), style.getStyle());
				gv.setConnectable(false);
				m_vertexToVisual.put(v, gv);
				
				
				//m_vgraph.moveCells(gv, 0.1, 1.0)
			}
			Set<DefaultWeightedEdge> edges = graph.edgeSet();
			for(DefaultWeightedEdge e : edges) {
				GraphVertex source = graph.getEdgeSource(e);
				mxCell gsource = m_vertexToVisual.get(source);
				GraphVertex target = graph.getEdgeTarget(e);
				mxCell gtarget = m_vertexToVisual.get(target);
				//String edgeName = null;
				//if (graph.hasEdgeWeights()) {
					// use edge weight as name for the edge
					//double weight = graph.getEdgeWeight(e);
					//edgeName = String.format("%6.0f", weight);
				//} else {					
				//}
				String label = m_currentAlgorithm.getEdgeLabel(e);
				String style = m_currentAlgorithm.getEdgeStyle(e);
				mxCell ge = (mxCell)m_vgraph.insertEdge(parent, null, label, gsource, gtarget, style);
				m_edgeToVisual.put(e, ge);
			}
			

		}
		finally
		{
			m_vgraph.getModel().endUpdate();
		}
	}
	
	protected void mouseWheelZoom(MouseWheelEvent e) 
	{
		if (e.getWheelRotation() < 0) {
			m_graphComponent.zoomIn();
		} else {
			m_graphComponent.zoomOut();
		}
		
		System.out.println(mxResources.get("scale") + ": " + (int) (100 * m_graphComponent.getGraph().getView().getScale()) + "%");
	}


	
	
	public void initializeAlgorithm() {
		m_algoState = m_currentAlgorithm.init(m_currentSubAlgorithm);
		
		createVisualGraph();
		
		// Filename der gespeicherten Positionen
		String positionFilename = (String)m_currentAlgorithm.getProperty(m_currentSubAlgorithm, VisualizedGraphAlgo.propertyPositionsFileName);

		
		m_graphComponent = new mxGraphComponent(m_vgraph);
		m_graphComponent.setPanning(true);
		m_graphComponent.setZoomFactor(1.1);
		MouseWheelListener wheelTracker = new MouseWheelListener() {
	    	public void mouseWheelMoved(MouseWheelEvent e) {
	    		if (e.isControlDown()) {	    			
	    			GraphVisualizer.this.mouseWheelZoom(e);
	    		}
	    		}
	    };
	    m_graphComponent.addMouseWheelListener(wheelTracker);
	    
		
		add(m_graphComponent, BorderLayout.CENTER);
		m_currentAlgorithm.createStyles(m_currentSubAlgorithm, m_vgraph, m_graphComponent);
		
		// Erzeuge grafische representation des Graphen
		populateVisualGraph(m_algoState, positionFilename);
		

		m_stepListener = new GraphAlgoStepListener<GraphVertex, DefaultWeightedEdge>() {

			@Override
			public void vertexChanged(GraphVertex vertex) {
				System.out.println("vertexChange " + vertex.getName());
				m_dirtyVertices.add(vertex);
			}
			
			@Override
			public void edgeChanged(DefaultWeightedEdge edge) {
				System.out.println("edgeChange");
				m_dirtyEdges.add(edge);
			}
			
			@Override
			public void algoSingleStep(String phaseName, String stepName) {
				System.out.println("step " + phaseName + " - " + stepName);
				try {
					
					m_accessCounterValue.setText(g_accessLabel + Integer.toString(m_algoState.getAccessCounter()));
					m_phaseName.setText(g_phaseLabel + phaseName);
					m_stepName.setText(g_stepLabel + ((stepName==null) ? " " : stepName));
	
					m_vgraph.getModel().beginUpdate();
					try
					{
	
						// ge�nderte Knoten aktualisieren
						for(GraphVertex v : m_dirtyVertices) {
							mxCell cell = m_vertexToVisual.get(v);
							VertexStyle style = m_currentAlgorithm.getVertexStyle(v);
							cell.setStyle(style.getStyle());
							cell.setValue(style.getLabel());
							// TODO: resize cell if necessary
							m_vgraph.getView().invalidate(cell);
						}
						m_dirtyVertices.clear();
						
						for(DefaultWeightedEdge e : m_dirtyEdges) {
							mxCell cell = m_edgeToVisual.get(e);
							String label = m_currentAlgorithm.getEdgeLabel(e);
							String style = m_currentAlgorithm.getEdgeStyle(e);
							cell.setStyle(style);
							cell.setValue(label);
							m_vgraph.getView().invalidate(cell);
						}
						m_dirtyEdges.clear();						
					}
					finally
					{
						m_vgraph.getModel().endUpdate();
					}
					m_vgraph.repaint();
					m_graphComponent.invalidate();
					m_graphComponent.refresh();
					
					// only stop for real step, not for a phase change
					if (stepName != null) {
						// try to acquire thread
						m_singleStepLock.acquire();
						// next step
					}
					
					
				} catch (InterruptedException e) {
					System.out.println("algo thread interrupted");
					e.printStackTrace();
				}
				// 
			}
		};

		
		m_vgraph.repaint();
		m_graphComponent.invalidate();
		m_graphComponent.refresh();
		
		this.validate();
		//this.invalidate();
				
    }

	public void startAlgorithm() {
		m_algoState.addGraphAlgoStateListener(m_stepListener);
		m_algorithmThread = new Thread(new Runnable() {
            public void run() {
        		try {
        			// acquire semaphore before starting algorithm, so it stops on the next step
        			m_singleStepLock.acquire();
                	m_currentAlgorithm.start();
        		} catch (InterruptedException e) {
        			System.out.println("algo thread interrupted");
        			e.printStackTrace();
        		}
            	m_algorithmThread = null;
            }
        });
		m_algorithmThread.start();
	}


	public void stopAlgorithm() {
		m_algoState.removeGraphAlgoStateListener(m_stepListener);
		if (m_algorithmThread != null) {
			m_algorithmThread.interrupt();
			m_algorithmThread = null;
			// TODO: locking + wait for exit
		}
	}
	
	public void cleanupAlgorithm() {
		
		m_currentAlgorithm.removeControls(m_currentSubAlgorithm, m_controlPane, m_graphComponent);		
		remove(m_graphComponent);
		m_currentAlgorithm.cleanup();
		m_graphComponent = null;
		m_vgraph = null;
		
		m_dirtyVertices.clear();
		m_dirtyEdges.clear();
		m_vertexToVisual.clear();
		m_edgeToVisual.clear();
		
		m_singleStepLock.drainPermits();
		m_singleStepLock.release();		
	}
	
	
	public static void main(String[] args)
	{
		//JFrame mainFrame = new JFrame("Graphic Visualizer");
		//mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// liste der ausw�hlbaren Algorithmen
		ArrayList<VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge>> m_algorithms = new ArrayList<VisualizedGraphAlgo<GraphVertex, DefaultWeightedEdge>>();
		
		// list of algorithms
		m_algorithms.add(new VisualAstar());
		m_algorithms.add(new VisualBDFirst());
        m_algorithms.add(new VisualDijkstra());
		// HIER WEITERE ALGORITHMEN EINF�GEN:
		
		
		GraphVisualizer visualizer = new GraphVisualizer(m_algorithms);
		visualizer.setSize(1280, 1024);

		
		//mainFrame.pack();
		//mainFrame.invalidate();
		//mainFrame.setVisible(true);
		//mainFrame.setSize(1280, 1024);
		
	}

}


/*
class GraphResizer implements ComponentListener {

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		if (m_graphComponent != null) {
			// resize graph component
			m_graphComponent.invalidate();
			Dimension dim = GraphicVisualizer.this.getSize();
			//m_vgraph.setMinimumGraphSize(new mxRectangle(0,0,dim.getWidth(), dim.getHeight()));
			//m_graphComponent.setSize(GraphicVisualizer.this.getSize());
			m_graphComponent.validate();
		}
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		if (m_graphComponent != null) {
			// resize graph component
			m_graphComponent.invalidate();
			//m_graphComponent.setSize(GraphicVisualizer.this.getSize());
			m_graphComponent.validate();
		}
		
	}
	
}

*/

/*
package jg2;\n\nimport javax.swing.JFrame;\nimport com.mxgraph.swing.mxGraphComponent;\nimport com.mxgraph.util.mxRectangle;\nimport com.mxgraph.util.mxResources;\nimport com.mxgraph.view.mxGraph;\nimport java.awt.Color;\nimport java.awt.event.MouseWheelEvent;\nimport java.awt.event.MouseWheelListener;\n\npublic class Main extends JFrame\n{\n    private static final long serialVersionUID = -2707712944901661771L;\n    private mxGraph graph;\n    private mxGraphComponent graphComponent;\n\n    
protected void mouseWheelZoom(MouseWheelEvent e) 
{
	if (e.getWheelRotation() < 0) {
		graphComponent.zoomIn();
		} else {
			graphComponent.zoomOut();
			}
	
	System.out.println(mxResources.get("scale") + ": "
	+ (int) (100 * graphComponent.getGraph().getView().getScale())
	+ "%");
}
    public Main() {\n        super("Hello, World!");\n        System.out.println("hej1");\n\n        graph = new mxGraph();\n        graph.setMinimumGraphSize(new mxRectangle(0, 0, 800, 800));\n        graph.setGridEnabled(true);\n        graph.setGridSize(10);\n\n        graph.getModel().beginUpdate();\n        try {\n            Object parent = graph.getDefaultParent();\n            Object v1 = graph.insertVertex(parent, null, "Hello\nagain", 20, 20, 80, 30, "strokeColor=red;fillColor=red");\n            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30);\n            graph.insertEdge(parent, null, "Edge", v1, v2);\n        } finally {\n            graph.getModel().endUpdate();\n        }\n\n        graphComponent = new mxGraphComponent(graph);\n        graphComponent.setPanning(true);\n        graphComponent.setBackground(Color.red);\n//      graphComponent.getViewport().setBackground(Color.green);\n
    
    graphComponent.setGridVisible(true);\n
    graphComponent.zoomOut();\n\n
    MouseWheelListener wheelTracker = new MouseWheelListener() {
    	public void mouseWheelMoved(MouseWheelEvent e) {
    		if (e.isControlDown()) {
    			
    			Main.this.mouseWheelZoom(e);
    			}
    		}
    	};
    	graphComponent.addMouseWheelListener(wheelTracker);
    	
    	
    	this.getContentPane().add(graphComponent);
    }
    
    public static void main(String[] args) {\n        Main frame = new Main();\n        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n        frame.setSize(400, 320);\n        frame.setVisible(true);\n    }\n}\n
}
}
*/