package Commands;

import java.math.BigDecimal;
import java.util.List;

import Enums.OBDUnit;
import Utils.Utils;

public class IgnitionTimingCommand extends Command{

    public IgnitionTimingCommand(  ){
        super( "01" , "0E" );
        parameterName = "K¹t wyprzedzenia zap³onu";
        unit = OBDUnit.degrees;
    }

    @Override
    public BigDecimal getDecimalValue( List<Byte> bytes ){
        float value;
        Utils.removeRedundantCharacters(bytes);
        Utils.removeRequestBytes(bytes);
        List< Integer > ints = Utils.getIntArray(  bytes );
        value = (ints.get( 0 )/2) -64;
        return new BigDecimal(value);
    }

}
