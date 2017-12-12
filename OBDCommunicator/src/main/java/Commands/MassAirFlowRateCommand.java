
package Commands;


import java.util.List;

import Enums.OBDUnit;

public class MassAirFlowRateCommand extends Command{

    public MassAirFlowRateCommand(){
        super( "01", "10" );
        parameterName = "Natê¿enie przep³ywu powietrza";
        unit = OBDUnit.gramPerSecond;
    }

    protected float computeValue( List<Integer> ints ){
        float value;
        value = (ints.get( 0 ) * 4096 + ints.get( 1 ) * 256 + ints.get( 2 ) * 16 + ints.get( 3 )) / 100;
        return value;
    }
}
