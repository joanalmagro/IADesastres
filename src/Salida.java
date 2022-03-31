import IA.Desastres.Centro;
import IA.Desastres.Grupo;

public class Salida {
    Centro c;
    Grupo g1;
    Grupo g2;
    Grupo g3;

    public Salida(Centro c, Grupo g1, Grupo g2, Grupo g3) {
        this.c = c;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
    }

    public double distancia() {
        // posibles distancias permutando el orden de los grupos
        double[] ds = new double[] {
                distanciaSalidaImpl(c, g1, g2, g3),
                distanciaSalidaImpl(c, g2, g3, g1),
                distanciaSalidaImpl(c, g3, g1, g2),
                distanciaSalidaImpl(c, g1, g3, g2),
                distanciaSalidaImpl(c, g2, g1, g3),
                distanciaSalidaImpl(c, g3, g2, g1)
        };

        // devolvemos el mínimo
        double min = Double.MAX_VALUE;
        for (int i = 0; i < 6; ++i) {
            min = Math.min(min, ds[i]);
        }
        return min;
    }

    // tiempo en hacer la salida c -> g1 -> g2 -> g3 -> c
    private static double distanciaSalidaImpl(Centro c, Grupo g1, Grupo g2, Grupo g3) {
        int x0 = c.getCoordX(), y0 = c.getCoordY();
        int x1 = g1.getCoordX(), y1 = g1.getCoordY();
        int x2 = g2.getCoordX(), y2 = g2.getCoordY();
        int x3 = g3.getCoordX(), y3 = g3.getCoordY();
        return d(x0, y0, x1, y1) +   // Centro  -> Grupo 1
                d(x1, y1, x2, y2) +  // Grupo 1 -> Grupo 2
                d(x2, y2, x3, y3) +  // Grupo 2 -> Grupo 3
                d(x3, y3, x0, y0);
    }


    // distancia euclidea entre (x1, y1) y (x2, y2)
    private static double d(int x1, int y1, int x2, int y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx*dx + dy*dy);
    }
}
