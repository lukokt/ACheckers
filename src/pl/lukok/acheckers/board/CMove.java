package pl.lukok.acheckers.board;

import android.graphics.Point;
import android.util.Log;
import pl.lukok.acheckers.entities.Entity;

import java.util.LinkedList;

/**
 * Created by lukasz on 1/5/14.
 */
public class CMove {

    private LinkedList<CJump> jumps;
    private int current = 0;

    public CMove() {
        init();
    }

/*
    public CMove(Point position) {
        init();
        addJump(position);
    }
*/

    private void init() {
        jumps = new LinkedList<CJump>();
    }

    private LinkedList<CJump> getJumps() {
        return jumps;
    }

/*
    public void addCapture(Entity entity) {
        captures.add(entity);
    }
*/

    public Point getDestination() {
        return getJumps().getLast().getPosition();
    }

    public void addJump(CJump jump) {
        getJumps().add(jump);
    }

    public boolean isDestination(Point dest) {
        return getJumps().getLast().equals(dest);
    }

//    public boolean hasCapture() {
//        return
//    }
//
//    public Entity getCaptured() {
//        if (!hasCapture()) {
//            return null;
//        }
//        return getCaptures().poll(); //.iterator().next();
//    }

    public boolean hasJumps() {
        return getJumps().size() > 0;
    }

    public CJump getCurrentJump() {
        return getJumps().get(current);
    }

    public boolean nextJump() {

        getCurrentJump().setAsJumped();
        if (getJumps().size() > current+1) {
            current++;
            return true;
        }
        return false;
    }

    private CJump getNextJump() {
        if (getJumps().size() > current) {
            return getJumps().get(current++);
        }
        return null;
    }
}
