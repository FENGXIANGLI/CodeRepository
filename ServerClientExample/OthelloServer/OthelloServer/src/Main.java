import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

	public static final int SERVER_PORT = 12345;
	 
	private static ServerSocket serverSocket = null;
	    
	   
	private static ExecutorService executorService = null;
	    //
	   
	private final static int POOL_SIZE = 2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        int cpuCount = Runtime.getRuntime().availableProcessors();
        
        executorService = Executors.newFixedThreadPool(cpuCount*POOL_SIZE);
        
   
        try {
			serverSocket=new ServerSocket(SERVER_PORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
        
        System.out.println("Waiting for client ... ");
        while(true){
        	try {
                // 接收客户连接,只要客户进行了连接,就会触发accept()从而建立连接
                Socket socket = serverSocket.accept();
                if(serverSocket==null){
                	System.out.println("accept failed");
                	break;
                }
                executorService.execute(new OthelloResponder(socket));//HelloResponser类的定义见后面
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

}
