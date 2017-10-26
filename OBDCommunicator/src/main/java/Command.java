import java.math.BigDecimal;

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

    public int getResponseTimeLimit() {
        return responseTimeLimit;
    }

    public void setResponseTimeLimit(int responseTimeLimit) {
        this.responseTimeLimit = responseTimeLimit;
    }

    public String getCommunicate() {
        return communicate;
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
