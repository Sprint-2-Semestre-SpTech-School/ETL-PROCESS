package org.example;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.processador.Processador;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class infoHardware {
    Looca looca = new Looca();
    private Integer codHardware;
    private String nomeCpu;
    private Long memoriaTotalRam;
    private String nomeDisco;
    private String nomeRede;
    private Integer fkMaquina;
    private Conexao conexao = new Conexao();
    private JdbcTemplate con = conexao.getConexaoBanco();

    public infoHardware(Integer codHardware,
                        String nomeCpu,
                        Long memoriaTotalRam,
                        String nomeDisco,
                        String nomeRede,
                        Integer fkMaquina) {
        this.codHardware = codHardware;
        this.nomeCpu = nomeCpu;
        this.memoriaTotalRam = memoriaTotalRam;
        this.nomeDisco = nomeDisco;
        this.nomeRede = nomeRede;
        this.fkMaquina = fkMaquina;
    }

    public infoHardware(Integer fkMaquina) {
        this.fkMaquina = fkMaquina;
    }

    public infoHardware() {
    }
    public void capturarDadosInfoHardware(){
        nomeCpu = looca.getProcessador().getNome();
        memoriaTotalRam = looca.getMemoria().getTotal() / (1024 * 1024);
        nomeDisco = looca.getGrupoDeDiscos().getDiscos().get(0).getNome();
//        nomeRede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(2).getNomeExibicao();
        con.update("INSERT INTO infoHardware (nomeCpu, memoriaTotalRam, nomeDisco, nomeRede, fkMaquina)" +
                "values (?, ?, ?, ?, ?)", nomeCpu, memoriaTotalRam, nomeDisco, null, fkMaquina);
    }
    public Integer consultarId(){
        List<Integer> codsHardware;

        String comandoSql = ("SELECT codHardware from infoHardware");
        codsHardware = con.queryForList(comandoSql, Integer.class);
        return codsHardware.get(codsHardware.size() - 1);
    }

    public Integer getCodHardware() {
        return codHardware;
    }

    public void setCodHardware(Integer codHardware) {
        this.codHardware = codHardware;
    }

    public String getNomeCpu() {
        return nomeCpu;
    }

    public void setNomeCpu(String nomeCpu) {
        this.nomeCpu = nomeCpu;
    }

    public Long getMemoriaTotalRam() {
        return memoriaTotalRam;
    }

    public void setMemoriaTotalRam(Long memoriaTotalRam) {
        this.memoriaTotalRam = memoriaTotalRam;
    }

    public String getNomeDisco() {
        return nomeDisco;
    }

    public void setNomeDisco(String nomeDisco) {
        this.nomeDisco = nomeDisco;
    }

    public String getNomeRede() {
        return nomeRede;
    }

    public void setNomeRede(String nomeRede) {
        this.nomeRede = nomeRede;
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
            codHardware: %d
            nomeCpu: '%s'
            memoriaTotalRam: %d
            nomeDisco: '%s'
            nomeRede: '%s'
            fkMaquina: %d""".formatted(codHardware,
                nomeCpu,
                memoriaTotalRam,
                nomeDisco,
                nomeRede,
                fkMaquina);
    }
}
