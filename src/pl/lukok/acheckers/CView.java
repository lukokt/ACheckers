package pl.lukok.acheckers;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import pl.lukok.ACheckers.R;
import pl.lukok.acheckers.board.CBoard;
import pl.lukok.acheckers.board.CField;
import pl.lukok.acheckers.board.CMove;
import pl.lukok.acheckers.entities.Draught;
import pl.lukok.acheckers.entities.Entity;
import pl.lukok.acheckers.entities.Queen;

import java.util.LinkedList;

/**
 * Created by lukasz on 12/30/13.
 */
public class CView extends View {

    private Rect boardRect, fieldRect;
    private int fieldSize;

    private Paint paint;
    private int background, fieldWhite, fieldBlack, draughtsWhite, draughtsBlack;

    private Canvas c;
    private Bitmap bitmap;

    private Game game;
    private boolean drag = false;

    public CView(Context context, Game game) {
        super(context);

        this.game = game;
        this.initSize(this.getWidth(), this.getHeight());

        this.bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        this.c = new Canvas();

        fieldWhite = getResources().getColor(R.color.fieldWhite);
        fieldBlack = getResources().getColor(R.color.fieldBlack);
        draughtsWhite = getResources().getColor(R.color.draughtsWhite);
        draughtsBlack = getResources().getColor(R.color.draughtsBlack);
        background = getResources().getColor(R.color.background);

        this.paint = new Paint();

//        this.setOnDragListener(new OnDragListener() {
//
//            public boolean onDrag(View view, DragEvent event) {
//
//                //    super.onDragEvent(event);
//
//                Log.d("onDragEvent: ", " ");
//
//                return false;
//            }
//
//        });
    }


//    @Override
//    public boolean onDrag(View view, DragEvent event) {
//        Log.d("onDrag View: ", " ");
//        return false;
//    }

//    public boolean onDragEvent(DragEvent event) {
//        Log.d("onDragEvent View: ", " ");
//
//        return false;
//    }

    public boolean onTouchEvent(MotionEvent event) {

 //       Log.d("event: ", (new Float(event.getX())).toString() + ", " + (new Float(event.getY())).toString());


        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                drag = false;
                Point down = this.convertToBoardPosition(event.getX(), event.getY());
                game.selectEntity(down);
                break;

            case MotionEvent.ACTION_MOVE:
                drag = true;
                PointF point = convertScreenToWorldPosition(event.getX(), event.getY());
                game.moveActiveEntity(point);
                break;

            case MotionEvent.ACTION_UP:
                if (drag) {
                    Point up = convertToBoardPosition(event.getX(), event.getY());
                    game.placeActiveEntity(up);
                }
                break;
        }
        invalidate();

        return true;//super.onTouchEvent(event);   FOR ACTION_MOVE
    }

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if(bitmap.isRecycled() || bitmap.getWidth()!=canvas.getWidth() || bitmap.getHeight()!=canvas.getHeight())
        {
            bitmap.recycle();
            bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            c.setBitmap(bitmap);
        }

        paint.setAntiAlias(true);
        paint.setColor(background);

        c.drawRect(0, 0, getWidth(), getHeight(), paint);

        this.render(c, game.getBoard());
        this.render(c, game.getOpponentPlayer().getEntities());
        this.render(c, game.getCurrentPlayer().getEntities());

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void render(Canvas canvas, LinkedList<Entity> entities) {
        if (entities.isEmpty()) {
            return;
        }

        for(int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            this.render(canvas, entity);
        }
    }

    private void render(Canvas canvas, CBoard board) {

         for(int x = 0; x < CBoard.BOARD_SIZE; x++) {
            for(int y = 0; y < CBoard.BOARD_SIZE; y++) {

                CField field = board.getField(new Point(x, y));
                paint.setColor(field.isWhite() ? fieldWhite : fieldBlack);
                canvas.drawRect(getFieldRect(field), paint);
            }
        }

        if (game.getCurrentPlayer().hasActiveEntity()) {

            CField field = game.getBoard().getField(game.getCurrentPlayer().getActiveEntity());
            if (field != null) {
                highlightField(canvas, field, Color.BLUE);
            }

            LinkedList<CMove> moves = game.getCurrentPlayer().getActiveEntity().getMoves(game.getBoard());
            for(int i = 0; i < moves.size(); i++) {
                render(canvas, moves.get(i));
            }
        }
    }

    private void render(Canvas canvas, CMove move) {

        Point dest = move.getDestination();
        highlightField(canvas, game.getBoard().getField(dest), Color.GREEN);
    }

    private void render(Canvas canvas, Entity entity) {
        switch (entity.getType()) {
            case Entity.TYPE_DRAUGHT:
                this.render(canvas, (Draught) entity);
                break;
            case Entity.TYPE_QUEEN:
                this.render(canvas, (Queen) entity);
                break;
        }
    }

    private void render(Canvas canvas, Draught draughtsman) {

        paint.setColor(draughtsman.getPlayer().isWhite() ? draughtsWhite : draughtsBlack);

        Point position = this.convertWorldToScreenPosition(draughtsman.getX(), draughtsman.getY());

        canvas.drawCircle(position.x, position.y, fieldSize/2-5, paint);
    }

    private void render(Canvas canvas, Queen draughtsman) {

        paint.setColor(draughtsman.getPlayer().isWhite() ? draughtsWhite : draughtsBlack);

        Point position = this.convertWorldToScreenPosition(draughtsman.getX(), draughtsman.getY());

        canvas.drawCircle(position.x, position.y, fieldSize/2-5, paint);

        paint.setColor(Color.BLACK);
        canvas.drawCircle(position.x, position.y, 5, paint);
    }

    private Point convertWorldToScreenPosition(float x, float y) {
        return new Point(boardRect.left + (int)(x * (float)boardRect.width()), boardRect.top + (int)(y * (float)boardRect.height()));
    }

    private PointF convertScreenToWorldPosition(float x, float y) {
        return new PointF((x - boardRect.left)/(float)getBoardSize(), (y-boardRect.top)/(float)getBoardSize());
    }

    private Point convertToBoardPosition(float x, float y) {
        int worldX = (int)Math.floor((x - boardRect.left) / fieldSize);
        int worldY = (int)Math.floor((y - boardRect.top) / fieldSize);

        return new Point(worldX, worldY);
    }

    private void highlightField(Canvas canvas, CField field, int color) {

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.NORMAL));

        canvas.drawRect(getFieldRect(field), paint);
    }

    private Rect getFieldRect(CField field) {
        fieldRect.offsetTo(boardRect.left + fieldSize * field.getX(), boardRect.top + fieldSize * field.getY());
        return fieldRect;
    }

    protected void onSizeChanged (int w, int h, int oldw, int oldh){
        initSize(w, h);
    }

    private void initSize(int width, int height) {

        fieldSize = Math.min(width, height) / CBoard.BOARD_SIZE;

        int offsetX = (width - getBoardSize()) / 2;
        int offsetY = (height - getBoardSize()) / 2;

        boardRect = new Rect(offsetX, offsetY, offsetX+getBoardSize(), offsetY+getBoardSize());
        fieldRect = new Rect(0, 0, fieldSize, fieldSize);
    }

    private int getBoardSize() {
        return CBoard.BOARD_SIZE*fieldSize;
    }
}