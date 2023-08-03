import java.util.List;

public interface Problema {

    public boolean isObjetivo();

    public List<Problema> gerarProblemasFilhos();

    public int getHeuristica();
}
