package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;

public class Cpu {
    Looca looca = new Looca();
   private String idCpu;
   private Long frequencia;
   private Integer numeroCpuFisica;
   private Integer numeroCpuLogica;
   private Integer numeroPacote;
   private Double usoDeCpu;
   private String tempoCapturas;
   private Integer fkCodHardware;
   private Integer fkMaquina;
   private Conexao conexao = new Conexao();
   private JdbcTemplate con = conexao.getConexaoBanco();

    public Cpu(String idCpu,
               Long frequencia,
               Integer numeroCpuFisica,
               Integer numeroCpuLogica, Integer numeroPacote,
               Double usoDeCpu,
               String tempoCapturas,
               Integer fkCodHardware,
               Integer fkMaquina) {
        this.idCpu = idCpu;
        this.frequencia = frequencia;
        this.numeroCpuFisica = numeroCpuFisica;
        this.numeroCpuLogica = numeroCpuLogica;
        this.numeroPacote = numeroPacote;
        this.usoDeCpu = usoDeCpu;
        this.tempoCapturas = tempoCapturas;
        this.fkCodHardware = fkCodHardware;
        this.fkMaquina = fkMaquina;
    }

    public Cpu(Integer fkCodHardware, Integer fkMaquina) {
        this.fkCodHardware = fkCodHardware;
        this.fkMaquina = fkMaquina;
    }

    public Cpu() {
    }
    public void capturarDadosCpu(){
        idCpu = looca.getProcessador().getId();
        frequencia = looca.getProcessador().getFrequencia() / 10000; // Mhz
        numeroCpuFisica = looca.getProcessador().getNumeroCpusFisicas();
        numeroCpuLogica = looca.getProcessador().getNumeroCpusLogicas();
        numeroPacote = looca.getProcessador().getNumeroPacotesFisicos();
        usoDeCpu = looca.getProcessador().getUso();

        con.update("INSERT INTO Cpu (idCpu, frequencia, numeroCpuFisica, numeroCpuLogica, numeroPacote, usoDeCpu," +
                        "tempoCapturas, fkCodHardware, fkMaquina) values (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)",
                idCpu, frequencia, numeroCpuFisica, numeroCpuLogica, numeroPacote, usoDeCpu, fkCodHardware, fkMaquina);
    }

    public String getIdCpu() {
        return idCpu;
    }

    public void setIdCpu(String idCpu) {
        this.idCpu = idCpu;
    }

    public Long getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Long frequencia) {
        this.frequencia = frequencia;
    }

    public Integer getNumeroCpuFisica() {
        return numeroCpuFisica;
    }

    public void setNumeroCpuFisica(Integer numeroCpuFisica) {
        this.numeroCpuFisica = numeroCpuFisica;
    }

    public Integer getNumeroCpuLogica() {
        return numeroCpuLogica;
    }

    public void setNumeroCpuLogica(Integer numeroCpuLogica) {
        this.numeroCpuLogica = numeroCpuLogica;
    }

    public Integer getNumeroPacote() {
        return numeroPacote;
    }

    public void setNumeroPacote(Integer numeroPacote) {
        this.numeroPacote = numeroPacote;
    }

    public Double getUsoDeCpu() {
        return usoDeCpu;
    }

    public void setUsoDeCpu(Double usoDeCpu) {
        this.usoDeCpu = usoDeCpu;
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
                idCpu: %s
                frequencia: %d
                Número de Cpu física: %d
                Número de Cpu lógica: %d
                Número de pacote: %d
                Uso de Cpu: %.2f
                Tempo de Captura: %s""".formatted(idCpu,
                frequencia,
                numeroCpuFisica,
                numeroCpuLogica,
                numeroPacote,
                usoDeCpu,
                tempoCapturas);
    }
}
