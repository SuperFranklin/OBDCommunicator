
package Core;


import gnu.io.*;

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
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SerialPortComunicator(Service service) {
        this.service = service;
    }

    @SuppressWarnings("restriction")
    Response conncet( String portName )
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {

        Response result = new Response();

        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

        if (portIdentifier.isCurrentlyOwned()) {
            result.addError(new Error("Error: Port is currently in use"));
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 1000);

            if (commPort instanceof SerialPort) {

                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                inStream = serialPort.getInputStream();
                outStream = serialPort.getOutputStream();
                serialWriter = new SerialWriter(outStream);
                serialReader = new SerialReader(inStream);

                try {
                    serialPort.notifyOnDataAvailable(true);
                    serialPort.addEventListener(serialReader);
                } catch (TooManyListenersException e) {
                    e.printStackTrace();
                }
                System.out.println("connected");

            } else {
                result.addError(new Error(" Port nie jest portem szeregowym"));
            }
        }
        return result;
    }

    public void send( String data ) {
        try {
            outStream.write(data.getBytes());
            outStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("restriction")
    public void close() {
        try {
            serialPort.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (serialPort != null) {
            serialPort.close();
            inStream = null;
            outStream = null;
        }
    }

    public Response sendAndGetResponse( Command command ) {
        Response result = new Response();
        send(command.getCommunicate());
        ScheduledFuture < Response > future =
                scheduler.schedule(new CommandScheduler(command), 20, TimeUnit.MILLISECONDS);
        try {
            result = future.get();
        } catch (InterruptedException e) {
            result.addError(new Error(e.toString()));
        } catch (ExecutionException e) {
            result.addError(new Error(e.toString()));
        }
        return result;
    }

    public List < Byte > getBufferAsList( byte [ ] buffer ) {
        List < Byte > result = new ArrayList < Byte >();
        int realLength = Utils.getRealBufferLength(buffer);
        for (int i = 0; i < realLength; i++) {
            result.add(new Byte(buffer[i]));
        }
        return result;
    }

    class CommandScheduler implements Callable < Response >{
        Response response = new Response();
        Command command;

        public CommandScheduler(Command command) {
            this.command = command;
        }

        public Response call() throws Exception {
            List<byte [ ]> buffers = serialReader.getBuffers();
            byte[] buffer = Utils.getBufferWithRequestData(  buffers );
            List < Byte > data = getBufferAsList(buffer);
            int length = buffer.length;
            if (data.size() != 0) {
                response.setDecimalValue(command.getDecimalValue(data));
            } else {
                response.addError(new Error("no data"));
            }
            return response;
        }
    }

}
