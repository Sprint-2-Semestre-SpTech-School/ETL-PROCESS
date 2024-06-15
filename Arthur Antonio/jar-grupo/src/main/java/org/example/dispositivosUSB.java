package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class dispositivosUSB {
    Looca looca = new Looca();
    private List deviceId;
    private String descricao;
    private Conexao conexao = new Conexao();
    private JdbcTemplate con = conexao.getConexaoBanco();
    public dispositivosUSB(List deviceId, String descricao) {
        this.deviceId = deviceId;
        this.descricao = descricao;
    }
    public dispositivosUSB() {
    }
    public void listarUSB(){
        for (int i = 0; i < looca.getDispositivosUsbGrupo().getDispositivosUsbConectados().size(); i++) {
            con.update("INSERT INTO DispositivosUSB (deviceId, descricao) values (?, ?)",
                    looca.getDispositivosUsbGrupo().getDispositivosUsbConectados().get(i).getIdDispositivoUsbExclusivo(),
                    looca.getDispositivosUsbGrupo().getDispositivosUsbConectados().get(i).getNome());
        }
    }

    @Override
    public String toString() {
        return """
            deviceId: %s
            descricao: '%s'""".formatted(deviceId,
                descricao);
    }
}
