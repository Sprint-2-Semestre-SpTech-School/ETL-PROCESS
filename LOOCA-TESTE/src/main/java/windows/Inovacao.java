package windows;

public class Inovacao {
    public String listarDescricao() {
        String listarUsbDevices = "Get-PnpDevice -PresentOnly | Where-Object { $_.InstanceId -match '^USB' } " +
                "| Select-Object Description, deviceID";
        return listarUsbDevices;
    }
    public String listarDeviceID() {
        String listarID = "Get-PnpDevice -PresentOnly | Where-Object { $_.InstanceId -match '^USB' }" +
                "| Select-Object Description";
        return listarID;
    }
}
