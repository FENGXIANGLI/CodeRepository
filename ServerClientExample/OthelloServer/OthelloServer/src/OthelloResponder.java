import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONObject;

enum UserState{
	CONNECTED,
	LOGGEDIN,
	WAITINGFORGAME,
	WAITINGFOROPPOSITESTEP,
	WAITINGFORSTEP,
	WAITINGFOROPPOSITEREGRETAGREE,
	WAITINGFORREGRETAGREE,
	GAMEOVER,
	WAITINGFOROPPOSITESTART
}
public class OthelloResponder implements Runnable {
	private Socket socket = null;
	public UserState state;
	private HashMap<UserState, ArrayList<String>> allowedHeaders;
	private ArrayList<String> noContentHeaders;
	public String uid;
	public GameSession session;
	private DataInputStream input;
	private DataOutputStream output;
	public int playerColor;
	
	public OthelloResponder(Socket socket){
		this.socket=socket;
		noContentHeaders=new ArrayList<String>();
		noContentHeaders.add("NGAME");
		noContentHeaders.add("NCACL");
		noContentHeaders.add("RGRET");
		noContentHeaders.add("ADDFT");
		noContentHeaders.add("GQUIT");
		noContentHeaders.add("GAGAN");
		noContentHeaders.add("GFQUT");
		noContentHeaders.add("LGOUT");
		noContentHeaders.add("SCORE");
		
		allowedHeaders=new HashMap<UserState, ArrayList<String>>();
		ArrayList<String> list;
		
		list=new ArrayList<String>();
		list.add("REGST");
		list.add("LOGIN");
		list.add("LGOUT");
		allowedHeaders.put(UserState.CONNECTED, list);
		
		list=new ArrayList<String>();
		list.add("NGAME");
		list.add("NCCHG");
		list.add("LGOUT");
		list.add("SCORE");
		allowedHeaders.put(UserState.LOGGEDIN, list);
		
		list=new ArrayList<String>();
		list.add("NCACL");
		allowedHeaders.put(UserState.WAITINGFORGAME, list);
		
		list=new ArrayList<String>();
		list.add("RGRET");
		list.add("SETPS");
		list.add("ADDFT");
		list.add("GFQUT");
		allowedHeaders.put(UserState.WAITINGFORSTEP, list);
		
		list=new ArrayList<String>();
		list.add("GFQUT");
		allowedHeaders.put(UserState.WAITINGFOROPPOSITESTEP, list);
		
		list=new ArrayList<String>();
		list.add("RRGRE");
		list.add("GFQUT");
		allowedHeaders.put(UserState.WAITINGFORREGRETAGREE, list);
		
		list=new ArrayList<String>();
		list.add("GFQUT");
		allowedHeaders.put(UserState.WAITINGFOROPPOSITEREGRETAGREE, list);
		
		list=new ArrayList<String>();
		list.add("GQUIT");
		list.add("GAGAN");
		list.add("SCORE");
		allowedHeaders.put(UserState.GAMEOVER, list);
		
		list=new ArrayList<String>();
		allowedHeaders.put(UserState.WAITINGFOROPPOSITESTART, list);
	}
	public String getNick(){
		DB_API instance;
		try {
			instance = DB_API.getInstance();
		} catch (API_Except e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		UserData userData=new UserData();
		instance.getUserByName(uid, userData);
		return userData.getNickName();
	}
	public String readStringOfLength(int length) throws IOException{
		int total=0;
		byte[] res=new byte[length];
		while(total<length){
			int count=input.read(res, total, length-total);
			total+=count;
		}
		return new String(res);
	}
	public DataPackage readPackage() throws IOException{		
		String header=this.readStringOfLength(5);
		System.out.println("--get from "+this.getNick()+"--");
		HashMap<String, Object> res;

		if(this.noContentHeaders.indexOf(new String(header))==-1){
			int length=input.readInt();
			if(length>1024) throw new IOException();
			String content=this.readStringOfLength(length);
			
			JSONObject obj=JSONObject.fromObject(content);
			if(obj==null) throw new IOException();
			res=new HashMap<String, Object>();
			for (Object key : obj.keySet()) {
				res.put((String)key, obj.get(key));
			}			
			System.out.println(header+" "+content);
		}else{
			System.out.println(header);
			res=null;
		}
		DataPackage pkg=new DataPackage(header,res);
		return pkg;
	}
	public Boolean sendPackage(DataPackage pkg) throws IOException{
		System.out.println("--send to "+this.getNick()+"--");
		if(pkg.content!=null) System.out.println(pkg.header+" "+pkg.content);
		else System.out.println(pkg.header);
		String content=null;
		if(pkg.content!=null) content=JSONObject.fromObject(pkg.content).toString();
		String header=pkg.header;
		output.writeBytes(header);
		if(content!=null){
			byte[] cb=content.getBytes();
			output.writeInt(cb.length);
			output.write(cb, 0, cb.length);
			output.flush();			
		}
		
		return true;
	}
	public DataPackage okPackage(String header){
		HashMap<String, Object> content=new HashMap<String, Object>();
		content.put("code", 0);
		content.put("msg", "ok");
		return new DataPackage(header, content);
	}
	public DataPackage errorPackageWithMsg(String header, int code, String msg){
		HashMap<String, Object> content=new HashMap<String, Object>();
		content.put("code", code);
		content.put("msg", msg);
		return new DataPackage(header, content);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
	        String clientIp = socket.getInetAddress().getHostAddress();
	            System.out.println("开始处理来自\""+clientIp+":"+socket.getPort()+"\"的请求。");
	            this.state=UserState.CONNECTED;
	            input = new DataInputStream(socket.getInputStream());
	            output = new DataOutputStream(socket.getOutputStream());
	            DB_API instance = DB_API.getInstance();
	            while(true){
	            	DataPackage pkg=null;
	            	try{
	            		pkg=this.readPackage();	            		
	            	}catch(IOException e){
	            		if(GameSession.waitingUser==this){
	            			GameSession.waitingUser=null;
	            		}else if(this.session!=null){
	            			this.session.oppositeOf(this).state=UserState.LOGGEDIN;
	            			try{
	            				this.socket.close();
	            				this.session.oppositeOf(this).sendPackage(new DataPackage("GDISC", null));
	            			}catch(Exception exp){}
	            			this.session.distribute();
	            		}
	            		return;
	            	}
	            	
	            	HashMap<String, Object> data=pkg.content;
	            	if(this.allowedHeaders.get(this.state).indexOf(pkg.header)==-1){
	            		this.sendPackage(this.errorPackageWithMsg("IVCMD", 1, "Header "+pkg.header+" is not allowed under state "+this.state));
	            		continue;
	            	}
	            	switch(pkg.header){
	            		case "REGST":
	            			String uid=(String) data.get("uid");
	            			String pwd=(String) data.get("pwd");
	            			String nick=(String) data.get("nick");
	            			
	            			UserData user=new UserData(uid,nick,pwd,0,0,0,0,0,new Date());
	            			System.out.println(uid+" "+pwd+" "+nick);
	            			API_Msg msg=instance.addUser(user);
	            			if(msg.isSuccess){
	            				this.sendPackage(this.okPackage("RREGS"));
	            				this.uid=uid;
	            				this.state=UserState.LOGGEDIN;
	            			}else{
	            				this.sendPackage(this.errorPackageWithMsg("RREGS", 1, msg.errNote));
	            			}
	            			
	            		break;
	            		case "LOGIN":
	            			uid=(String) data.get("uid");
	            			pwd=(String) data.get("pwd");
	            			UserData udata=new UserData();
	            			msg=instance.login(uid, pwd, udata);
	            			if(msg.isSuccess){
	            				nick=udata.getNickName();
	            				HashMap<String, Object> content=new HashMap<String, Object>();
	            				content.put("code", 0);
	            				content.put("msg", "ok");
	            				content.put("nick", nick);
	            				content.put("score",udata.getGrades());
	            				this.uid=uid;
	            				this.state=UserState.LOGGEDIN;
	            				this.sendPackage(new DataPackage("RLOGI",content));
	            			}else{
	            				this.sendPackage(this.errorPackageWithMsg("RLOGI", 1, msg.errNote));
	            			}
	            			
	            		break;
	            		case "NGAME":
	            			if(GameSession.wantToJoinASession(this)){
	            				this.state=this.playerColor==1?UserState.WAITINGFORSTEP:UserState.WAITINGFOROPPOSITESTEP;
	            				this.session.oppositeOf(this).state=this.playerColor==1?UserState.WAITINGFOROPPOSITESTEP:UserState.WAITINGFORSTEP;
	            			}else{
	            				this.state=UserState.WAITINGFORGAME;
	            			}
	            		break;
	            		case "SETPS":
	            			int x=(int)pkg.content.get("x");
	            			int y=(int)pkg.content.get("y");
	            			if(this.session.setPs(x, y, this.playerColor)){
	            				this.sendPackage(this.okPackage("RSETP"));
	            				this.session.oppositeOf(this).sendPackage(pkg);
	            			}else{
	            				this.sendPackage(this.errorPackageWithMsg("RSETP", 1, "Illigal position!"));
	            			}
	            			if(this.session.chess.canPut(this.session.oppositeOf(this).playerColor)){
	            				this.state=UserState.WAITINGFOROPPOSITESTEP;
	            				this.session.oppositeOf(this).state=UserState.WAITINGFORSTEP;
	            			}else if(this.session.chess.canPut(this.playerColor)){
	            				this.state=UserState.WAITINGFORSTEP;
	            				this.session.oppositeOf(this).state=UserState.WAITINGFOROPPOSITESTEP;
	            			}else{
	            				this.session.checkGameIsOver();
	            			}
	            		break;
	            		case "RSETP":
	            			System.out.println(this.uid+" says ok.");
	            		break;
	            		case "NCACL":
	            			this.state=UserState.LOGGEDIN;
	            			GameSession.waitingUser=null;
	            		break;
	            		case "NCCHG":
	            			this.sendPackage(this.okPackage("RCNCH"));
	            			//TODO Change the nick in the DB.
	            		break;
	            		case "RGRET":
	            			if(this.session.canRegret(this.playerColor)){
	            				this.session.oppositeOf(this).sendPackage(pkg);
	            				this.state=UserState.WAITINGFOROPPOSITEREGRETAGREE;
	            				this.session.oppositeOf(this).state=UserState.WAITINGFORREGRETAGREE;
	            			}else{
	            				this.sendPackage(this.errorPackageWithMsg("IVCMD", 1,"You can't regret now."));
	            			}
	            			
	            		break;
	            		case "RRGRE":
	            			if((int)data.get("code")==0){
	            				this.session.regret(this.playerColor);
	            			}
	            			this.state=UserState.WAITINGFOROPPOSITESTEP;
	            			this.session.oppositeOf(this).state=UserState.WAITINGFORSTEP;
	            			this.session.oppositeOf(this).sendPackage(pkg);
	            		break;
	            		case "ADDFT":
	            			this.session.oppositeOf(this).sendPackage(pkg);
	            			this.state=UserState.GAMEOVER;
	            			this.session.oppositeOf(this).state=UserState.GAMEOVER;
	            		break;
	            		case "GQUIT":
	            			this.state=UserState.LOGGEDIN;
	            			this.session.oppositeOf(this).sendPackage(pkg);
	            			this.session.oppositeOf(this).state=UserState.LOGGEDIN;
	            			this.session.distribute();
	            		break;
	            		case "GAGAN":
	            			if(this.session.oppositeOf(this).state==UserState.GAMEOVER){
	            				this.sendPackage(new DataPackage("GWTAG",null));
	            			}else if(this.session.oppositeOf(this).state==UserState.WAITINGFOROPPOSITESTART){
	            				this.session.start();
	            			}
	            		break;
	            		case "GFQUT":
	            			this.session.oppositeOf(this).sendPackage(pkg);
	            			this.state=UserState.LOGGEDIN;
	            			this.session.oppositeOf(this).state=UserState.LOGGEDIN;
	            			this.session.distribute();
	            		break;
	            		case "LGOUT":
	            			input.close();
	            			output.close();
	            			this.socket.close();
	            			return;
	            		case "SCORE":
	            			UserData ud=new UserData();
	            			instance.getUserByName(this.getNick(), ud);
	            			int ys=ud.getGrades();
	            			int tos=-1;
	            			if(this.session!=null){
	            				ud=new UserData();
	            				instance.getUserByName(this.session.oppositeOf(this).uid, ud);
	            				tos=ud.getGrades();
	            			}
	            			HashMap<String, Object> map=new HashMap<String, Object>();
	            			map.put("yourScore", ys);
	            			map.put("oppositeScore", tos);
	            			this.sendPackage(new DataPackage("RSCOR",map));
	            		break;
	            		default:
	            			this.sendPackage(this.errorPackageWithMsg("IVCMD", 3, "Unexpected Header "+pkg.header));
	            		break;
	            	}
	            }
	            
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (API_Except e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
	            try {
	                if(socket!=null)
	                    socket.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	}

}
