package game.rules;

import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.framework.Metrics;
import game.strategy.EUGState;

import aima.core.search.framework.Metrics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*public class EUGHeuristic<STATE,ACTION,PLAYER> extends IterativeDeepeningAlphaBetaSearch<STATE,ACTION,PLAYER> {

    public static <STATE, ACTION, PLAYER> EUGHeuristic<STATE, ACTION, PLAYER> createFor(
            Game<STATE, ACTION, PLAYER> game, double utilMin, double utilMax, int time) {
        return new EUGHeuristic<>(game, utilMin, utilMax, time);
    }

    public EUGHeuristic(Game<STATE, ACTION, PLAYER> game, double utilMin, double utilMax, int time) {
        super(game,utilMin,utilMax,time);
    }

    //todo
    @Override
    protected double eval(STATE state, PLAYER player) {
        super.eval(state, player);
        EUGState s = (EUGState)state;
        //System.out.println(s.toString());
        return (s.getUtility());
    }
}*/

public class EUGHeuristic<STATE, ACTION, PLAYER> implements AdversarialSearch<STATE, ACTION> {
    public static final String METRICS_NODES_EXPANDED = "nodesExpanded";
    public static final String METRICS_MAX_DEPTH = "maxDepth";
    protected Game<STATE, ACTION, PLAYER> game;
    protected double utilMax;
    protected double utilMin;
    protected int currDepthLimit;
    private boolean heuristicEvaluationUsed;
    private EUGHeuristic.Timer timer;
    private boolean logEnabled;
    private Metrics metrics = new Metrics();

    public static <STATE, ACTION, PLAYER> EUGHeuristic<STATE, ACTION, PLAYER> createFor(Game<STATE, ACTION, PLAYER> game, double utilMin, double utilMax, int time) {
        return new EUGHeuristic(game, utilMin, utilMax, time);
    }

    public EUGHeuristic(Game<STATE, ACTION, PLAYER> game, double utilMin, double utilMax, int time) {
        this.game = game;
        this.utilMin = utilMin;
        this.utilMax = utilMax;
        this.timer = new EUGHeuristic.Timer(time);
    }

    public void setLogEnabled(boolean b) {
        this.logEnabled = b;
    }

    public ACTION makeDecision(STATE state) {
        this.metrics = new Metrics();
        StringBuffer logText = null;
        PLAYER player = this.game.getPlayer(state);
        List<ACTION> results = this.game.getActions(state);
        this.timer.start();
        this.currDepthLimit = 0;

        do {
            this.incrementDepthLimit();
            if(this.logEnabled) {
                logText = new StringBuffer("depth " + this.currDepthLimit + ": ");
            }

            this.heuristicEvaluationUsed = false;
            EUGHeuristic.ActionStore<ACTION> newResults = new EUGHeuristic.ActionStore();
            for(ACTION action : results) {
                double value = this.minValue(this.game.getResult(state, action), player, -1.0D / 0.0, 1.0D / 0.0, 1);

                if(this.timer.timeOutOccured()) {
                    break;
                }

                newResults.add(action, value);

                if(this.logEnabled) {
                    logText.append(action + "->" + value + " ");
                }
            }

            if(newResults.size() == results.size()) {
                results = newResults.actions;
                if(this.logEnabled) {
                    System.out.println(logText);
                }
                if(!this.timer.timeOutOccured() &&
                        (this.hasSafeWinner(((Double)newResults.utilValues.get(0)).doubleValue()) ||
                                newResults.size() > 1 &&
                                this.isSignificantlyBetter(((Double)newResults.utilValues.get(0)).doubleValue(), ((Double)newResults.utilValues.get(1)).doubleValue()))) {
                    break;
                }
            }

        } while(!this.timer.timeOutOccured() && this.heuristicEvaluationUsed);

        return results.get(0);
    }

    public double maxValue(STATE state, PLAYER player, double alpha, double beta, int depth) {
        this.updateMetrics(depth);
        if(!this.game.isTerminal(state) && depth < this.currDepthLimit && !this.timer.timeOutOccured()) {
            double value = Double.NEGATIVE_INFINITY;

            for (ACTION action : orderActions(state, game.getActions(state), player, depth)) {
                value = Math.max(value, this.minValue(this.game.getResult(state, action), player, alpha, beta, depth + 1));
                if(value >= beta) {
                    return value;
                }
                alpha = Math.max(alpha, value);
            }

            return value;
        } else {
            return this.eval(state, player);
        }
    }

    public double minValue(STATE state, PLAYER player, double alpha, double beta, int depth) {
        this.updateMetrics(depth);
        if(!this.game.isTerminal(state) && depth < this.currDepthLimit && !this.timer.timeOutOccured()) {
            double value = Double.POSITIVE_INFINITY;

            for (ACTION action : orderActions(state, game.getActions(state), player, depth)) {
                value = Math.min(value, this.maxValue(this.game.getResult(state, action), player, alpha, beta, depth + 1));
                if(value <= alpha) {
                    return value;
                }
                beta = Math.min(beta, value);
            }

            return value;
        } else {
            return this.eval(state, player);
        }
    }

    private void updateMetrics(int depth) {
        this.metrics.incrementInt("nodesExpanded");
        this.metrics.set("maxDepth", Math.max(this.metrics.getInt("maxDepth"), depth));
    }

    public Metrics getMetrics() {
        return this.metrics;
    }

    protected void incrementDepthLimit() {
        ++this.currDepthLimit;
    }

    protected boolean isSignificantlyBetter(double newUtility, double utility) {
        return false;
    }

    protected boolean hasSafeWinner(double resultUtility) {
        return resultUtility <= this.utilMin || resultUtility >= this.utilMax;
    }

    protected double eval(STATE state, PLAYER player) {
        if(!this.game.isTerminal(state)) {
            this.heuristicEvaluationUsed = true;

        }
        return this.game.getUtility(state, player);
    }

    public List<ACTION> orderActions(STATE state, List<ACTION> actions, PLAYER player, int depth) {
        return actions;
    }

    private static class ActionStore<ACTION> {
        private List<ACTION> actions;
        private List<Double> utilValues;

        private ActionStore() {
            this.actions = new ArrayList();
            this.utilValues = new ArrayList();
        }

        void add(ACTION action, double utilValue) {
            int idx;
            for(idx = 0; idx < this.actions.size() && utilValue <= ((Double)this.utilValues.get(idx)).doubleValue(); ++idx) {
                ;
            }

            this.actions.add(idx, action);
            this.utilValues.add(idx, Double.valueOf(utilValue));
        }

        int size() {
            return this.actions.size();
        }
    }

    private static class Timer {
        private long duration;
        private long startTime;

        Timer(int maxSeconds) {
            this.duration = 1000L * (long)maxSeconds;
        }

        void start() {
            this.startTime = System.currentTimeMillis();
        }

        boolean timeOutOccured() {
            return System.currentTimeMillis() > this.startTime + this.duration;
        }
    }
}
