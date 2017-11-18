package Commands;

import java.math.BigDecimal;
import java.util.List;

import Enums.OBDUnit;
import Utils.Utils;

public class EngineLoadCommand extends Command{

    public EngineLoadCommand(){
        super("01" , "04");
        parameterName = "Obci¹¿enie silnika";
        unit = OBDUnit.percent;
    }
    
    public BigDecimal getDecimalValue( List<Byte> bytes ){
        float value;
        Utils.removeRedundantCharacters(bytes);
        Utils.removeRequestBytes(bytes);
        List< Integer > ints = Utils.getIntArray(  bytes );
        value = (ints.get( 0 )*100)/255;
        return new BigDecimal(value);
    }
    
}
