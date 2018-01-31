package DAO;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import DataBase.DBProvider;
import Entities.TroubleCode;
import Gui.TroubleCodesDialog;

public class DTCUtil{
    
    public static TroubleCode getTroubleCodeDesc( String code ) {
        TroubleCode result = new TroubleCode();
        try {
            CachedRowSet crs = DBProvider.executeQueryAndGetResult( "select desc from DTC where code ='P"+code+"'" );
            if(crs.next()) {
            String desc = crs.getString( "DESC" );
            result.setCode( code );
            result.setDescription( desc );
            }
        }catch(SQLException e) {
            System.err.println( e );
        }
        
        return result;
    }

}
