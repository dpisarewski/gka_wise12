package a2_p02_dp_bl;

import java.awt.Point;

import org.jgrapht.graph.DefaultWeightedEdge;
import a1_p02_dp_bl.*;

public class CreateHeuristicGraph {


	
	public static GraphCommon<GraphVertex, DefaultWeightedEdge> create(int numKonten, int width, int height) {
		//int numKonten = 20;
		
		// kreis um einen knoten der frei bleiben soll
		double keepOutArea = 100;
		// gr��e des randbereichs der frei gehalten werden soll (center des knoten)
		double borderFree = 40;
		// Mindestszahl von Kanten pro Knoten
		int minVertexConnectivity = 1;
		// Maximale Anzahl von Kanten pro Knoten
		int maxVertexConnectivity = 8;
		// Maximale Anzahl von Kanten die von einem Knoten ausgehend erzeugt werden (ohne von anderen Knoten kommende)
		int maxEdgesToCreateAtVertex = 3;
		// Mindestens soviele Kanten zwischen Start und Ziel
		int minPathEdges = 3;
				
		double minX = borderFree;
		double areaWidth = width - 2*borderFree;
		double minY = borderFree;
		double areaHeight = height - 2*borderFree;
		
		
		
		// speichert die erzeugten Kanten
		GraphVertex[] vertices = new GraphVertex[numKonten];
		// die 2d koordinaten der kanten
		Point[] vertexCoordinates = new Point[numKonten];
		// die entfernung vom ziel in kanten
		int[] distanceFromTarget = new int[numKonten];
		// Anzahl Kanten die von diesem Knoten abgehen
		int[] edgesFromVertex = new int[numKonten];

		
		vertices[0] = new GraphVertex("z");
		for(int i=1; i<numKonten-1; i++)
			vertices[i] = new GraphVertex("v" + Integer.toString(i));
		vertices[numKonten-1] = new GraphVertex("s");
		
		
		GraphCommon<GraphVertex, DefaultWeightedEdge> graph = new GraphUndirected(DefaultWeightedEdge.class);
		
		double squaredKeepOutArea = keepOutArea*keepOutArea;
		for(int i=0; i<numKonten; i++) {
			distanceFromTarget[i] = Integer.MAX_VALUE;
			edgesFromVertex[i] = 0;
			GraphVertex v = vertices[i];
			double x = 0.0, y = 0.0;
			double zDistanceSquared = 0.0;
			boolean posAccepted = false;
			while(!posAccepted) {
				x = minX + Math.random() * areaWidth;
				y = minY + Math.random() * areaHeight;
				posAccepted = true;
				// �berpr�fe alle bisher erstellten knoten ob der mindestabstand eingehalten wurde
				for(int j=0; j<i; j++) {
					double dx = x - vertexCoordinates[j].getX();
					double dy = y - vertexCoordinates[j].getY();
					// quadrierte Distanz zwischen den Knoten
					double d = dx*dx + dy*dy;
					if (j==0)
						zDistanceSquared = d;
					if (d < squaredKeepOutArea) {
						posAccepted = false;
						break;
					}
				}
			}
			vertexCoordinates[i] = new Point((int)x,(int)y);
			// rundung mit floor um nicht zu �bersch�tzen
			v.setNumericAttribute((int)Math.floor(Math.sqrt(zDistanceSquared)));
			System.out.println("Erstelle Knoten \"" +  vertices[i].getName() + "\" an Position [" + Integer.toString((int)x) + "," + Integer.toString((int)y) + "] Entfernung " + v.getNumericAttribute());
			v.setPosition(x, y);
			graph.addVertex(v);
		}
		
		// TODO: edges erstellen:
		// Mindestens ein Weg von der Quelle zum Ziel muss m�glich sein
		// Es soll keine isolierten Teilgraphen geben
		// Es soll keine doppelten Knoten geben
		// Es soll keine Schleifen geben
		
		// Dies ist im Prinzip eine umgedrehte Tiefensuche, vom Ziel ausgehend, bis alle Kanten
		
		
		// Ziel ist 0 edges von sich selbst entfernt
		distanceFromTarget[0] = 0;
		
		// Soviele Schritte wie garantierte Mindestentfernung zwischen Quelle und Ziel
		for(int n=0; n<minPathEdges; n++) {
			System.out.println("Step " + n);
			for(int ti=0; ti<numKonten; ti++) {
				// als zielknoten nur solche nehmen die genau die gew�nschte entfernung vom ziel haben
				if (distanceFromTarget[ti] == n) {
					int numEdgesToCreate = (int)(Math.random()*(maxEdgesToCreateAtVertex+1-minVertexConnectivity) + minVertexConnectivity);
					for(int ei=0; ei<numEdgesToCreate; ei++) {
						int si = 0;
						int siTries = numKonten*2;
						do {
							si = (int)(Math.random()*numKonten);
							if (--siTries <= 0)
								break;
							// testen ob kante schon existiert, ab es eine schleife ist, oder ob 
							if ( (si==ti) || (si==(numKonten-1)) || (graph.getEdge(vertices[si], vertices[ti]) != null) )
								continue;
							// teste auch auf eine kante in gegenrichtung falls der graph ungerichtet ist
							if (!graph.isDirected() && graph.getEdge(vertices[ti], vertices[si]) != null)
								continue;
							// so lange versuchen bis ein Knoten gefunden wurde dessen Entfernung vom Zielknoten gleich oder gr��er ist
							if ((distanceFromTarget[si]!=Integer.MAX_VALUE) && ((distanceFromTarget[si] < (n-1)) || distanceFromTarget[si] > (n+1)))
								continue;
							break;
						} while (true);
						if (siTries <= 0)
							break;
						// TODO: create edge
						distanceFromTarget[si] = (distanceFromTarget[si] > n) ? n + 1 : distanceFromTarget[ti];
						System.out.println("Erstelle Kante von \"" +  vertices[si].getName() + "\" zu \"" +  vertices[ti].getName() + "\"");
						edgesFromVertex[si]++;
						edgesFromVertex[ti]++;
						DefaultWeightedEdge edge = graph.addEdge(vertices[si], vertices[ti]);
						// set weight > distance
						double dx = vertexCoordinates[si].getX() - vertexCoordinates[ti].getX();
						double dy = vertexCoordinates[si].getX() - vertexCoordinates[ti].getX();
						// entfernung �ber phytagoras ausrechnen
						double dist = Math.sqrt(dx*dx + dy*dy);
						dist = (Math.random()*2.0+1.0)*dist + 1.0;
						graph.setEdgeWeight(edge, dist);
					}
				}
			}
		}
		
		
		System.out.println("Normale Kanten");
		for(int ti=0; ti<numKonten; ti++) {
			int n = distanceFromTarget[ti];
			// alle knoten auslassen die wir schon 
			if (n < (minPathEdges-1)) 
				continue;
			int numEdgesToCreate = (int)(Math.random()*(maxEdgesToCreateAtVertex+1-minVertexConnectivity) + minVertexConnectivity);
			for(int ei=0; ei<numEdgesToCreate; ei++) {
				// nicht mehr als die maximal erlaubte anzahl kanten zu diesem knoten
				if ((edgesFromVertex[ti] >= maxVertexConnectivity))
					break;
				int si = 0;
				int siTries = numKonten*2;
				do {
					si = (int)(Math.random()*numKonten);
					if (--siTries <= 0)
						break;
					// testen ob kante schon existiert, ab es eine schleife ist, oder ob 
					if ( (si==ti) || (graph.getEdge(vertices[si], vertices[ti]) != null) || (edgesFromVertex[si] >= maxVertexConnectivity))
						continue;
					// teste auch auf eine kante in gegenrichtung falls der graph ungerichtet ist
					if (!graph.isDirected() && graph.getEdge(vertices[ti], vertices[si]) != null)
						continue;
					// alle knoten auslassen die zur garantie geh�ren
					if (distanceFromTarget[si] < (minPathEdges-1))
						continue;
					// so lange versuchen bis ein Knoten gefunden wurde dessen Entfernung vom Zielknoten gleich oder gr��er ist
					break;
				} while (true);
				if (siTries <= 0)
					break;
				if (distanceFromTarget[si] < distanceFromTarget[ti]) {
					distanceFromTarget[ti] = distanceFromTarget[si] +1;
				} else {
					distanceFromTarget[si] = distanceFromTarget[ti] +1;					
				}
				System.out.println("Erstelle Kante von \"" +  vertices[si].getName() + "\" zu \"" +  vertices[ti].getName() + "\"");
				edgesFromVertex[si]++;
				edgesFromVertex[ti]++;
				DefaultWeightedEdge edge = graph.addEdge(vertices[si], vertices[ti]);
				// set weight > distance
				double dx = vertexCoordinates[si].getX() - vertexCoordinates[ti].getX();
				double dy = vertexCoordinates[si].getX() - vertexCoordinates[ti].getX();
				// entfernung �ber phytagoras ausrechnen
				double dist = Math.sqrt(dx*dx + dy*dy);
				dist = (Math.random()*2.0+1.0)*dist + 1.0;
				graph.setEdgeWeight(edge, dist);
			}
			
		}
		
		
		
		
		
		return graph;
	}
	
	
}
