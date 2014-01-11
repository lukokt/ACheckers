package pl.lukok.acheckers.board;

import pl.lukok.acheckers.entities.Entity;

/**
 * Created by lukasz on 12/30/13.
 */
public class CField {

    protected int x, y;

    protected Entity entity;
    // TODO to encja powinna miec liste dostepnych ruchow
    // by po ponownym zaznaczeniu nie liczyc ich od nowa

    public CField(int x, int y) {

        this.x = x;
        this.y = y;
        this.entity = null;
    }

    public boolean isWhite() {
        return (this.x + this.y) % 2 == 0;
    }

    public boolean hasEntity() {
        return this.entity != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isBlack() {
        return !this.isWhite();
    }

    public void removeEntity() {
        this.setEntity(null);
    }

    public boolean hasEntity(Entity entity) {
        if (hasEntity()) {
            return getEntity().equals(entity);
        }
        return false;
    }
}
