
package Commands;


import java.util.List;

import Enums.OBDUnit;

public class IntakeManifoldPressureCommand extends Command{

    public IntakeManifoldPressureCommand(){
        super( "01", "0B" );
        parameterName = "Ciœnienie w kolektorze dolotowym";
        unit = OBDUnit.Pa;
    }

    protected float computeValue( List<Integer> ints ){
        float value;
        value = ints.get( 0 );
        return value;
    }
}
