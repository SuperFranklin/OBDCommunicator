package Commands;

import java.math.BigDecimal;
import java.util.List;

import Enums.OBDUnit;
import Utils.Utils;

public class VehicleSpeedCommand extends Command{
    
    public VehicleSpeedCommand(){
        super( "01", "0D" );
        parameterName= "Prêdkoœæ pojazdu";
        unit= OBDUnit.rmp;
    }

    public BigDecimal getDecimalValue( List<Byte> bytes ){
        float value;
        Utils.removeSpaces( bytes );
        Utils.removeRedundantCharacters( bytes );
        //System.out.println( bytes );
        if(checkPIDs( bytes )){ 
            Utils.removeRequestBytes( bytes );
            System.out.println( "----------------------------------------------------------------------------------------------" );
            System.out.println("&"+System.currentTimeMillis() +" ## "+  parameterName );
            System.out.println("&"+System.currentTimeMillis() +" ## "+  "czyste :"+bytes );
            List<Integer> ints= Utils.getIntArray( bytes );
            System.out.println("&"+System.currentTimeMillis() +" ## "+  "ints ->" +ints );
                value= ints.get( 0 ) * 16 + ints.get( 1 );
                return new BigDecimal( value );
            
        }
        System.out.println( "NPE error bytes : " + bytes);
        return null;

    }

}
