import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.*;

public class Main extends PApplet {

    public static List<Ponto> pontos = new ArrayList<>();
    public static Map<Estado, Integer> estados = new HashMap<>();

    public static int coeficienteProfundidade = 1;
    public static int coeficienteHeuristica = 1;
    public Queue<Estado> proximosEstados;
    public Estado estadoRaiz;
    public boolean keepSearching = true;
    public int iteracoes = -1;
    public int estadosExplorados = 0;
    int translateX = 0;
    int translateY = 0;
    double scale = 1.0;

    static public void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(1200, 700);
    }

    public void setup() {
        background(0);
        frameRate(60);
        strokeWeight(2);
        estadoRaiz = new Estado(
                new Ponto(width / 2f, 100, null),
                null,
                new ProblemaQuebraCabeca(
                        new Integer[][]{{7, 3, 1},
                                        {5, 0, 6},
                                        {8, 2, 4}}));
        proximosEstados = new PriorityQueue<>();
        proximosEstados.add(estadoRaiz);
    }

    //Função para desenhar os estados
    public void drawEstado(Estado estado) {
        for (Estado estadoFilho : estado.getEstadosFilhos()) {
            drawEstado(estadoFilho);
        }
        if (estado.isCaminhoSolucao()) {
            fill(0, 120, 0);
            stroke(0, 120, 0);
        } else {
            fill(0, 0, 0, 100);
            stroke(0, 0, 0, 100);
        }
        if (estado.getEstadoPai() != null) {
            line(estado.getPosicao().getX(), estado.getPosicao().getY(),
                    estado.getEstadoPai().getPosicao().getX(), estado.getEstadoPai().getPosicao().getY());
        }
        rect(estado.getPosicao().getX(), estado.getPosicao().getY(), 30, 60);
        fill(255);
        if(scale >= 1.0)
            text(estado.getProblema().toString(), estado.getPosicao().getX(), estado.getPosicao().getY() + 10);
    }

    //Função para atualizar a posição dos estados
    public void updateEstado(Estado estado) {
        estado.getPosicao().update();
//        System.out.println(estado.getPosicao());
        for (Estado estadoFilho : estado.getEstadosFilhos()) {
            updateEstado(estadoFilho);
        }
    }

    public void deletaEstado(Estado estado) {
        estado.getEstadosFilhos().removeIf(estadoFilho -> !estadoFilho.isCaminhoSolucao());
        for (Estado estadoFilho : estado.getEstadosFilhos()) {
            if(estadoFilho.isCaminhoSolucao())
                deletaEstado(estadoFilho);
        }
    }

    public void draw() {
        background(255);
        iteracoes++;
        if(mousePressed){
            translateX += mouseX - pmouseX;
            translateY += mouseY - pmouseY;
        }
        translate(translateX, translateY); //vai deslocar a tela
        scale((float) scale); //vai setar o zoom
        while (keepSearching && !proximosEstados.isEmpty()) {
            Estado estado = proximosEstados.poll();
            assert estado != null;
            estadosExplorados++;
//            System.out.println("Estado atual: " + estado.getProblema().getHeuristica() + " heuristica");
            if (estado.getProblema().isObjetivo()) {
                System.out.println("Solução encontrada!");
                System.out.println("Número de passos para solução: " + estado.getProfudindidade());
                System.out.println("Número de estados explorados: " + estadosExplorados);
                System.out.println("Número de estados gerados: " + pontos.size());
                keepSearching = false;
                estado.setCaminhoSolucao(true);
                deletaEstado(estadoRaiz);
            } else {
                estado.gerarEstadosFilhos();
                proximosEstados.addAll(estado.getEstadosFilhos());
            }
        }
        updateEstado(estadoRaiz);
        drawEstado(estadoRaiz);
    }

    public void keyPressed() {
        if(key == '+') scale *= 1.1;
        if(key == '-') scale /= 1.1;
    }
}
