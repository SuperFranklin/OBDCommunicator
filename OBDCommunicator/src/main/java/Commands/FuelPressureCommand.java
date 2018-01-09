package Commands;

import java.math.BigDecimal;
import java.util.List;

public class FuelPressureCommand extends DecValueCommand{

    public FuelPressureCommand(  ){
        super( "01", "0A" );
    }

    @Override
    protected float computeValue( List<Integer> ints ){
        // TODO Auto-generated method stub
        return 0;
    }
}
