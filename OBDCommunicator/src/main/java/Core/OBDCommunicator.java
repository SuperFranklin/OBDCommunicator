package Core;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import DAO.DTCUtil;
import DAO.ParameterUtil;
import DataBase.DBProvider;
import Enums.Parameter;
import Gui.MainScreen;
import Utils.FactoryService;

/**
 * Created by pc on 2017-10-04.
 */
public class OBDCommunicator {

    public static void main(String ...args){
        FactoryService.getDisplayer();
        System.out.println( ParameterUtil.getParameterValue( Parameter.filePath ) );
        
    }
}
