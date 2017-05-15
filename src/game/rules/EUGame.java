package game.rules;

import aima.core.search.adversarial.Game;
import game.strategy.EUGAction;
import game.strategy.EUGState;

import java.util.List;

public class EUGame implements Game<EUGState,EUGAction,Short>{

    private EUGState state;

    public EUGame(EUGState state) {
        this.state = state;
    }

    @Override
    public EUGState getInitialState() {
        return state;
    }

    @Override
    public Short[] getPlayers() {
        return new Short[]{EUGState.WHITE,EUGState.BLACK};
    }

    @Override
    public Short getPlayer(EUGState eugState) {
        return eugState.getCurrentPlayer();
    }

    @Override
    public List<EUGAction> getActions(EUGState eugState) {
        return eugState.getActions();
    }

    @Override
    public EUGState getResult(EUGState eugState, EUGAction action) {
        return eugState.applyAction(action);
    }

    @Override
    public boolean isTerminal(EUGState eugState) {
        return (eugState.getCurrentPhase() == EUGState.PHASE3 && (eugState.getBlackCheckersTable() < 3 || eugState.getWhiteCheckersTable() < 3 ));
    }

    //TODO non siamo sempre i bianchi -> tornare 1 quando vince il giocatore corrente
    //non è l'euristica, quella va fatta estendendo l'algoritmo metodo eval.
    @Override
    public double getUtility(EUGState eugState, Short i) {
       return eugState.getUtility();
    }
}
