
package Commands;


import java.util.List;

import Enums.OBDUnit;

public class EngineCoolantTemperature extends Command{

    public EngineCoolantTemperature(){
        super( "01", "05" );
        parameterName = "Temperatura silnika";
        unit = OBDUnit.celciusDegrees;
    }

    // Zwraca rzeczywist� warto�� parametru
    protected float computeValue( List<Integer> ints ){
        float value;
        value = (ints.get( 0 ) * 16 + ints.get( 1 )) - 40;
        return value;
    }
}
