import lombok.Data;

import java.util.List;

@Data
public class ProblemaDosJarros implements Problema {
    private int jarro4l = 0;
    private int jarro3l = 0;

    @Override
    public boolean isObjetivo() {
        return jarro4l == 2;
    }

    @Override
    public List<Problema> gerarProblemasFilhos() {
        return List.of(encherJarro4l(),
                        encherJarro3l(),
                        esvaziarJarro4l(),
                        esvaziarJarro3l(),
                        transferirJarro4lParaJarro3l(),
                        transferirJarro3lParaJarro4l());
    }

    public ProblemaDosJarros encherJarro4l() {
        ProblemaDosJarros problema = new ProblemaDosJarros();
        problema.setJarro4l(4);
        problema.setJarro3l(jarro3l);
        return problema;
    }

    public ProblemaDosJarros encherJarro3l() {
        ProblemaDosJarros problema = new ProblemaDosJarros();
        problema.setJarro4l(jarro4l);
        problema.setJarro3l(3);
        return problema;
    }

    public ProblemaDosJarros esvaziarJarro4l() {
        ProblemaDosJarros problema = new ProblemaDosJarros();
        problema.setJarro4l(0);
        problema.setJarro3l(jarro3l);
        return problema;
    }

    public ProblemaDosJarros esvaziarJarro3l() {
        ProblemaDosJarros problema = new ProblemaDosJarros();
        problema.setJarro4l(jarro4l);
        problema.setJarro3l(0);
        return problema;
    }

    public ProblemaDosJarros transferirJarro4lParaJarro3l() {
        ProblemaDosJarros problema = new ProblemaDosJarros();
        int jarro4l = this.jarro4l;
        int jarro3l = this.jarro3l;
        if (jarro4l + jarro3l <= 3) {
            problema.setJarro4l(0);
            problema.setJarro3l(jarro4l + jarro3l);
        } else {
            problema.setJarro4l(jarro4l - (3 - jarro3l));
            problema.setJarro3l(3);
        }
        return problema;
    }

    public ProblemaDosJarros transferirJarro3lParaJarro4l() {
        ProblemaDosJarros problema = new ProblemaDosJarros();
        int jarro4l = this.jarro4l;
        int jarro3l = this.jarro3l;
        if (jarro4l + jarro3l <= 4) {
            problema.setJarro4l(jarro4l + jarro3l);
            problema.setJarro3l(0);
        } else {
            problema.setJarro4l(4);
            problema.setJarro3l(jarro3l - (4 - jarro4l));
        }
        return problema;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof ProblemaDosJarros problema)) return false;
        return jarro4l == problema.getJarro4l() && jarro3l == problema.getJarro3l();
    }

    @Override
    public int getHeuristica() {
        return 0;
    }

    @Override
    public String toString() {
        return "(" + jarro4l + ", " + jarro3l + ")";
    }

}
