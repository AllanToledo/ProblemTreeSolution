import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Ponto {
    private float x = 0;
    private float y = 0;
    private float velX = 0;
    private float velY = 0;
    @ToString.Exclude
    private Ponto pontoPai;
    @ToString.Exclude
    private List<Ponto> pontosFilhos;
    private float weight = 1;
    public static List<Ponto> pontos = new ArrayList<>();
    static float maxRegistredVelocity = 0f;
    private float mass = 1;

    public Ponto(float x, float y, Ponto pontoPai) {
        this.x = x;
        this.y = y;
        this.pontoPai = pontoPai;
        this.pontosFilhos = new ArrayList<>();
        if (pontoPai != null){
            pontosFilhos.add(pontoPai);
            pontoPai.pontosFilhos.add(this);
        }
        pontos.add(this);
    }

    public void update() {
        if (pontoPai == null)
            return;
        float forcaX = 0;
        float forcaY = 2;
        for (Ponto other : pontos) {
            float dst = dist(other) / 10;
            if (dst == 0) continue;
//            if (dst < 5) dst = 5;
            float force = (10f / (dst * dst));
            float cos = (other.getX() - x) / dst;
            float sin = (other.getY() - y) / dst;
            forcaX -= force * cos;
            forcaY -= force * sin;
        }
        for (Ponto other : pontosFilhos) {
            float dst = dist(other);
            float force = (0.1f * dst);
            force = Math.max(1f, force);
            float cos = (other.getX() - x) / dst;
            float sin = (other.getY() - y) / dst;
            forcaX += force * cos;
            forcaY += force * sin;
        }

        velX = (forcaX / mass) + velX * 0.5f;
        velY = (forcaY / mass) + velY * 0.5f;
        float vel = (float) Math.sqrt(velX * velX + velY * velY);
        float limitSped = 10;
        if(vel > limitSped){
            velX = (velX / vel) * limitSped;
            velY = (velY / vel) * limitSped;
        }

        if(Float.isNaN(velX) || Float.isNaN(velY)){
            velX = 0;
            velY = 0;
        }
        x += velX;
        y += velY;
    }

    public float dist(Ponto ponto) {
        return (float) Math.sqrt(Math.pow(x - ponto.getX(), 2) + Math.pow(y - ponto.getY(), 2));
    }
}
