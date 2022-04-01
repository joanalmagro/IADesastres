import IA.Desastres.Centro;
import IA.Desastres.Grupo;

import java.util.ArrayList;

// Representa una salida de un helicóptero. El helicoptero se encuentra en el centro C y rescata a los grupos
// g1, g2, y g3. Si alguno es nulo, no lo tiene en cuenta.
class DesastresSalidaHelicoptero {
    Centro c;
    Grupo g1;
    Grupo g2;
    Grupo g3;

    public DesastresSalidaHelicoptero(Centro c, Grupo g1, Grupo g2, Grupo g3) {
        this.c = c;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
    }

    public double getDistanciaVuelo() {
        ArrayList<Grupo> grupos = new ArrayList<>();
        if (g1 != null) grupos.add(g1);
        if (g2 != null) grupos.add(g2);
        if (g3 != null) grupos.add(g3);

        // Posibles trayectos permutando el orden de los grupos no nulos. buscamos el óptimo
        ArrayList<ArrayList<Grupo>> trayectos = permute(grupos);

        double distanciaOptimaGrupos = Double.MAX_VALUE;
        ArrayList<Grupo> trayectoOptimo = new ArrayList<>();

        for (ArrayList<Grupo> trayecto : trayectos) {
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

            return d(c.getCoordX(), c.getCoordY(), g0.getCoordX(), g0.getCoordY()) +
                    distanciaOptimaGrupos +
                    d(gf.getCoordX(), gf.getCoordY(), c.getCoordX(), c.getCoordY());
        }
    }


    // [1,2,3] -> [ [1,2,3],[2,3,1],[3,1,2],[1,3,2],[2,1,3],[3,2,1] ]
    private static<T> ArrayList<ArrayList<T>> permute(ArrayList<T> arr) {
        ArrayList<ArrayList<T>> list = new ArrayList<>();
        permuteImpl(list, new ArrayList<>(), arr);
        return list;
    }

    private static<T> void permuteImpl(ArrayList<ArrayList<T>> list, ArrayList<T> resultList, ArrayList<T> arr) {
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

    // distancia euclidea entre (x1, y1) y (x2, y2)
    private static double d(int x1, int y1, int x2, int y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }
}
