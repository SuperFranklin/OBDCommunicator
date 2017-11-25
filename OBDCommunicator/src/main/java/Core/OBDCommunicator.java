package Core;

import java.sql.SQLException;

import DataBase.DBProvider;
import Gui.MainScreen;
import Utils.FactoryService;

/**
 * Created by pc on 2017-10-04.
 */
public class OBDCommunicator {

    public static void main(String ...args){
        new MainScreen();
        try{
            System.out.println( DBProvider.getFromTableFromWhere( "DTC", "CODE", "P0104" ).getString( 1 ) );
        }catch (SQLException e){
            System.out.println( e );
        }
    }
}
