
package Core;


import java.io.IOException;
import java.io.OutputStream;

public class SerialWriter implements Runnable{

    private final static int END_STREAM = -1;
    private OutputStream outputStream;

    public SerialWriter( OutputStream out ){
        outputStream = out;
    }

    public void run(){
        try{
            int c = 0;
            while ((c = System.in.read()) > END_STREAM){
                this.outputStream.write( c );
                outputStream.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
