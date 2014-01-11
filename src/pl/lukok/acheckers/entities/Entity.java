package pl.lukok.acheckers.entities;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import pl.lukok.acheckers.Player;
import pl.lukok.acheckers.board.*;

import java.util.LinkedList;

/**
 * Created by lukasz on 12/30/13.
 */
abstract public class Entity {

    // x, y position in the world from 0 to 1
    protected float x, y;

    // destination position in the world
    protected PointF destination;

    protected float dx, dy, minDx, minDy, speed;

    private Player player;

    private LinkedList<CMove> moves;
    private CMove currentMove;

    protected int type;

    public static final int TYPE_DRAUGHT = 1;
    public static final int TYPE_QUEEN = 2;

    public static final int MOVE_DIRECTION_UP    = -1;
    public static final int MOVE_DIRECTION_DOWN  = 1;
    public static final int MOVE_DIRECTION_LEFT  = -1;
    public static final int MOVE_DIRECTION_RIGHT = 1;

    public Entity(Player player, float x, float y) {

        this.setPosition(new PointF(x, y));
        this.stop();
        this.player = player;
        this.minDx = minDy = 1/500f;
    }

    protected abstract boolean canGoUp();
    protected abstract boolean canGoDown();
    protected abstract int getMoveDistance();


    synchronized public void update(long elapsedTime) {

        if (this.isMovingX()) {
            this.x += dx * elapsedTime;
        }

        if (this.isMovingY()) {
            this.y += dy * elapsedTime;
        }

        if (destination != null) {

            float diffX = Math.abs(this.x - destination.x);
            float diffY = Math.abs(this.y - destination.y);

            float minDiffX = 1.2f*elapsedTime*Math.abs(dx);
            float minDiffY = 1.2f*elapsedTime*Math.abs(dy);

            Log.d("is destination: ", diffX + " < " + minDiffX + "  && " + diffY + " < " + minDiffY);

            if (diffX < minDiffX && diffY < minDiffY) {

                setPosition(destination);
                if (!startNextMove()) {
                    stop();
                    if (currentMove != null && currentMove.isDestination(getBoardPosition())) {
                        getPlayer().finishedTurn();
                    }
                }
            }
        }
    }

    protected LinkedList<CMove> getMoves() {
        return moves;
    }

    public LinkedList<CMove> getMoves(CBoard board) {
        if (getMoves() == null) {
            selectMoves(board);
        }
        return getMoves();
    }

    public void selectMoves(CBoard board) {

        Point position = getBoardPosition();
        moves = new LinkedList<CMove>();

        if (canGoUp()) {
            addMovesUp(board, position);
        } else {
            // TODO sprawdz bicie do tylu
        }

        if (canGoDown()) {
            addMovesDown(board, position);
        } else {
            // TODO sprawdz bicie do tylu
        }
    }

    private void addMovesUp(CBoard board, Point position) {
        addMoves(board, MOVE_DIRECTION_LEFT, MOVE_DIRECTION_UP, position, position.y);
        addMoves(board, MOVE_DIRECTION_RIGHT, MOVE_DIRECTION_UP, position, position.y);
    }

    private void addMovesDown(CBoard board, Point position) {
        addMoves(board, MOVE_DIRECTION_LEFT, MOVE_DIRECTION_DOWN, position, CBoard.BOARD_SIZE - position.y - 1);
        addMoves(board, MOVE_DIRECTION_RIGHT, MOVE_DIRECTION_DOWN, position, CBoard.BOARD_SIZE - position.y - 1);
    }

