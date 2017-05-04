package game.utils;

import game.strategy.Player;

public class Launcher {

	static int DEFAULT_RAM_MB = 2 * 1024;
	static int DEFAULT_TIME_SEC = 60;

	public static void main(String[] args) {
		Color col = Color.WHITE;
		int moveTimeSec = DEFAULT_TIME_SEC;
		int ramMb = DEFAULT_RAM_MB;

		// process args
		switch (args.length) {
		case 3: // memory
			ramMb = Integer.parseInt(args[2]);

		case 2: // time
			moveTimeSec = Integer.parseInt(args[1]);

		case 1: // color
			col = (args[0].equals("w")) ? Color.WHITE : ((args[0].equals("b")) ? Color.BLACK : null);
			if (col == null) {
				usage();
				System.exit(-1);
			}
			break;

		default:
			usage();
			System.exit(-1);
		}

		Player p = new Player(col, moveTimeSec * 1000, ramMb * 1024 * 1024);
		p.start();
	}

	public static void usage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Usage error\n");
		sb.append("java -jar w|b [time] [memMax]\n");
		sb.append("[time] -> answering time interval in seconds\n");
		sb.append("[memMax] -> max size of memory available in MB\n");
		System.out.println(sb.toString());
	}

}
