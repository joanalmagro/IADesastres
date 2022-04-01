import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {
    public static void main(String[] args) throws Exception {
        // Parametros del problema
        int ncentros = 5;
        int ngrupos = 100;
        int nhelicopteros = 1;

        int seed = 31415;

        // Parámetros Simulated Annealing
        int steps = 300000;
        int stiter = 5000;
        int k = 5;
        double lamb = 0.01;

        // Algoritmos de busqueda: HC o SA
        Search algHC = new HillClimbingSearch();
        Search algSA = new SimulatedAnnealingSearch(steps, stiter, k, lamb);

        // Inicialicamos estado
        DesastresEstado estadoInicial = new DesastresEstado(ncentros, nhelicopteros, ngrupos, seed);

        System.out.println("---------------------- ESTADO INICIAL ---------------");
        System.out.println(estadoInicial.display());
        System.out.println();

        // Create the Problem object
        Problem p = new Problem(estadoInicial,
                new DesastresSuccessorFunction(),
                new DesastresGoalTest(),
                new DesastresHeuristicFunction1());

        // Instantiate the SearchAgent object
        long t0 = java.lang.System.currentTimeMillis();
        SearchAgent agent = new SearchAgent(p, algHC);
        long tf = java.lang.System.currentTimeMillis();

        DesastresEstado estadoFinal = (DesastresEstado) algHC.getGoalState();


        System.out.println("---------------------- ESTADO FINAL ---------------");
        System.out.println(estadoFinal.display());
        System.out.println();
        System.out.println("Elapsed time: " + (tf - t0) + "ms");

        System.out.println(agent.getActions().toString());
        System.out.println(agent.getInstrumentation().toString());
    }
}
