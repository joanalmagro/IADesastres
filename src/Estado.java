import IA.Desastres.Centro;
import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import IA.Desastres.Grupos;

import java.util.ArrayList;

public class Estado {

    class Salida {
        Grupo g1;
        Grupo g2;
        Grupo g3;

        public Salida(Grupo g1, Grupo g2, Grupo g3) {
            this.g1 = g1;
            this.g2 = g2;
            this.g3 = g3;
        }
    }

    enum Heuristico {

    }

    private static final double CAPACIDAD_HELICOPTERO = 15;

    private static final double VELOCIDAD_HELICOPTERO = 100.0/60; // km/min
    private static final double COOLDOWN_HELICOPTERO = 10; //min
    private static final double TIEMPO_RES_PERSONA = 1; // min
    private static final double FACTOR_HERIDO = 2;
    // Dades:
    //    velocidad_helicoptero = 100km/h
    //    cooldown_helicoptero  = 10mins
    //    capacitat_helicoptero = 15pers
    //    temps/pers            = 2min si prioritat(grup) = 1, 1min otherwise

    private Centros centros; // ArrayList<Centro>;
    private Grupos grupos; // ArrayList<Grupo>;


    private ArrayList<ArrayList<ArrayList<Salida>>> salidas;
    // -> Estat


    public double tiempoTotal() {
        double tiempo = 0; // en minutos
        // para cada centro,
        for (int iCentro = 0; iCentro < centros.size(); ++iCentro) {
            Centro centro = centros.get(iCentro);
            // para cada helicóptero en el centro,
            for (int iHelicop = 0; iHelicop < centro.getNHelicopteros(); ++iHelicop) {
                // para cada salida del helicóptero,
                for (Salida salida : salidas.get(iCentro).get(iHelicop)) {
                    // añadimos el tiempo de vuelo vuelo
                    tiempo += distanciaSalida(centro, salida) / VELOCIDAD_HELICOPTERO;

                    // y el tiempo de rescatar a los grupos
                    tiempo += tiempoRescate(salida.g1) + tiempoRescate(salida.g2) + tiempoRescate(salida.g3);
                }
                tiempo += COOLDOWN_HELICOPTERO;
            }
        }
        return tiempo;
    }

    private double tiempoRescate(Grupo g) {
        double tiempo = g.getNPersonas() * TIEMPO_RES_PERSONA;
        if (g.getPrioridad() == 1) tiempo *= 2;
        return tiempo;
    }

    private double distanciaSalida(Centro c, Salida s) {
        double[] ds = new double[6];
        ds[0] = distanciaSalidaImpl(c, s.g1, s.g2, s.g3);
        ds[0] = distanciaSalidaImpl(c, s.g1, s.g2, s.g3);
        ds[0] = distanciaSalidaImpl(c, s.g1, s.g2, s.g3);
        ds[0] = distanciaSalidaImpl(c, s.g1, s.g2, s.g3);
        ds[0] = distanciaSalidaImpl(c, s.g1, s.g2, s.g3);
        ds[0] = distanciaSalidaImpl(c, s.g1, s.g2, s.g3);

        double min = Double.MAX_VALUE;
        for (int i = 0; i < 6; ++i) {
            min = Math.min(min, ds[i]);
        }
        return min;
    }

    // Tiempo en hacer el trayecto
    private double distanciaSalidaImpl(Centro c, Grupo g1, Grupo g2, Grupo g3) {
        int x0 = c.getCoordX(), y0 = c.getCoordY();
        int x1 = g1.getCoordX(), y1 = g1.getCoordY();
        int x2 = g2.getCoordX(), y2 = g2.getCoordY();
        int x3 = g3.getCoordX(), y3 = g3.getCoordY();
        return d(x0, y0, x1, y1) +   // Centro -> Grupo 1
                d(x1, y1, x2, y2) +  // Grupo 1 -> Grupo 2
                d(x2, y2, x3, y3) +  // Grupo 2 -> Grupo 3
                d(x3, y3, x0, y0);
    }

    private double d(int x1, int y1, int x2, int y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx*dx + dy*dy);
    }
}
