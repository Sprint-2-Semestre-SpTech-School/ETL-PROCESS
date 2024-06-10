package org.example.Capturas;

import com.github.britooo.looca.api.core.Looca;
import org.example.Jdbc.Conexao;
import org.example.Jdbc.ConexaoServer;
import org.example.TipoHardware;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class Hardware {
    protected TipoHardware tipoHardware;
    protected String nomeHardware;
    protected String unidadeCaptacao;
    protected Double valorTotal;
    protected Integer fkMaquina;
    protected Looca looca = new Looca();
    protected Conexao conexao = new Conexao();
    protected ConexaoServer conexao02 = new ConexaoServer();
    protected JdbcTemplate con = conexao.getConexaoBanco();
    protected JdbcTemplate con02 = conexao02.getConexaoBanco();

    public Hardware(TipoHardware tipoHardware,
                    String nomeHardware,
                    String unidadeCaptacao,
                    Double valorTotal,
                    Integer fkMaquina,
                    Looca looca,
                    Conexao conexao,
                    ConexaoServer conexao02,
                    JdbcTemplate con,
                    JdbcTemplate con02) {
        this.tipoHardware = tipoHardware;
        this.nomeHardware = nomeHardware;
        this.unidadeCaptacao = unidadeCaptacao;
        this.valorTotal = valorTotal;
        this.fkMaquina = fkMaquina;
        this.looca = looca;
        this.conexao = conexao;
        this.conexao02 = conexao02;
        this.con = con;
        this.con02 = con02;
    }

    public Hardware() {
    }

    public abstract void capturarDados(Integer fkMaquina);
    public abstract void capturarDados();
    public abstract void inserirDados();
}
