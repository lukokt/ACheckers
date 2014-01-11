package pl.lukok.acheckers.board;

import android.graphics.Point;
import android.util.Log;
import pl.lukok.acheckers.Player;
import pl.lukok.acheckers.entities.Entity;

import java.util.LinkedList;

/**
 * Created by lukasz on 12/30/13.
 */
public class CBoard {

    public static int BOARD_SIZE = 8;
    public static int BOARD_TOP  = 0;

    protected CField[][] board;

    public CBoard() {
        this.init();
    }

    public void init() {
        this.board = new CField[CBoard.BOARD_SIZE][CBoard.BOARD_SIZE];

        for(int x = 0; x < CBoard.BOARD_SIZE; x++) {
            for(int y = 0; y < CBoard.BOARD_SIZE; y++) {
                this.board[x][y] = new CField(x, y);
            }
        }
    }

    public CField getField(Point position) {
        if (!CBoardUtils.isValidPosition(position)) {
            return null;
        }
        return this.getField(position.x, position.y);
    }

    public CField getField(int x, int y) {
        if (!CBoardUtils.isValidPosition(new Point(x, y))) {
            return null;
        }
        return this.board[x][y];
    }

    public boolean isEntityAt(Point position) {
        return getField(position).hasEntity();
    }

    public boolean isOpponentEntityAt(Player player, Point position) {
        return !getField(position).getEntity().getPlayer().equals(player);
    }

    public Entity getEntityFrom(Point position) {
        return getField(position).getEntity();
    }

    public CField getField(Entity captured) {

        Point boardPosition = captured.getBoardPosition();
        return getField(boardPosition);
    }

    public CField searchField(Entity entity) {
        for(int x = 0; x < CBoard.BOARD_SIZE; x++) {
            for(int y = 0; y < CBoard.BOARD_SIZE; y++) {
                if( getField(x, y).hasEntity(entity)) {
                    return getField(x, y);
                }
            }
        }
        return null;
    }

    public void move(Entity entity, Point destination) {
        getField(entity).removeEntity();
        getField(destination).setEntity(entity);
    }

    public void cleanEntities() {
        for(int x = 0; x < CBoard.BOARD_SIZE; x++) {
            for(int y = 0; y < CBoard.BOARD_SIZE; y++) {
                getField(x, y).removeEntity();
            }
        }
    }

    public void setEntity(Entity entity) {

        CField field = getField(entity);

        if (field != null) {
            field.setEntity(entity);
        }
    }
}
