package game.strategy;

public class EUGAction {

    private String from="";
    private String to="";
    private String remove="";

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setRemove(String remove) {
        this.remove = remove;
    }

    public String getFrom() {

        return from;
    }

    public String getTo() {
        return to;
    }

    public String getRemove() {
        return remove;
    }

    public boolean hasRemove(){
        return !remove.equals("");
    }

    @Override
    public String toString() {
        return from+to+remove;
    }
}
