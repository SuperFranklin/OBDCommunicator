
package Core;


import java.io.IOException;
import java.util.Map;

import Commands.DecValueCommand;
import Gui.MainScreen;
import Gui.TerminalDialog;
import Utils.Error;
import Utils.FactoryService;
import Utils.Response;
import gnu.io.SerialPort;

public class ServiceImpl{

    SerialPortComunicator serialPortComunicator = FactoryService.getSerialPortComunicator();
    MainScreen displayer;
    TerminalDialog terminalDialog;

    public ServiceImpl(){}

    public Response sendBytes( String message ){
        return serialPortComunicator.sendCommunicate( message + "\r" );
    }

    public Response connect( String portNumber, Integer protocolNr, Integer baudRate ){
        Response response = new Response();
        try{
            response = serialPortComunicator.conncet( portNumber, protocolNr, baudRate );
        }catch (Exception e){
            response.addError( new Error( e.toString() ) );
        }
        return response;
    }
    public Response clearDCTs() {
        Response response = new Response();
        try{
            response = serialPortComunicator.sendAndGetResponse( "04" );
        }catch (Exception e){
            response.addError( new Error( e.toString() ) );
        }
        return response;
    }

    public Response closePort(){
        return serialPortComunicator.closePort();
    }

    public Response sendAndGetResponse( DecValueCommand command ){
        return serialPortComunicator.sendAndGetResponse( command, true );
    }

    public Response sendAndGetResponse( String command ){
        return serialPortComunicator.sendAndGetResponse( command );
    }

    public SerialPort getConnectedSerialPort(){
        return serialPortComunicator.getSerialPort();
    }

    public void setConnectionPanelParameters( String portName, String status, String boudRate ){
        displayer = FactoryService.getDisplayer();
        displayer.setConnectionPanelParameters( portName, boudRate, status );
    }

    public Map<String, String> getDTCMap(){
        return serialPortComunicator.getDTCMap();
    }

    public String getPortName(){
        return serialPortComunicator.getSerialPort().getName();
    }

    public SerialPortComunicator getSerialPortComunicator(){
        return serialPortComunicator;
    }

    public void setSerialPortComunicator( SerialPortComunicator serialPortComunicator ){
        this.serialPortComunicator = serialPortComunicator;
    }

    public MainScreen getDisplayer(){
        return displayer;
    }

    public void setDisplayer( MainScreen displayer ){
        this.displayer = displayer;
    }

    public TerminalDialog getTerminalDialog(){
        return terminalDialog;
    }

    public void setTerminalDialog( TerminalDialog terminalDialog ){
        this.terminalDialog = terminalDialog;
    }

}
