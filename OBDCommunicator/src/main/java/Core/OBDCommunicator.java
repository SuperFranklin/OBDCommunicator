
package Core;


import DAO.DTCUtil;
import DAO.ParameterUtil;
import DataBase.DBProvider;
import Enums.Parameter;
import Utils.FactoryService;

public class OBDCommunicator{

    public static void main( String ...args ){
        FactoryService.getDisplayer();
        System.out.println( ParameterUtil.getParameterValue( Parameter.headers ) );
        ParameterUtil.setParameterValue( Parameter.headers, "ON" );
        System.out.println( ParameterUtil.getParameterValue( Parameter.headers ) );
    }
}
