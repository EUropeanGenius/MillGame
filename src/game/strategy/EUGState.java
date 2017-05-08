package game.strategy;

import it.unibo.ai.didattica.mulino.domain.State;

public class EUGState extends State{
	
	private static final long serialVersionUID = 1L;
	
	private State.Checker currentPlayer = State.Checker.WHITE;


	public EUGState(Checker currentPlayer) {
		super();
		this.currentPlayer = currentPlayer;
	}

	public void togglePlayer(){
		currentPlayer = (currentPlayer == State.Checker.WHITE) ? State.Checker.BLACK : State.Checker.WHITE;
	}
	
	public State.Checker getCurrentPlayer() {
		return currentPlayer;
	}
	
	@Override
	public State clone(){
		
		EUGState result = new EUGState(this.currentPlayer);
		result.getBoard().putAll(this.getBoard());

		// update the checkers available to the players
		result.setWhiteCheckers(this.getWhiteCheckers());
		result.setBlackCheckers(this.getBlackCheckers());
		result.setWhiteCheckersOnBoard(this.getWhiteCheckersOnBoard());
		result.setBlackCheckersOnBoard(this.getBlackCheckersOnBoard());

		// update the phase
		result.setCurrentPhase(this.getCurrentPhase());
		
		return result;

	}
	
	
	
	
	

}
