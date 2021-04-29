import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class Main {

    public static final int M = 27;
    private static  BitSet register = new BitSet(M);

    public static void main(String[] args) {
        register.set(0, M, true);
        try (FileInputStream fin = new FileInputStream("test.txt");
            FileOutputStream fOut = new FileOutputStream("encrypt.txt")) {   // read to buffer from file
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer, 0, fin.available());

            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) (buffer[i] ^ generateByteKey());                //encrypt
            }

            fOut.write(buffer);                                                    // write encrypt data

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        register.set(0, M, true);
        try (FileOutputStream fOut = new FileOutputStream("decrypt.txt");
             FileInputStream fin = new FileInputStream("encrypt.txt")) {

            byte[] bufferEnc = new byte[fin.available()];                         // read encrypt data
            fin.read(bufferEnc, 0, fin.available());

            for (int i = 0; i < bufferEnc.length; i++) {                          // decrypt data
                bufferEnc[i] = (byte) (bufferEnc[i] ^ generateByteKey());
            }

            fOut.write(bufferEnc);                                                // write decrypt data
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // x^27 + x^8 + x^7 + x + 1
    public static final byte generateByteKey() {
        BitSet resultKey = new BitSet(0);

        for (int i = 0; i < 8; i++) {
            resultKey.set(i, register.get(M - 1));
            BitSet tmp = (BitSet) register.clone();
            for (int j = 1; j <= M - 1; j++) {
                register.set(j, tmp.get(j - 1));
            }
            register.set(0, (register.get(26) ^ register.get(7) ^ register.get(6) ^ register.get(0)));
        }
        return resultKey.toByteArray()[0];
    }
}
