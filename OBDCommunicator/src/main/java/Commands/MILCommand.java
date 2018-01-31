package Commands;

import java.util.List;

import Core.SerialPortComunicator;
import Utils.ByteUtils;
import Utils.FactoryService;
import Utils.Response;

public class MILCommand{

    private SerialPortComunicator portComunicator = FactoryService.getSerialPortComunicator();
    private String SID;
    private String PID;
    private boolean lighted = false;
    private int numberOfDetectedError =0;
    
    
    
    public MILCommand() {
        this.SID = "01";
        this.PID ="01";
        getInfo();
        
    }

    public void getInfo() {
        Response response = portComunicator.sendAndGetResponse( SID + PID );
        
        List<Byte> bytes = response.getBytes();
        ByteUtils.removeRedundantCharacters( bytes );
        ByteUtils.removeRequestBytes( bytes );
        List<Integer> numbers = ByteUtils.getNumberArray( bytes );
        List<Boolean> binary =ByteUtils.integersToBinary( numbers );
        
        if(binary.get( 0 )== true) lighted= true;
        numberOfDetectedError = numbers.get( 1 );
    }
    
    public boolean milIsLighted() {
        return lighted;
    }
    public int getNumberOfDetectedError() {
        return numberOfDetectedError;
    }
    
}
