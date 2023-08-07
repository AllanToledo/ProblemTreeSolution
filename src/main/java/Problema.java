import java.util.List;

public abstract class Problema {

    abstract public String getPasso();
    abstract public boolean isObjetivo();

    abstract public List<Problema> gerarProblemasFilhos();

    abstract public int getHeuristica();
}
