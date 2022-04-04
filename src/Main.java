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

        // Seed aleatoria
        int seed = 31416;

        // Algoritmo de búsqueda
        Search alg = new HillClimbingSearch();
        boolean useHC = true;

        // Parámetros Simulated Annealing
        int steps = 3000;
        int stiter = 5000;
        int k = 5;
        double lamb = 0.01;

        // Heuristic function
        int heuristicFunction = 1;

        int solucionInicial = DesastresEstado.INIT_CENTRO_EQUIT;

        try {
            int i = 0;
            // Parsear parametros entrada
            while(i < args.length) {
                String arg = args[i++];
                if ("-ncentros".equals(arg)) {
                    ncentros = Integer.parseInt(args[i++]);
                } else if ("-ngrupos".equals(arg)) {
                    ngrupos = Integer.parseInt(args[i++]);
                } else if ("-nhelicopteros".equals(arg)) {
                    nhelicopteros = Integer.parseInt(args[i++]);
                } else if ("-seed".equals(arg)) {
                    seed = Integer.parseInt(args[i++]);
                } else if ("-HC".equals(arg)) {
                    alg = new HillClimbingSearch();
                } else if ("-SA".equals(arg)) {
                    steps = Integer.parseInt(args[i++]);
                    stiter = Integer.parseInt(args[i++]);
                    k = Integer.parseInt(args[i++]);
                    lamb = Double.parseDouble(args[i++]);
                    alg = new SimulatedAnnealingSearch(steps, stiter, k, lamb);
                    useHC = false;
                } else if ("-init".equals(arg)) {
                    solucionInicial = Integer.parseInt(args[i++]);
                    if (solucionInicial < 0 || solucionInicial > 2) throw new Exception();
                } else if ("-heuristic".equals(arg)) {
                    heuristicFunction = Integer.parseInt(args[i++]);
                    if (heuristicFunction != 1 && heuristicFunction != 2) throw new Exception();
                } else {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            System.err.println("Usage: java jar IADesastres.jar [-ncentros numero-centros] [-nhelicopteros numero-helicopteros] [-seed custom-seed] [None|-HC>|-SA n-steps st-iter k lamb] [-init 0|1|2] [-heuristic 0|1]");
            return;
        }

        System.out.println("---------------- PARÁMETROS EJECUCIÓN ------------------");
        System.out.println(String.format("PROBLEMA\n  grupos: %d\n  centros: %d\n  helicopteros: %d", ngrupos, ncentros, nhelicopteros));
        System.out.println(String.format("SEED: %s", seed));
        System.out.println(String.format("ALGORITMO: %s", useHC?
                "HillClimb" :
                String.format("SimulatedAnnealing %d %d %d %d", steps, stiter, k, lamb)));
        System.out.println(String.format("SOLUCION_INICIAL: %d", solucionInicial));
        System.out.println(String.format("HEURISTIC: %d", heuristicFunction));

        // Inicialicamos estado
        DesastresEstado estadoInicial = new DesastresEstado(ncentros, nhelicopteros, ngrupos, seed, solucionInicial);

        System.out.println("---------------------- INFO CENTROS ---------------");
        System.out.println(estadoInicial.infoGrupos());

        System.out.println("---------------------- ESTADO INICIAL ---------------");
        System.out.println(estadoInicial.infoRescates());
        System.out.println(estadoInicial.infoTiempos());

        // Problema
        Problem p = new Problem(estadoInicial,
                new DesastresSuccessorFunction(),
                new DesastresGoalTest(),
                heuristicFunction == 1? new DesastresHeuristicFunction1() : new DesastresHeuristicFunction2());

        // Ejecuta SearchAgent
        long t0 = java.lang.System.currentTimeMillis();
        SearchAgent agent = new SearchAgent(p, alg);
        long tf = java.lang.System.currentTimeMillis();
        DesastresEstado estadoFinal = (DesastresEstado) alg.getGoalState();

        System.out.println("---------------------- ESTADO FINAL ---------------");
        System.out.println(estadoFinal.infoRescates());
        System.out.println(estadoFinal.infoTiempos());


        System.out.println("---------------------- SEARCH AGENT INFO ---------------");
        System.out.println("Elapsed time: " + (tf - t0) + "ms");
        System.out.println(agent.getActions().toString());
        System.out.println(agent.getInstrumentation().toString());
    }
}
