package pl.lukok.acheckers;

import android.graphics.Point;
import android.graphics.PointF;
import pl.lukok.acheckers.board.CBoard;
import pl.lukok.acheckers.board.CBoardUtils;
import pl.lukok.acheckers.board.CField;
import pl.lukok.acheckers.board.CMove;
import pl.lukok.acheckers.entities.Entity;
import pl.lukok.acheckers.entities.EntityFactory;

/**
 * Created by lukasz on 12/31/13.
 */
public class Game {

    private Player playerWhite, playerBlack, current;
    private CBoard board;

    public Game() {

        this.startNewGame();
    }

    public boolean isRunning() {
        return this.getCurrentPlayer().isMoving();
    }

    public void startNewGame() {

        this.playerWhite = new Player(Player.TYPE_WHITE, "Łukasz");
        this.playerBlack = new Player(Player.TYPE_BLACK, "Imi");
        this.current = playerWhite;

        this.initBoard();
    }

    private void initBoard() {

        this.board = new CBoard();

        for(int i = 0; i < CBoard.BOARD_SIZE; i++) {

            // player white
            if (board.getField(i, 0).isBlack()) {
                setDraught(playerBlack, getBoard().getField(i, 0));
            }
            if (board.getField(i, 1).isBlack()) {
                setDraught(playerBlack, getBoard().getField(i, 1));
            }
            if (board.getField(i, 2).isBlack()) {
                setDraught(playerBlack, getBoard().getField(i, 2));
            }

            // player black
            if (board.getField(i, 5).isBlack()) {
                setDraught(playerWhite, getBoard().getField(i, 5));
            }
            if (board.getField(i, 6).isBlack()) {
                setDraught(playerWhite, getBoard().getField(i, 6));
            }
            if (board.getField(i, 7).isBlack()) {
                setDraught(playerWhite, getBoard().getField(i, 7));
            }
        }
    }

    protected void removeEntity(Entity captured) {

        Player player = captured.getPlayer();
        CField field = getBoard().getField(captured);

        player.getEntities().remove(captured);
        field.removeEntity();
    }

    protected void setEntity(Player player, CField field, int type) {

        Entity old = field.getEntity();
        Entity entity = EntityFactory.createEntity(player, field.getX(), field.getY(), type);
        field.setEntity(entity);

        if (old != null && player.getEntities().contains(old)) {
            player.getEntities().remove(old);
        }

        player.getEntities().add(entity);
    }

    protected void setDraught(Player player, CField field) {
        this.setEntity(player, field, Entity.TYPE_DRAUGHT);
    }

    protected void setQueen(CField field) {
        this.setEntity(field.getEntity().getPlayer(), field, Entity.TYPE_QUEEN);
    }

    protected boolean move(Entity entity, Point boardPosition) {

        CMove move = entity.getMove(boardPosition);

        if (move != null) {
            if (entity.setMove(move)) {
                getBoard().move(entity, boardPosition);
                return true;
            }
        }
        return false;
    }

    public void selectEntity(Point position) {

        if (getCurrentPlayer().hasActiveEntity()) {

            if (getCurrentPlayer().getActiveEntity().isAllowedMove(position)) {
                move(getCurrentPlayer().getActiveEntity(), position);
                return;
            }
        }

        // TODO nie set tylo select active entity at
        getCurrentPlayer().setActiveEntity(position);
    }

    public void moveActiveEntity(PointF point) {

        if (getCurrentPlayer().hasActiveEntity()) {
            getCurrentPlayer().getActiveEntity().setPosition(point);
        }
    }

    public void placeActiveEntity(Point position) {
        if (getCurrentPlayer().hasActiveEntity()) {
            if (!move(getCurrentPlayer().getActiveEntity(), position)) {
                CField field = getBoard().searchField(getCurrentPlayer().getActiveEntity());
                if (field != null) {
                    PointF moveBack = CBoardUtils.convertBoardToWorldPosition(field.getX(), field.getY());
                    getCurrentPlayer().getActiveEntity().move(moveBack);
                }
            }
        }
    }

    synchronized public void switchCurrentPlayer() {

        getCurrentPlayer().finishedTurn();
        rebuildEntityPositions();

        if (getCurrentPlayer() == getPlayerWhite()) {
            setCurrentPlayer(getPlayerBlack());
        } else {
            setCurrentPlayer(getPlayerWhite());
        }

        getCurrentPlayer().startTurn();
    }

    protected void rebuildEntityPositions() {
        getBoard().cleanEntities();
        for(int i = 0; i < getPlayerWhite().getEntities().size(); i++) {
            getBoard().setEntity(getPlayerWhite().getEntities().get(i));
        }
        for(int i = 0; i < getPlayerBlack().getEntities().size(); i++) {
            getBoard().setEntity(getPlayerBlack().getEntities().get(i));
        }
    }

    synchronized public void update(long elapsedTime) {

        getCurrentPlayer().update(elapsedTime);

        if (getCurrentPlayer().hasActiveEntity()) {
            if (getCurrentPlayer().getActiveEntity().hasCaptureEntity()) {
                removeEntity(getCurrentPlayer().getActiveEntity().getCapturedEntity());
            }
        }

        if (!isRunning()) {
            getCurrentPlayer().unselectActiveEntity();
        }
    }

    public void setCurrentPlayer(Player current) {
        this.current = current;
    }

    public Player getPlayerWhite() {
        return this.playerWhite;
    }

    public Player getPlayerBlack() {
        return this.playerBlack;
    }

    public Player getCurrentPlayer() {
        return this.current;
    }

    public Player getOpponentPlayer() {
        if (getCurrentPlayer() == getPlayerWhite()) {
            return getPlayerBlack();
        } else {
            return getPlayerWhite();
        }
    }

    public CBoard getBoard() {
        return this.board;
    }
}
