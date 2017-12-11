
package Commands;


import java.util.List;

import Enums.OBDUnit;

public class ThrottlePositionCommand extends Command{

    public ThrottlePositionCommand(){
        super( "01", "11" );
        unit = OBDUnit.percent;
        parameterName = "Po³o¿enie przepustnicy";
    }

    protected float computeValue( List<Integer> ints ){
        float value;
        value = (ints.get( 0 ) * 16 + ints.get( 1 )) * 100 / 255;
        return value;
    }
}
