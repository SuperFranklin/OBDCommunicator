
package Commands;


import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.nio.Buffer;
import java.util.List;

import javax.swing.JPanel;

import Enums.OBDUnit;
import Utils.Response;
import Utils.Utils;

public class RPMCommand extends Command{

    public RPMCommand(){
        super( "01", "0C" );
        parameterName= "Prêdkoœæ obrotowa";
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
            System.out.println("&"+System.currentTimeMillis() +" ## "+ "czyste :"+ bytes );
            List<Integer> ints= Utils.getIntArray( bytes );
            System.out.println("&"+System.currentTimeMillis() +" ## "+  "ints ->" +ints );
                value= (ints.get( 0 ) * 4096 + ints.get( 1 ) * 256 + ints.get( 2 ) * 16 + ints.get( 3 )) / 4;
                return new BigDecimal( value );
            
        }
        System.out.println( "NPE error bytes : " + bytes);
        return null;

    }
    
    

}


