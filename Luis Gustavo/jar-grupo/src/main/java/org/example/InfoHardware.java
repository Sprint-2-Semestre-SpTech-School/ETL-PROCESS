package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.Jdbc.Conexao;
import org.example.Jdbc.ConexaoServer;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class InfoHardware {
    Looca looca = new Looca();
    private Integer codHardware;
    private String nomeCpu;
    private Long memoriaTotalRam;
    private String nomeDisco;
    private String nomeRede;
    private Integer fkMaquina;
    private Conexao conexao = new Conexao();
    private ConexaoServer conexao02 = new ConexaoServer();
    private JdbcTemplate con = conexao.getConexaoBanco();
    private JdbcTemplate con02 = conexao.getConexaoBanco();

    public InfoHardware(Looca looca,
                        Integer codHardware,
                        String nomeCpu,
                        Long memoriaTotalRam,
                        String nomeDisco,
                        String nomeRede,
                        Integer fkMaquina,
                        Conexao conexao,
                        ConexaoServer conexao02,
                        JdbcTemplate con,
                        JdbcTemplate con02) {
        this.looca = looca;
        this.codHardware = codHardware;
        this.nomeCpu = nomeCpu;
        this.memoriaTotalRam = memoriaTotalRam;
        this.nomeDisco = nomeDisco;
        this.nomeRede = nomeRede;
        this.fkMaquina = fkMaquina;
        this.conexao = conexao;
        this.conexao02 = conexao02;
        this.con = con;
        this.con02 = con02;
    }

    public InfoHardware(Integer fkMaquina) {
        this.fkMaquina = fkMaquina;
    }

    public InfoHardware() {
    }

    public void capturarDadosInfoHardware() {
        nomeCpu = looca.getProcessador().getNome();
        memoriaTotalRam = looca.getMemoria().getTotal() / (1024 * 1024);
        nomeDisco = looca.getGrupoDeDiscos().getDiscos().get(0).getNome();
//        nomeRede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(2).getNomeExibicao();

        try {
            con.update("INSERT INTO InfoHardware (nomeCpu, memoriaTotalRam, nomeDisco, nomeRede, fkMaquina)" +
                    "values (?, ?, ?, ?, ?)", nomeCpu, memoriaTotalRam, nomeDisco, null, fkMaquina);
            con02.update("INSERT INTO InfoHardware (nomeCpu, memoriaTotalRam, nomeDisco, nomeRede, fkMaquina)" +
                    "values (?, ?, ?, ?, ?)", nomeCpu, memoriaTotalRam, nomeDisco, null, fkMaquina);
        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'infoHardware' sql" + e.getMessage());
        }

    }

    public Integer consultarId() {
        List<Integer> codsHardware;

        String comandoSql = ("SELECT codHardware from InfoHardware");
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
