package game.utils;

import game.strategy.EUGClient;
import game.strategy.Player;
import it.unibo.ai.didattica.mulino.domain.State;

public class Launcher {

	static int DEFAULT_TIME_SEC = 60;

	public static void main(String[] args) {
		State.Checker col = State.Checker.EMPTY;
		int moveTimeSec = DEFAULT_TIME_SEC;

		// process args
		switch (args.length) {
		case 2: // time
			moveTimeSec = Integer.parseInt(args[1]);

		case 1: // color
			col = (args[0].equals("White")) ? State.Checker.WHITE : ((args[0].equals("Black")) ? State.Checker.BLACK : null);
			if (col == null) {
				usage();
				System.exit(-1);
			}
			break;

		default:
			usage();
			System.exit(-1);
		}
		EUGClient client = null;
		try{
			client = new EUGClient(col);
		}catch(Exception e){
			System.exit(1);
		}
		Player p = new Player(col, moveTimeSec, client);
		p.start();
		
	}

	public static void usage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Usage error\n");
		sb.append("java -jar EUropeanGenius.jar White|Black [timeout]\n");
		sb.append("[timeout] -> answering time interval in seconds\n");
		System.out.println(sb.toString());
	}

}
