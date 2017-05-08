package game.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aima.core.search.adversarial.Game;
import game.strategy.EUGState;
import it.unibo.ai.didattica.mulino.actions.*;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;

public class MillGame implements Game<EUGState, Action, State.Checker> {

	private EUGState state;

	public MillGame(EUGState state) {
		this.state = state;
	}

	@Override
	public List<Action> getActions(EUGState currstate) {

		State.Phase fase = currstate.getCurrentPhase();
		List<Action> actions = new ArrayList<Action>();
		switch (fase.toString()) {
		case "First":
			Phase1Action action1 = new Phase1Action();
			action1.setPutPosition(phase1Stratergy(currstate));
			if (Util.hasCompletedTriple(currstate, action1.getPutPosition(), currstate.getCurrentPlayer())) {
				action1.setRemoveOpponentChecker(phase1RemoveStrategy(currstate));
			}
			actions.add(action1);
			break;
		case "Second":
			break;
		case "Final":
			break;
		default:
			break;
		}

		return actions;
	}

	@Override
	public EUGState getInitialState() {
		return state;
	}

	@Override
	public Checker getPlayer(EUGState currstate) {
		return State.Checker.WHITE;
	}

	@Override
	public Checker[] getPlayers() {
		return new Checker[] { State.Checker.WHITE, State.Checker.BLACK };
	}

	@Override
	public EUGState getResult(EUGState s, Action a) {

		State.Phase fase = s.getCurrentPhase();
		EUGState e = null;

		switch (fase.toString()) {
		case "First":
			try {
				e = (EUGState) Phase1.applyMove(s, a, s.getCurrentPlayer());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.togglePlayer();
			break;
		case "Second":
			try {
				e = (EUGState) Phase2.applyMove(s, a, s.getCurrentPlayer());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.togglePlayer();
			break;
		case "Final":
			try {
				e = (EUGState) PhaseFinal.applyMove(s, a, s.getCurrentPlayer());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.togglePlayer();
			break;

		default:
			break;

		}

		return e;
	}

	@Override
	public double getUtility(EUGState currstate, Checker check) {
		if (currstate.getBlackCheckersOnBoard() < 3)
			return 0;
		else if (currstate.getWhiteCheckersOnBoard() < 3)
			return 1;
		else
			throw new IllegalArgumentException("State is not terminal.");
	}

	@Override
	public boolean isTerminal(EUGState currstate) {
		return (currstate.getBlackCheckersOnBoard() < 3 || currstate.getWhiteCheckersOnBoard() < 3);
	}

	private String phase1Stratergy(EUGState state) {

		HashMap<String, Checker> board = state.getBoard();

		for (String s : board.keySet()) {
			if (board.get(s) == Checker.EMPTY) {
				return s;
			}
		}
		throw new IllegalStateException();
	}

	private String phase1RemoveStrategy(EUGState state) {

		HashMap<String, Checker> board = state.getBoard();
		Checker oppositeChecker = (state.getCurrentPlayer() == State.Checker.WHITE) ? State.Checker.BLACK
				: State.Checker.WHITE;

		for (String s : board.keySet()) {
			if (board.get(s) == oppositeChecker) {
				return s;
			}
		}
		throw new IllegalStateException();
	}

}
