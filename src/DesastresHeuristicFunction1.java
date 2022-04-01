import aima.search.framework.HeuristicFunction;

public class DesastresHeuristicFunction1 implements HeuristicFunction {
    @Override
    // Devolvemos el tiempo en hacer el rescate total.
    public double getHeuristicValue(Object o) {
        DesastresEstado desastresEstado = (DesastresEstado)o;
        return desastresEstado.tiempoRescateTotalYPrioridad()[0];
    }
}
