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
			currentPhase = (whiteCheckersHand ==0 && blackCheckersHand == 0) ? EUGState.PHASE2 : currentPhase;
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
				clone.board[row][column] = this.getPositionChecker(row, column);
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
			List<short[]> allCurrentPositions = this.getCurrentPlayerPositions();
			for (short[] e : empties) {
				List<short[]> currentAvailablePos = (actualCurrentPhase() == EUGState.PHASE2) ?
						this.getCurrentPlayerAdiacentPositions(e[0],e[1]) : allCurrentPositions;

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

	private List<short[]> getCurrentPlayerAdiacentPositions(short row,short col){
		List<short[]> adiacentPos = new ArrayList<short[]>();
		for(short[] pos : Hamburger.neighbors.get(row+","+col)){
			if (board[pos[0]][pos[1]] == this.currentPlayer) {
				adiacentPos.add(new short[] { pos[0], pos[1] });
			}
		}
		return adiacentPos;
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

	public short getUtility(){
		short index=0;
		if(!(this.getCurrentPhase() == EUGState.PHASE1)) {
			if (this.getCheckersTable(this.notMe()) == 2)  return 3000;
			else if (this.getCheckersTable(whoAmI) == 2) return -3000;
		}
		else{
			if(this.whiteCheckersTable == 0 || this.blackCheckersTable == 0) return 0;
		}

		short myCheckersTable = this.getCheckersTable(whoAmI);
		short foeCheckersTable = this.getCheckersTable(this.notMe());
		short myMills = this.numberOfMills(whoAmI);
		short foeMills = this.numberOfMills(this.notMe());
		short myCouples = this.numberOfOpenCouples(whoAmI);
		short foeCouples = this.numberOfOpenCouples(this.notMe());
		short myDegree = this.degreesOfFreedom(whoAmI);
		short foeDegree = this.degreesOfFreedom(this.notMe());

		switch(this.currentPhase){
			case EUGState.PHASE1:
				short myCheckersShouldTable = ((short)(TOT_CHECKERS - this.getCheckersHand(whoAmI)));
				short foeCheckersShouldTable =((short)(TOT_CHECKERS - this.getCheckersHand(this.notMe())));

				if((myCheckersTable/myCheckersShouldTable) > (foeCheckersTable/foeCheckersShouldTable))
					index +=30;
				else index -=30;

				index += myMills*20*(TOT_CHECKERS - this.getCheckersHand(whoAmI))/3;
				index -= foeMills*25*(TOT_CHECKERS - this.getCheckersHand(this.notMe()))/3;
				index += (myCouples-myMills)*150*(this.getCheckersHand(whoAmI)+1)/3;
				index -= (foeCouples-foeMills)*180*(this.getCheckersHand(this.notMe())+1)/3;
				index += (myDegree-foeDegree-1)*(TOT_CHECKERS-this.getCheckersHand(whoAmI));
				break;
			case EUGState.PHASE2:
				if(this.getCheckersTable(whoAmI) > this.getCheckersTable(this.notMe()))	index +=35;
				else index -=35;

				index += myMills*25*TOT_CHECKERS/3;
				index -= foeMills*30*TOT_CHECKERS/3;
				index += (myCouples-myMills)*5/3;
				index -= (foeCouples-foeMills)*6/3;
				index += (myDegree-foeDegree-1)*(TOT_CHECKERS-(this.getCheckersTable(whoAmI)-this.getCheckersTable(this.notMe())));
				break;
			case EUGState.PHASE3:
				if(this.getCheckersTable(whoAmI) >= this.getCheckersTable(this.notMe())){ //loro ne hanno 3 e noi di piu
					index += myMills*290;
					index -= foeMills*90;
					index += (myCouples-myMills);
					index -= (foeCouples-foeMills)*2;
					
				}else{ //noi ne abbiamo 3 e loro possono averne o 3 o di piu -> BLOCCARLO O STATO RIPETUTO
					index += myMills*75;
					index -= foeMills*290;
					index += (myCouples-myMills);
					index -= (foeCouples-foeMills)*2;
					
				}
				break;
		}

		return index;
	}

	private short notMe(){
		return (whoAmI == EUGState.WHITE) ? EUGState.BLACK : EUGState.WHITE;
	}

	private short numberOfMills(short color){
		short mills=0;
		for (short[][] mill : Hamburger.mills) {
			if(this.board[mill[0][0]][mill[0][1]] == color &&
					this.board[mill[1][0]][mill[1][1]] == color &&
					this.board[mill[2][0]][mill[2][1]] == color)
				mills++;
		}
		return mills;
	}
	
	private short degreesOfFreedom(short color){
		short degrees = 0;
		for (short row = 0; row < EUGState.ROWS; row++) {
			for (short col = 0; col < EUGState.COLUMNS; col++) {
				if(board[row][col] != color) continue;
				for(short[] neighbor : Hamburger.neighbors.get(row+","+col)){
					if(board[neighbor[0]][neighbor[1]] == EUGState.EMPTY)
						degrees++;
				}
			}
		}
		return degrees;
	}

	private short numberOfCouples(short color){
		short couples=0,k=0;
		for (short[][] mill : Hamburger.mills) {
			for (int i = 0; i<3; i++)
				if(this.board[mill[i][0]][mill[i][1]] == color) k++;

			if(k == 2)
				couples++;
		}
		return couples;
	}

	private short numberOfOpenCouples(short color){
		short couples=0;
		for (short[][] mill : Hamburger.mills) {
			if((this.board[mill[0][0]][mill[0][1]] == color && this.board[mill[1][0]][mill[1][1]] == color && this.board[mill[2][0]][mill[2][1]] == EUGState.EMPTY) ||
					(this.board[mill[0][0]][mill[0][1]] == color && this.board[mill[1][0]][mill[1][1]] == EUGState.EMPTY && this.board[mill[2][0]][mill[2][1]] == color) ||
					(this.board[mill[0][0]][mill[0][1]] == EUGState.EMPTY && this.board[mill[1][0]][mill[1][1]] == color && this.board[mill[2][0]][mill[2][1]] == color))
				couples++;
		}
		return couples;
	}

	private short numberOfWannabeMill(short color){
		short couples=0;
		for (short[][] mill : Hamburger.mills) {
			if((this.board[mill[0][0]][mill[0][1]] == color && this.board[mill[1][0]][mill[1][1]] == color && this.board[mill[2][0]][mill[2][1]] == EUGState.EMPTY)){
				List<short[]> neighbors = Hamburger.neighbors.get(mill[2][0]+","+mill[2][1]);
				for(short[] neigh : neighbors){
					if(neigh[0] != mill[0][0] && neigh[1] != mill[0][1] && neigh[0] != mill[1][0] && neigh[1] != mill[1][1]) {
						if(board[neigh[0]][neigh[1]] == color)
							couples++;
					}
				}
			}
			else if(this.board[mill[0][0]][mill[0][1]] == color && this.board[mill[1][0]][mill[1][1]] == EUGState.EMPTY && this.board[mill[2][0]][mill[2][1]] == color){
				List<short[]> neighbors = Hamburger.neighbors.get(mill[1][0]+","+mill[1][1]);
				for(short[] neigh : neighbors){
					if(neigh[0] != mill[0][0] && neigh[1] != mill[0][1] && neigh[0] != mill[2][0] && neigh[1] != mill[2][1]) {
						if(board[neigh[0]][neigh[1]] == color)
							couples++;
					}
				}
			}
			else if(this.board[mill[0][0]][mill[0][1]] == EUGState.EMPTY && this.board[mill[1][0]][mill[1][1]] == color && this.board[mill[2][0]][mill[2][1]] == color){
				List<short[]> neighbors = Hamburger.neighbors.get(mill[0][0]+","+mill[0][1]);
				for(short[] neigh : neighbors){
					if(neigh[0] != mill[2][0] && neigh[1] != mill[2][1] && neigh[0] != mill[1][0] && neigh[1] != mill[1][1]) {
						if(board[neigh[0]][neigh[1]] == color)
							couples++;
					}
				}
			}
		}
		return couples;
	}

	private short getCheckersTable(short color){
		return (color == EUGState.WHITE) ? this.whiteCheckersTable : this.blackCheckersTable;
	}

	private short getCheckersHand(short color){
		return (color == EUGState.WHITE) ? this.whiteCheckersHand : this.blackCheckersHand;
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
