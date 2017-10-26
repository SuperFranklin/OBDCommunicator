import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObdResponse {

    private List<OBDError> errors = new ArrayList<OBDError>(); 
    private BigDecimal value;
    public ObdResponse() {
        
    }
    
    
    public boolean hasErrors() {
        if( !errors.isEmpty() ) {
            return true;
        }
        return false;
    }
    
    public List<OBDError> getErrors(){
        return errors;
    }


    public BigDecimal getValue() {
        return value;
    }
    public void addError(OBDError arg) {
        errors.add(arg);
    }
    


    public void setErrors(List<OBDError> errors) {
        this.errors = errors;
    }


    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
    
   
}
