package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;

public class Volume {
    Looca looca = new Looca();
    private String pontoMontagem;
    private String sistemaArmazenamento;
    private Long volumeDisponivel;
    private Long volumeTotal;
    private Integer quantidadeDeVolume;
    private String tempoCapturas;
    private Integer fkDisco;
    private Integer fkCodHardware;
    private Integer fkMaquina;
    private Conexao conexao = new Conexao();
//    private Conexao02 conexao02 = new Conexao02();
    private JdbcTemplate con = conexao.getConexaoBanco();
//    private JdbcTemplate con02 = conexao02.getConexaoBanco();

    public Volume(String pontoMontagem,
                  String sistemaArmazenamento,
                  Long volumeDisponivel,
                  Long volumeTotal,
                  Integer quantidadeDeVolume,
                  String tempoCapturas,
                  Integer fkDisco,
                  Integer fkCodHardware,
                  Integer fkMaquina) {
        this.pontoMontagem = pontoMontagem;
        this.sistemaArmazenamento = sistemaArmazenamento;
        this.volumeDisponivel = volumeDisponivel;
        this.volumeTotal = volumeTotal;
        this.quantidadeDeVolume = quantidadeDeVolume;
        this.tempoCapturas = tempoCapturas;
        this.fkDisco = fkDisco;
        this.fkCodHardware = fkCodHardware;
        this.fkMaquina = fkMaquina;
    }
    public Volume(Integer fkDisco, Integer fkCodHardware, Integer fkMaquina) {
        this.fkDisco = fkDisco;
        this.fkCodHardware = fkCodHardware;
        this.fkMaquina = fkMaquina;
    }
    public Volume() {
    }
    public void capturarDadosVolume(){
        pontoMontagem = looca.getGrupoDeDiscos().getVolumes().get(0).getPontoDeMontagem();
        sistemaArmazenamento = looca.getGrupoDeDiscos().getVolumes().get(0).getTipo();
        volumeDisponivel = looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel();
        volumeTotal = looca.getGrupoDeDiscos().getVolumes().get(0).getTotal();
        quantidadeDeVolume = looca.getGrupoDeDiscos().getQuantidadeDeVolumes();

        con.update("INSERT INTO Volume (pontoMontagem, sistemaArmazenamento, volumeDisponivel, volumeTotal, " +
                "quantidadeDeVolume, tempoCapturas , fkDisco, fkCodHardware, fkMaquina) values (?, ?, ?, ?, ?, " +
                        "CURRENT_TIMESTAMP, ?, ?, ?)",
                pontoMontagem, sistemaArmazenamento, volumeDisponivel, volumeTotal, quantidadeDeVolume,
                fkDisco, fkCodHardware, fkMaquina);
    }

    public String getPontoMontagem() {
        return pontoMontagem;
    }

    public void setPontoMontagem(String pontoMontagem) {
        this.pontoMontagem = pontoMontagem;
    }

    public String getSistemaArmazenamento() {
        return sistemaArmazenamento;
    }

    public void setSistemaArmazenamento(String sistemaArmazenamento) {
        this.sistemaArmazenamento = sistemaArmazenamento;
    }

    public Long getVolumeDisponivel() {
        return volumeDisponivel;
    }

    public void setVolumeDisponivel(Long volumeDisponivel) {
        this.volumeDisponivel = volumeDisponivel;
    }

    public Long getVolumeTotal() {
        return volumeTotal;
    }

    public void setVolumeTotal(Long volumeTotal) {
        this.volumeTotal = volumeTotal;
    }

    public Integer getQuantidadeDeVolume() {
        return quantidadeDeVolume;
    }

    public void setQuantidadeDeVolume(Integer quantidadeDeVolume) {
        this.quantidadeDeVolume = quantidadeDeVolume;
    }

    public String getTempoCapturas() {
        return tempoCapturas;
    }

    public void setTempoCapturas(String tempoCapturas) {
        this.tempoCapturas = tempoCapturas;
    }

    public Integer getFkDisco() {
        return fkDisco;
    }

    public void setFkDisco(Integer fkDisco) {
        this.fkDisco = fkDisco;
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
            pontoMontagem: '%s'
            sistemaArmazenamento: '%s'
            volumeDisponivel: %d
            volumeTotal: %d
            quantidadeDeVolume: %d
            tempoCapturas: '%s'
            fkDisco: %d
            fkCodHardware: %d
            fkMaquina: %d""".formatted(pontoMontagem,
                sistemaArmazenamento,
                volumeDisponivel,
                volumeTotal,
                quantidadeDeVolume,
                tempoCapturas,
                fkDisco,
                fkCodHardware,
                fkMaquina);
    }
}
