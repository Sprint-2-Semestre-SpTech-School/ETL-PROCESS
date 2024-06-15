package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class Disco {
    Looca looca = new Looca();
    private Integer idDisco;
    private Long escritas;
    private Long bytesEscritos;
    private Long leituras;
    private Long bytesLidos;
    private String tempoCapturas;
    private Integer fkCodHardware;
    private Integer fkMaquina;
    private Conexao conexao = new Conexao();
    private JdbcTemplate con = conexao.getConexaoBanco();

    public Disco(Integer idDisco,
                 Long escritas,
                 Long bytesEscritos,
                 Long leituras,
                 Long bytesLidos,
                 String tempoCapturas) {
        this.idDisco = idDisco;
        this.escritas = escritas;
        this.bytesEscritos = bytesEscritos;
        this.leituras = leituras;
        this.bytesLidos = bytesLidos;
        this.tempoCapturas = tempoCapturas;
    }

    public Disco(Integer fkCodHardware, Integer fkMaquina) {
        this.fkCodHardware = fkCodHardware;
        this.fkMaquina = fkMaquina;
    }

    public Disco() {
    }

    public void capturarDadosDisco(){
        com.github.britooo.looca.api.group.discos.Disco disco = looca.getGrupoDeDiscos().getDiscos().get(0);
        escritas = disco.getEscritas();
        bytesEscritos = disco.getBytesDeEscritas();
        leituras = disco.getLeituras();
        bytesLidos = disco.getBytesDeLeitura();

        con.update("INSERT INTO Disco (escritas, bytesEscritos, leituras, bytesLidos, tempoCapturas, " +
                "fkCodHardware, fkMaquina) values (?, ?, ? ,? ,CURRENT_TIMESTAMP,? , ?)",
                escritas, bytesEscritos, leituras, bytesLidos, fkCodHardware, fkMaquina);
    }
    public Integer consultarId(){
        List<Integer> idsDisco;

        String comandoSql = ("SELECT idDisco from Disco");
        idsDisco = con.queryForList(comandoSql, Integer.class);
        return idsDisco.get(idsDisco.size() - 1);
    }

    public Integer getIdDisco() {
        return idDisco;
    }

    public void setIdDisco(Integer idDisco) {
        this.idDisco = idDisco;
    }

    public Long getEscritas() {
        return escritas;
    }

    public void setEscritas(Long escritas) {
        this.escritas = escritas;
    }

    public Long getBytesEscritos() {
        return bytesEscritos;
    }

    public void setBytesEscritos(Long bytesEscritos) {
        this.bytesEscritos = bytesEscritos;
    }

    public Long getLeituras() {
        return leituras;
    }

    public void setLeituras(Long leituras) {
        this.leituras = leituras;
    }

    public Long getBytesLidos() {
        return bytesLidos;
    }

    public void setBytesLidos(Long bytesLidos) {
        this.bytesLidos = bytesLidos;
    }

    public String getTempoCapturas() {
        return tempoCapturas;
    }

    public void setTempoCapturas(String tempoCapturas) {
        this.tempoCapturas = tempoCapturas;
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
            idDisco: %d
            escritas: %d
            bytesEscritos: %d
            leituras: %d
            bytesLidos: %d
            tempoCapturas: '%s'
            fkCodHardware: %d
            fkMaquina: %d""".formatted(idDisco,
                escritas,
                bytesEscritos,
                leituras,
                bytesLidos,
                tempoCapturas,
                fkCodHardware,
                fkMaquina);
    }
}
