package Utils;

public enum OBDUnit {
    kmh, 
    Pa, 
    rmp;
     
    public String getText(OBDUnit unit) {
        String result = null;
        
        switch( unit ) {
            case kmh :
                return "km/h";
            case Pa :
                return "Pa";
            case rmp :
                return "obr/min";
        }
        
        return result;
    }
}
