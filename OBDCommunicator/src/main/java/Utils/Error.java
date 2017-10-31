package Utils;

public class Error {
    private String errorMsg;

    public Error(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
    public String toString() {
        return errorMsg;
    }

}
