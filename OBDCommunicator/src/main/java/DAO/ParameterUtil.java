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
            CachedRowSet crs = DBProvider.executeQueryAndGetResult( "select value from PARAMETER where NAME='"+p+"'");
            crs.next();
            result = crs.getString( "VALUE" );
        }catch(SQLException e) {
            System.err.println( e );
        }
        
        return result;
    }
public static void setParameterValue( Parameter p, String value ) {
            DBProvider.executeUpdateQuery( "update PARAMETER SET value='"+value +"' where NAME='"+p+"'");
            //result = crs.getString( "VALUE" );
    }
}
