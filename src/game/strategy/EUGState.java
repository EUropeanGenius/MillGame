package game.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import game.rules.Hamburger;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;

public class EUGState {

	public static final short EMPTY = 0;
	public static final short WHITE = 1;
	public static final short BLACK = 2;

	public static final short PHASE1 = 0;
	public static final short PHASE2 = 1;
	public static final short PHASE3 = 2;

	private static final short ROWS = 8;
	private static final short COLUMNS = 3;
	private static final short TOT_CHECKERS = 9;

	private short currentPlayer;
	private short currentPhase = PHASE1;
	private short blackCheckersHand = TOT_CHECKERS;
	private short whiteCheckersHand = TOT_CHECKERS;
	private short blackCheckersTable = 0;
	private short whiteCheckersTable = 0;
	private short whoAmI;

	private short[][] board = new short[ROWS][COLUMNS];

	public EUGState(State s, State.Checker color) {
		Map<String, State.Checker> initialBoard = s.getBoard();
		for (String pos : s.getPositions()) {
			short[] rc = Hamburger.stringToRC.get(pos.toLowerCase());
			board[rc[0]][rc[1]] = EUGState.checkerToShort(initialBoard.get(pos));
		}

		blackCheckersHand = (short) s.getBlackCheckers();
		whiteCheckersHand = (short) s.getWhiteCheckers();
		blackCheckersTable = (short) s.getBlackCheckersOnBoard();
		whiteCheckersTable = (short) s.getWhiteCheckersOnBoard();
		currentPlayer = EUGState.checkerToShort(color);
		currentPhase = convertPhase(s.getCurrentPhase());
		whoAmI = currentPlayer;
	}

	public EUGState() {
		for (short row = 0; row < EUGState.ROWS; row++) {
			for (short column = 0; column < EUGState.COLUMNS; column++) {
				board[row][column] = 0;
			}
		}
	}

	public static short checkerToShort(Checker checker) {
		if (checker == (State.Checker.EMPTY))
			return EUGState.EMPTY;
		else if (checker == (State.Checker.WHITE))
			return EUGState.WHITE;

		return EUGState.BLACK;
	}

	public static short convertPhase(State.Phase phase) {
		if (phase == State.Phase.FIRST)
			return EUGState.PHASE1;
		else if (phase == State.Phase.SECOND)
			return EUGState.PHASE2;

		return EUGState.PHASE3;
	}

	public static State.Phase EUGPhaseToChesaniPhase(Short phase){
		if (phase == EUGState.PHASE1)
			return State.Phase.FIRST;
		else if (phase == EUGState.PHASE2)
			return State.Phase.SECOND;

		return State.Phase.FINAL;
	}

	public void togglePlayer() {
		currentPlayer = (currentPlayer == EUGState.WHITE) ? EUGState.BLACK : EUGState.WHITE;
		return;
	}

	public void togglePhase() {
		switch (currentPhase) {
		case EUGState.PHASE1:
			currentPhase = (whiteCheckersHand | blackCheckersHand) == 0 ? EUGState.PHASE2 : currentPhase;
			break;
		case EUGState.PHASE2:
			currentPhase = (whiteCheckersTable == 3 || blackCheckersTable == 3) ? EUGState.PHASE3 : currentPhase;
			break;
		default:
			break;
		}
	}

	public EUGState clone() {
		EUGState clone = new EUGState();
		for (short row = 0; row < EUGState.ROWS; row++) {
			for (short column = 0; column < EUGState.COLUMNS; column++) {
				board[row][column] = this.getPositionChecker(row, column);
			}
		}
		clone.setCurrentPlayer(this.getCurrentPlayer());
		clone.setWhiteCheckersHand(this.getWhiteCheckersHand());
		clone.setBlackCheckersHand(this.getBlackCheckersHand());
		clone.setCurrentPhase(this.getCurrentPhase());
		clone.setWhiteCheckersTable(this.getWhiteCheckersTable());
		clone.setBlackCheckersTable(this.getBlackCheckersTable());
		clone.whoAmI = this.whoAmI;
		return clone;
	}

