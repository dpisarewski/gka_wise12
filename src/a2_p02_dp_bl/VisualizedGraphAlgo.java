package a2_p02_dp_bl;

import javax.swing.JComponent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public interface VisualizedGraphAlgo<V,E> {
	public final static String propertyPositionsFileName = "PosFileName";
	
	
	// Anzahl der Unterpunkte / Testruns die durch diese Klasse implementiert werden
	public int getNumSubs();	
	// Das ist der Name der im Auswahlmenu angezeigt wird
	public String getTitle(int subIndex);	
	// Verschiedene Properties
	// GraphPositionsFile: Dateiname der Datei in der die String gespeichert werden
	public Object getProperty(int subIndex, String propertyName);

		
	// Schritt 1: Initialisierung: graph laden, state initialisieren
	public GraphAlgoState<V,E> init(int subIndex);

	// Schritt 2: styles erzeugen die f�r die visualisierung ben�tigt werden
	public void createStyles(int subIndex, mxGraph visualGraph, mxGraphComponent graphComponent);
	
	// Schritt 3: add custom swing controls to parent
	public void addControls(int subIndex, JComponent controlParent, mxGraphComponent graphComponent);
	
	// Schritt 4: den eigentlichen Algorithmus starten
	public void start();
	
	// Schritt 5: remove any custom swing controls from parent
	public void removeControls(int subIndex, JComponent controlParent, mxGraphComponent graphComponent);
	
	// Schritt 6: den rest aufr�umen, so dass der Algorithmus ein zweites mal durchlaufen kann
	public void cleanup();
	
	
	// Die Beschriftung f�r den entsprechenden Knoten
	//public String getVertexLabel(V vertex);
	//public String getVertexStyle(V vertex);
	public VertexStyle getVertexStyle(V vertex);

	// Kantenbeschriftung + mxGraph Style
	public String getEdgeLabel(E edge);
	public String getEdgeStyle(E edge);

}
