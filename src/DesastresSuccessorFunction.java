import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class DesastresSuccessorFunction implements SuccessorFunction {
    @Override
    public List<Successor> getSuccessors(Object o) {
        DesastresEstado estado = (DesastresEstado) o;
        List<Successor> successors = new ArrayList<>();

        // Hardcore!
        for (int igrupo = 0; igrupo < estado.getNGrupos(); ++igrupo) {
            for (int centro0 = 0; centro0 < estado.getNCentros(); ++centro0)
                for (int centroF = 0; centroF < estado.getNCentros(); ++centroF)
                    for (int helic0 = 0; helic0 < estado.getNHelicopteros(centro0); ++helic0)
                        for (int helicF = 0; helicF < estado.getNHelicopteros(centroF); ++helicF) {
                            DesastresEstado next = estado.reasignaGrupo(igrupo, centro0, helic0, centroF, helicF);
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
