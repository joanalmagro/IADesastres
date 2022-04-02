import IA.Desastres.Grupo;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class DesastresSuccessorFunction implements SuccessorFunction {
    @Override
    public List getSuccessors(Object o) {
        DesastresEstado estado = (DesastresEstado) o;
        ArrayList<Successor> successors = new ArrayList<>();

        // Too much!
        /*
        for (Grupo g : s.getGrupos())
        for (int centro0 = 1; centro0 < s.getCentros().size(); centro0++)
            for (int centroF = 0; centroF <= centro0; centroF++)
                for (int helic0 = 0; helic0 < s.getCentros().get(centro0).getNHelicopteros(); ++helic0)
                    for (int helicF = 0; helic0 <= s.getCentros().get(centroF).getNHelicopteros(); ++helicF) {
                        DesastresEstado next = s.move(g, centro0, helic0, centroF, helicF);
                        if (next != null) {
                            successors.add(next);
                        }
                    }
         */


        for (int igrupo = 0; igrupo < estado.getGrupos().size(); ++igrupo) {
            Grupo g = estado.getGrupos().get(igrupo);

            // Reasignamos el centro del grupo g de N a N+1
            for (int centro = 0; centro < estado.getCentros().size(); ++centro) {
                int centroF = (centro + 1) % estado.getCentros().size();
                DesastresEstado estado1 = estado.reasignaGrupo(g, centro, 0, centroF, 0);
                if (estado1 != null)
                    successors.add(new Successor(
                            "Mueve grupo " + igrupo + " del centro " + centro + " al centro " + centroF,
                            estado1));

                // Reasignamos el centro del grupo g de N a N+1
                for (int helic = 0; helic < estado.getCentros().get(centro).getNHelicopteros(); ++helic) {
                    int helicF = (helic + 1) % estado.getCentros().get(centro).getNHelicopteros();
                    DesastresEstado estado2 = estado.reasignaGrupo(g, centro, helic, centro, helicF);
                    if (estado2 != null)
                        successors.add(new Successor(
                                "Mueve grupo del centro " + centro + " y helicoptero " + helic + " al helicoptero " + helicF,
                                estado2));
                }
            }
        }


        return successors;
    }
}
