
package Commands;


import java.math.BigDecimal;
import java.util.List;

import Enums.OBDUnit;
import Utils.Utils;

public class IntakeManifoldPressureCommand extends Command{

    public IntakeManifoldPressureCommand(){
        super( "01", "0B" );
        parameterName= "Ciœnienie w kolektorze dolotowym";
        unit= OBDUnit.Pa;
    }

    @Override
    public BigDecimal getDecimalValue( List<Byte> bytes ){
        float value;
        Utils.removeRedundantCharacters( bytes );
        Utils.removeRequestBytes( bytes );
        List<Integer> ints= Utils.getIntArray( bytes );
        value= ints.get( 0 );
        return new BigDecimal( value );
    }

}
