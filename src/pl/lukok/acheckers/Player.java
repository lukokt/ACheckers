package pl.lukok.acheckers;

import android.graphics.Point;
import android.util.Log;
import pl.lukok.acheckers.entities.Entity;

import java.util.LinkedList;

/**
 * Created by lukasz on 12/31/13.
 */
public class Player {
    public static final int TYPE_WHITE  = 1;
    public static final int TYPE_BLACK  = 2;

    private boolean finished = false;
    private int type;

    private Entity activeEntity = null;

    private LinkedList<Entity> entities = null;

    public Player(int type) {

        this.entities = new LinkedList<Entity>();
        this.setType(type);
    }

    public boolean isWhite() {
        return this.getType() == Player.TYPE_WHITE;
    }

    public boolean isBlack() {
        return this.getType() == Player.TYPE_BLACK;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Entity getActiveEntity() {
        return activeEntity;
    }

    public void setActiveEntity(Entity activeEntity) {
        this.activeEntity = activeEntity;
    }

    public boolean hasActiveEntity() {
        return this.getActiveEntity() != null;
    }

    public void unselectActiveEntity() {
        this.activeEntity = null;
    }

    public boolean setActiveEntity(Point position) {

        for(int i = 0; i < this.getEntities().size(); i++) {
            if (this.getEntities().get(i).hasBoardPosition(position)) {
                this.setActiveEntity(this.getEntities().get(i));
                return true;
            }
        }

        this.unselectActiveEntity();
        return false;
    }

    public void update(long elapsedTime) {

//        if (hasActiveEntity()) {
//            getActiveEntity().update(elapsedTime);
//        }

        for(int i = 0; i < getEntities().size(); i++) {
            this.getEntities().get(i).update(elapsedTime);
        }
    }

    public boolean isMoving() {

        if (!hasActiveEntity()) {
             return false;
        }

        return getActiveEntity().isMoving();

//        for(int i = 0; i < this.getEntities().size(); i++) {
//            if (this.getEntities().get(i).isMoving()) {
//                return true;
//            }
//        }
//        return false;
    }

    public LinkedList<Entity> getEntities() {
        return entities;
    }

    public boolean equals(Player player) {
        return (this.isWhite() == player.isWhite());
    }

    public void switchTurn() {

        Log.d("SWITCH TURN: ", " !!!!! ");

        for(int i = 0; i < getEntities().size(); i++) {
            getEntities().get(i).clearMoves();
        }

        unselectActiveEntity();
        finished = false;
    }

    public boolean hasFinished() {
        return finished;
    }

    public void finishedTurn() {
        Log.d("finish turn: ", "true");
        finished = true;
    }
}
