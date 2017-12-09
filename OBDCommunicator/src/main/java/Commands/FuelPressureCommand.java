package Commands;

import java.math.BigDecimal;
import java.util.List;

public class FuelPressureCommand extends Command{

    public FuelPressureCommand(  ){
        super( "01", "0A" );
    }

    @Override
    public BigDecimal getDecimalValue( List<Byte> bytes ){
        // TODO Auto-generated method stub
        return null;
    }
    

}
