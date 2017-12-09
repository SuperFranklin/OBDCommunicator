package Core;
import java.io.IOException;
import java.io.OutputStream;

public class SerialWriter implements Runnable {
    private OutputStream outputStream;

    public SerialWriter(OutputStream out) {
        outputStream = out;
    }

    public void run() {
        try {
            int c = 0;
            while ((c = System.in.read()) > -1) {
                this.outputStream.write(c);
                outputStream.flush();
            }
        } catch (IOException e) {
            System.out.println(" Serial Writer error : " + e);
        }
    }
}