	public void addChecker(short row, short col) {
		if (currentPlayer == EUGState.WHITE) {
			whiteCheckersTable++;
			whiteCheckersHand--;
		} else {
			blackCheckersTable++;
			blackCheckersHand--;
		}
		board[row][col] = currentPlayer;
	}

	public void move(short rowFrom, short colFrom, short rowTo, short colTo) {
		board[rowTo][colTo] = board[rowFrom][colFrom];
		board[rowFrom][colFrom] = EUGState.EMPTY;
	}

	public void removeChecker(short row, short col) {
		if (currentPlayer == EUGState.WHITE)
			blackCheckersTable--;
		else
			whiteCheckersTable--;

		board[row][col] = EUGState.EMPTY;
	}

	public List<EUGAction> getActions() {
		List<EUGAction> actions = new ArrayList<EUGAction>();
		List<short[]> empties = this.getEmptyPositions();
		List<short[]> foes = this.getFoePositions();
		switch (currentPhase) {
		case EUGState.PHASE1:
			for (short[] e : empties) {
				if (doesMill(e[0], e[1], currentPlayer)) {
					for (short[] f : foes) {
						actions.add(new EUGAction() {
							{
								setTo(e[0], e[1]);
								setRemove(f[0], f[1]);
							}
						});
					}
				} else {
					actions.add(new EUGAction() {
						{
							setTo(e[0], e[1]);
						}
					});
				}
			}
			break;
		case EUGState.PHASE2 :
		case EUGState.PHASE3 :
			List<short[]> allCurrentPositions = getCurrentPlayerPositions();
			for (short[] e : empties) {
				List<short[]> currentAvailablePos = (actualCurrentPhase() == EUGState.PHASE2) ?
						Hamburger.neighbors.get(e[0]+","+e[1]) : allCurrentPositions;
				for (short[] a : currentAvailablePos) {
					if (doesMillPhase2(a[0], a[1], e[0], e[1])) {
						for (short[] f : foes) {
							actions.add(new EUGAction() {
								{
									setTo(e[0], e[1]);
									setRemove(f[0], f[1]);
									setFrom(a[0], a[1]);
								}
							});
						}
					} else {
						actions.add(new EUGAction() {
							{
								setTo(e[0], e[1]);
								setFrom(a[0], a[1]);
							}
						});
					}
				}
			}
			break;
		}
		
		return actions;
	}

	private short actualCurrentPhase() {
		if (this.currentPhase == EUGState.PHASE3) {
			if (currentPlayer == EUGState.WHITE)
				return (whiteCheckersTable > 3) ? EUGState.PHASE2 : EUGState.PHASE3;
			return (blackCheckersTable > 3) ? EUGState.PHASE2 : EUGState.PHASE3;
		}
		return this.currentPhase;
	}

	private boolean doesMill(short row, short col,short player) {
		List<short[][]> availablePos = Hamburger.mulan.get(row+","+col);
		for (short[][] av: availablePos) {
			if(
					board[av[0][0]][av[0][1]] == player &&
					board[av[1][0]][av[1][1]] == player
					) return true;
		}
		return false;
	}
	private boolean doesMillPhase2(short fromRow, short fromCol, short toRow, short toCol) {
		board[fromRow][fromCol] = EUGState.EMPTY;
		boolean mill = doesMill(toRow,toCol,currentPlayer);
		board[fromRow][fromCol] = currentPlayer;
		return mill;
	}

	private List<short[]> getEmptyPositions() {
		List<short[]> empties = new ArrayList<short[]>();
		for (short row = 0; row < EUGState.ROWS; row++) {
			for (short col = 0; col < EUGState.COLUMNS; col++) {
				if (board[row][col] == EUGState.EMPTY) {
					empties.add(new short[] { row, col });
				}
			}
		}
		return empties;
	}

