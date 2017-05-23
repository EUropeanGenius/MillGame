package game.strategy;

import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import game.rules.EUGHeuristic;
import game.rules.EUGame;
import it.unibo.ai.didattica.mulino.domain.State;

public class Player {
	private State.Checker color;
	private int moveInterval;
	private EUGClient client;

	public Player(State.Checker color, int moveInterval, EUGClient client) {
		super();
		this.color = color;
		this.moveInterval = moveInterval;
		this.client = client;
	}

	public State.Checker getColor() {
		return color;
	}

	public void setColor(State.Checker color) {
		this.color = color;
	}

	public long getMoveInterval() {
		return moveInterval;
	}

	public void setMoveInterval(int moveInterval) {
		this.moveInterval = moveInterval;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EUropeanGenius\n");
		sb.append("[\n");
		sb.append("\tColor : "+ this.getColor().toString()+"\n");
		sb.append("\tTime (s): "+ this.getMoveInterval()+"\n");
		sb.append("]\n");
		return sb.toString();
	}
	
	public void start(){
		boolean myTurn = (color == State.Checker.WHITE);
		State state = null;
		String act;
		
		//read initial state
		try{
			state = client.read();
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		while(true){
			if(!myTurn){
				try{
					state = client.read();
				}catch(Exception e){
					e.printStackTrace();
					return;
				}
			}
			
			myTurn = false;
			EUGState eugState = new EUGState(state,this.color);
			act = computeMove(eugState);
			System.out.println(act.toString());
			try{
				client.write(act,EUGState.EUGPhaseToChesaniPhase(eugState.getCurrentPhase()));
			}catch(Exception e){
				e.printStackTrace();
				return;
			}
			
			try{
				state = client.read();
			}catch(Exception e){
				e.printStackTrace();
				return;
			}
			
		}
	}
	
	private String computeMove(EUGState state) {
		EUGame game = new EUGame(state);
		EUGState currState = game.getInitialState();
		EUGHeuristic<EUGState, EUGAction, Short> search = EUGHeuristic.createFor(game,-30000,3000,this.moveInterval-2);
		//search.setLogEnabled(true);
		return search.makeDecision(currState).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + (int) (moveInterval ^ (moveInterval >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (color != other.color)
			return false;
		if (moveInterval != other.moveInterval)
			return false;

		return true;
	}
	 

}
