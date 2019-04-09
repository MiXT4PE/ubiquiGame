package ubiquigame.common.constants;

import java.awt.*;

public enum PlayerIdentifier {

    PLAYER1(1, new Color(222/255f, 44/255f, 33/255f, 1f)),
    PLAYER2(2, new Color(33/255f, 161/255f, 222/255f, 1f)),
    PLAYER3(3, new Color(222/255f, 218/255f, 33/255f, 1f)),
    PLAYER4(4, new Color(65/255f, 222/255f, 33/255f, 1f));

    public final Color color;
    public final int index;

    PlayerIdentifier(int i, Color color) {
        this.index = i;
        this.color = color;
    }

    public static Color getColorForIndex(int index) {
        switch (index) {
            case 1:
                return PLAYER1.color;
            case 2:
                return PLAYER2.color;
            case 3:
                return PLAYER3.color;
            case 4:
                return PLAYER4.color;
            default:
                return null;
        }
    }
}
