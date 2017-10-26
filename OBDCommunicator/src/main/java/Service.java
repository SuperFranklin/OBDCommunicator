import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

/**
 * Created by pc on 2017-10-04.
 */
public class Service {

    SerialPortComunicator serialPortComunicator = new SerialPortComunicator(this);
    GuiDisplayer displayer;
    TerminalDialog terminalDialog;
    public Service(GuiDisplayer displayer) {
        this.displayer = displayer;
        
    }
    public void sendBytes(String message){
        serialPortComunicator.send(message+"\r");
    }
    public void connect(String portNumber){
        try {
            serialPortComunicator.conncet(portNumber);
        } catch (Exception e){
            System.err.println("Service, connect error : " + e);
        }
    }
    public void closePort(){
        serialPortComunicator.close();
    }
    public void setResponseToTerminal(String txt) {
        displayer.getTerminalDialog().setText(txt);
    }
    public ObdResponse sendAndGetResponse(Command command) {
        ObdResponse result = new ObdResponse();
        serialPortComunicator.sendAndGetResponse(command);
        return result;
    }
    
}
