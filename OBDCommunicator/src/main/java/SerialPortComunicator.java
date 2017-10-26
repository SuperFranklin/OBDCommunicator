import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pc on 2017-10-01.
 */
public class SerialPortComunicator {

    @SuppressWarnings("unused")
    private Service service;
    private OutputStream outStream;
    private InputStream inStream;
    @SuppressWarnings("restriction")
    private SerialPort serialPort;
    private SerialWriter serialWriter;
    private SerialReader serialReader;
    // TODO trzeba bêdzie scheduler.shutdown wywo³aæ przy zamkniêciu jFrame
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SerialPortComunicator(Service service) {
        this.service = service;
    }

    @SuppressWarnings("restriction")
    void conncet(String portName)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {

        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
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
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    public void send(String data) {
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

    public ObdResponse sendAndGetResponse(Command command) {
        ObdResponse result = null;
        send(command.getCommunicate());
        scheduler.schedule( new CommandScheduler( command ), 100, TimeUnit.MILLISECONDS );

        return result;
    }

    public List<Byte> getBufferAsList(byte[] buffer) {
        List<Byte> result = new ArrayList<Byte>();
        for (int i = 0; i < buffer.length; i++) {
            result.add(new Byte(buffer[i]));
        }
        return result;
    }

    class CommandScheduler implements Runnable {
        ObdResponse response;
        Command command;

        public CommandScheduler(Command command) {
            this.command = command;
        }

        public void run() {
            byte[] buffer = serialReader.getBuffer();
            List<Byte> data = getBufferAsList(buffer);
            int length = buffer.length;
            if (data.size() != 0) {
                //BigDecimal result = command.computeResult(new String(buffer, 0, length));
                System.out.println(" result testowy = " + new String(buffer, 0, length) );
                
            } else {
                response.addError(new OBDError("no data"));
            }

        }
    }

}
