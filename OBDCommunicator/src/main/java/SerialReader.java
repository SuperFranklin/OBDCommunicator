import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.DisplayMode;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.DocumentEvent.EventType;
import javax.swing.plaf.synth.SynthSeparatorUI;

public class SerialReader implements SerialPortEventListener {

    private Service service;
    private InputStream in;
    private byte[] buffer = new byte[24];

    public SerialReader(InputStream in) {
        this.in = in;
        service = FactoryService.getService();
    }

    @SuppressWarnings("restriction")
    public void serialEvent(SerialPortEvent event) {
        int data;
        if (event.getEventType() == 1) {

            try {
                int len = 0;
                while ((data = in.read()) > -1) {
                    if (data == '\n') {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                service.setResponseToTerminal(new String(buffer, 0, len));

            } catch (IOException e) {
                System.out.println(" System reader error : " + e);
            }
        }
    }

    public byte[] getBuffer() {
        return buffer;
    }

}
