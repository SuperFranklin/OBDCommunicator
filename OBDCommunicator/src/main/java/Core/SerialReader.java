
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
        buffer= new byte[ 24 ];
        if(event.getEventType() == 1){

            try{
                int len= 0;
                while ((data= in.read()) > -1){
                    if(data == '\n'){
                        break;
                    }
                    buffer[ len++ ]= ( byte ) data;
                }
                buffers.add( buffer );
                service.setResponseToTerminal( new String( buffer, 0, len ));
                
            }catch (IOException e){
                System.out.println( " System reader error : " + e );
            }
        }
    }

    public List<byte[]> getBuffers(){
        return buffers;
    }

    public void clearBuffers(){
        buffers.clear();
    }

}
