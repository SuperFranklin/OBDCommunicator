
package Commands;


import java.util.List;

import Enums.OBDUnit;

public class IgnitionTimingCommand extends DecValueCommand{

    public IgnitionTimingCommand(){
        super( "01", "0E" );
        parameterName = "K¹t wyprzedzenia zap³onu";
        unit = OBDUnit.degrees;
    }

    protected float computeValue( List<Integer> ints ){
        float value;
        value = (ints.get( 0 ) / 2) - 64;
        return value;
    }
}
