
package Commands;


import java.math.BigDecimal;
import java.util.List;

import Enums.OBDUnit;
import Utils.Utils;

public class MassAirFlowRateCommand extends Command{

    public MassAirFlowRateCommand(){
        super( "01", "10" );
        parameterName= "Natê¿enie przep³ywu powietrza";
        unit= OBDUnit.rmp;
    }

    public BigDecimal getDecimalValue( List<Byte> bytes ){
        float value;
        Utils.removeSpaces( bytes );
        Utils.removeRedundantCharacters( bytes );
        
        if(checkPIDs( bytes )){
            Utils.removeRequestBytes( bytes );
            System.out.println( "----------------------------------------------------------------------------------------------" );
            System.out.println("&"+System.currentTimeMillis() +" ## "+  parameterName );
            System.out.println("&"+System.currentTimeMillis() +" ## "+  "czyste :"+bytes );
            List<Integer> ints= Utils.getIntArray( bytes );
            System.out.println("&"+System.currentTimeMillis() +" ## "+  "ints ->" +ints );
                value= (ints.get( 0 ) * 4096 + ints.get( 1 ) * 256 + ints.get( 2 ) * 16 + ints.get( 3 )) / 100;
                return new BigDecimal( value );
            
        }
        System.out.println( "NPE error bytes : " + bytes);
        return null;

    }

}
