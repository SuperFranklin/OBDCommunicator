
package Core;


import gnu.io.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import Commands.Command;
import Utils.Error;
import Utils.Response;
import Utils.Utils;

/**
 * Created by pc on 2017-10-01.
 */
public class SerialPortComunicator{

    @SuppressWarnings("unused")
    private Service service;
    private OutputStream outStream;
    private InputStream inStream;
    @SuppressWarnings("restriction")
    private SerialPort serialPort;
    private SerialWriter serialWriter;
    private SerialReader serialReader;
    private ScheduledExecutorService scheduler= Executors.newScheduledThreadPool( 1 );
    private File communicatorFile = new File( "communicator.txt" );
    public SerialPortComunicator(){}

    @SuppressWarnings("restriction")
    Response conncet( String portName )
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException{

        Response result= new Response();

        CommPortIdentifier portIdentifier= CommPortIdentifier.getPortIdentifier( portName );

        if(portIdentifier.isCurrentlyOwned()){
            result.addError( new Error( "Error: Port is currently in use" ) );
        }else{
            CommPort commPort= portIdentifier.open( this.getClass().getName(), 1000 );

            if(commPort instanceof SerialPort){

                serialPort= ( SerialPort ) commPort;
                serialPort.setSerialPortParams( 9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE );

                inStream= serialPort.getInputStream();
                outStream= serialPort.getOutputStream();
                serialWriter= new SerialWriter( outStream );
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
    public Response sendAndGetResponse( String command ){
        Response result= new Response();
        send( command );
        try {
        Thread.sleep( 15 );
        List<byte[]> buffers= serialReader.getBuffers();
        byte[] buffer= Utils.getBufferWithRequestData( buffers );
        serialReader.clearBuffers();
        List<Byte> data= getBufferAsList( buffer );
        result.setBytes( data );
        }catch(Exception e) {
            result.addError( new Error( e.toString() ));
        }
        
        return result;
    }
    //return result with decimal value and bytes
    public Response sendAndGetResponse( Command command, boolean writeToCommunicatorFile ){
        Response result= new Response();
        send( command.getCommunicate() );
        try {
        Thread.sleep( 3 );
        List<byte[]> buffers= serialReader.getBuffers();
        byte[] buffer= Utils.getBufferWithRequestData( buffers );
        System.out.println( Utils.getStringFromByteArray( buffer ) );
        ///TODO w chuj chujowe rozwi¹zanie
        if(Utils.getRealBufferLength( buffer ) < 4) {
            result.addError( new Error( "no data" ) );
            return result;
        }
        serialReader.clearBuffers();
        List<Byte> data= getBufferAsList( buffer );
        result.setBytes( data );
        BigDecimal decimalValue = command.getDecimalValue( data );
        if(data.size() != 0 && decimalValue != null){
            result.setDecimalValue( decimalValue );
        }else{
            result.addError( new Error( "no data" ) );
        }
        }catch(Exception e) {
            result.addError( new Error( e.toString() ));
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
    
    
    


}
