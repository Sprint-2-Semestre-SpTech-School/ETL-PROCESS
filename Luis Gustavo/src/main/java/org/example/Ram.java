package org.example;

import com.github.britooo.looca.api.core.Looca;
import com.mysql.cj.log.Log;
import org.springframework.jdbc.core.JdbcTemplate;

public class Ram {
    Looca looca = new Looca();
    private Integer idRam;
    private Long memoriaDisponivel;
    private Long memoriaEmUso;
    private Long tempoCapturas;
    private Integer fkCodHardware;
    private Integer fkMaquina;
    private Conexao conexao = new Conexao();
    private JdbcTemplate con = conexao.getConexaoBanco();

    public Ram(Integer idRam, Long memoriaDisponivel, Long memoriaEmUso, Long tempoCapturas) {
        this.idRam = idRam;
        this.memoriaDisponivel = memoriaDisponivel;
        this.memoriaEmUso = memoriaEmUso;
        this.tempoCapturas = tempoCapturas;
    }

    public Ram(Integer fkCodHardware, Integer fkMaquina) {
        this.fkCodHardware = fkCodHardware;
        this.fkMaquina = fkMaquina;
    }

    public Ram() {
    }

        public void capturarDadosRam(){
            memoriaDisponivel = looca.getMemoria().getDisponivel();
            memoriaEmUso = looca.getMemoria().getEmUso();
            con.update("INSERT INTO Ram (memoriaDisponivel, memoriaEmUso, tempoCapturas, fkCodHardware, fkMaquina)" +
                    "values (? , ?, CURRENT_TIMESTAMP, ?, ?)",
            memoriaDisponivel, memoriaEmUso, fkCodHardware, fkMaquina);
        }

    public Integer getIdRam() {
        return idRam;
    }

    public void setIdRam(Integer idRam) {
        this.idRam = idRam;
    }

    public Long getMemoriaDisponivel() {
        return memoriaDisponivel;
    }

    public void setMemoriaDisponivel(Long memoriaDisponivel) {
        this.memoriaDisponivel = memoriaDisponivel;
    }

    public Long getMemoriaEmUso() {
        return memoriaEmUso;
    }

    public void setMemoriaEmUso(Long memoriaEmUso) {
        this.memoriaEmUso = memoriaEmUso;
    }

    public Long getTempoCapturas() {
        return tempoCapturas;
    }

    public void setTempoCapturas(Long tempoCapturas) {
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
            idRam: %d
            memoriaDisponivel: %d
            memoriaEmUso: %d
            tempoCapturas: %s
            fkCodHardware: '%s'
            fkMaquina: %d""".formatted(idRam,
                memoriaDisponivel,
                memoriaEmUso,
                tempoCapturas,
                fkCodHardware,
                fkMaquina);
    }
}
