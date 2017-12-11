
package Commands;


import java.math.BigDecimal;
import java.util.List;

import Enums.OBDUnit;
import Utils.ByteUtils;

public class EngineLoadCommand extends Command{

    public EngineLoadCommand(){
        super( "01", "04" );
        parameterName = "Obci¹¿enie silnika";
        unit = OBDUnit.percent;
    }

    @Override
    protected float computeValue( List<Integer> ints ){
        float value;
        value = (ints.get( 0 ) * 100) / 255;
        return value;
    }
}
