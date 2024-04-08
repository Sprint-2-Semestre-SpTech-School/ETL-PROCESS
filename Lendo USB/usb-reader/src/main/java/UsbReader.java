import javax.usb.*;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class UsbReader {
    public void lerNome (final UsbDevice device) throws UnsupportedEncodingException, UsbException {
        // Criar um parâmetro device do tipo UsbDevice
        // O final do java é como se fosse o const do JS, mas ele pode ser aplicado a métodos etc.

        final UsbDeviceDescriptor desc01 = device.getUsbDeviceDescriptor();
        final byte iManufacturer = desc01.iManufacturer();
        final byte iProduct = desc01.iProduct();

        // Os nomes nas placas USB vem em bytes, então para lermos precisa ser convertido para string
        // O método getString é próprio da API
        String nomeProdutora = device.getString(iManufacturer); // Convertendo para string
        String nomeProduto = device.getString(iProduct); // Convertendo para string

        if (iManufacturer == 0 || iProduct == 0) {
            System.out.println("Não foi identificado um dispositivo USB");
        } else {
            System.out.println("""
                    Nome da Produtora: %s
                    Nome do Produto: %s""".formatted(nomeProdutora, nomeProduto));
        }
    }

    public void processarDispositivos(final UsbDevice device) {
        try {
            lerNome(device);
        }catch (Exception e){
            System.out.println("Ignorando dispositivo problemático..." + e);
        }
    }
}

