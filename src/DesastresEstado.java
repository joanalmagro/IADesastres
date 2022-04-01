import IA.Desastres.Centro;
import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import IA.Desastres.Grupos;

import java.util.ArrayList;

public class DesastresEstado {

    private static final double COOLDOWN_SALIDA = 10; // min

    private Centros centros;
    private Grupos grupos;


    private ArrayList<ArrayList<ArrayList<DesastresSalidaHelicoptero>>> salidas;
    //      \centros/ \helicop/ \salidas/



    // Constructor
    public DesastresEstado(int ncentros, int nhelicopteros, int ngrupos, int seed) {
        this.centros = new Centros(ncentros, nhelicopteros, seed);
        this.grupos = new Grupos(ngrupos, seed);

        // TODO Solucion Inicial
    }

    // Devuelve un nuevo estado resultando de cambiar la asignacion del
    public DesastresEstado move(int igrupo, int centro0, int helicoptero0, int centroF, int helicopteroF) {
        Grupo grupo = grupos.get(igrupo);
        ArrayList<DesastresSalidaHelicoptero> salidasHelicoptero0 = salidas.get(centro0).get(helicoptero0);
        for (DesastresSalidaHelicoptero salida : salidasHelicoptero0) {

        }
        return null;
    }


    // devuelve el tiempo total en rescatar a todos los grupos,
    // el tiempo total hasta rescatar todos a los grupos de prioridad
    public double[] tiempoRescateTotalYPrioridad() {
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
                ArrayList<DesastresSalidaHelicoptero> salidasHelicoptero = salidas.get(iCentro).get(iHelicop);
                // para cada salida del helicóptero,
                boolean primeraSalida = true;
                for (DesastresSalidaHelicoptero salida : salidasHelicoptero) {
                    if (primeraSalida) {
                        primeraSalida = false;
                    } else {
                        tiempoTotal = COOLDOWN_SALIDA;
                    }
                    // añadimos el tiempo de vuelo vuelo
                    tiempoTotal += salida.tiempo();
                    // marcamos los grupos como rescatados
                    gruposPrioridadNoRescatados.remove(salida.g1);
                    gruposPrioridadNoRescatados.remove(salida.g2);
                    gruposPrioridadNoRescatados.remove(salida.g3);
                }
                if (gruposPrioridadNoRescatados.isEmpty()) {
                    tiempoPrioridad = tiempoTotal;
                }
            }
        }
        return new double[]{tiempoTotal, tiempoPrioridad};
    }

    public String display() {
        return "";
    }

    @Override
    public DesastresEstado clone() throws CloneNotSupportedException {
        // No hace falta clonar centros y grupos ya que son constantes.
        DesastresEstado ret = (DesastresEstado) (super.clone());

        // clonamos las salidas
        ret.salidas = (ArrayList<ArrayList<ArrayList<DesastresSalidaHelicoptero>>>)salidas.clone();
        return ret;
    }
}
