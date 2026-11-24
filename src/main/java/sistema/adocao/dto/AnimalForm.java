package sistema.adocao.dto;

import org.springframework.web.multipart.MultipartFile;
import sistema.adocao.model.*;

public class AnimalForm {
    private String nome;
    private Especie especie = Especie.CACHORRO;
    private String raca;
    private Porte porte = Porte.MEDIO;
    private Sexo sexo = Sexo.MACHO;
    private int idadeAnos;
    private int idadeMeses;
    private String cidade;
    private boolean convivePets = true;
    private String descricao;
    private boolean vacinado;
    private boolean castrado;
    private boolean vermifugado;
    private String observacoesSaude;
    private StatusAnimal status = StatusAnimal.DISPONIVEL;
    private MultipartFile foto;

    public static AnimalForm de(Animal a) {
        AnimalForm f = new AnimalForm();
        f.nome = a.getNome();
        f.especie = a.getEspecie();
        f.raca = a.getRaca();
        f.porte = a.getPorte();
        f.sexo = a.getSexo();
        f.idadeAnos = a.getIdadeMeses() / 12;
        f.idadeMeses = a.getIdadeMeses() % 12;
        f.cidade = a.getCidade();
        f.convivePets = a.isConvivePets();
        f.descricao = a.getDescricao();
        f.vacinado = a.isVacinado();
        f.castrado = a.isCastrado();
        f.vermifugado = a.isVermifugado();
        f.observacoesSaude = a.getObservacoesSaude();
        f.status = a.getStatus();
        return f;
    }

    public int totalMeses() { return Math.max(0, idadeAnos) * 12 + Math.max(0, idadeMeses); }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Especie getEspecie() { return especie; }
    public void setEspecie(Especie especie) { this.especie = especie; }
    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public Porte getPorte() { return porte; }
    public void setPorte(Porte porte) { this.porte = porte; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public int getIdadeAnos() { return idadeAnos; }
    public void setIdadeAnos(int idadeAnos) { this.idadeAnos = idadeAnos; }
    public int getIdadeMeses() { return idadeMeses; }
    public void setIdadeMeses(int idadeMeses) { this.idadeMeses = idadeMeses; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public boolean isConvivePets() { return convivePets; }
    public void setConvivePets(boolean convivePets) { this.convivePets = convivePets; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public boolean isVacinado() { return vacinado; }
    public void setVacinado(boolean vacinado) { this.vacinado = vacinado; }
    public boolean isCastrado() { return castrado; }
    public void setCastrado(boolean castrado) { this.castrado = castrado; }
    public boolean isVermifugado() { return vermifugado; }
    public void setVermifugado(boolean vermifugado) { this.vermifugado = vermifugado; }
    public String getObservacoesSaude() { return observacoesSaude; }
    public void setObservacoesSaude(String observacoesSaude) { this.observacoesSaude = observacoesSaude; }
    public StatusAnimal getStatus() { return status; }
    public void setStatus(StatusAnimal status) { this.status = status; }
    public MultipartFile getFoto() { return foto; }
    public void setFoto(MultipartFile foto) { this.foto = foto; }
}
