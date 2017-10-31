
package Core;


import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

import Commands.Command;
import Gui.GuiDisplayer;
import Gui.TerminalDialog;
import Utils.Error;
import Utils.Response;

public class Service {

    SerialPortComunicator serialPortComunicator = new SerialPortComunicator(this);
    GuiDisplayer displayer;
    TerminalDialog terminalDialog;

    public Service(GuiDisplayer displayer) {
        this.displayer = displayer;

    }

    public void sendBytes(String message) {
        serialPortComunicator.send(message + "\r");
    }

    public Response connect(String portNumber) {
        Response response = new Response();
        try {
            response = serialPortComunicator.conncet(portNumber);
        } catch (Exception e) {
            response.addError(new Error("Service, connect error : " + e));
        }
        return response;
    }

    public void closePort() {
        serialPortComunicator.close();
    }

    public void setResponseToTerminal(String txt) {
        displayer.getTerminalDialog().setText(txt);
    }

    public Response sendAndGetResponse(Command command) {
        return serialPortComunicator.sendAndGetResponse(command);
    }

}
