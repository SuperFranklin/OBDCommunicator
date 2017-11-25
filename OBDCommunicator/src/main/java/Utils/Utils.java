
package Utils;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils{

    public static List<Byte> removeRequestBytes( List<Byte> list ){
        list.remove( 0 );
        list.remove( 0 );
        list.remove( 0 );
        list.remove( 0 );

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
            if (!( b > 47 && b<58 || b>64 && b< 71)) {
                it.remove();
            }
        }

        return bytes;
    }

    public static byte[] getBufferWithRequestData( List<byte[]> buffers ){

        for(byte[] buffer : buffers){
            //TODO tutaj tez œrednie rozwi¹zanie, powinno to byæ jakos skuteczniej rozwi¹zane, bo czasami
            //przechodzi pusty bufor
            if(getRealBufferLength( buffer ) > 7 && bufferIsNoData( buffer ) && !Utils.bufferContainNegativByte( buffer ) ){
                return buffer;
            }

        }
        // TODO œrednie rozwi¹zanie
        return new byte[ 1 ];
    }
    public static boolean bufferIsNoData( byte[] buffer) {
        if (buffer[0] == 78 && buffer[1] ==79) {
            return false;
        }
            return true;
        
    }
    public static boolean bufferContainNegativByte( byte[] buffer) {
        for ( byte b : buffer) {
            if( b < 0) {
                return true;
            }
        }
        return false;
    }
    
    
    public static List< Integer > getIntArray( List<Byte> list ) {
        List< Integer > result = new ArrayList<Integer>();
        for( Byte b : list) {
            int i = byteToInt( b );
            result.add(  new Integer(  i ) );

        }
        return result;
    }
   
    public static int byteToInt(byte b) {
        int result = 0;
        
        if(b>47 && b < 58) {
            result = b - 48;
        }
        if( b>64 && b<71) {
            result = b - 55;
        }
        
        return result;
    }

    public static void removeSpaces( List<Byte> bytes ){

        Iterator<Byte> it= bytes.iterator();
        while (it.hasNext()){
            Byte b= it.next();
            if ( b == 32 ) {
                it.remove();
            }
        }
        
    }
}
