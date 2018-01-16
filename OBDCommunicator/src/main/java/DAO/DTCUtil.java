package DAO;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import DataBase.DBProvider;
import Gui.TroubleCodesDialog;

public class DTCUtil{
    
    public static String getTroubleCodeDesc( String code ) {
        String result = new String( "");
        try {
            CachedRowSet crs = DBProvider.executeQueryAndGetResult( "select desc from DTC where code ='P"+code+"'" );
            if(crs.next()) {
            result = crs.getString( "DESC" );
            }
        }catch(SQLException e) {
            System.err.println( e );
        }
        
        return result;
    }

}
