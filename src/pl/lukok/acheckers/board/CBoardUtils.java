package pl.lukok.acheckers.board;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

/**
 * Created by lukasz on 12/31/13.
 */
public class CBoardUtils {



    public static PointF convertBoardToWorldPosition(int x, int y) {
        return new PointF((float)(x+1)/(float)CBoard.BOARD_SIZE - 1f/16f, (float)(y+1)/(float)CBoard.BOARD_SIZE - 1f/16f);
    }

    public static Point convertWorldToBoardPosition(float x, float y) {
        float fieldSize = 1/(float)CBoard.BOARD_SIZE;

//        Log.d("convertWorldToBoardPosition x: ", x + " / " + fieldSize + " = " + (x / fieldSize));
//        Log.d("convertWorldToBoardPosition y: ", y + " / " + fieldSize + " = " + (y / fieldSize));

        return new Point((int)Math.floor(x / fieldSize), (int)Math.floor(y / fieldSize));
    }

    public static boolean isValidPosition(Point position) {
        return (position.x >= 0 && position.x < CBoard.BOARD_SIZE &&
                position.y >= 0 && position.y < CBoard.BOARD_SIZE);
    }
}
