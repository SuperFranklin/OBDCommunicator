
package Commands;


import java.util.List;

import Enums.OBDUnit;

public class RPMCommand extends Command{

    public RPMCommand(){
        super( "01", "0C" );
        parameterName = "Prêdkoœæ obrotowa";
        unit = OBDUnit.rmp;
    }

    protected float computeValue( List<Integer> ints ){
        float value;
        value = (ints.get( 0 ) * 4096 + ints.get( 1 ) * 256 + ints.get( 2 ) * 16 + ints.get( 3 )) / 4;
        return value;
    }
}
