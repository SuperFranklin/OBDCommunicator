
package Core;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.DocumentEvent.EventType;

import Utils.ByteUtils;
import Utils.FactoryService;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialReader implements SerialPortEventListener{

    private static final int DATA_AVAIABLE = 1;
    private InputStream inputStream;
    private byte[] buffer;

    public SerialReader( InputStream in ){
        this.inputStream = in;
    }

    @SuppressWarnings("restriction")
    public void serialEvent( SerialPortEvent event ){
        int data;

        if(event.getEventType() == DATA_AVAIABLE){
            buffer = new byte[ 64 ];
            try{
                int len = 0;
                while ((data = inputStream.read()) > -1){
                    buffer[ len++ ] = ( byte ) data;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public byte[] getBuffer(){
        return buffer;
    }


}
