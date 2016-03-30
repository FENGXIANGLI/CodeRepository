import java.util.HashMap;

public class DataPackage {
	public String header;
	public HashMap<String, Object> content;
	public DataPackage(String header, HashMap<String, Object> content){
		this.header=header;
		this.content=content;
	}
}
