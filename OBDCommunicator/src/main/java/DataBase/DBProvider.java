
package DataBase;


import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetWarning;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;
import com.sun.rowset.CachedRowSetImpl;

public class DBProvider{

    private static final String DB_URL= "jdbc:sqlite:src/main/resources/OBDExplorerDB.db";
    private static final String DRIVER= "org.sqlite.JDBC";
    private static Connection c= null;
    private static Statement stat= null;
    private static ResultSet result= null;

    private static Statement createStatement(){
        try{
            Class.forName( DRIVER );
            c= DriverManager.getConnection( DB_URL );
            System.out.println( "Opened database successfully" );
            return c.createStatement();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return null;
        }
    }
    
    private static void closeConnection() {
        try {
        result.close();
        stat.close();
        c.close();
        }catch (SQLException e) {
            System.err.println( e.toString() );
        }
    }
    
    public static CachedRowSet executeQueryAndGetResult(String sqlQuery) {
        CachedRowSet cachedRowSet= null;
        try {
        cachedRowSet = new CachedRowSetImpl();
        stat = createStatement();
        result = stat.executeQuery( sqlQuery );
        cachedRowSet.populate( result );
        closeConnection();
        }catch (SQLException e) {
            System.err.println( e.toString() );
        }
        return cachedRowSet;

    }
    
    public static void executeUpdateQuery(String sqlQquery) {
        try{
            stat = createStatement();
            stat.executeUpdate( sqlQquery );
        }catch (SQLException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        closeConnection();
    }
    
    
}

