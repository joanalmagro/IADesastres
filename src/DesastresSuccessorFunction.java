import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class DesastresSuccessorFunction implements SuccessorFunction {
    @Override
    public List getSuccessors(Object o) {
        DesastresEstado s = (DesastresEstado) o;
        ArrayList<DesastresEstado> successors = new ArrayList<>();

        // TODO: implementar los estados successores.

        return successors;
    }
}
