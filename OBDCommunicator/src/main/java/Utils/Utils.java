
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
            if(b.equals( new Byte( ( byte ) 32 ) )){
                it.remove();
            }
            if(b.equals( new Byte( ( byte ) 62 ) )){
                it.remove();
            }
            if(b.equals( new Byte( ( byte ) 13 ) )){
                it.remove();
            }

        }

        return bytes;
    }

    public static byte[] getBufferWithRequestData( List<byte[]> buffers ){

        for(byte[] buffer : buffers){
            if(getRealBufferLength( buffer ) >= 8){
                return buffer;
            }

        }
        // TODO œrednie rozwi¹zanie
        return new byte[ 1 ];
    }
    
    public static List< Integer > getIntArray( List<Byte> list ) {
        List< Integer > result = new ArrayList<Integer>();
        for( Byte b : list) {
            char c = (char) b.byteValue();
            int i = Character.getNumericValue(  c );
            result.add(  new Integer(  i ) );
        }
        return result;
    }
}
