package game.rules;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import game.strategy.EUGState;

/**
 * Created by alessandroberlati on 13/05/2017.
 */
public class EUGHeuristic<STATE,ACTION,PLAYER> extends IterativeDeepeningAlphaBetaSearch<STATE,ACTION,PLAYER> {

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
        System.out.println(s.toString());
        return (s.getUtility());
    }
}
