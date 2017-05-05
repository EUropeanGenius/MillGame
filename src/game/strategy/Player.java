package game.strategy;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unibo.ai.didattica.mulino.domain.State;

public class Player {
//	private long ram; //bytes
	//private Color color; //enum
	private State.Checker color;
	private long moveInterval; //millis
	private EUGClient client;
//	private static long FACTOR = 1024*1024;
	
	public Player(State.Checker color, long moveInterval, long ram, EUGClient client) {
		super();
//		this.ram = ram;
		this.color = color;
		this.moveInterval = moveInterval;
		this.client = client;
	}

//	public long getRam() {
//		return ram/FACTOR;
//	}
//
//	public void setRam(long ram) {
//		this.ram = ram*FACTOR;
//	}

	public State.Checker getColor() {
		return color;
	}

	public void setColor(State.Checker color) {
		this.color = color;
	}

	public long getMoveInterval() {
		return moveInterval/1000;
	}

	public void setMoveInterval(long moveInterval) {
		this.moveInterval = moveInterval*1000;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EUropeanGenius\n");
		sb.append("[\n");
		sb.append("\tColor : "+ this.getColor().toString()+"\n");
		sb.append("\tTime (s): "+ this.getMoveInterval()+"\n");
//		sb.append("\tRam (MB): "+ this.getRam()+"\n");
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
			act = computeMove(state);
			try{
				client.write(act, state.getCurrentPhase());
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
	
	private String computeMove(State state) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("nuova azione");
		try{
			return in.readLine();
		}catch(Exception e){}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + (int) (moveInterval ^ (moveInterval >>> 32));
//		result = prime * result + (int) (ram ^ (ram >>> 32));
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
//		if (ram != other.ram)
//			return false;
		return true;
	}
	 
	
	
	
	
}
