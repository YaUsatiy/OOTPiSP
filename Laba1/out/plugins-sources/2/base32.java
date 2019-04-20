import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base32InputStream;
import org.apache.commons.codec.binary.Base32OutputStream;

import java.io.*;

public class base32 implements Plugin{

    public void processing(File file){
        if(file.getName().endsWith(".base32")){
            try {
                FileOutputStream fost = new FileOutputStream(file.getName().substring(0, file.getName().lastIndexOf('.')));
                FileInputStream fist = new FileInputStream(file);
                InputStream is = new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return fist.read();
                    }
                };
                Base32InputStream base32InputStream = new Base32InputStream(is);
                int _byte;
                while ((_byte = base32InputStream.read()) != -1) {
                    fost.write(_byte);
                }
                base32InputStream.close();
                fist.close();
                fost.close();

                FileInputStream fis = new FileInputStream((file.getName().substring(0, file.getName().lastIndexOf('.'))));
                FileOutputStream fos = new FileOutputStream(file);
                while ((_byte = fis.read()) != -1){
                    fos.write(_byte);
                }
                fis.close();
                fos.close();
            }
            catch (Exception e){
                System.out.println("base32 decoding error");
                e.printStackTrace();
            }
        }
        else{
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(file.getName() + ".base32");
                OutputStream os = new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        fos.write(b);
                    }
                };
                Base32OutputStream base32OutputStream = new Base32OutputStream(os, true);
                int _byte;
                while ((_byte = fis.read()) != -1){
                    base32OutputStream.write(_byte);
                }
                base32OutputStream.close();
                fis.close();
                fos.close();
                file.delete();
            }
            catch (Exception ex){
                System.out.println("base32 encoding error");
                ex.printStackTrace();
            }
        }

    }

}