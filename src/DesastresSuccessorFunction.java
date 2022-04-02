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

        // Hardcore!
        for (int igrupo = 0; igrupo < estado.getGrupos().size(); ++igrupo) {
            Grupo g = estado.getGrupos().get(igrupo);
            for (int centro0 = 0; centro0 < estado.getCentros().size(); ++centro0)
                for (int centroF = 0; centroF < estado.getCentros().size(); ++centroF)
                    for (int helic0 = 0; helic0 < estado.getCentros().get(centro0).getNHelicopteros(); ++helic0)
                        for (int helicF = 0; helicF < estado.getCentros().get(centroF).getNHelicopteros(); ++helicF) {
                            DesastresEstado next = estado.reasignaGrupo(g, centro0, helic0, centroF, helicF);
                            if (next != null) {
                                successors.add(new Successor(
                                        String.format("Reasigna grupo %s del centro %s y helicptero %s al centro %s y helicoptero %s",
                                                igrupo, centro0, helic0, centroF, helicF),
                                        next));
                            }
                        }

        }
        return successors;
    }
}
