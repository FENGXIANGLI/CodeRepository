/**
 * Created by liu on 15-6-3.
 */
public class API_Except extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public API_Except(String errorNote){
        super(errorNote);
    }
}
