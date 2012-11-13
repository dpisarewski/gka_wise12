package a01_p02_dp_bl;

import java.io.IOException;
import java.text.ParseException;

public interface GraphSerialization
{
	public void serialize(java.io.Writer out) throws IOException;
	public void deserialize(java.io.Reader in) throws IOException, ParseException;
}
