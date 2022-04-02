import aima.search.framework.HeuristicFunction;

public class DesastresHeuristicFunction2 implements HeuristicFunction {

    @Override
    // Devolvemos el producto del tiempo en hacer el rescate total y el tiempo en hacer el rescate de los grupos de prioridad.
    // TODO: Probar con la suma
    public double getHeuristicValue(Object o) {
        DesastresEstado desastresEstado = (DesastresEstado)o;
        double[] tiempos = desastresEstado.tiempoRescateTotalYPrioridad();
        return (tiempos[0] * tiempos[1]);
    }
}
