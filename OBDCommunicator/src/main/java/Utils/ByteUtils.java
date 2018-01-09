
package Utils;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ByteUtils{
    
    private final static int REQUEST_BYTES =8;

    public static List<Byte> removeRequestBytes( List<Byte> list){
        
        for(int i=0; i< REQUEST_BYTES; i++) {
            list.remove( 0 );
        }

        return list;
    }

    public static int getRealBufferLength( byte[] buffer ){
        int i= 0;
        for(i= 0; i < buffer.length; i++){
            if(buffer[ i ] == 0){
                break;
            }
        }
        return i;
    }

    public static List<Byte> removeRedundantCharacters( List<Byte> bytes ){

        Iterator<Byte> it= bytes.iterator();

        while (it.hasNext()){
            Byte b= it.next();
            if(!(b > 47 && b < 58 || b > 64 && b < 71)){
                it.remove();
            }
        }

        return bytes;
    }

    public static byte[] getBufferWithRequestData( List<byte[]> buffers ){

        for(byte[] buffer : buffers){
            // TODO tutaj tez œrednie rozwi¹zanie, powinno to byæ jakos skuteczniej rozwi¹zane, bo czasami
            // przechodzi pusty bufor
            if(getRealBufferLength( buffer ) > 7 && bufferIsNoData( buffer )
                    && !ByteUtils.bufferContainNegativByte( buffer )){
                return buffer;
            }

        }
        // TODO œrednie rozwi¹zanie
        return new byte[ 1 ];
    }

    public static boolean bufferIsNoData( byte[] buffer ){
        if(buffer[ 0 ] == 78 && buffer[ 1 ] == 79){
            return false;
        }
        return true;

    }

    public static boolean bufferContainNegativByte( byte[] buffer ){
        for(byte b : buffer){
            if(b < 0){
                return true;
            }
        }
        return false;
    }

    public static List<Integer> getNumberArray( List<Byte> list ){
        List<Integer> result= new ArrayList<Integer>();
        for(Byte b : list){
            int i= byteToInt( b );
            result.add( new Integer( i ) );

        }
        return result;
    }

    public static int byteToInt( byte b ){
        int result= 0;

        if(b > 47 && b < 58){
            result= b - 48;
        }
        if(b > 64 && b < 71){
            result= b - 55;
        }

        return result;
    }

    public static void removeSpaces( List<Byte> bytes ){

        Iterator<Byte> it= bytes.iterator();
        while (it.hasNext()){
            Byte b= it.next();
            if(b == 32){
                it.remove();
            }
        }
    }

    public static byte[] getPrimitivArrayFromBytes( List<Byte> bytes ){

        byte[] array= new byte[ bytes.size() ];

        for(int i= 0; i < bytes.size(); i++){

            array[ i ]= bytes.get( i );
        }

        return array;
    }
    
    public static String getStringFromByteArray(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<bytes.length;i++) {
            sb.append( bytes[i] );
        }
        return sb.toString();
    }

    public static String getStringFromBytes( List<Byte> bytes ){

        byte[] array= getPrimitivArrayFromBytes( bytes );
        return new String( array );

    }
    
    public static String byteToBites(byte b) {
        String s =("0000000" + Integer.toBinaryString(0xFF & b)).replaceAll(".*(.{8})$", "$1");
        return s;
    }
    public static boolean[] byteArray2BitArray(byte[] bytes) {
        boolean[] bits = new boolean[bytes.length * 8];
        for (int i = 0; i < bytes.length * 8; i++) {
          if ((bytes[i / 8] & (1 << (7 - (i % 8)))) > 0)
            bits[i] = true;
        }
        return bits;
      }
    
    public static List<Boolean> integersToBinary(List<Integer> integers) {
        List<Boolean> result = new ArrayList<>();
        for(Integer i : integers) {
            int intValue = i.intValue();
            boolean[] binary = toBinary( intValue, 4);
            for(boolean b : binary) {
                result.add( new Boolean( b ) );
            }
        }
        return result;
    }
    
    private static boolean[] toBinary(int number, int base) {
        final boolean[] ret = new boolean[base];
        for (int i = 0; i < base; i++) {
            ret[base - 1 - i] = (1 << i & number) != 0;
        }
        return ret;
    }
    
}
