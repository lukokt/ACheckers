package pl.lukok.acheckers.board;

import android.graphics.Point;
import pl.lukok.acheckers.entities.Entity;

/**
 * Created by lukasz on 1/6/14.
 */
public class CJump {

    private Point position;
    private Entity captured;
    private boolean taken;

    public CJump() {
    }

    public CJump(Point position) {

        this.position = position;
    }

    public CJump(int x, int y) {

        this.position = new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public boolean equals(Point point) {
        return position.equals(point.x, point.y);
    }

    public boolean wasJumped() {
        return taken;
    }

    public void setAsJumped() {
        taken = true;
    }

    public void addCapture(Entity entity) {
        this.captured = entity;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Entity getCaptured() {
        return captured;
    }

    public void setCaptured(Entity captured) {
        this.captured = captured;
    }
}
