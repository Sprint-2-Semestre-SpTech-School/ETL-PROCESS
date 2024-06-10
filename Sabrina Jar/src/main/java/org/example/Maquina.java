package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.Jdbc.Conexao;
import org.example.Jdbc.ConexaoServer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.ls.LSOutput;

import java.util.List;

public class Maquina {
    Looca looca = new Looca();
    private Integer idMaquina;
    private String usuario;
    private String sistemaOperacional;
    private Double temperatura;
    private Long tempoAtividade;
    private String destino;
    private String descricao;
    private Integer fkProjeto;
    private Integer fkEmpresa;
    private Conexao conexao = new Conexao();
    private ConexaoServer conexao02 = new ConexaoServer();
    private JdbcTemplate con = conexao.getConexaoBanco();
    private JdbcTemplate con02 = conexao02.getConexaoBanco();

    public Maquina(
            Looca looca,
            Integer idMaquina,
            String usuario,
            String sistemaOperacional,
            Double temperatura,
            Long tempoAtividade,
            String destino,
            String descricao,
            Integer fkProjeto,
            Integer fkEmpresa,
            Conexao conexao,
            ConexaoServer conexao02
    ) {
        this.looca = looca;
        this.idMaquina = idMaquina;
        this.usuario = usuario;
        this.sistemaOperacional = sistemaOperacional;
        this.temperatura = temperatura;
        this.tempoAtividade = tempoAtividade;
        this.destino = destino;
        this.descricao = descricao;
        this.fkProjeto = fkProjeto;
        this.fkEmpresa = fkEmpresa;
        this.conexao = conexao;
        this.conexao02 = conexao02;
    }

    public Maquina(Integer fkProjeto, Integer fkEmpresa) {
        this.fkProjeto = fkProjeto;
        this.fkEmpresa = fkEmpresa;
    }

    public Maquina() {

    }

    public void capturarDadosMaquina() {

        usuario = System.getProperty("user.name");
        sistemaOperacional = looca.getSistema().getSistemaOperacional();
        temperatura = looca.getTemperatura().getTemperatura();
        tempoAtividade = looca.getSistema().getTempoDeAtividade() / 3600; // Valor em horas

    }

    public void inserirDadosMaquina(Integer fkProjeto, Integer fkEmpresa) {
        try {
            con02.update("UPDATE Maquina SET usuario =?, sistemaOperacional =?, temperatura =?, tempoAtividade =?, " +
                            "fkProjeto =?, fkEmpresa =?",
                    usuario, sistemaOperacional, temperatura, tempoAtividade, fkProjeto, fkEmpresa);

            con.update("UPDATE Maquina SET usuario =?, sistemaOperacional =?, temperatura =?, tempoAtividade =?, " +
                            "fkProjeto =?, fkEmpresa =?",
                    usuario, sistemaOperacional, temperatura, tempoAtividade, fkProjeto, fkEmpresa);

        } catch (RuntimeException e) {
            System.out.println("Erro de conexão 'Maquina' sql" + e.getMessage());
        }
    }

    public Integer consultarId() {
        try{
        String comandoSql = "SELECT idMaquina from Maquina order by idMaquina DESC LIMIT 1";
        idMaquina = con.queryForObject(comandoSql, Integer.class);
        }catch (RuntimeException e){
            System.out.println(" Erro na consulta de ID " + e.getMessage());
        }
        return idMaquina;
    }


    public Integer consultarProjeto(){
        String comandoSql = "SELECT fkProjeto from maquina where idMaquina = %d".formatted(consultarId());
        return fkProjeto = con.queryForObject(comandoSql, Integer.class);
    }

    public Integer consultarEmpresa(){
        String comandoSql = "SELECT fkEmpresa from maquina where idMaquina = %d".formatted(consultarId());
        return fkEmpresa = con.queryForObject(comandoSql, Integer.class);
    }

    public String consultarUsuarioPorId() {
        Integer ultimoIdMaquina = consultarId();
        if(ultimoIdMaquina != null) {
            String querySql = "SELECT usuario from Maquina where idMaquina = %d".formatted(ultimoIdMaquina);
            String usuario = con.queryForObject(querySql, String.class);
            return usuario;
        }
        return "erro ";
    }

    public Looca getLooca() {
        return looca;
    }

    public void setLooca(Looca looca) {
        this.looca = looca;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(Integer idMaquina) {
        this.idMaquina = idMaquina;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public void setSistemaOperacional(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Long getTempoAtividade() {
        return tempoAtividade;
    }

    public void setTempoAtividade(Long tempoAtividade) {
        this.tempoAtividade = tempoAtividade;
    }

    public Integer getFkProjeto() {
        return fkProjeto;
    }

    public void setFkProjeto(Integer fkProjeto) {
        this.fkProjeto = fkProjeto;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    @Override
    public String toString() {
        return """
                idMaquina: %d
                usuario: '%s'
                sistemaOperacional: '%s'
                temperatura: %.2f
                tempoAtividade: %d
                fkProjeto: %d
                fkEmpresa: %d""".formatted(idMaquina,
                usuario,
                sistemaOperacional,
                temperatura,
                tempoAtividade,
                fkProjeto,
                fkEmpresa);
    }
}
