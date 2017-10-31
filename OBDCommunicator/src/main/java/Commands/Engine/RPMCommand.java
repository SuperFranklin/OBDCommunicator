
package Commands.Engine;


import java.math.BigDecimal;
import java.nio.Buffer;
import java.util.List;

import Commands.Command;
import Utils.Response;
import Utils.Utils;

public class RPMCommand extends Command{

    private final String parameterName = " Prêdkoœæ obrotowa";
    public RPMCommand(String communicate) {
        super(communicate);
    }

    public BigDecimal getDecimalValue( List < Byte > bytes ) {
        float value;
        Utils.removeRedundantCharacters(bytes);
        Utils.removeRequestBytes(bytes);
        List< Integer > ints = Utils.getIntArray(  bytes );
        value = (ints.get( 0 )* 4096 + ints.get( 1 )* 256 + ints.get( 2 ) * 16 + ints.get( 3 )) / 4;
        System.out.println( "value = " + value );
        return new BigDecimal(value);
    }
    
    public String getParameterName() {
        return parameterName;
    }

}
