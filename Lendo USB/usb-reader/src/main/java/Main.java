import javax.usb.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws UsbException {
        try{
            UsbReader leitor01 = new UsbReader();

            UsbServices services = UsbHostManager.getUsbServices();

            UsbHub computadorHub = services.getRootUsbHub();

            List<UsbDevice> devices = computadorHub.getAttachedUsbDevices();

            for (UsbDevice device : devices){
                leitor01.processarDispositivos(device);
            }
        }catch (UsbException e){
            System.out.println("Erro ao acessar dispositivos USB: " + e.getMessage());
        }
    }
}
