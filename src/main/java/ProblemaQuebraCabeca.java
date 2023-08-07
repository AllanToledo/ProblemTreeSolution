import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProblemaQuebraCabeca extends Problema {

    public Integer[][] tabuleiro = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
    public long identidade = 0L;
    public long objetivo = 876543210L;

    public int euristica = 0;


    @Override
    public String getPasso() {
        return null;
    }

    @Override
    public boolean isObjetivo() {
        return identidade == objetivo;
    }

    @Override
    public int getHeuristica() {
        return euristica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProblemaQuebraCabeca that = (ProblemaQuebraCabeca) o;
        return identidade == that.identidade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identidade);
    }

    public ProblemaQuebraCabeca() {}

    public ProblemaQuebraCabeca(Integer[][] tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    @Override
    public List<Problema> gerarProblemasFilhos() {
        int i = 0, j = 0;
        outerLoop:
        for(i = 0; i < 3; i++)
            for(j = 0; j < 3; j++)
                if(tabuleiro[i][j] == 0) break outerLoop;
        List<Problema> problemas = new ArrayList<>();
        if(i > 0) problemas.add(trocar(i, j, i - 1, j));
        if(i < 2) problemas.add(trocar(i, j, i + 1, j));
        if(j > 0) problemas.add(trocar(i, j, i, j - 1));
        if(j < 2) problemas.add(trocar(i, j, i, j + 1));
        return problemas;
    }

    public ProblemaQuebraCabeca trocar(int i1, int j1, int i2, int j2) {
        ProblemaQuebraCabeca problema = new ProblemaQuebraCabeca();
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                problema.tabuleiro[i][j] = tabuleiro[i][j];
        problema.tabuleiro[i1][j1] = tabuleiro[i2][j2];
        problema.tabuleiro[i2][j2] = 0;
        problema.identidade = 0L;
        problema.euristica = 0;
        for(int i = 2; i >= 0; i--)
            for(int j = 2; j >= 0; j--){
                problema.identidade = problema.identidade * 10 + problema.tabuleiro[i][j];
                long distX = Math.abs(problema.tabuleiro[i][j] % 3 - j);
                long distY = Math.abs(problema.tabuleiro[i][j] / 3 - i);
                problema.euristica += (distX + distY);
            }
        return problema;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++)
                s.append(tabuleiro[i][j]).append(" ");
            s.append("\n");
        }
        return s.toString();
    }
}
