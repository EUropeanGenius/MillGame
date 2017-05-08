package game.strategy;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Phase2Action;
import it.unibo.ai.didattica.mulino.actions.PhaseFinalAction;
import it.unibo.ai.didattica.mulino.client.MulinoClient;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;

public class EUGClient extends MulinoClient {

	public EUGClient(Checker player) throws UnknownHostException, IOException {
		super(player);
		// TODO Auto-generated constructor stub
	}
	
//	public void write(String actionString, State.Phase phase) throws IOException, ClassNotFoundException {
//        Action action = null;
//        switch (phase) {
//            case FIRST:
//                Phase1Action phase1Action = new Phase1Action();
//                phase1Action.setPutPosition(actionString.substring(0, 2));
//                if (actionString.length() == 4) {
//                    phase1Action.setRemoveOpponentChecker(actionString.substring(2, 4));
//                } else {
//                    phase1Action.setRemoveOpponentChecker(null);
//                }
//                action = phase1Action;
//                break;
//            case SECOND:
//                Phase2Action phase2Action = new Phase2Action();
//                phase2Action.setFrom(actionString.substring(0, 2));
//                phase2Action.setTo(actionString.substring(2, 4));
//                if (actionString.length() == 6) {
//                    phase2Action.setRemoveOpponentChecker(actionString.substring(4, 6));
//                } else {
//                    phase2Action.setRemoveOpponentChecker(null);
//                }
//                action = phase2Action;
//                break;
//            case FINAL:
//                PhaseFinalAction phaseFinalAction = new PhaseFinalAction();
//                phaseFinalAction.setFrom(actionString.substring(0, 2));
//                phaseFinalAction.setTo(actionString.substring(2, 4));
//                if (actionString.length() == 6) {
//                    phaseFinalAction.setRemoveOpponentChecker(actionString.substring(4, 6));
//                } else {
//                    phaseFinalAction.setRemoveOpponentChecker(null);
//                }
//                action = phaseFinalAction;
//                break;
//        }
//        this.write(action);
//    }

}
