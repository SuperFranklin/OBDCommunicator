
package Core;


import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.DisplayMode;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.DocumentEvent.EventType;
import javax.swing.plaf.synth.SynthSeparatorUI;

import Utils.FactoryService;
import Utils.ByteUtils;

public class SerialReader implements SerialPortEventListener{

    private Service service;
    private InputStream in;
    private byte[] buffer;
    private List<byte[]> buffers= new ArrayList<byte[]>();

    public SerialReader( InputStream in ){
        this.in= in;
        service= FactoryService.getService();
    }

    @SuppressWarnings("restriction")
    public void serialEvent( SerialPortEvent event ){
        int data;
        
        //if(event.getEventType() == 1){
            System.out.println("&"+System.currentTimeMillis() +" ## "+ event.toString() );
            buffer= new byte[ 64 ];    
            try{
                int len= 0;
                while ((data= in.read()) > -1){
                    /*if(data == '\n'){
                       // break;
                    }*/
                    
                    buffer[ len++ ]= ( byte ) data;
                }
                System.out.println("&"+System.currentTimeMillis() +" ## "+  ByteUtils.getStringFromByteArray( buffer ) );
                
            }catch (IOException e){
                System.out.println("&"+System.currentTimeMillis() +" ## "+  " System reader error : " + e );
            }
        //}
    }
    
    private String bufferToString( byte[] buffer) {
        StringBuilder builder = new StringBuilder();
        for( byte b : buffer) {
            builder.append( b );
        }
        return builder.toString();
    }

    public List<byte[]> getBuffers(){
        return buffers;
    }
    public byte[] getBuffer() {
        return buffer;
    }

    public void clearBuffers(){
        buffers.clear();
    }

}
