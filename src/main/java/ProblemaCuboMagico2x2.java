import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProblemaCuboMagico2x2 extends Problema {

    final int BRANCO = 1;
    final int AZUL = 3;
    final int VERDE = 6;
    final int AMARELO = 5;
    final int VERMELHO = 2;
    final int LARANJA = 4;

    String passo;

    Integer[][] matriz =
            {{0, 0, 1, 1, 0, 0}, //Topo
            {0, 0, 1, 1, 0, 0},
            {2, 2, 3, 3, 4, 4}, //Frente
            {2, 2, 3, 3, 4, 4},
            {0, 0, 5, 5, 0, 0}, //Baixo
            {0, 0, 5, 5, 0, 0},
            {0, 0, 6, 6, 0, 0}, //Atras
            {0, 0, 6, 6, 0, 0}};

    short[] identity;
    short[] objetive = {1335, 1335, 1446, 1446, 2256, 2256};

    int heuristica = 0;

    private void generateIdentityAndHeuristica() {
        short count = 0;
        identity = new short[]{0, 0, 0, 0, 0, 0};
        heuristica = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                if (matriz[i][j] == 0) continue;
                identity[count % 6] *= 10;
                identity[count % 6] += matriz[i][j];
                count++;
                heuristica += correctPlace(i, j) ? 0 : 1;
            }
        }
    }

    private boolean correctPlace(int i, int j) {
        return switch (matriz[i][j]) {
            case 1 -> i < 2;
            case 2 -> j < 2;
            case 3 -> (i > 1 && i < 4) && (j > 1 && j < 4);
            case 4 -> j >= 4;
            case 5 -> i >= 4 && i <= 5;
            case 6 -> i >= 6 && i <= 7;
            default -> false;
        };
    }

    public ProblemaCuboMagico2x2(String[][] emojiMatrix) {
        Integer[][] matriz = new Integer[8][6];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                matriz[i][j] = toInteger(emojiMatrix[i][j]);
            }
        }
        this.matriz = matriz;
        generateIdentityAndHeuristica();
    }

    public ProblemaCuboMagico2x2(Integer[][] matriz) {
        if (matriz.length != 8 || matriz[0].length != 6)
            throw new IllegalArgumentException("Matriz deve ser 8x6");
        if (matriz[1][2] != 1 && matriz[2][1] != 2 && matriz[3][2] != 3)
            throw new IllegalArgumentException("Cubo não está orientado corretamente!");
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 6; j++)
                if (matriz[i][j] < 0 || matriz[i][j] > 6)
                    throw new IllegalArgumentException("Matriz deve conter apenas números de 0 a 6");
        this.matriz = matriz;
        generateIdentityAndHeuristica();
    }

    public ProblemaCuboMagico2x2() {
        generateIdentityAndHeuristica();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ProblemaCuboMagico2x2 problema)) return false;
        for (int i = 0; i < 6; i++)
            if (identity[i] != problema.identity[i]) return false;

        return true;
    }

    @Override
    public String getPasso() {
        if(passo == null) return "Estado inicial";
        return passo;
    }

    @Override
    public boolean isObjetivo() {
        for (int i = 0; i < 6; i++)
            if (identity[i] != objetive[i]) return false;
        return true;
    }

    @Override
    public List<Problema> gerarProblemasFilhos() {
        List<Problema> filhos = new ArrayList<>(6);
        filhos.add(rotacaoLateralHoraria());
        filhos.add(rotacaoLateralAntiHoraria());
        filhos.add(rotacaoBaseHoraria());
        filhos.add(rotacaoBaseAntiHoraria());
        filhos.add(rotacaoFundoHoraria());
        filhos.add(rotacaoFundoAntiHoraria());
        return filhos;
    }

    Problema rotacaoFundoAntiHoraria() {
        ProblemaCuboMagico2x2 problema = new ProblemaCuboMagico2x2();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 6; j++)
                problema.matriz[i][j] = matriz[i][j];

        /*
            Entretando, a coluna precisa se mover duas vezes,
        por que a lateral a gente além de trocas as peças de movimento, elas também estão
        sendo giradas, então a coluna precisa de 2 movimentos também para acompanhar
               [...][0,2]->[0,3][...]
               [...][...]  [...][...]
          [2,0][...][...]  [...][...][2,5]
            ^                          v
          [3,0][...][...]  [...][...][3,5]
               [...][...]  [...][...]
               [...][5,2]<-[5,3][...]
               [...][6,2]<-[6,3][...]
                      v      ^
               [...][7,2]->[7,3][...]
         */

        int last;
        for (int step = 0; step < 2; step++) {
            last = problema.matriz[2][0];
            problema.matriz[2][0] = problema.matriz[3][0];
            problema.matriz[3][0] = problema.matriz[5][2];
            problema.matriz[5][2] = problema.matriz[5][3];
            problema.matriz[5][3] = problema.matriz[3][5];
            problema.matriz[3][5] = problema.matriz[2][5];
            problema.matriz[2][5] = problema.matriz[0][3];
            problema.matriz[0][3] = problema.matriz[0][2];
            problema.matriz[0][2] = last;
        }

        last = problema.matriz[6][2];
        problema.matriz[6][2] = problema.matriz[6][3];
        problema.matriz[6][3] = problema.matriz[7][3];
        problema.matriz[7][3] = problema.matriz[7][2];
        problema.matriz[7][2] = last;

        problema.passo = "Rotação Esquerda Cima";
        problema.generateIdentityAndHeuristica();
        return problema;
    }

    Problema rotacaoFundoHoraria() {
        ProblemaCuboMagico2x2 problema = new ProblemaCuboMagico2x2();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 6; j++)
                problema.matriz[i][j] = matriz[i][j];

        /*
            Entretando, a coluna precisa se mover duas vezes,
        por que a lateral a gente além de trocas as peças de movimento, elas também estão
        sendo giradas, então a coluna precisa de 2 movimentos também para acompanhar
               [...][0,2]<-[0,3][...]
               [...][...]  [...][...]
          [2,0][...][...]  [...][...][2,5]
            v                          ^
          [3,0][...][...]  [...][...][3,5]
               [...][...]  [...][...]
               [...][5,2]->[5,3][...]
               [...][6,2]->[6,3][...]
                      ^      v
               [...][7,2]<-[7,3][...]
         */

        int last;
        for (int step = 0; step < 2; step++) {
            last = problema.matriz[2][0];
            problema.matriz[2][0] = problema.matriz[0][2];
            problema.matriz[0][2] = problema.matriz[0][3];
            problema.matriz[0][3] = problema.matriz[2][5];
            problema.matriz[2][5] = problema.matriz[3][5];
            problema.matriz[3][5] = problema.matriz[5][3];
            problema.matriz[5][3] = problema.matriz[5][2];
            problema.matriz[5][2] = problema.matriz[3][0];
            problema.matriz[3][0] = last;
        }

        last = problema.matriz[6][2];
        problema.matriz[6][2] = problema.matriz[7][2];
        problema.matriz[7][2] = problema.matriz[7][3];
        problema.matriz[7][3] = problema.matriz[6][3];
        problema.matriz[6][3] = last;


        problema.passo = "Rotação Esquerda Baixo";
        problema.generateIdentityAndHeuristica();
        return problema;
    }

    Problema rotacaoBaseAntiHoraria() {
        ProblemaCuboMagico2x2 problema = new ProblemaCuboMagico2x2();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 6; j++)
                problema.matriz[i][j] = matriz[i][j];

                /* Explicação do movimento
        Entretando, a coluna precisa se mover duas vezes,
        por que a lateral a gente além de trocas as peças de movimento, elas também estão
        sendo giradas, então a coluna precisa de 2 movimentos também para acompanhar
        [3,0]<-[3,1]<-[3,2]<-[3,3]<-[3,4]<-[3,5]<-[6,3]
               [...]  [4,2]<-[4,3]  [...]
                        v      ^
               [...]  [5,2]->[5,3]  [...]
               [3,0]->[6,2]->[6,3]->[3,5]
               [...]  [...]  [...]  [...]
         */
        //Gira a face lateral

        int first;
        for (int step = 0; step < 2; step++) {
            first = problema.matriz[3][0];
            for (int j = 7; j >= 0; j--) {
                int aux;
                if (j <= 5) {
                    aux = problema.matriz[3][j];
                    problema.matriz[3][j] = first;
                } else {
                    aux = problema.matriz[6][9 - j];
                    problema.matriz[6][9 - j] = first;
                }
                first = aux;
            }
        }

        first = problema.matriz[4][2];
        problema.matriz[4][2] = problema.matriz[4][3];
        problema.matriz[4][3] = problema.matriz[5][3];
        problema.matriz[5][3] = problema.matriz[5][2];
        problema.matriz[5][2] = first;


        problema.passo = "Rotação Base Esquerda";
        problema.generateIdentityAndHeuristica();
        return problema;
    }

    Problema rotacaoBaseHoraria() {
        ProblemaCuboMagico2x2 problema = new ProblemaCuboMagico2x2();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 6; j++)
                problema.matriz[i][j] = matriz[i][j];

                /* Explicação do movimento
        Entretando, a coluna precisa se mover duas vezes,
        por que a lateral a gente além de trocas as peças de movimento, elas também estão
        sendo giradas, então a coluna precisa de 2 movimentos também para acompanhar
        [3,0]->[3,1]->[3,2]->[3,3]->[3,4]->[3,5]->[6,3]
               [...]  [4,2]->[4,3]  [...]
                        ^      v
               [...]  [5,2]<-[5,3]  [...]
               [3,0]<-[6,2]<-[6,3]<-[3,5]
               [...]  [...]  [...]  [...]
         */
        int last;
        for (int step = 0; step < 2; step++) {
            last = problema.matriz[6][2];
            for (int j = 0; j < 8; j++) {
                int aux;
                if (j <= 5) {
                    aux = problema.matriz[3][j];
                    problema.matriz[3][j] = last;
                } else {
                    aux = problema.matriz[6][9 - j];
                    problema.matriz[6][9 - j] = last;
                }
                last = aux;
            }
        }

        last = problema.matriz[4][2];
        problema.matriz[4][2] = problema.matriz[5][2];
        problema.matriz[5][2] = problema.matriz[5][3];
        problema.matriz[5][3] = problema.matriz[4][3];
        problema.matriz[4][3] = last;


        problema.passo = "Rotação Base Direita";
        problema.generateIdentityAndHeuristica();
        return problema;
    }

    Problema rotacaoLateralAntiHoraria() {
        ProblemaCuboMagico2x2 problema = new ProblemaCuboMagico2x2();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 6; j++)
                problema.matriz[i][j] = matriz[i][j];

        //Move 2 elementos da coluna para baixo
        int last; //get last element
        for (int step = 0; step < 2; step++) {
            last = problema.matriz[7][3];
            for (int i = 0; i < 8; i++) {
                int aux = problema.matriz[i][3];
                problema.matriz[i][3] = last;
                last = aux;
            }
        }

        /* Explicação do movimento
        Entretando, a coluna precisa se mover duas vezes,
        por que a lateral a gente além de trocas as peças de movimento, elas também estão
        sendo giradas, então a coluna precisa de 2 movimentos também para acompanhar
        [...][...]  [...]
          V
        [...][2,4]<-[2,5]
          V    v      ^
        [...][3,4]->[3,5]
          V
        [...][...]  [...]
         */
        //Gira a face lateral
        last = problema.matriz[3][5];
        problema.matriz[3][5] = problema.matriz[3][4];
        problema.matriz[3][4] = problema.matriz[2][4];
        problema.matriz[2][4] = problema.matriz[2][5];
        problema.matriz[2][5] = last;


        problema.passo = "Rotação Direita Baixo";
        //Precisa gerar a identidade do problema
        problema.generateIdentityAndHeuristica();
        return problema;
    }

    Problema rotacaoLateralHoraria() {
        ProblemaCuboMagico2x2 problema = new ProblemaCuboMagico2x2();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 6; j++)
                problema.matriz[i][j] = matriz[i][j];

        //Move 2 elementos da coluna para baixo
        int first;
        for (int step = 0; step < 2; step++) {
            first = problema.matriz[0][3]; //pega o primeiro elemento
            for (int i = 7; i >= 0; i--) {
                int aux = problema.matriz[i][3];
                problema.matriz[i][3] = first;
                first = aux;
            }
        }

        /* Explicação do movimento
        Entretando, a coluna precisa se mover duas vezes,
        por que a lateral a gente além de trocas as peças de movimento, elas também estão
        sendo giradas, então a coluna precisa de 2 movimentos também para acompanhar
        [...][...]  [...]
          ^
        [...][2,4]->[2,5]
          ^    ^      v
        [...][3,4]<-[3,5]
          ^
        [...][...]  [...]
         */
        //Gira a face lateral
        first = problema.matriz[3][5];
        problema.matriz[3][5] = problema.matriz[2][5];
        problema.matriz[2][5] = problema.matriz[2][4];
        problema.matriz[2][4] = problema.matriz[3][4];
        problema.matriz[3][4] = first;


        problema.passo = "Rotação Direita Cima";
        //Precisa gerar a identidade do problema
        problema.generateIdentityAndHeuristica();
        return problema;
    }

    @Override
    public int getHeuristica() {
        return heuristica;
    }

    public String toEmoji(int i) {
        return switch (i) {
            case BRANCO -> "🤍";
            case AMARELO -> "💛";
            case VERMELHO -> "❤️";
            case LARANJA -> "🧡";
            case VERDE -> "💚";
            case AZUL -> "💙";
            default -> "🖤";
        };
    }

    public Integer toInteger(String i) {
        return switch (i) {
            case "🤍" -> BRANCO;
            case "💛" -> AMARELO;
            case "❤️" -> VERMELHO;
            case "🧡" -> LARANJA;
            case "💚" -> VERDE;
            case "💙" -> AZUL;
            default -> 0;
        };
    }

    public String toStringEmoji() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append("[" + toEmoji(matriz[i][0]));
            for (int j = 1; j < 6; j++) {
                sb.append("," + toEmoji(matriz[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(Arrays.toString(matriz[i]));
            sb.append("\n");
        }
        return sb.toString();
    }
}
