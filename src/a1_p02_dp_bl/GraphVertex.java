package a1_p02_dp_bl;


public class GraphVertex implements Comparable<GraphVertex>
{
	String m_name = null;
	int m_numAttribute = 0;
	Object m_attribute = null;
	double m_x = Double.NaN;
	double m_y = Double.NaN;
	
	public GraphVertex()
	{
	}
	public GraphVertex(String name)
	{
		m_name = name;
	}
	
	public GraphVertex(String name, int attr)
	{
		m_name = name;
		m_numAttribute = attr;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public int getNumericAttribute()
	{
		return m_numAttribute;
	}
	
	public void setPosition(double x, double y)
	{
		m_x = x;
		m_y = y;
	}

	public double getX()
	{
		return m_x;
	}
	public double getY()
	{
		return m_y;
	}
	
	
	public void setNumericAttribute(int attr){
		m_numAttribute = attr;
	}
	
	public int compareTo(GraphVertex vertex)
	{
		return m_name.compareTo(vertex.m_name);
	}
	
	public boolean equals(Object obj)
	{
    	if (this == obj)
    		return true;
    	if (obj == null)
    		return false;
    	if (getClass() != obj.getClass())
    		return false;
    	return m_name.equals(((GraphVertex)obj).m_name);
	}

	public int hashCode() {
		return m_name.hashCode();
	}
	
	public String toString ()
	{
		return m_name;
	}
	
}

 

