package pl.lukok.acheckers.entities;

import android.graphics.PointF;
import pl.lukok.acheckers.Player;

/**
 * Created by lukasz on 12/30/13.
 */
public class Draught extends Entity {

    public Draught(Player player, PointF position) {
        super(player, position.x, position.y);
        setType(Entity.TYPE_DRAUGHT);

        // dlugos jednego pola na sekunde
        setSpeed(12);
    }

    @Override
    protected boolean canGoUp() {
        return getPlayer().isWhite();
    }

    @Override
    protected boolean canGoDown() {
        return getPlayer().isBlack();
    }

    @Override
    protected int getMoveDistance() {
        return 1;
    }
}
