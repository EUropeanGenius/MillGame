package game.strategy;

import game.rules.Hamburger;

public class EUGAction {

    private short toRow = -1;
    private short toCol = -1;
    private short fromRow = -1;
    private short fromCol = -1;
    private short removeRow = -1;
    private short removeCol = -1;

    public void setTo(short row, short col){
        this.toRow = row;
        this.toCol = col;
    }

    public void setFrom(short row, short col){
        this.fromRow = row;
        this.fromCol = col;
    }

    public void setRemove(short row, short col){
        this.removeRow = row;
        this.removeCol = col;
    }

    public boolean hasRemove(){
        return (removeRow != -1 && removeCol != -1);
    }

    public String toString(){
        String action="";
        if(fromRow != -1 && fromCol != -1)
            action+=Hamburger.RCToString.get(""+fromRow+fromCol);
        if(toRow != -1 && toCol != -1)
            action+=Hamburger.RCToString.get(""+toRow+toCol);
        if(removeRow != -1 && removeCol != -1)
            action+=Hamburger.RCToString.get(""+removeRow+removeCol);

        return (action);
    }
    public short getToRow() {
        return toRow;
    }

    public void setToRow(short toRow) {
        this.toRow = toRow;
    }

    public short getToCol() {
        return toCol;
    }

    public void setToCol(short toCol) {
        this.toCol = toCol;
    }

    public short getFromRow() {
        return fromRow;
    }

    public void setFromRow(short fromRow) {
        this.fromRow = fromRow;
    }

    public short getFromCol() {
        return fromCol;
    }

    public void setFromCol(short fromCol) {
        this.fromCol = fromCol;
    }

    public short getRemoveRow() {
        return removeRow;
    }

    public void setRemoveRow(short removeRow) {
        this.removeRow = removeRow;
    }

    public short getRemoveCol() {
        return removeCol;
    }

    public void setRemoveCol(short removeCol) {
        this.removeCol = removeCol;
    }
}
