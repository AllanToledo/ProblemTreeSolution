import java.util.*;

public class MainWithoutGraphics {

    public static Queue<Estado> proximosEstados;
    public static Estado estadoRaiz;
    public static boolean keepSearching = true;
    public static int estadosExplorados = 0;

    static public void main(String[] args) {
        estadoRaiz = new Estado(
                null,
                null,
                new ProblemaCuboMagico2x2(new Integer[][]{
                        {0, 0, 5, 6, 0, 0}, //Topo
                        {0, 0, 1, 3, 0, 0},
                        {2, 2, 3, 4, 1, 5}, //Frente
                        {4, 1, 6, 5, 4, 1},
                        {0, 0, 4, 6, 0, 0}, //Baixo
                        {0, 0, 5, 6, 0, 0},
                        {0, 0, 3, 2, 0, 0}, //Atras
                        {0, 0, 3, 2, 0, 0}}));
        proximosEstados = new PriorityQueue<>();
        proximosEstados.add(estadoRaiz);
        while (keepSearching && !proximosEstados.isEmpty()) {
            Estado estado = proximosEstados.poll();
            assert estado != null;
            estadosExplorados++;
            if (estado.getProblema().isObjetivo()) {
                System.out.println("Solução encontrada!");
                System.out.println("Número de passos para solução: " + estado.getProfudindidade());
                System.out.println("Número de estados explorados: " + estadosExplorados);
                keepSearching = false;
                estado.setCaminhoSolucao(true);
                deletaEstado(estadoRaiz);
            } else {
                estado.gerarEstadosFilhos();
                proximosEstados.addAll(estado.getEstadosFilhos());
            }
        }
        Estado aux = estadoRaiz;
        System.out.println(aux.getProblema().getPasso());
        System.out.println(((ProblemaCuboMagico2x2) aux.getProblema()).toStringEmoji());
        while (!aux.getEstadosFilhos().isEmpty()) {
            for (Estado filho : aux.getEstadosFilhos()) {
                if (filho.isCaminhoSolucao()) {
                    aux = filho;
                    break;
                }
            }
            System.out.println(aux.getProblema().getPasso());
           // System.out.println(((ProblemaCuboMagico2x2) aux.getProblema()).toStringEmoji());
        }
        System.out.println(((ProblemaCuboMagico2x2) aux.getProblema()).toStringEmoji());

    }

    public static void deletaEstado(Estado estado) {
        estado.getEstadosFilhos().removeIf(estadoFilho -> !estadoFilho.isCaminhoSolucao());
        for (Estado estadoFilho : estado.getEstadosFilhos()) {
            if (estadoFilho.isCaminhoSolucao())
                deletaEstado(estadoFilho);
        }
    }

}
