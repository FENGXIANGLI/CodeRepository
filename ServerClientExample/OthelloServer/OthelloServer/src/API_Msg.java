public class API_Msg {
    public boolean isSuccess;
    public String errNote;

    public API_Msg(){}
    public API_Msg(boolean isSuccess, String errNote){
        this.isSuccess = isSuccess;
        this.errNote = errNote;
    }
}
