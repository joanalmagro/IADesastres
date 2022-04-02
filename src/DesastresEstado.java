import IA.Desastres.Centro;
import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import IA.Desastres.Grupos;

import java.util.ArrayList;

public class DesastresEstado {

    public enum ModoInicial {
        ESTUPIDO,
        LLENA_RESCATES,
        PRIMERO_PRIO,
    }

    private static final double COOLDOWN_RESCATE = 10; // min

    private final Centros centros;
    private final Grupos grupos;

    private ArrayList<ArrayList<ArrayList<DesastresRescate>>> rescates;
    //      \centros/ \helicop/ \rescates/

    public Centros getCentros() { return centros; }
    public Grupos getGrupos() { return grupos; }
    public int getNRescates(int centro, int helicoptero) {
        return rescates.get(centro).get(helicoptero).size();
    }


    private DesastresEstado(Centros centros, Grupos grupos) {
        this.centros = centros;
        this.grupos = grupos;
    }

    // Constructor
    public DesastresEstado(int ncentros, int nhelicopteros, int ngrupos, int seed, ModoInicial inicio) {
        this.centros = new Centros(ncentros, nhelicopteros, seed);
        this.grupos = new Grupos(ngrupos, seed);

        rescates = new ArrayList<>();
        // Inicializamos los trayectos de los helicopteros
        for (Centro centro : centros) {
            ArrayList<ArrayList<DesastresRescate>> rescatesCentro = new ArrayList<>();
            for (int i = 0; i < centro.getNHelicopteros(); ++i) {
                rescatesCentro.add(new ArrayList<>());
            }
            rescates.add(rescatesCentro);
        }


        switch (inicio) {
            case ESTUPIDO: // El primer helicoptero del primer grupo lo hace todo
                for (Grupo grupo : grupos) {
                    rescates.get(0).get(0).add(new DesastresRescate(centros.get(0), grupo, null, null));
                }
                break;
            case LLENA_RESCATES: // Igual que el primero, pero intentando juntar grupos en un mismo rescate
                for (Grupo grupo : grupos) {
                    ArrayList<DesastresRescate> rescatesH = rescates.get(0).get(0);
                    boolean asignado = false;
                    for (DesastresRescate rescate : rescatesH) {
                        if (rescate.asignaGrupo(grupo)) {
                            asignado = true;
                            break;
                        }
                    }
                    if (!asignado)
                        rescatesH.add(new DesastresRescate(centros.get(0), grupo, null, null));

                }
            case PRIMERO_PRIO:
                /* TODO! Implementar más soluciones iniciales. Por ejemplo que primero se rescaten los grupos de prioridad */
                break;
        }
    }


    /**
     * @return Si grupo era rescatado por el helicoptero0 del centro0 devuelve un nuevo estado en el que
     * grupo pasa a ser rescatado por el helicopteroF del centroF.
     *
     * Si grupo directamente no era rescatado por el helicoptero0 del centro0 devuelve null.
     */
    public DesastresEstado reasignaGrupo(Grupo grupo, int centro0, int helicoptero0, int centroF, int helicopteroF) {
        ArrayList<DesastresRescate> rescatesHelicoptero0 = this.rescates.get(centro0).get(helicoptero0);

        for (int i = 0; i < rescatesHelicoptero0.size(); ++i) {
            if (rescatesHelicoptero0.get(i).rescataGrupo(grupo)) {
                DesastresEstado resultado = this.clone();
                DesastresRescate rescate0 = resultado.rescates.get(centro0).get(helicoptero0).get(i);
                rescate0.desasignaGrupo(grupo);
                if (rescate0.personasRescatadas() == 0) {
                    resultado.rescates.get(centro0).get(helicoptero0).remove(rescate0);
                }

                ArrayList<DesastresRescate> rescatesHelicopteroF = resultado.rescates.get(centroF).get(helicopteroF);

                boolean asignado = false;
                for (DesastresRescate rescateF : rescatesHelicopteroF) {
                    if (rescateF.asignaGrupo(grupo)) {
                        asignado = true;
                        break;
                    }
                }
                if (!asignado) {
                    rescatesHelicopteroF.add(new DesastresRescate(centros.get(centroF), grupo, null, null));
                }
                return resultado;
            }
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

        double tiempoTotal = 0.0; // en minutos
        double tiempoPrioridad = -1; // tiempo en rescatar los grupos con prioridad

        // para cada centro,
        for (int iCentro = 0; iCentro < centros.size(); ++iCentro) {
            Centro centro = centros.get(iCentro);
            // para cada helicóptero en el centro,
            for (int iHelicop = 0; iHelicop < centro.getNHelicopteros(); ++iHelicop) {
                ArrayList<DesastresRescate> rescatesHelicoptero = rescates.get(iCentro).get(iHelicop);
                // para cada rescate del helicóptero,
                boolean primerRescate = true;
                for (DesastresRescate rescate : rescatesHelicoptero) {
                    if (primerRescate) {
                        primerRescate = false;
                    } else {
                        tiempoTotal += COOLDOWN_RESCATE;
                    }
                    // añadimos el tiempo de vuelo vuelo
                    tiempoTotal += rescate.tiempo();

                    // marcamos los grupos como rescatados
                    for (Grupo g : rescate.grupos())
                        gruposPrioridadNoRescatados.remove(g);
                }
                if (gruposPrioridadNoRescatados.isEmpty() && tiempoPrioridad == -1) {
                    tiempoPrioridad = tiempoTotal;
                }
            }
        }
        return new double[]{tiempoTotal, tiempoPrioridad};
    }

    public String infoGrupos() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < grupos.size(); ++i) {
            Grupo g = grupos.get(i);
            ret.append(String.format("\nGrupo %d: %d pers   %s", i, g.getNPersonas(), g.getPrioridad() == 1? "PRIO" : ""));
        }
        return ret.toString();
    }

    public String infoRescates() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < centros.size(); ++i) {
            ret.append(String.format("\nCentro %d:", i));
            for (int j = 0; j < centros.get(i).getNHelicopteros(); ++j) {
                ret.append(String.format("\n\t Helicoptero %d: ", j));
                for (DesastresRescate res : rescates.get(i).get(j)) {
                    ret.append("{ ");
                    for (Grupo g : res.grupos())
                        ret.append(grupos.indexOf(g)).append(" ");
                    ret.append("}  ");
                }
            }
        }
        return ret.toString();
    }

    public String infoTiempos() {
        double[] tiempos = this.tiempoRescateTotalYPrioridad();
        return String.format("\nTiempo total rescates: %s min\nTiempo hasta todos PRIO rescatados: %s min\n", tiempos[0], tiempos[1]);
    }

    @Override
    public DesastresEstado clone() {
        ArrayList<ArrayList<ArrayList<DesastresRescate>>> rescatesC = new ArrayList<>();
        for (ArrayList<ArrayList<DesastresRescate>> rescatesCentro : rescates) {
            ArrayList<ArrayList<DesastresRescate>> rescatesCentroC = new ArrayList<>();
            for (ArrayList<DesastresRescate> rescatesHelicop : rescatesCentro) {
                ArrayList<DesastresRescate> rescatesHelicopC = new ArrayList<>();
                for (DesastresRescate rescate : rescatesHelicop) {
                    rescatesHelicopC.add(rescate.clone());
                }
                rescatesCentroC.add(rescatesHelicopC);
            }
            rescatesC.add(rescatesCentroC);
        }
        DesastresEstado cloned = new DesastresEstado(centros, grupos);
        cloned.rescates = rescatesC;
        return cloned;
    }
}
