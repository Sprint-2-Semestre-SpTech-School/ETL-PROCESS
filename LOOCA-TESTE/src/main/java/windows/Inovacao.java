package windows;

public class Inovacao {
    public String listarDescricao() {
        String listarUsbDevices = "Get-PnpDevice -PresentOnly | Where-Object { $_.InstanceId -match '^USB' } " +
                "| Select-Object Description, DeviceID";
        return listarUsbDevices;
    }
    public String listarDeviceID() {
        String listarID = "";
    }
}
