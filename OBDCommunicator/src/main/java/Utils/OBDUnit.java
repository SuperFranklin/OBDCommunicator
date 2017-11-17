package Utils;

public enum OBDUnit {
    kmh, 
    Pa, 
    rmp,
    percent,
    degrees,
    celciusDegrees
    ;
     
    public static String getText(OBDUnit unit) {
        String result = null;
        
        switch( unit ) {
            case kmh :
                return "km/h";
            case Pa :
                return "Pa";
            case rmp :
                return "obr/min";
            case percent :
                return "%";
            case degrees :
                return "°";
            case celciusDegrees :
                return "°C";
        }
        
        return result;
    }
}
