package game.strategy;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;

public class EUGState {
	
	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;

	public static final int PHASE1 = 0;
	public static final int PHASE2 = 1;
	public static final int PHASE3 = 2;

	private static final int ROWS = 8;
	private static final int COLUMNS = 3;
	private static final int TOT_CHECKERS = 9;

	private int currentPlayer;
	private int currentPhase = PHASE1;
	private int blackCheckersHand = TOT_CHECKERS;
	private int whiteCheckersHand = TOT_CHECKERS;
	private int blackCheckersTable = 0;
	private int whiteCheckersTable = 0;

	private int[][] board = new int[ROWS][COLUMNS];

	public static final Map<String,int[]> stringToRC = new HashMap<String,int[]>(){
		{
			put("a1", new int[]{0, 0});
			put("a4", new int[]{1, 0});
			put("a7", new int[]{2, 0});
			put("d7", new int[]{3, 0});
			put("g7", new int[]{4, 0});
			put("g4", new int[]{5, 0});
			put("g1", new int[]{6, 0});
			put("d1", new int[]{7, 0});

			put("b2", new int[]{0, 1});
			put("b4", new int[]{1, 1});
			put("b6", new int[]{2, 1});
			put("d6", new int[]{3, 1});
			put("f6", new int[]{4, 1});
			put("f4", new int[]{5, 1});
			put("f2", new int[]{6, 1});
			put("d2", new int[]{7, 1});

			put("c3", new int[]{0, 2});
			put("c4", new int[]{1, 2});
			put("c5", new int[]{2, 2});
			put("d5", new int[]{3, 2});
			put("e5", new int[]{4, 2});
			put("e4", new int[]{5, 2});
			put("e3", new int[]{6, 2});
			put("d3", new int[]{7, 2});
		}
	};
	
	public EUGState	(State s){
		Map<String,State.Checker> initialBoard = s.getBoard();
		for(String pos : s.getPositions()){
			int[] rc = stringToRC.get(pos.toLowerCase());
			board[rc[0]][rc[1]] = EUGState.checkerToInt(initialBoard.get(pos));
		}

		blackCheckersHand = s.getBlackCheckers();
		whiteCheckersHand = s.getWhiteCheckers();
		blackCheckersTable = s.getBlackCheckersOnBoard();
		whiteCheckersTable = s.getWhiteCheckersOnBoard();
 		currentPlayer = EUGState.WHITE;
		currentPhase = convertPhase(s.getCurrentPhase());
	}

	public EUGState(){
		for (int row= 0; row < EUGState.ROWS; row++ ) {
			for (int column= 0; column < EUGState.COLUMNS; column++ ) {
				board[row][column] = 0;
			}
		}
	}

	public static int checkerToInt(Checker checker) {
		if(checker == (State.Checker.EMPTY))
			return EUGState.EMPTY;
		else if(checker == (State.Checker.WHITE))
			return EUGState.WHITE;
		
		return EUGState.BLACK;
	}

	public int convertPhase(State.Phase phase){
		if(phase == State.Phase.FIRST) return EUGState.PHASE1;
		else if(phase == State.Phase.FIRST) return EUGState.PHASE2;

		return EUGState.PHASE3;
	}

	public void togglePlayer(){
		currentPlayer = (currentPlayer == EUGState.WHITE) ? EUGState.BLACK : EUGState.WHITE;
		return;
	}

	public void togglePhase(){
		switch (currentPhase){
			case EUGState.PHASE1:
				currentPhase = (whiteCheckersHand | blackCheckersHand) == 0 ?  EUGState.PHASE2 : currentPhase;
				break;
			case EUGState.PHASE2:
				currentPhase = (whiteCheckersTable == 3 || blackCheckersTable == 3) ? EUGState.PHASE3 : currentPhase;
				break;
			default: break;
		}
	}
	public EUGState clone( EUGState toClone){
		EUGState clone =  new EUGState();
		for (int row= 0; row < EUGState.ROWS; row++ ) {
			for (int column= 0; column < EUGState.COLUMNS; column++ ) {
				board[row][column] = toClone.getPositionChecker(row,column);
			}
		}
		clone.setCurrentPlayer(toClone.getCurrentPlayer());
		clone.setWhiteCheckersHand(toClone.getWhiteCheckersHand());
		clone.setBlackCheckersHand(toClone.getBlackCheckersHand());
		clone.setCurrentPhase(toClone.getCurrentPhase());
		clone.setWhiteCheckersTable(toClone.getWhiteCheckersTable());
		clone.setBlackCheckersTable(toClone.getBlackCheckersTable());
		return clone;
	}

	public void addChecker(int row, int col){
		if(currentPlayer == EUGState.WHITE){
			whiteCheckersTable++;
			whiteCheckersHand--;
		}
		else{
			blackCheckersTable++;
			blackCheckersHand--;
		}
		board[row][col] = currentPlayer;
	}

	public void move(int rowFrom, int colFrom, int rowTo, int colTo){
		board[rowTo][colTo] = board[rowFrom][colFrom];
		board[rowFrom][colFrom] = EUGState.EMPTY;
	}

	public void removeChecker(int row, int col){
		if(currentPlayer == EUGState.WHITE)
			whiteCheckersTable--;
		else
			blackCheckersTable--;

		board[row][col] = EUGState.EMPTY;
	}

	public List<EUGAction> getActions(){
		return null;
	}

	public EUGState applyAction(EUGAction action){
		EUGState newState = clone(this);
		if(newState.getCurrentPhase() == EUGState.PHASE1){
			int [] to = EUGState.stringToRC.get(action.getTo());
			newState.addChecker(to[0],to[1]);
		}
		else{
			int[] to = EUGState.stringToRC.get(action.getTo());
			int[] from = EUGState.stringToRC.get(action.getFrom());
			newState.move(from[0],from[1],to[0],to[1]);
		}
		if(action.hasRemove()){
			int[] pos = EUGState.stringToRC.get(action.getRemove());
			newState.removeChecker(pos[0],pos[1]);
		}
		newState.togglePlayer();
		newState.togglePhase();
		return newState;
	}

	public int getPositionChecker(int row, int column){
		return this.board[row][column];
	}
	public int getCurrentPlayer(){
		return this.currentPlayer;
	}
	public int[][] getBoard() { return this.board; }
	public int getBlackCheckersHand() {
		return blackCheckersHand;
	}
	public int getWhiteCheckersHand() {
		return whiteCheckersHand;
	}
	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	public void setBlackCheckersHand(int blackCheckersHand) {
		this.blackCheckersHand = blackCheckersHand;
	}
	public void setWhiteCheckersHand(int whiteCheckersHand) {
		this.whiteCheckersHand = whiteCheckersHand;
	}
	public int getCurrentPhase() {
		return currentPhase;
	}
	public void setCurrentPhase(int currentPhase) {
		this.currentPhase = currentPhase;
	}
	public int getBlackCheckersTable() {
		return blackCheckersTable;
	}
	public void setBlackCheckersTable(int blackCheckersTable) {
		this.blackCheckersTable = blackCheckersTable;
	}
	public int getWhiteCheckersTable() {
		return whiteCheckersTable;
	}
	public void setWhiteCheckersTable(int whiteCheckersTable) {
		this.whiteCheckersTable = whiteCheckersTable;
	}

	/*
	 * Azioni possibili
	 * Aggiornamento stato dopo una data action
	 */
	

}
