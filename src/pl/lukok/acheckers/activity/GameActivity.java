package pl.lukok.acheckers.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import pl.lukok.acheckers.CView;
import pl.lukok.acheckers.Game;


/**
 * Created by lukasz on 12/30/13.
 */
public class GameActivity extends Activity {

    private Handler frame = new Handler();

    private static int FRAME_RATE = 30; // 20 frames per second

    private Game game;
    private CView view;

    protected long lastUpdate = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new Game();
        view = new CView(this, game);
        super.setContentView(view);

        frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            frame.removeCallbacks(frameUpdate);

            if (lastUpdate == 0) {
                lastUpdate = System.currentTimeMillis();
            }

            long elapsedTime = System.currentTimeMillis() - lastUpdate;

            if (game.isRunning()) {
                // is running  any animation
                game.update(elapsedTime);
                view.invalidate();
                if (game.getCurrentPlayer().hasFinished()) {
                    game.switchCurrentPlayer();
                }
            }

            lastUpdate = System.currentTimeMillis();

            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };

}
