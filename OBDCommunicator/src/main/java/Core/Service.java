
package Core;


import java.util.ArrayList;
import java.util.List;

import Commands.Command;
import Entities.TroubleCode;
import Gui.MainScreen;
import Gui.TerminalDialog;
import Utils.Error;
import Utils.FactoryService;
import Utils.Response;

public class Service{

    SerialPortComunicator serialPortComunicator= FactoryService.getSerialPortComunicator();
    MainScreen displayer;
    TerminalDialog terminalDialog;

    public Service(){}

    public void sendBytes( String message ){
        serialPortComunicator.send( message + "\r" );
    }

    public Response connect( String portNumber ){
        Response response= new Response();
        try{
            response= serialPortComunicator.conncet( portNumber );
        }catch (Exception e){
            response.addError( new Error( "Service, connect error : " + e ) );
        }
        return response;
    }

    public List<TroubleCode> getListOfTroubleCodes(){
        List<TroubleCode> result= new ArrayList<TroubleCode>();
        //Response r= serialPortComunicator.sendAndGetResponse( "03" );

        return result;
    }

    public void closePort(){
        serialPortComunicator.close();
    }

    /*
     * public void setResponseToTerminal(String txt) { displayer.getTerminalDialog().setText(txt); }
     */

    public Response sendAndGetResponse( Command command ){
        return serialPortComunicator.sendAndGetResponse( command,  true );
    }
    
    public Response sendAndGetResponse( String command) {
        return serialPortComunicator.sendAndGetResponse( command );
    }
    

}
