package org.example;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import org.springframework.jdbc.core.JdbcTemplate;

public class Rede {
    Looca looca = new Looca();
    private Integer idRede;
    private String enderecoIPV4;
    private String enderecoMAC;
    private Long pacotesEnviados;
    private Long pacotesRecebidos;
    private String nomeHost;
    private String tempoCaputras;
    private Integer fkCodHardware;
    private Integer fkMaquina;
    private Conexao conexao = new Conexao();
    private JdbcTemplate con = conexao.getConexaoBanco();

    public Rede(Integer idRede,
                String enderecoIPV4,
                String enderecoMAC,
                Long pacotesEnviados,
                Long pacotesRecebidos,
                String nomeHost,
                String tempoCaputras,
                Integer fkCodHardware,
                Integer fkMaquina) {
        this.idRede = idRede;
        this.enderecoIPV4 = enderecoIPV4;
        this.enderecoMAC = enderecoMAC;
        this.pacotesEnviados = pacotesEnviados;
        this.pacotesRecebidos = pacotesRecebidos;
        this.nomeHost = nomeHost;
        this.tempoCaputras = tempoCaputras;
        this.fkCodHardware = fkCodHardware;
        this.fkMaquina = fkMaquina;
    }

    public Rede(Integer fkCodHardware, Integer fkMaquina) {
        this.fkCodHardware = fkCodHardware;
        this.fkMaquina = fkMaquina;
    }

    public Rede() {
    }

    public void capturarDadosRede(){
//        RedeInterface redeInterface = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(2);
//
//        enderecoIPV4 = redeInterface.getEnderecoIpv4().get(0);
//        enderecoMAC = redeInterface.getEnderecoMac();
//        pacotesEnviados = redeInterface.getPacotesEnviados();
//        pacotesRecebidos = redeInterface.getPacotesRecebidos();
//        nomeHost = looca.getRede().getParametros().getHostName();

        con.update("INSERT INTO Rede (enderecoIPV4, enderecoMAC, pacotesEnviados, pacotesRecebidos, " +
                "nomeHost, tempoCapturas, fkCodHardware, fkMaquina) values (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)",
                null, null, null, null, null, null, null);
    }

    public Integer getIdRede() {
        return idRede;
    }

    public void setIdRede(Integer idRede) {
        this.idRede = idRede;
    }

    public String getEnderecoIPV4() {
        return enderecoIPV4;
    }

    public void setEnderecoIPV4(String enderecoIPV4) {
        this.enderecoIPV4 = enderecoIPV4;
    }

    public String getEnderecoMAC() {
        return enderecoMAC;
    }

    public void setEnderecoMAC(String enderecoMAC) {
        this.enderecoMAC = enderecoMAC;
    }

    public Long getPacotesEnviados() {
        return pacotesEnviados;
    }

    public void setPacotesEnviados(Long pacotesEnviados) {
        this.pacotesEnviados = pacotesEnviados;
    }

    public Long getPacotesRecebidos() {
        return pacotesRecebidos;
    }

    public void setPacotesRecebidos(Long pacotesRecebidos) {
        this.pacotesRecebidos = pacotesRecebidos;
    }

    public String getNomeHost() {
        return nomeHost;
    }

    public void setNomeHost(String nomeHost) {
        this.nomeHost = nomeHost;
    }

    public String getTempoCaputras() {
        return tempoCaputras;
    }

    public void setTempoCaputras(String tempoCaputras) {
        this.tempoCaputras = tempoCaputras;
    }

    public Integer getFkCodHardware() {
        return fkCodHardware;
    }

    public void setFkCodHardware(Integer fkCodHardware) {
        this.fkCodHardware = fkCodHardware;
    }

    public Integer getFkMaquina() {
        return fkMaquina;
    }

    public void setFkMaquina(Integer fkMaquina) {
        this.fkMaquina = fkMaquina;
    }

    @Override
    public String toString() {
        return """
            idRede: %d
            enderecoIPV4: '%s'
            enderecoMAC: '%s'
            pacotesEnviados: %d
            pacotesRecebidos: %d
            nomeHost: '%s'
            tempoCaputras: '%s'
            fkCodHardware: %d
            fkMaquina: %d""".formatted(idRede,
                enderecoIPV4,
                enderecoMAC,
                pacotesEnviados,
                pacotesRecebidos,
                nomeHost,
                tempoCaputras,
                fkCodHardware,
                fkMaquina);
    }
}
