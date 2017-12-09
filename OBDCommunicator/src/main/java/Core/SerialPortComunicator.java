
package Core;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import Commands.Command;
import Utils.Error;
import Utils.FactoryService;
import Utils.Response;
import Utils.Utils;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * Created by pc on 2017-10-01.
 */
public class SerialPortComunicator{

    @SuppressWarnings("unused")
    private Service service ;
    private OutputStream outStream;
    private InputStream inStream;
    @SuppressWarnings("restriction")
    private SerialPort serialPort;
    //private SerialWriter serialWriter;
    private SerialReader serialReader;
    private ScheduledExecutorService scheduler= Executors.newScheduledThreadPool( 1 );
    private File communicatorFile = new File( "communicator.txt" );
    private int boudRate = 9600;
    
    
    public SerialPortComunicator(){}

    
    @SuppressWarnings("restriction")
    Response conncet( String portName )
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException{

        service = FactoryService.getService();
        Response result= new Response();

        CommPortIdentifier portIdentifier= CommPortIdentifier.getPortIdentifier( portName );

        if(portIdentifier.isCurrentlyOwned()){
            result.addError( new Error( "Error: Port is currently in use" ) );
        }else{
            CommPort commPort= portIdentifier.open( this.getClass().getName(), 8 );

            if(commPort instanceof SerialPort){

                serialPort= ( SerialPort ) commPort;
                serialPort.setSerialPortParams( 38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE );

                inStream= serialPort.getInputStream();
                outStream= serialPort.getOutputStream();
                //serialWriter= new SerialWriter( outStream );
                serialReader= new SerialReader( inStream );

                try{
                    serialPort.notifyOnDataAvailable( true );
                    serialPort.addEventListener( serialReader );
                }catch (TooManyListenersException e){
                    e.printStackTrace();
                }
                System.out.println( "connected" );

            }else{
                result.addError( new Error( " Port nie jest portem szeregowym" ) );
            }
        }
        service.setConnectionPanelParameters( portName, "connected", Integer.toString( boudRate ) );
        return result;
    }

    public void send( String data ){
        try{
            outStream.write( data.getBytes() );
            outStream.flush();
            

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @SuppressWarnings("restriction")
    public void close(){
        try{
            serialPort.getOutputStream().flush();
        }catch (IOException e){
            e.printStackTrace();
        }

        if(serialPort != null){
            serialPort.close();
            inStream= null;
            outStream= null;
        }
    }
    //return result with bytes
    public synchronized Response sendAndGetResponse( String command ){
        Response result= new Response();
        send( command+"\r" );
        try {
        Thread.sleep( 80 );
        byte[] buffer= serialReader.getBuffer();
        serialReader.clearBuffers();
        List<Byte> data= getBufferAsList( buffer );
        result.setBytes( data );
        }catch(Exception e) {
            result.addError( new Error( e.toString() ));
        }
        
        return result;
    }
    
    public List<String> getListOfTroubleCodes(){
        List<String> result = new ArrayList();
        Response response = sendAndGetResponse( "03" );
        List<Byte> bytes = response.getBytes();
        int modulo;
        System.out.println( Utils.getStringFromBytes( bytes ) );
        for(int i=0;i<bytes.size();i++) {
            modulo = i%8;
            if(modulo==0 || modulo==1) bytes.set( i, new Byte( "0" ));
            
        }
        Iterator<Byte> it = bytes.iterator();
        while(it.hasNext()) {
            Byte b = it.next();
            if(b.byteValue() == 48) it.remove();
        }
        System.out.println( Utils.getStringFromBytes( bytes ) );
        
        
        return result;
    }
    //return result with decimal value and bytes
    public Response sendAndGetResponse( Command command, boolean writeToCommunicatorFile ){
        Response result= new Response();
        send( command.getCommunicate() );
        try {
        Thread.sleep( 200 );
        //List<byte[]> buffers= serialReader.getBuffers();
        byte[] buffer= serialReader.getBuffer();
        System.out.println( Utils.getStringFromByteArray( buffer ) );
        ///TODO w chuj chujowe rozwi¹zanie
        if(Utils.getRealBufferLength( buffer ) < 4) {
            result.addError( new Error( "no data" ) );
            return result;
        }
        //serialReader.clearBuffers();
        List<Byte> data= getBufferAsList( buffer );
        result.setBytes( data );
        BigDecimal decimalValue = command.getDecimalValue( data );
        //System.out.println( decimalValue );
        if(decimalValue != null){
            result.setDecimalValue( decimalValue );
        }else{
            result.addError( new Error( "no data" ) );
        }
        }catch(Exception e) {
            //result.addError( new Error( e.prin ));
            System.out.print( "&"+System.currentTimeMillis() +" ## " );
            e.printStackTrace();
        }
        
        return result;
    }

    public List<Byte> getBufferAsList( byte[] buffer ){
        List<Byte> result= new ArrayList<Byte>();
        int realLength= Utils.getRealBufferLength( buffer );
        for(int i= 0; i < realLength; i++){
            result.add( new Byte( buffer[ i ] ) );
        }
        return result;
    }
    
    public SerialPort getConnectedSerialPort() {
        return serialPort;
    }
    


}
