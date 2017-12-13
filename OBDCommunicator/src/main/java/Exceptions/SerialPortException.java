package Exceptions;

public class SerialPortException extends RuntimeException{

    private String message;
    
    public SerialPortException(String msg) {
        this.message = msg;
    }
    
    public String toString() {
        return message;
    }
}
