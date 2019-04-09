package ubiquigame.games.square_run;

public class Tile {
    private boolean isPassable = true;
    private boolean isWay;
    private boolean isStart;
    private boolean isFinish;

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public void setWay(boolean way) {
        isWay = way;
    }

    public boolean isWay() {
        return isWay;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isFinish() {
        return isFinish;
    }
}