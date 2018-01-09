
package Commands;


import java.util.List;

import Enums.OBDUnit;

public class VehicleSpeedCommand extends DecValueCommand{

    public VehicleSpeedCommand(){
        super( "01", "0D" );
        parameterName = "Prêdkoœæ pojazdu";
        unit = OBDUnit.rmp;
    }

    protected float computeValue( List<Integer> ints ){
        float value;
        value = ints.get( 0 ) * 16 + ints.get( 1 );
        return value;
    }
}