    private void addMoves(CBoard board, int dirX, int dirY, Point p, int maxMoves) {

        int movesCount = Math.min(maxMoves, getMoveDistance());
        boolean capture = false;

        CMove move = new CMove();
        CJump jump = new CJump();

        for(int i=0, y=p.y, x=p.x; i < movesCount; i++) {

            y+=dirY;
            x+=dirX;
            Point position = new Point(x, y);
            jump.setPosition(position);

            if (!CBoardUtils.isValidPosition(position)) {
                break;
            }
            if (board.isEntityAt(position)) {
                if (capture) {
                    break;
                }

                if (board.isOpponentEntityAt(getPlayer(), position)) {
                    movesCount++; // TODO to tylko dla pionkow
                    capture = true;
                    jump.addCapture(board.getEntityFrom(position));
                    continue;
                }
                break;
            }

            move.addJump(jump);
            moves.add(move);
            move = new CMove();

            if (capture) {
                break;
            }
        }
    }

    public void setPosition(PointF position) {
        this.x = position.x;
        this.y = position.y;
        this.destination = null;
    }

    public void stop() {
        this.dx = 0;
        this.dy = 0;
    }

    public void move(PointF dest) {
        this.move(dest.x, dest.y);
    }

    public void move(float x, float y) {

        this.destination = new PointF(x, y);

        float diffX = x - this.x;
        float diffY = y - this.y;

        float speedCoefficient = getSpeed(diffX, diffY);

        dx = Math.abs(diffX) * speedCoefficient * Math.signum(diffX);
        dy = Math.abs(diffY) * speedCoefficient * Math.signum(diffY);

        Log.d("move dx: ", dx + " with " + getSpeed(diffX, diffY) + " // " + this.x + " - " + destination.x + " = " + (this.x - destination.x));
        Log.d("move dy: ", dy + " with " + getSpeed(diffY, diffY) + " // " + this.y + " - " + destination.y + " = " + (this.y - destination.y));
    }

    protected void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public Point getBoardPosition() {
        return CBoardUtils.convertWorldToBoardPosition(this.getX(), this.getY());
    }

    public boolean hasBoardPosition(Point position) {
        return this.getBoardPosition().equals(position.x, position.y);
    }

    public boolean isMoving() {
        return (isMovingX() || isMovingY());
    }

    private boolean isMovingY() {
        return dy != 0;
    }

    private boolean isMovingX() {
        return dx != 0;
    }

    protected float getSpeed(float diffX, float diffY) {
        return (Math.abs(diffX) < 0.1f && Math.abs(diffY) < 0.1f ? speed * 5 : speed);
    }

    /**
     *
     * @param speed in fields per secound
     */
    protected void setSpeed(int speed) {
        this.speed = speed/(CBoard.BOARD_SIZE*1000f);
    }

    public void clearMoves() {
        moves = null;
        currentMove = null;
    }

    public CMove getMove(Point boardPosition) {
        for(int i = 0; i < getMoves().size(); i++) {
            CMove move = getMoves().get(i);
            if (move.isDestination(boardPosition)) {
                return move;
            }
        }
        return null;
    }

    public boolean setMove(CMove move) {

        currentMove = move;
        return startMove();
    }

    protected boolean startNextMove() {

        if (currentMove == null) {
            return false;
        }

        if (currentMove.nextJump()) {
            return startMove();
        }

        return false;
    }

    protected boolean startMove() {
        if (!currentMove.hasJumps()) {
            return false;
        }
        Point jump = currentMove.getCurrentJump().getPosition();
        PointF dest = CBoardUtils.convertBoardToWorldPosition(jump.x, jump.y);
        move(dest);
        return true;
    }

    public CMove getCurrentMove() {
        return currentMove;
    }

    public boolean hasCaptureEntity() {

        if (getCurrentMove() == null) {
            return false;
        }

        if (getCurrentMove().getCurrentJump().wasJumped()) {
            return getCurrentMove().getCurrentJump().getCaptured() != null;
        }

        return false;
    }

    public Entity getCapturedEntity() {
        return getCurrentMove().getCurrentJump().getCaptured();
    }

    public boolean isAllowedMove(Point dest) {
        for(int i = 0; i < getMoves().size(); i++) {
            if (getMoves().get(i).isDestination(dest)) {
                Log.d("Entity::isAllowedMove: ", " ");
                return true;
            }
        }
        return false;
    }
}
