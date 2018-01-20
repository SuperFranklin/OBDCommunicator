
package Utils;


import Core.SerialPortComunicator;
import Core.ServiceImpl;
import DataBase.DBProvider;
import Gui.MainScreen;

public class FactoryService{

    private static ServiceImpl service;
    private static MainScreen displayer;
    private static SerialPortComunicator serialPortComunicator;
    private static DBProvider dbProvider;

    public static ServiceImpl getService(){

        if(service == null){
            service = new ServiceImpl();
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
