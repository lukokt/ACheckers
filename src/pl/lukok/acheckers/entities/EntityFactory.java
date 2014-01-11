package pl.lukok.acheckers.entities;

import android.graphics.PointF;
import pl.lukok.acheckers.Player;
import pl.lukok.acheckers.board.CBoardUtils;

/**
 * Created by lukasz on 1/2/14.
 */
public class EntityFactory {

    public static Entity createEntity(Player player, int x, int y, int type) {

        PointF position = CBoardUtils.convertBoardToWorldPosition(x, y);

        switch (type) {
            case Entity.TYPE_DRAUGHT:
                return new Draught(player, position);
            case Entity.TYPE_QUEEN:
                return new Queen(player, position);
        }

        return null;
    }

    public static Draught createDraught(Player player, int x, int y) {
        return (Draught)EntityFactory.createEntity(player, x, y, Entity.TYPE_DRAUGHT);
    }

    public static Queen createQueen(Player player, int x, int y) {
        return (Queen)EntityFactory.createEntity(player, x, y, Entity.TYPE_QUEEN);
    }
}
