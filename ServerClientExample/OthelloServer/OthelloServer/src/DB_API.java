import java.sql.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by liu on 15-6-3.
 */
public class DB_API {
    private Connection connection;
    private Statement statement;
    private final String userName = "root";
    private final String pwd = "1l0vepkulsu";
    private static DB_API instance = null;
    private DB_API() throws API_Except{
        // login and connect
        String url = "jdbc:mysql://localhost:3306/GameData?user=" + userName + "&password=" + pwd;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
            throw new API_Except("JDBC Driver Not Found" + e.getMessage());
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            throw new API_Except("Connection Failure" + e.getMessage());
        }
    }

    public static DB_API getInstance() throws API_Except{
        if (instance == null)
            instance = new DB_API();
        return instance;
    }

    public API_Msg addUser(UserData user){
        if (isExist(user.getUserName())){
            return new API_Msg(false, "User already exists.");
        }
        String sql = "INSERT INTO UserInfo (userName, pwd, nickName," +
                " grades, level, winRounds, loseRounds, tieRounds, lastLogin)" +
                " VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement preState;
        try {
            preState = connection.prepareStatement(sql);
            preState.setString(1, user.getUserName());
            preState.setString(2, user.getPassword());
            preState.setString(3, user.getNickName());
            preState.setInt(4, user.getGrades());
            preState.setInt(5, user.getLevel());
            preState.setInt(6, user.getWinRounds());
            preState.setInt(7, user.getLoseRounds());
            preState.setInt(8, user.getTieRounds());
            Timestamp now = new Timestamp(user.getLastLogin().getTime());
            preState.setTimestamp(9, now);

            preState.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new API_Msg(false, "Fail to insert");
        }
        return new API_Msg(true, "Success");
    }

    private boolean isExist(String userName){
        String sql = "SELECT userName FROM UserInfo WHERE userName=?";
        PreparedStatement preState;
        try{
            preState = connection.prepareStatement(sql);
            preState.setString(1, userName);
            ResultSet resultSet = preState.executeQuery();
            return resultSet.next();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public API_Msg renewUser(UserData other){
        String sql = "UPDATE UserInfo SET userName=?,pwd=?,nickName=?,grades=?," +
                     "level=?,winRounds=?,loseRounds=?,tieRounds=?,lastLogin=?" +
                     " WHERE userId=?";
        PreparedStatement preState;
        try{
            preState = connection.prepareStatement(sql);
            preState.setString(1, other.getUserName());
            preState.setString(2, other.getPassword());
            preState.setString(3, other.getNickName());
            preState.setInt(4, other.getGrades());
            preState.setInt(5, other.getLevel());
            preState.setInt(6, other.getWinRounds());
            preState.setInt(7, other.getLoseRounds());
            preState.setInt(8, other.getTieRounds());
            long timeStamp = other.getLastLogin().getTime();
            preState.setTimestamp(9, new Timestamp(timeStamp));
            preState.setInt(10, other.getId());
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new API_Msg(false, "Invalid SQL update.");
        }
        try{
            preState.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new API_Msg(false, "Fail to update data table.");
        }
        return new API_Msg(true, "Success");
    }

    public API_Msg removeUserById(int id){
        String sql = "DELETE FROM UserInfo WHERE userId=" + id;
        try{
            statement.executeUpdate(sql);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new API_Msg(false, "Fail to remove data.");
        }
        return new API_Msg(true, "Success");
    }

    public API_Msg removeUserByName(String userName){
        String sql = "DELETE FROM UserInfo WHERE userName=?";
        PreparedStatement preState;
        try{
            preState = connection.prepareStatement(sql);
            preState.setString(1, userName);
            preState.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new API_Msg(false, "Fail to remove data.");
        }
        return new API_Msg(true, "Success");
    }

    public API_Msg getUserByName(String name, UserData userData){
        String sql = "SELECT * FROM UserInfo WHERE userName=?";
        PreparedStatement preState;
        ResultSet result;
        try{
            preState = this.connection.prepareStatement(sql);
            preState.setString(1, name);
            result = preState.executeQuery();
            if (!result.next()){
                // not exists
                return new API_Msg(false, "No user named " + name);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new API_Msg(false, "Invalid SQL query.");
        }
        fillOneItem(result, userData);
        return new API_Msg(true, "Success");
    }

    private boolean fillOneItem(ResultSet result, UserData user){
        try {
            int uid = result.getInt("userId");
            String name = result.getString("userName");
            String pwd = result.getString("pwd");
            String nickName = result.getString("nickName");
            int grades = result.getInt("grades");
            int level = result.getInt("level");
            int winRounds = result.getInt("winRounds");
            int loseRounds = result.getInt("loseRounds");
            int tieRounds = result.getInt("tieRounds");
            Date lastLogin = result.getTimestamp("lastLogin");
            user.setId(uid)
                .setUserName(name)
                .setPassword(pwd)
                .setNickName(nickName)
                .setGrades(grades)
                .setLevel(level)
                .setWinRounds(winRounds)
                .setLoseRounds(loseRounds)
                .setTieRounds(tieRounds)
                .setLastLogin(lastLogin);
        }
        catch (SQLException e){
            return false;
        }
        return true;
    }

    public API_Msg execQuery(PreparedStatement preState, ArrayList<UserData> users){
        ResultSet result;
        try {
            result = preState.executeQuery();
            while(result.next()) {
                UserData user = new UserData();
                if (fillOneItem(result, user))
                    users.add(user);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new API_Msg(false, "Fail to execute query.");
        }
        return new API_Msg(true, "Success");
    }

    public API_Msg login(String userName, String pwd, UserData user){
        if (!this.getUserByName(userName, user).isSuccess){
            return new API_Msg(false, "Invalid username.");
        }
        if (!pwd.equals(user.getPassword())) {
            return new API_Msg(false, "Invalid Password");
        }
        Date now = new Date();
        user.setLastLogin(now);
        if (!renewUser(user).isSuccess){
            return new API_Msg(false, "Fail to renew login data.");
        }
        return new API_Msg(true, "Success.");
    }

 // beat over
    public API_Msg userDefeatUser(String userName1, String userName2){
        // find user
        UserData user1 = new UserData();
        if (!getUserByName(userName1, user1).isSuccess){
            return new API_Msg(false, "User: " + userName1 + " not found.");
        }
        UserData user2 = new UserData();
        if (!getUserByName(userName2, user2).isSuccess){
            return new API_Msg(false, "User: " + userName2 + " not found.");
        }
        // set grades
        int levelDiff = user1.getLevel() - user2.getLevel();
        if (levelDiff <= 3){
            user1.setGrades(user1.getGrades() + 20 - 5 * levelDiff);
        }
        else{
            user1.setGrades(user1.getGrades() + 5);
        }
        user1.setWinRounds(user1.getWinRounds() + 1);
        user2.setLoseRounds(user2.getLoseRounds() + 1);
        // renew
        if (!renewUser(user1).isSuccess){
            return new API_Msg(false, "Fail to renew user: " + userName1 + ", data stay unchanged.");
        }
        if (!renewUser(user2).isSuccess){
            return new API_Msg(false, "Fail to renew user: " + userName2 + ", data stay unchanged.");
        }
        return new API_Msg(true, "Success.");
    }

    // tie
    public API_Msg userTieUser(String userName1, String userName2){
        // find user
        UserData user1 = new UserData();
        if (!getUserByName(userName1, user1).isSuccess){
            return new API_Msg(false, "User: " + userName1 + " not found.");
        }
        UserData user2 = new UserData();
        if (!getUserByName(userName2, user2).isSuccess){
            return new API_Msg(false, "User: " + userName2 + " not found.");
        }
        // set
        user1.setTieRounds(user1.getTieRounds() + 1);
        user2.setTieRounds(user2.getTieRounds() + 1);
        // renew
        if (!renewUser(user1).isSuccess){
            return new API_Msg(false, "Fail to renew user: " + userName1 + ", data stay unchanged.");
        }
        if (!renewUser(user2).isSuccess){
            return new API_Msg(false, "Fail to renew user: " + userName2 + ", data stay unchanged.");
        }
        return new API_Msg(true, "Success.");
    }
    
    public API_Msg dispose(){
        try {
            statement.close();
            connection.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new API_Msg(false, "Fail to close connection");
        }
        instance = null;
        return new API_Msg(true, "Success");
    }

    public void logout(){
        // renew lastLogin item
        // do nothing
    }

}
