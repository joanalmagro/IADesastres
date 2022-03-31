import IA.Desastres.Centro;
import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import IA.Desastres.Grupos;

import java.util.ArrayList;

public class Estado {

    private static final double CAPACIDAD_HELICOPTERO = 15;

    private static final double VELOCIDAD_HELICOPTERO = 100.0/60; // km/min
    private static final double COOLDOWN_HELICOPTERO = 10; //min
    private static final double TIEMPO_RES_PERSONA = 1; // min
    private static final double FACTOR_HERIDO = 2;

    private Centros centros; // ArrayList<Centro>;
    private Grupos grupos; // ArrayList<Grupo>;


    private ArrayList<ArrayList<ArrayList<Salida>>> salidas;
    //      \centros/ \helicop/ \salidas/

    // Constructor
    public Estado(int ncentros, int nhelicopteros, int ngrupos, int seed) {
        this.centros = new Centros(ncentros, nhelicopteros, seed);
        this.grupos = new Grupos(ngrupos, seed);

        // TODO Solucion Inicial

    }


    // devuelve el tiempo total en rescatar a todos los grupos,
    // el tiempo total hasta rescatar todos a los grupos de prioridad
    private double[] tiempos() {
        // Inicialicamos lo grupos con prioridad no rescatados
        ArrayList<Grupo> gruposPrioridadNoRescatados = new ArrayList<>();
        for (Grupo grupo : grupos) {
            if (grupo.getPrioridad() == 1) {
                gruposPrioridadNoRescatados.add(grupo);
            }
        }

        double tiempoTotal = 0; // en minutos
        double tiempoPrioridad = 0; // tiempo en rescatar los grupos con prioridad

        // para cada centro,
        for (int iCentro = 0; iCentro < centros.size(); ++iCentro) {
            Centro centro = centros.get(iCentro);
            // para cada helicóptero en el centro,
            for (int iHelicop = 0; iHelicop < centro.getNHelicopteros(); ++iHelicop) {
                // para cada salida del helicóptero,
                for (Salida salida : salidas.get(iCentro).get(iHelicop)) {
                    // añadimos el tiempo de vuelo vuelo
                    tiempoTotal += salida.distancia() / VELOCIDAD_HELICOPTERO;

                    // y el tiempo de rescatar a los grupos
                    tiempoTotal += tiempoRescate(salida.g1) + tiempoRescate(salida.g2) + tiempoRescate(salida.g3);

                    // marcamos los grupos como rescatados
                    gruposPrioridadNoRescatados.remove(salida.g1);
                    gruposPrioridadNoRescatados.remove(salida.g2);
                    gruposPrioridadNoRescatados.remove(salida.g3);
                }
                tiempoTotal += COOLDOWN_HELICOPTERO;

                if (gruposPrioridadNoRescatados.isEmpty()) {
                    tiempoPrioridad = tiempoTotal;
                }
            }
        }
        return new double[]{tiempoTotal, tiempoPrioridad};
    }

    private static double tiempoRescate(Grupo g) {
        double tiempo = g.getNPersonas() * TIEMPO_RES_PERSONA;
        if (g.getPrioridad() == 1) tiempo *= FACTOR_HERIDO;
        return tiempo;
    }
}
