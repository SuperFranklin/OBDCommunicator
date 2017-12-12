package Enums;

public enum OBDUnit {
    kmh, 
    Pa, 
    rmp,
    percent,
    degrees,
    celciusDegrees,
    gramPerSecond
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
            case gramPerSecond :
                return "g/s";
        }
        
        return result;
    }
}
