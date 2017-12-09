
package Commands;


import java.math.BigDecimal;
import java.util.List;

import Enums.OBDUnit;
import Utils.Utils;

public class EngineCoolantTemperature extends Command{

    public EngineCoolantTemperature(){
        super( "01", "05" );
        parameterName= "Temperatura silnika";
        unit= OBDUnit.celciusDegrees;
    }

    @Override
    public BigDecimal getDecimalValue( List<Byte> bytes ){
        float value;
        Utils.removeSpaces( bytes );
        Utils.removeRedundantCharacters( bytes );
        
        if(checkPIDs( bytes )){
            Utils.removeRequestBytes( bytes );
            System.out.println( "----------------------------------------------------------------------------------------------" );
            System.out.println("&"+System.currentTimeMillis() +" ## "+  parameterName );
            System.out.println("&"+System.currentTimeMillis() +" ## "+ "czyste :"+ bytes );
            List<Integer> ints= Utils.getIntArray( bytes );
            System.out.println("&"+System.currentTimeMillis() +" ## "+  "ints ->" +ints );
            value= (ints.get( 0 ) * 16 + ints.get( 1 )) - 40;
            return new BigDecimal( value );
        }
        System.out.println( "NPE error bytes : " + bytes);
        return null;
    }

}
