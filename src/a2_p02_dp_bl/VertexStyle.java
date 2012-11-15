package a2_p02_dp_bl;

public class VertexStyle {

	String m_label;
	String m_style;
	double m_width;
	double m_height;
	
	public VertexStyle(String label, String style, double width, double height) {
		m_label = label;
		m_style = style;
		m_width = width;
		m_height = height;
	}
	
	public String getLabel() {
		return m_label;
	}
	public String getStyle() {
		return m_style;
	}
	public double getWidth() {
		return m_width;
	}
	public double getHeight() {
		return m_height;
	}

}
