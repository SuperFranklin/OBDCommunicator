
package Utils;


import Core.SerialPortComunicator;
import Core.Service;
import DataBase.DBProvider;
import Gui.MainScreen;

public class FactoryService{

    private static Service service;
    private static MainScreen displayer;
    private static SerialPortComunicator serialPortComunicator;
    private static DBProvider dbProvider;

    public static Service getService(){

        if(service == null){
            service = new Service();
        }
        service.setSerialPortComunicator( getSerialPortComunicator() );
        return service;
    }

    public static MainScreen getDisplayer(){
        if(displayer == null){
            displayer = new MainScreen();
        }
        
        return displayer;
    }

    public static SerialPortComunicator getSerialPortComunicator(){
        if(serialPortComunicator == null){
            serialPortComunicator = new SerialPortComunicator(service);
        }
        return serialPortComunicator;
    }

}
