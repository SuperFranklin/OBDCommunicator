package DAO;

import java.sql.SQLException;
import Enums.Parameter;
import javax.sql.rowset.CachedRowSet;

import DataBase.DBProvider;

public class ParameterUtil{
    
    private static final String TABLE_NAME="PARAMETER";
    
    public static String getParameterValue( Parameter p ) {
        
        String result = new String( "");
        try {
            CachedRowSet crs = DBProvider.executeQueryAndGetResult( "select value from PARAMETER where NAME='filepath'" );
            crs.next();
            result = crs.getString( "VALUE" );
        }catch(SQLException e) {
            System.err.println( e );
        }
        
        return result;
    }
}
