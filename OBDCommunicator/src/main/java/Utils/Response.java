package Utils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Response {

    private List<Error> errors = new ArrayList<Error>(); 
    private List<Byte> bytes;
    private BigDecimal decimalValue;
    public Response() {
        
    }
    
    
    public boolean hasErrors() {
        if( !errors.isEmpty() ) {
            return true;
        }
        return false;
    }
    
    public List<Error> getErrors(){
        return errors;
    }


    public List<Byte> getBytes() {
        return bytes;
    }
    public void addError(Error arg) {
        errors.add(arg);
    }
    


    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }


    public void setBytes(List<Byte> bytes) {
        this.bytes = bytes;
    }
    
    
    public BigDecimal getDecimalValue() {
        return decimalValue;
    }


    public void setDecimalValue(BigDecimal decimalValue) {
        this.decimalValue = decimalValue;
    }


    public String getErrorAsString() {
        StringBuilder result = new StringBuilder();
        for( Error error : errors) {
            result.append( error.toString());
            result.append("\n");
        }
        
        return result.toString();
    }
    
    
   
}
