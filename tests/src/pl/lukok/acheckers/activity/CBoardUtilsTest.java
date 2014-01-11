package pl.lukok.acheckers.activity;

import android.graphics.Point;
import android.graphics.PointF;
import junit.framework.TestCase;
import pl.lukok.acheckers.board.CBoardUtils;

/**
 * Created by lukasz on 12/31/13.
 */
public class CBoardUtilsTest extends TestCase {




    public void testConvertBoardToWorldPosition() throws Exception {

        PointF p = CBoardUtils.convertBoardToWorldPosition(0, 0);
        assertEquals(new PointF(0.0625f, 0.0625f), p);

        p = CBoardUtils.convertBoardToWorldPosition(7, 7);
        assertEquals(new PointF(0.9375f, 0.9375f), p);

    }

    public void testConvertWorldToBoardPosition() throws Exception {
        Point p = CBoardUtils.convertWorldToBoardPosition(0.0625f, 0.0625f);
        assertEquals(new Point(0, 0), p);

        p = CBoardUtils.convertWorldToBoardPosition(0.9375f, 0.9375f);
        assertEquals(new Point(7, 7), p);
    }

}
