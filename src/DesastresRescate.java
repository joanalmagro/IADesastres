import IA.Desastres.Centro;
import IA.Desastres.Grupo;

import java.util.ArrayList;
import java.util.List;

// Representa un rescate de un helicóptero. El helicoptero se encuentra en el centro c y rescata a los grupos
// no nulos de entre g1, g2, y g3.
class DesastresRescate {

    private static final int CAPACIDAD_HELICOPTERO = 15;
    private static final double VELOCIDAD_HELICOPTERO = 100.0/60.0; // km/min
    private static final double TIEMPO_RESCATE_PERSONA = 1.0; // min
    private static final double FACTOR_HERIDO = 2.0; // se tarda el doble en rescatar a un herido

    private final Centro c;
    private Grupo g1;
    private Grupo g2;
    private Grupo g3;

    public DesastresRescate(Centro c, Grupo g1, Grupo g2, Grupo g3) {
        this.c = c;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
    }

    // Lista con los grupos (possiblemente nulos) de este rescate
    public List<Grupo> grupos() {
        List<Grupo> ret = new ArrayList<>();
        ret.add(g1);
        ret.add(g2);
        ret.add(g3);
        return ret;
    }

    @Override
    public DesastresRescate clone() {
        return new DesastresRescate(c, g1, g2, g3);
    }

    // Numero total de personas rescatadas en este rescate
    public int personasRescatadas() {
        return (g1 == null ? 0 : g1.getNPersonas()) +
                (g2 == null ? 0 : g2.getNPersonas()) +
                (g3 == null ? 0 : g3.getNPersonas());
    }

    // Intenta asignar el grupo g a los grupos rescatados. Si ha sido posible, devuelve true
    public boolean asignaGrupo(Grupo g) {
        if (g.getNPersonas() + personasRescatadas() > CAPACIDAD_HELICOPTERO || rescataGrupo(g)) {
            return false;
        }
        if (g1 == null) {
            g1 = g;
        } else if (g2 == null) {
            g2 = g;
        } else if (g3 == null) {
            g3 = g;
        } else {
            return false;
        }
        return true;
    }

    // Devuelve true si este rescate rescata al grupo g
    public boolean rescataGrupo(Grupo g) {
        return g == null || g.equals(g1) || g.equals(g2) || g.equals(g3);
    }

    // Desasigna el grupo g a este rescate
    public void desasignaGrupo(Grupo g) {
        if (g.equals(g1)) {
            g1 = null;
        } else if (g.equals(g2)) {
            g2 = null;
        } else if (g.equals(g3)) {
            g3 = null;
        }
    }

    // Tiempo total en hacer el rescate de los grupos de este rescate
    public double tiempo() {
        return tiempoRescate() + tiempoVuelo();
    }

    private double tiempoRescate() {
        double tiempo = 0.0;
        for (Grupo g : grupos()) {
            if (g != null) {
                double factor = g.getPrioridad() == 1? FACTOR_HERIDO : 1;
                tiempo += factor * g.getNPersonas() * TIEMPO_RESCATE_PERSONA;
            }
        }
        return tiempo;
    }

    private double tiempoVuelo() {
        List<Grupo> grupos = new ArrayList<>();
        if (g1 != null) grupos.add(g1);
        if (g2 != null) grupos.add(g2);
        if (g3 != null) grupos.add(g3);

        // Posibles trayectos permutando el orden de los grupos no nulos. buscamos el óptimo
        List<List<Grupo>> trayectos = permute(grupos);

        double distanciaOptimaGrupos = Double.MAX_VALUE;
        List<Grupo> trayectoOptimo = new ArrayList<>();

        for (List<Grupo> trayecto : trayectos) {
            double distanciaTemp = 0;
            for (int i = 1; i < trayecto.size(); ++i) {
                distanciaTemp += d(trayecto.get(i - 1).getCoordX(), trayecto.get(i - 1).getCoordY(),
                        trayecto.get(i).getCoordX(), trayecto.get(i).getCoordY());
            }
            if (distanciaTemp < distanciaOptimaGrupos) {
                distanciaOptimaGrupos = distanciaTemp;
                trayectoOptimo = trayecto;
            }
        }

        if (trayectoOptimo.isEmpty()) {
            return 0;
        } else {
            Grupo g0 = trayectoOptimo.get(0);
            Grupo gf = trayectoOptimo.get(trayectoOptimo.size() - 1);
            double distancia = d(c.getCoordX(), c.getCoordY(), g0.getCoordX(), g0.getCoordY()) +
                    distanciaOptimaGrupos +
                    d(gf.getCoordX(), gf.getCoordY(), c.getCoordX(), c.getCoordY());
            return distancia / VELOCIDAD_HELICOPTERO;
        }
    }


    // Utils

    // distancia euclidea entre (x1, y1) y (x2, y2)
    private static double d(int x1, int y1, int x2, int y2) { return Math.hypot(x1 - x2, y1 - y2); }

    // [1,2,3] -> [ [1,2,3],[2,3,1],[3,1,2],[1,3,2],[2,1,3],[3,2,1] ]
    private static<T> List<List<T>> permute(List<T> arr) {
        List<List<T>> list = new ArrayList<>();
        permuteImpl(list, new ArrayList<>(), arr);
        return list;
    }

    private static<T> void permuteImpl(List<List<T>> list, List<T> resultList, List<T> arr) {
        if (resultList.size() == arr.size()) {
            list.add(new ArrayList<>(resultList));
        } else {
            for (T t : arr){
                if (!resultList.contains(t)) {
                    resultList.add(t);
                    permuteImpl(list, resultList, arr);
                    resultList.remove(resultList.size() - 1);
                }
            }
        }
    }
}
