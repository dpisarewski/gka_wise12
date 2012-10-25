package a1_p02_dp_bl;

public class GraphVertex<Attribute> implements Comparable<GraphVertex<Attribute>>
{
	String name;
	Attribute attribute;
	
	public GraphVertex(String name)
	{
		this.name = name;
	}
	
	public GraphVertex(String name, Attribute attr)
	{
		this.name = name;
		this.attribute = attr;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Attribute getAttribute()
	{
		return attribute;
	}
	
	public int compareTo(GraphVertex<Attribute> vertex)
	{
		return name.compareTo(vertex.name);
	}
	
	public boolean equals(GraphVertex<Attribute> vertex)
	{
		return name.equals(vertex);
	}
	
	public String toString ()
	{
		return name;
	}
	
}
