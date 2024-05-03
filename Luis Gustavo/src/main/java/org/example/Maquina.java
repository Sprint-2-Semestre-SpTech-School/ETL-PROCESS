package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class Maquina {
    Looca looca = new Looca();
    private Integer idMaquina;
    private String usuario;
    private String destino;
    private String sistemaOperacional;
    private Double temperatura;
    private Long tempoAtividade;
    private Integer fkProjeto;
    private Integer fkEmpresa;
    private Conexao conexao = new Conexao();
    private JdbcTemplate con = conexao.getConexaoBanco();

    public Maquina(String usuario,
                   String destino,
                   String sistemaOperacional,
                   Double temperatura,
                   Long tempoAtividade,
                   Integer fkProjeto,
                   Integer fkEmpresa) {
        this.looca = looca;
        this.idMaquina = idMaquina;
        this.usuario = usuario;
        this.destino = destino;
        this.sistemaOperacional = sistemaOperacional;
        this.temperatura = temperatura;
        this.tempoAtividade = tempoAtividade;
        this.fkProjeto = fkProjeto;
        this.fkEmpresa = fkEmpresa;
    }
    public Maquina(Integer fkProjeto, Integer fkEmpresa) {
        this.fkProjeto = fkProjeto;
        this.fkEmpresa = fkEmpresa;
    }
    public Maquina() {
    }

    public void capturarDadosMaquina(){
        usuario = System.getProperty("user.name");
        destino = "Extract";
        sistemaOperacional = looca.getSistema().getSistemaOperacional();
        temperatura = looca.getTemperatura().getTemperatura();
        tempoAtividade = looca.getSistema().getTempoDeAtividade() / 3600; // Valor em horas
    }
    public void inserirDadosMaquina(){
        con.update("INSERT INTO Maquina (usuario, destino, sistemaOperacional, temperatura, tempoAtividade, " +
                "fkProjeto, fkEmpresa) values (?, ?, ?, ?, ?, ?, ?)", usuario, destino, sistemaOperacional,
                temperatura, tempoAtividade, fkProjeto, fkEmpresa);
    }
    public Integer consultarId(){
        List<Integer> idsMaquina;

        String comandoSql = ("SELECT idMaquina from Maquina");
        idsMaquina = con.queryForList(comandoSql, Integer.class);
        return idsMaquina.get(idsMaquina.size() - 1);
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

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
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
            destino: '%s'
            sistemaOperacional: '%s'
            temperatura: %.2f
            tempoAtividade: %d
            fkProjeto: %d
            fkEmpresa: %d""".formatted(idMaquina,
                usuario,
                destino,
                sistemaOperacional,
                temperatura,
                tempoAtividade,
                fkProjeto,
                fkEmpresa);
    }
}
