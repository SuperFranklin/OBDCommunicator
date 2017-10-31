package Commands;
import java.math.BigDecimal;
import java.util.List;

import Core.Service;
import Utils.FactoryService;
import Utils.OBDUnit;
import Utils.Response;

public abstract class Command {

    private int responseTimeLimit;
    private String communicate;
    private Service service = FactoryService.getService();
    private OBDUnit unit;
    private long startTime;
    private long currentTime;
    
    public Command(String communicate) {
        this.communicate = communicate;
        
    }
    public abstract BigDecimal getDecimalValue( List< Byte > bytes);
    

    public int getResponseTimeLimit() {
        return responseTimeLimit;
    }

    public void setResponseTimeLimit(int responseTimeLimit) {
        this.responseTimeLimit = responseTimeLimit;
    }

    public String getCommunicate() {
        return communicate +"\r";
    }

    public void setCommunicate(String communicate) {
        this.communicate = communicate;
    }

    public OBDUnit getUnit() {
        return unit;
    }

    public void setUnit(OBDUnit unit) {
        this.unit = unit;
    }

    public BigDecimal computeResult(String string) {
        // TODO Auto-generated method stub
        return new BigDecimal(900);
    }
    
    
    
    
    
}
