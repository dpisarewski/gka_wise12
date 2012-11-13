package a01_p02_dp_bl;

public class GraphVertex<Attribute> implements Comparable<GraphVertex<Attribute>>
{
	String m_name = null;
	Attribute m_attribute = null;
	
	public GraphVertex()
	{
	}
	public GraphVertex(String name)
	{
		m_name = name;
	}
	
	public GraphVertex(String name, Attribute attr)
	{
		m_name = name;
		m_attribute = attr;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public Attribute getAttribute()
	{
		return m_attribute;
	}
	
	public void setAttribute(Attribute attr){
		m_attribute = attr;
	}
	
	public int compareTo(GraphVertex<Attribute> vertex)
	{
		return m_name.compareTo(vertex.m_name);
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj)
	{
    	if (this == obj)
    		return true;
    	if (obj == null)
    		return false;
    	if (getClass() != obj.getClass())
    		return false;
    	return m_name.equals(((GraphVertex<Attribute>)obj).m_name);
	}

	public int hashCode() {
		return m_name.hashCode();
	}
	
	public String toString ()
	{
		return m_name;
	}
	
}
