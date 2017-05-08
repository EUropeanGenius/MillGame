package game.rules;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;

public class MillGame implements Game<State, Action, State.Checker>{
	
	private State state;
	
	public MillGame(State state){
		this.state = state;
	}

	@Override
	public List<Action> getActions(State currstate) {
		State.Phase fase = currstate.getCurrentPhase();
		List<Action> actions =  new ArrayList<Action>();
		switch(fase.toString()){
		case "first": break;
		case "second": break;
		case "final": break;
		default: break;
		}
	}

	@Override
	public State getInitialState() {
		return state;
	}

	@Override
	public Checker getPlayer(State currstate) {
		return State.Checker.WHITE;
	}

	@Override
	public Checker[] getPlayers() {
		return new Checker [] {State.Checker.WHITE, State.Checker.BLACK};
	}

	@Override
	public State getResult(State s, Action a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getUtility(State currstate, Checker check) {
		if(currstate.getBlackCheckersOnBoard() < 3)
			return 0;
		else if(currstate.getWhiteCheckersOnBoard() < 3)
			return 1;
		else
			throw new IllegalArgumentException("State is not terminal.");
	}

	@Override
	public boolean isTerminal(State currstate) {
		return (currstate.getBlackCheckersOnBoard() < 3 || currstate.getWhiteCheckersOnBoard() <3);
	}


}
