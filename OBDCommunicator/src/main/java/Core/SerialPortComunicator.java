
package Core;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import Commands.Command;
import DAO.DTCUtil;
import Exceptions.SerialPortException;
import Utils.ByteUtils;
import Utils.Error;
import Utils.FactoryService;
import Utils.Response;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialPortComunicator{

    private Service service;
    private OutputStream outStream;
    private InputStream inStream;
    private SerialPort serialPort;
    private SerialReader serialReader;
    private File communicatorFile = new File( "communicator.txt" );
    private CommPortIdentifier portIdentifier;
    private CommPort commPort = null;

    public SerialPortComunicator(){}

    public Response conncet( String portName ) throws Exception{
        Response result = new Response();
        service = FactoryService.getService();

        portIdentifier = CommPortIdentifier.getPortIdentifier( portName );
        if(portIdentifier.isCurrentlyOwned()){
            result.addError( new Error( "Port is currently in use" ) );
        }else{
            try{
                initConnection();
            }catch (Exception ex){
                result.addError( ex.getMessage() );
            }
        }
        return result;
    }

    private void initConnection() throws Exception{
        commPort = portIdentifier.open( this.getClass().getName(), 8 );
        if(commPort instanceof SerialPort){
            serialPort = ( SerialPort ) commPort;
            setSerialPortParameters();
            initStreams();
            addEventListener();
        }else{
            throw new SerialPortException( "Port nie jest portem szeregowym" );
        }
    }

    private void setSerialPortParameters() throws UnsupportedCommOperationException{
        serialPort.setSerialPortParams( Core.Parameters.boudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE );
        serialPort.notifyOnDataAvailable( true );
    }

    private void addEventListener() throws TooManyListenersException{
        serialPort.addEventListener( serialReader );
    }

    private void initStreams() throws Exception{
        inStream = serialPort.getInputStream();
        outStream = serialPort.getOutputStream();
        serialReader = new SerialReader( inStream );
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
    public Response close(){
        Response response = new Response();
        if(serialPort != null){
            try{
                serialPort.getOutputStream().flush();
            }catch (IOException e){
                response.addError( new Error( e.toString() ) );
            }
            serialPort.close();
            inStream = null;
            outStream = null;
            service.setConnectionPanelParameters( "", "Disconnected", "" );
        }else{
            response.addError( new Error( "Brak po³¹czenia z portem szeregowym" ) );
        }
        return response;
    }

    // return result with bytes
    public synchronized Response sendAndGetResponse( String command ){
        Response result = new Response();
        send( command + "\r" );
        sleep( 500 );
        try{
            byte[] buffer = serialReader.getBuffer();
            serialReader.clearBuffers();
            List<Byte> data = getBufferAsList( buffer );
            result.setBytes( data );
        }catch (Exception e){
            result.addError( new Error( e.toString() ) );
        }

        return result;
    }

    public Map<String, String> getDTCMap(){

        Map<String, String> map = new HashMap<>();
        List<String> result = new ArrayList();
        Response response = sendAndGetResponse( "03" );
        List<Byte> bytes = response.getBytes();
        bytes.remove( 0 );
        bytes.remove( 0 );
        bytes = ByteUtils.removeRedundantCharacters( bytes );
        int modulo;
        System.out.println( ByteUtils.getStringFromBytes( bytes ) );
        for(int i = 0; i < bytes.size(); i++){
            modulo = i % 14;
            if(modulo == 0 || modulo == 1) bytes.set( i, ( byte ) 0 );

        }
        Iterator<Byte> it = bytes.iterator();
        while (it.hasNext()){
            Byte b = it.next();
            if(b.byteValue() == ( byte ) 0) it.remove();
        }
        System.out.println( ByteUtils.getStringFromBytes( bytes ) );
        byte[] code = new byte[ 4 ];
        for(int x = 0; x <= bytes.size() / 4; x++){
            for(int i = 0; i < 4; i++){
                code[ i ] = bytes.get( i ).byteValue();
            }
            bytes.remove( 0 );
            bytes.remove( 0 );
            bytes.remove( 0 );
            bytes.remove( 0 );

            String codeTxt = new String( code );
            map.put( codeTxt, DTCUtil.getTroubleCodeDesc( codeTxt ) );
        }
        return map;
    }

    // return result with decimal value and bytes
    public Response sendAndGetResponse( Command command, boolean writeToCommunicatorFile ){
        Response result = new Response();
        send( command.getCommunicate() );
        sleep( 200 );
        try{
            byte[] buffer = serialReader.getBuffer();
            System.out.println( ByteUtils.getStringFromByteArray( buffer ) );
            /// TODO poprawiæ, bo to bardzo prymitywne i tylko do testów
            if(ByteUtils.getRealBufferLength( buffer ) < 4){
                result.addError( new Error( "no data" ) );
                return result;
            }
            List<Byte> data = getBufferAsList( buffer );
            result.setBytes( data );
            BigDecimal decimalValue = command.parseAndGetDecimalValue( data );
            if(decimalValue != null){
                result.setDecimalValue( decimalValue );
            }else{
                result.addError( new Error( "no data" ) );
            }
        }catch (Exception e){
            result.addError( new Error( e.toString() ) );
        }

        return result;
    }

    public List<Byte> getBufferAsList( byte[] buffer ){
        List<Byte> result = new ArrayList<Byte>();
        int realLength = ByteUtils.getRealBufferLength( buffer );
        for(int i = 0; i < realLength; i++){
            result.add( new Byte( buffer[ i ] ) );
        }
        return result;
    }

    private void sleep( long mills ){
        try{
            Thread.sleep( mills );
        }catch (InterruptedException e){}
    }

    public SerialPort getConnectedSerialPort(){
        return serialPort;
    }

    public SerialPort getSerialPort(){
        return serialPort;
    }
    
    

}
