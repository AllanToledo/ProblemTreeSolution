import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Estado implements Comparable<Estado>{
    private Ponto posicao;
    @ToString.Exclude
    private List<Estado> estadosFilhos;
    @ToString.Exclude
    private Estado estadoPai;
    private Problema problema;
    private boolean caminhoSolucao = false;
    private int profudindidade = 0;
    private int euristicaEProfunidade;
    public Estado(Ponto posicao, Estado estadoPai, Problema problema) {
        this.posicao = posicao;
        this.estadoPai = estadoPai;
        if(estadoPai != null)
            this.profudindidade = estadoPai.profudindidade + 1;
        this.problema = problema;
        euristicaEProfunidade = this.profudindidade * Main.coeficienteProfundidade +
                this.problema.getHeuristica() * Main.coeficienteHeuristica;
        estadosFilhos = new ArrayList<>();
    }

    public void gerarEstadosFilhos() {
        if(!estadosFilhos.isEmpty()) return;
        float x = posicao.getX();
        float y = posicao.getY();
        for(Problema problemaFilho: problema.gerarProblemasFilhos()){
            Ponto posicaoFilho = new Ponto(x + ((float) Math.random() * 20f - 10),  y + ((float) Math.random() * 20f - 10), posicao);
            Estado novo = new Estado(posicaoFilho,this, problemaFilho);
            Integer nivelRegistrado = Main.estados.get(novo);
            if(nivelRegistrado == null || novo.profudindidade < nivelRegistrado){
                Main.estados.put(novo, novo.profudindidade);
                estadosFilhos.add(novo);
            }
        }
    }

    public List<Estado> getEstadosFilhos() {
        return estadosFilhos;
    }

    public void setCaminhoSolucao(boolean caminhoSolucao) {
        this.caminhoSolucao = caminhoSolucao;
        if(estadoPai != null) estadoPai.setCaminhoSolucao(caminhoSolucao);
    }

    @Override
    public int compareTo(Estado o) {
        return euristicaEProfunidade - o.euristicaEProfunidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estado estado = (Estado) o;
        return problema.equals(estado.problema);
    }

    @Override
    public int hashCode() {
        return problema.hashCode();
    }
}
