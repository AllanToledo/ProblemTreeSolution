import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
public class ProblemaCuboMagico2x2 extends Problema {

    final short BRANCO = 1;
    final short AZUL = 3;
    final short VERDE = 6;
    final short AMARELO = 5;
    final short VERMELHO = 2;
    final short LARANJA = 4;

    String passo;
    short lastOp = 0;
    short[][] matriz = new short[8][6];
//    short[][] matriz =
//            {{0, 0, 1, 1, 0, 0}, //Topo
//            {0, 0, 1, 1, 0, 0},
//            {2, 2, 3, 3, 4, 4}, //Frente
//            {2, 2, 3, 3, 4, 4},
//            {0, 0, 5, 5, 0, 0}, //Baixo
//            {0, 0, 5, 5, 0, 0},
//            {0, 0, 6, 6, 0, 0}, //Atras
//            {0, 0, 6, 6, 0, 0}};

    long identity;
    long objetive = 752077003448015L;

    short heuristica = 0;

    private void generateIdentityAndHeuristica() {
        identity = 0;
        heuristica = 0;
        int ki, jl;
        for (short i = 0; i < 8; i++) {
            for (short j = 0; j < 6; j++) {
                if (matriz[i][j] == 0) continue;
                identity *= 6;
                identity += matriz[i][j] - 1;
                heuristica += correctPlace(i, j) ? -1 : 1;
                for (short k = -1; k < 2; k++) {
                    for (short l = -1; l < 2; l++) {
                        ki = k + i;
                        jl = j + l;
                        if (((ki) >> 2) << 3 != (i >> 2) << 3) continue;
                        if (((jl) >> 2) * 6 != (j >> 2) * 6) continue;
                        if ((ki) < 0 || (ki) > 7) continue;
                        if ((jl) < 0 || (jl) > 5) continue;
                        if (l == 0 && k == 0) continue;
                        heuristica += (matriz[i][j] != matriz[ki][jl]) ? 1 : -1;
                    }
                }
            }
        }
    }

    private boolean correctPlace(short i, short j) {
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
        short[][] matriz = new short[8][6];
        for (short i = 0; i < 8; i++) {
            for (short j = 0; j < 6; j++) {
                matriz[i][j] = toshorteger(emojiMatrix[i][j]);
            }
        }
        this.matriz = matriz;
        generateIdentityAndHeuristica();
    }

    public ProblemaCuboMagico2x2(short[][] matriz) {
        if (matriz.length != 8 || matriz[0].length != 6)
            throw new IllegalArgumentException("Matriz deve ser 8x6");
        if (matriz[1][2] != 1 && matriz[2][1] != 2 && matriz[3][2] != 3)
            throw new IllegalArgumentException("Cubo não está orientado corretamente!");
        for (short i = 0; i < 8; i++)
            for (short j = 0; j < 6; j++)
                if (matriz[i][j] < 0 || matriz[i][j] > 6)
                    throw new IllegalArgumentException("Matriz deve conter apenas números de 0 a 6");
        this.matriz = matriz;
        generateIdentityAndHeuristica();
    }

    public ProblemaCuboMagico2x2() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ProblemaCuboMagico2x2 problema)) return false;

        return identity == problema.identity;
    }

    @Override
    public String getPasso() {
        if (passo == null) return "Estado inicial";
        return passo;
    }

    @Override
    public boolean isObjetivo() {
        return identity == objetive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity);
    }

    @Override
    public List<Problema> gerarProblemasFilhos() {
        List<Problema> filhos = new ArrayList<>(7);
//        if (lastOp != -1)
        filhos.add(rotacaoLateralHoraria());
//        if (lastOp != 1)
        filhos.add(rotacaoLateralAntiHoraria());
//        if (lastOp != -2)
        filhos.add(rotacaoBaseHoraria());
//        if (lastOp != 2)
        filhos.add(rotacaoBaseAntiHoraria());
//        if (lastOp != -3)
        filhos.add(rotacaoFundoHoraria());
//        if (lastOp != 3)
        filhos.add(rotacaoFundoAntiHoraria());
        return filhos;
    }

    Problema rotacaoFundoAntiHoraria() {
        ProblemaCuboMagico2x2 problema = new ProblemaCuboMagico2x2();
        problema.lastOp = -3;
        for (short i = 0; i < 8; i++)
            for (short j = 0; j < 6; j++)
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

        short last;
        for (short step = 0; step < 2; step++) {
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
        problema.lastOp = 3;
        for (short i = 0; i < 8; i++)
            for (short j = 0; j < 6; j++)
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

        short last;
        for (short step = 0; step < 2; step++) {
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
        problema.lastOp = -2;
        for (short i = 0; i < 8; i++)
            for (short j = 0; j < 6; j++)
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

        short first;
        for (short step = 0; step < 2; step++) {
            first = problema.matriz[3][0];
            for (short j = 7; j >= 0; j--) {
                short aux;
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
        problema.lastOp = 2;
        for (short i = 0; i < 8; i++)
            for (short j = 0; j < 6; j++)
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
        short last;
        for (short step = 0; step < 2; step++) {
            last = problema.matriz[6][2];
            for (short j = 0; j < 8; j++) {
                short aux;
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
        problema.lastOp = -1;
        for (short i = 0; i < 8; i++)
            for (short j = 0; j < 6; j++)
                problema.matriz[i][j] = matriz[i][j];

        //Move 2 elementos da coluna para baixo
        short last; //get last element
        for (short step = 0; step < 2; step++) {
            last = problema.matriz[7][3];
            for (short i = 0; i < 8; i++) {
                short aux = problema.matriz[i][3];
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
        problema.lastOp = 1;
        for (short i = 0; i < 8; i++)
            for (short j = 0; j < 6; j++)
                problema.matriz[i][j] = matriz[i][j];

        //Move 2 elementos da coluna para baixo
        short first;
        for (short step = 0; step < 2; step++) {
            first = problema.matriz[0][3]; //pega o primeiro elemento
            for (short i = 7; i >= 0; i--) {
                short aux = problema.matriz[i][3];
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

    public String toEmoji(short i) {
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

    public short toshorteger(String i) {
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
        for (short i = 0; i < 8; i++) {
            sb.append("[" + toEmoji(matriz[i][0]));
            for (short j = 1; j < 6; j++) {
                sb.append("," + toEmoji(matriz[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (short i = 0; i < 8; i++) {
            sb.append(Arrays.toString(matriz[i]));
            sb.append("\n");
        }
        return sb.toString();
    }
}