	//return checkers that are not in a triple, unless there are only triples
	//idea delle maschere
	private List<short[]> getFoePositions() {
		boolean foeNotInTriple = false;
		List<short[]> allPos = new ArrayList<short[]>();
		List<short[]> notInTriplePos = new ArrayList<short[]>();
		short foe = this.getOppositePlayer();
		for (short row = 0; row < EUGState.ROWS; row++) {
			for (short col = 0; col < EUGState.COLUMNS; col++) {
				if (board[row][col] == foe) {
					allPos.add(new short[] { row, col });
					if(!doesMill(row,col,foe)) {
						notInTriplePos.add(new short[]{row, col});
						foeNotInTriple = true;
					}
				}
			}
		}
		return foeNotInTriple ? notInTriplePos : allPos;
	}

	private List<short[]> getCurrentPlayerPositions() {
		List<short[]> currentPos = new ArrayList<short[]>();
		for (short row = 0; row < EUGState.ROWS; row++) {
			for (short col = 0; col < EUGState.COLUMNS; col++) {
				if (board[row][col] == this.currentPlayer) {
					currentPos.add(new short[] { row, col });
				}
			}
		}
		return currentPos;
	}

	private short getOppositePlayer() {
		return (currentPlayer == EUGState.WHITE) ? EUGState.BLACK : EUGState.WHITE;
	}

	public EUGState applyAction(EUGAction action) {
		EUGState newState = this.clone();
		if (newState.getCurrentPhase() == EUGState.PHASE1)
			newState.addChecker(action.getToRow(), action.getToCol());
		else
			newState.move(action.getFromRow(), action.getFromCol(), action.getToRow(), action.getToCol());

		if (action.hasRemove())
			newState.removeChecker(action.getRemoveRow(), action.getRemoveCol());

		newState.togglePlayer();
		newState.togglePhase();
		return newState;
	}

	//todo
	public double getUtility(){
		double index=-1;
		if(blackCheckersTable < whiteCheckersTable) index= 1;
		else if(blackCheckersTable == whiteCheckersTable) index = 0;
		return (whoAmI == EUGState.BLACK) ? index*(-1) : index;
	}

	public short getPositionChecker(short row, short column) {
		return this.board[row][column];
	}

	public short getCurrentPlayer() {
		return this.currentPlayer;
	}

	public short[][] getBoard() {
		return this.board;
	}

	public short getBlackCheckersHand() {
		return blackCheckersHand;
	}

	public short getWhiteCheckersHand() {
		return whiteCheckersHand;
	}

	public void setCurrentPlayer(short currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void setBlackCheckersHand(short blackCheckersHand) {
		this.blackCheckersHand = blackCheckersHand;
	}

	public void setWhiteCheckersHand(short whiteCheckersHand) {
		this.whiteCheckersHand = whiteCheckersHand;
	}

	public short getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(short currentPhase) {
		this.currentPhase = currentPhase;
	}

	public short getBlackCheckersTable() {
		return blackCheckersTable;
	}

	public void setBlackCheckersTable(short blackCheckersTable) {
		this.blackCheckersTable = blackCheckersTable;
	}

	public short getWhiteCheckersTable() {
		return whiteCheckersTable;
	}

	public void setWhiteCheckersTable(short whiteCheckersTable) {
		this.whiteCheckersTable = whiteCheckersTable;
	}

	@Override
	public String toString() {
		return "EUGState{" +
				"currentPlayer=" + currentPlayer +
				", currentPhase=" + currentPhase +
				", blackCheckersHand=" + blackCheckersHand +
				", whiteCheckersHand=" + whiteCheckersHand +
				", blackCheckersTable=" + blackCheckersTable +
				", whiteCheckersTable=" + whiteCheckersTable +
				", whoAmI=" + whoAmI +
				", board=" + Arrays.toString(board) +
				'}';
	}
}
