
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

import Commands.DecValueCommand;
import DAO.DTCUtil;
import Exceptions.SerialPortException;
import Utils.ByteUtils;
import Utils.Error;
import Utils.Response;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialPortComunicator {

    private ServiceImpl service;
    private OutputStream outStream;
    private InputStream inStream;
    private SerialPort serialPort;
    private SerialReader serialReader;
    private File communicatorFile = new File( "communicator.txt" );
    private CommPortIdentifier portIdentifier;
    private CommPort commPort = null;
    private final static int TIMEOUT = 2000;

    public SerialPortComunicator( ServiceImpl service ){
        this.service = service;
    }

    public Response conncet( String portName ) throws Exception{
        Response result = new Response();

        portIdentifier = CommPortIdentifier.getPortIdentifier( portName );
        if(portIdentifier.isCurrentlyOwned()){
            result.addError( new Error( Message.PORT_IS_CURRENTLY_USED ) );
        }else{
            try{
                initConnection();
            }catch (Exception ex){
                result.addError( ex.getMessage() );
            }
        }
        
        if(!result.hasErrors()) {
            sleep( 500 );
            boolean openOBDConnectionSuccesfully;
            openOBDConnectionSuccesfully = initScanToolConnection();
            if(!openOBDConnectionSuccesfully) result.addError( "Nie uda³o siê nawi¹zaæ po³¹czenia z pojazdem" );
            
        }
        return result;
    }
    
    private boolean initScanToolConnection() {
        Response response;
        for(int i=0;i<10;i++) {
            response = sendAndGetResponse( "0100" );
            if(!response.getBytes().isEmpty()) return true;
            sleep( 100 );
        }
        return false;
    }

    private void initConnection() throws Exception{
        commPort = portIdentifier.open( this.getClass().getName(), TIMEOUT );
        if(commPort instanceof SerialPort){
            serialPort = ( SerialPort ) commPort;
            setSerialPortParameters();
            initStreams();
            addEventListener();
        }else{
            throw new SerialPortException( Message.PORT_IS_NOT_SERIAL );
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

    public Response sendCommunicate( String msg ){
        Response result = new Response();
        try {
        outStream.write( msg.getBytes() );
        outStream.flush();
        }catch (Exception e) {
            result.addError( e.toString() );
        }
        return result;
    }

    @SuppressWarnings("restriction")
    public Response closePort(){
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
            service.setConnectionPanelParameters( "", Message.DISCONNECTED, "" );
        }else{
            response.addError( new Error( Message.NO_CONNECTION ) );
        }
        return response;
    }

    // return result with bytes
    public synchronized Response sendAndGetResponse( String command ){
        Response result = new Response();
        try{
            sendCommunicate( command + "\r" );
            sleep( 500 );
            byte[] buffer = serialReader.getBuffer();
            List<Byte> data = getBufferAsList( buffer );
            result.setBytes( data );
        }catch (Exception e){
            result.addError( new Error( e.toString() ) );
        }

        return result;
    }

    public Map<String, String> getDTCMap(){

        Map<String, String> map = new HashMap<>();
        Response response = sendAndGetResponse( "03" );
        List<Byte> bytes = response.getBytes();
        bytes.remove( 0 );
        bytes.remove( 0 );
        bytes = ByteUtils.removeRedundantCharacters( bytes );
        int modulo;
        for(int i = 0; i < bytes.size(); i++){
            modulo = i % 14;
            if(modulo == 0 || modulo == 1) bytes.set( i, ( byte ) 0 );

        }
        Iterator<Byte> it = bytes.iterator();
        while (it.hasNext()){
            Byte b = it.next();
            if(b.byteValue() == ( byte ) 0) it.remove();
        }
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
            if(code.length==4 && !codeTxt.equals( "0000" )) {
                map.put( codeTxt, DTCUtil.getTroubleCodeDesc( codeTxt ) );
            }
        }
        
        return map;
    }

    // return result with decimal value and bytes
    public Response sendAndGetResponse( DecValueCommand command, boolean writeToCommunicatorFile ){
        Response result = new Response();
        try{
            sendCommunicate( command.getCommunicate() );
            sleep( 200 );
            byte[] buffer = serialReader.getBuffer();
            List<Byte> data = getBufferAsList( buffer );
            result.setBytes( data );
            BigDecimal decimalValue = command.parseAndGetDecimalValue( data );
            if(decimalValue != null){
                result.setDecimalValue( decimalValue );
            }else{
                result.addError( new Error( Message.NO_DATA ) );
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


    public SerialPort getSerialPort(){
        return serialPort;
    }

 

}
