import javax.usb.*;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class FindDevices {
    public static void main(String[] args) {
        UsbReader leitor01 = new UsbReader();
        try{
            UsbServices services = UsbHostManager.getUsbServices();
            UsbHub rootHub = services.getRootUsbHub();
            List<UsbDevice> devices = rootHub.getAttachedUsbDevices();

            for (UsbDevice device : devices){
                printDeviceInfo(device);
            }
        } catch (UsbException e) {
            throw new RuntimeException(e);
        }
    }
    private static void printDeviceInfo(UsbDevice device) throws UsbException {
        // Exibe algumas informações sobre o dispositivo
        UsbDeviceDescriptor descriptor = device.getUsbDeviceDescriptor();
        System.out.println("Device Vendor ID: " + descriptor.idVendor());
        System.out.println("Device Product ID: " + descriptor.idProduct());
        System.out.println("Device Class: " + descriptor.bDeviceClass());
        System.out.println("Device Subclass: " + descriptor.bDeviceSubClass());
        System.out.println("Device Protocol: " + descriptor.bDeviceProtocol());
        System.out.println();
    }
}
