
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
        if(checkPIDs( bytes )){
            Utils.removeRequestBytes( bytes );

            Utils.removeRedundantCharacters( bytes );

            List<Integer> ints= Utils.getIntArray( bytes );
            value= (ints.get( 0 ) * 4096 + ints.get( 1 ) * 256 + ints.get( 2 ) * 16 + ints.get( 3 )) / 4;
            return new BigDecimal( value );
        }else {
            return null;
        }

    }
    
    

}


