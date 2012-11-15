package a2_p02_dp_bl;

import java.util.EventListener;

public interface GraphAlgoStepListener<V, E> extends EventListener {
	void algoSingleStep(String phaseName, String stepName);	
	void vertexChanged(V vertex);
	void edgeChanged(E edge);
}
