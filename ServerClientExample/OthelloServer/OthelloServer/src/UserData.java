import java.util.Date;

public class UserData {
    private int id;
    private String userName;
    private String nickName;
    private String password;
    private int grades;
    private int level;
    private int winRounds;
    private int loseRounds;
    private int tieRounds;
    private Date lastLogin;

    public UserData(){
        userName = "";
        nickName = "";
        password = "";
        grades = 0;
        level = 0;
        winRounds = 0;
        loseRounds = 0;
        tieRounds = 0;
        lastLogin = new Date();
    }

    public UserData(String userName, String nickName,
                    String password, int grades, int level,
                    int winRounds, int loseRounds, int tieRounds, Date lastLogin){
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.grades = grades;
        this.level = level;
        this.winRounds = winRounds;
        this.loseRounds = loseRounds;
        this.tieRounds = tieRounds;
        this.lastLogin = lastLogin;
    }

    // getters
    public int getId() {
        return id;
    }
    public int getLevel() {
        return level;
    }
    public int getLoseRounds() {
        return loseRounds;
    }
    public int getTieRounds() {
        return tieRounds;
    }
    public String getNickName(){
        return nickName;
    }
    public int getWinRounds() {
        return winRounds;
    }
    public int getGrades(){
        return grades;
    }
    public String getPassword() {
        return password;
    }
    public String getUserName() {
        return userName;
    }
    public Date getLastLogin() {
        return lastLogin;
    }

    // setters
    public UserData setId(int id){
        this.id = id;
        return this;
    }
    public UserData setLevel(int level) {
        this.level = level;
        return this;
    }
    public UserData setLoseRounds(int loseRounds) {
        this.loseRounds = loseRounds;
        return this;
    }

    public UserData setGrades(int grades) {
        this.grades = grades;
        return this;
    }

    public UserData setPassword(String password) {
        this.password = password;
        return this;
    }
    public UserData setNickName(String nickName){
        this.nickName = nickName;
        return this;
    }
    public UserData setTieRounds(int tieRounds) {
        this.tieRounds = tieRounds;
        return this;
    }
    public UserData setUserName(String userName) {
        this.userName = userName;
        return this;
    }
    public UserData setWinRounds(int winRounds) {
        this.winRounds = winRounds;
        return this;
    }

    public UserData setLastLogin(Date date){
        this.lastLogin = date;
        return this;
    }

    public String toJson(){
        return this.toString();
    }

    @Override
    public String toString(){
        String s = "{" + "\"userName\":\"" + this.userName + "\","
                       + "\"password\":\"" + this.password + "\","
                       + "\"nickName\":\"" + this.nickName + "\","
                       + "\"grades\":"     + this.grades   + ","
                       + "\"level\":"      + this.level    + ","
                       + "\"winRounds\":"  + this.winRounds+ ","
                       + "\"loseRounds\":" + this.loseRounds+","
                       + "\"tieRounds\":"  + this.tieRounds+ ","
                       + "\"lastLogin\":\""  + this.getLastLogin() + "\"" +
                  "}";
        return s;
    }
}
