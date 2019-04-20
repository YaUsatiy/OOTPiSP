import java.io.*;
import java.util.Base64;

public class base64 implements Plugin{

    public void processing(File file){
        if(file.getName().endsWith(".base64")){
            try{
                System.out.println("base64 decoding is working");
                FileOutputStream fos = new FileOutputStream(file.getName().substring(0, file.getName().lastIndexOf('.')));
                Base64.Decoder decoder = Base64.getDecoder();
                InputStream is = decoder.wrap(new FileInputStream(file));
                int _byte;
                while ((_byte = is.read()) != -1){
                    fos.write(_byte);
                }
                is.close();
                fos.close();
                is = new FileInputStream((file.getName().substring(0, file.getName().lastIndexOf('.'))));
                fos = new FileOutputStream(file);
                while ((_byte = is.read()) != -1){
                    fos.write(_byte);
                }
                is.close();
                fos.close();
            }
            catch (Exception e){
                System.out.println("base64 decoding error");
                e.printStackTrace();
            }
        }
        else{
            try {
                System.out.println("base64 encoding is working");
                FileInputStream fis = new FileInputStream(file);
                Base64.Encoder encoder = Base64.getEncoder();
                OutputStream out = encoder.wrap(new FileOutputStream(file.getName() + ".base64"));
                int _byte;
                while ((_byte = fis.read()) != -1){
                    out.write(_byte);
                }
                out.close();
                fis.close();
                file.delete();
            }
            catch (Exception ex){
                System.out.println("base64 encoding error");
                ex.printStackTrace();
            }
        }
    }

}