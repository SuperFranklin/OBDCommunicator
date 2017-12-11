
package Commands;


import java.math.BigDecimal;
import java.util.List;

import Enums.OBDUnit;
import Utils.ByteUtils;

public abstract class Command{

    protected int responseTimeLimit;
    protected String communicate;
    protected String SID;
    protected String PID;
    protected OBDUnit unit;
    protected String parameterName;

    public Command( String SID, String PID ){
        this.SID = SID;
        this.PID = PID;
        communicate = SID + PID;
    }

    public BigDecimal parseAndGetDecimalValue( List<Byte> bytes ){
        ByteUtils.removeSpaces( bytes );
        ByteUtils.removeRedundantCharacters( bytes );
        if(checkPIDs( bytes )){
            ByteUtils.removeRequestBytes( bytes );
            List<Integer> numbers = ByteUtils.getNumberArray( bytes );
            return new BigDecimal( computeValue( numbers ) );
        }
        return null;
    }

    protected abstract float computeValue( List<Integer> ints );

    // TODO dopisaæ jak bêdzie czas, a je¿eli nie to niech zwraca true chwilowo
    public boolean checkPIDs( List<Byte> bytes ){
        /*
         * byte[] pidBytes= PID.getBytes(); //TODO obs³u¿yc przypadek, gdy rozmiar bytes <4 if(pidBytes[ 0 ] ==
         * bytes.get( 2 ).byteValue() && pidBytes[ 1 ] == bytes.get( 3 ).byteValue()){ return true; } return false;
         */
        return true;
    }

    public int getResponseTimeLimit(){
        return responseTimeLimit;
    }

    public void setResponseTimeLimit( int responseTimeLimit ){
        this.responseTimeLimit = responseTimeLimit;
    }

    public String getCommunicate(){
        return communicate + "\r\n";
    }

    public void setCommunicate( String communicate ){
        this.communicate = communicate;
    }

    public OBDUnit getUnit(){
        return unit;
    }

    public void setUnit( OBDUnit unit ){
        this.unit = unit;
    }

    public BigDecimal computeResult( String string ){
        // TODO Auto-generated method stub
        return new BigDecimal( 900 );
    }

    public String getParameterName(){
        return parameterName;
    }

}
