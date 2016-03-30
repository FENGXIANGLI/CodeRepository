import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class GameSession {
	public OthelloResponder player1;
	public OthelloResponder player2;
	ChessBoard chess;
	public static OthelloResponder waitingUser;
	public static ArrayList<GameSession> sessionPool=new ArrayList<GameSession>();
	
	public GameSession(OthelloResponder player1, OthelloResponder player2){
		this.player1=player1;
		this.player2=player2;
	}
	public void start() throws IOException{
		chess=new ChessBoard();
		chess.init();
		DB_API instance=null;
		try {
			instance=DB_API.getInstance();
		} catch (API_Except e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap<String, Object> content;
		content=new HashMap<String, Object>();
		content.put("color", 1);
		content.put("opnick", this.player2.getNick());
		UserData userData=new UserData();
		instance.getUserByName(this.player2.uid, userData);
		content.put("opscore", userData.getGrades());
		player1.playerColor=1;
		player1.sendPackage(new DataPackage("GSTRT", content));
		content=new HashMap<String, Object>();
		content.put("color", -1);
		content.put("opnick", this.player1.getNick());
		userData=new UserData();
		instance.getUserByName(this.player1.uid, userData);
		content.put("opscore", userData.getGrades());
		player2.playerColor=-1;
		player2.sendPackage(new DataPackage("GSTRT", content));
		player1.session=this;
		player2.session=this;
	}
	public void distribute(){
		this.player1.session=null;
		this.player2.session=null;
		this.player1=null;
		this.player2=null;
		sessionPool.remove(this);
	}
	public static Boolean wantToJoinASession(OthelloResponder responder){
		if(waitingUser!=null){
			GameSession session=new GameSession(waitingUser, responder);
			sessionPool.add(session);
			try{
				session.start();
			}catch(IOException e){
				try{
					session.player1.sendPackage(new DataPackage("GDISC",null));
				}catch(IOException err){
					
				}
				try{
					session.player2.sendPackage(new DataPackage("GDISC",null));
				}catch(IOException err){
					
				}
				session.distribute();
				return false;
			}
			return true;
		}else{
			waitingUser=responder;
			try {
				responder.sendPackage(new DataPackage("GWAIT",null));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	}
	public Boolean setPs(int x, int y, int color){
		return chess.put(x, y, color);
	}
	public OthelloResponder oppositeOf(OthelloResponder player){
		return this.playerOfColor(-player.playerColor);
	}
	public OthelloResponder playerOfColor(int color){
		if(color==player1.playerColor) return player1;
		return player2;
	}
	public void checkGameIsOver(){
		int g=this.isGameOver();
		DB_API instance=null;
		try {
			instance=DB_API.getInstance();
		} catch (API_Except e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(g==0){
			instance.userTieUser(this.player1.uid, this.player2.uid);
		}else if(g==1){
			String winner=this.player1.playerColor==g?this.player1.uid:this.player2.uid;
			String loser=this.player1.playerColor==g?this.player2.uid:this.player1.uid;
			instance.userDefeatUser(winner, loser);
		}
		HashMap<String, Object> data=new HashMap<String, Object>();
		data.put("winner", g);
		try {
			UserData userData=new UserData();
			instance.getUserByName(this.player1.uid, userData);
			int Score1=userData.getGrades();
			userData=new UserData();
			instance.getUserByName(this.player2.uid, userData);
			int Score2=userData.getGrades();
			data.put("yourScore", Score1);
			data.put("oppositeScore", Score2);
			this.player1.sendPackage(new DataPackage("GOVER", data));
			data.put("yourScore", Score2);
			data.put("oppositeScore", Score1);
			this.player2.sendPackage(new DataPackage("GOVER", data));
			this.player1.state=UserState.GAMEOVER;
			this.player2.state=UserState.GAMEOVER;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public int isGameOver(){
		return chess.winner();
	}
	public Boolean regret(int color){
		return this.chess.regret(color);
	}
	public Boolean canRegret(int color){
		return color==1?this.chess.getCanUndoB():this.chess.getCanUndoW();
	}
}
