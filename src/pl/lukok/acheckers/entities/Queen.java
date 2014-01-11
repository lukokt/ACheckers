package pl.lukok.acheckers.entities;

import android.graphics.PointF;
import pl.lukok.acheckers.Player;

/**
 * Created by lukasz on 12/30/13.
 */
public class Queen extends Entity {

    public Queen(Player player, PointF position) {
        super(player, position.x, position.y);
        setType(Entity.TYPE_QUEEN);

        // dlugos trzech pola na sekunde
        setSpeed(10);
    }

    @Override
    protected boolean canGoUp() {
        return true;
    }

    @Override
    protected boolean canGoDown() {
        return true;
    }

    @Override
    protected int getMoveDistance() {
        return 7;
    }
}
