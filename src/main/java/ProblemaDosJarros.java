import lombok.Data;

import java.util.List;

@Data
public class Problema {
    private int jarro4l = 0;
    private int jarro3l = 0;

    public boolean isObjetivo() {
        return jarro4l == 2;
    }

    public List<Problema> gerarProblemasFilhos() {
        return List.of(encherJarro4l(),
                        encherJarro3l(),
                        esvaziarJarro4l(),
                        esvaziarJarro3l(),
                        transferirJarro4lParaJarro3l(),
                        transferirJarro3lParaJarro4l());
    }

    public Problema encherJarro4l() {
        Problema problema = new Problema();
        problema.setJarro4l(4);
        problema.setJarro3l(jarro3l);
        return problema;
    }

    public Problema encherJarro3l() {
        Problema problema = new Problema();
        problema.setJarro4l(jarro4l);
        problema.setJarro3l(3);
        return problema;
    }

    public Problema esvaziarJarro4l() {
        Problema problema = new Problema();
        problema.setJarro4l(0);
        problema.setJarro3l(jarro3l);
        return problema;
    }

    public Problema esvaziarJarro3l() {
        Problema problema = new Problema();
        problema.setJarro4l(jarro4l);
        problema.setJarro3l(0);
        return problema;
    }

    public Problema transferirJarro4lParaJarro3l() {
        Problema problema = new Problema();
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

    public Problema transferirJarro3lParaJarro4l() {
        Problema problema = new Problema();
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
        if(!(obj instanceof Problema problema)) return false;
        return jarro4l == problema.getJarro4l() && jarro3l == problema.getJarro3l();
    }

    @Override
    public String toString() {
        return "(" + jarro4l + ", " + jarro3l + ")";
    }

}
