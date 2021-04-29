import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class pol {

    static long I = 0b111111111111111111111111111111; // 1073741823

    static byte generateK() {
        long S = I;
        I = (((I >> 29) ^ (I >> 15) ^ (I >> 14) ^ I) & 1) | ((I << 1) & 0b111111111111111111111111111111);

        if ((S & 0b100000000000000000000000000000) == 0) { // 536870912
            return 0;
        }
        return 1;
    }

    static byte generateKByte() {
        byte S = 0;

        for (int i = 0; i < 7; i++) {
            S = (byte) ((S | generateK()) << 1);
        }

        S = (byte) (S | generateK());

        return S;
    }

    public static void main(String[] Args) {

        try (FileInputStream fIn = new FileInputStream("test.txt");
             FileOutputStream fOut = new FileOutputStream("encrypt.txt")) {
            byte[] bIn = new byte[fIn.available()];;
            fIn.read(bIn, 0, fIn.available());

            for (int i = 0; i < bIn.length; i++) {
                bIn[i] = (byte) (bIn[i] ^ generateKByte());
            }

            fOut.write(bIn);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        I = 0b111111111111111111111111111111;

        try (FileInputStream fIn = new FileInputStream("encrypt.txt");
             FileOutputStream fOut = new FileOutputStream("decrypt.txt")) {
            byte[] bIn = new byte[fIn.available()];
            fIn.read(bIn, 0, fIn.available());

            for (int i = 0; i < bIn.length; i++) {
                bIn[i] = (byte) (bIn[i] ^ generateKByte());
            }

            fOut.write(bIn);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
