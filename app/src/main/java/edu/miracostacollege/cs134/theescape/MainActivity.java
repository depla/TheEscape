package edu.miracostacollege.cs134.theescape;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.miracostacollege.cs134.theescape.model.Direction;
import edu.miracostacollege.cs134.theescape.model.Player;
import edu.miracostacollege.cs134.theescape.model.Zombie;

import static edu.miracostacollege.cs134.theescape.model.BoardValues.FREE;
import static edu.miracostacollege.cs134.theescape.model.BoardValues.EXIT;
import static edu.miracostacollege.cs134.theescape.model.BoardValues.OBST;
import static edu.miracostacollege.cs134.theescape.model.Direction.DOWN;
import static edu.miracostacollege.cs134.theescape.model.Direction.LEFT;
import static edu.miracostacollege.cs134.theescape.model.Direction.RIGHT;
import static edu.miracostacollege.cs134.theescape.model.Direction.UP;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private int wins = 0;
    private int losses = 0;

    public static final int TOTAL_ROWS = 8;
    public static final int TOTAL_COLS = 8;

    public static final int PLAYER_ROW = 1;
    public static final int PLAYER_COL = 1;

    public static final int ZOMBIE_ROW = 2;
    public static final int ZOMBIE_COL = 4;

    public static final int EXIT_ROW = 5;
    public static final int EXIT_COL = 7;

    private static final float FLING_THRESHOLD = 500f;

    private LinearLayout boardLinearLayout;
    private TextView winsTextView;
    private TextView lossesTextView;
    private GestureDetector gestureDetector;

    private Player player;
    private Zombie zombie;

    final int gameBoard[][] = {
            {OBST, OBST, OBST, OBST, OBST, OBST, OBST, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, OBST, OBST},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, OBST, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, FREE, EXIT},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, OBST, OBST, OBST, OBST, OBST, OBST, OBST}
    };

    ImageView viewBoard[][] = new ImageView[TOTAL_ROWS][TOTAL_COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLinearLayout = findViewById(R.id.boardLinearLayout);
        winsTextView = findViewById(R.id.winsTextView);
        lossesTextView = findViewById(R.id.lossesTextView);

        gestureDetector = new GestureDetector(this, this);
        startNewGame();
    }

    //override onTouchEvent method ctrl o
    //distributes touch event to the entire activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void startNewGame() {
        //nested for loop
        for(int i = 0; i < TOTAL_ROWS; i++)
        {
            LinearLayout row = (LinearLayout) boardLinearLayout.getChildAt(i);

            for(int j = 0; j < TOTAL_COLS; j++)
            {
                ImageView imageView = (ImageView) row.getChildAt(j);

                //put it into the imageBoard
                viewBoard[i][j] = imageView;

                //determine what image to place in the view (from the model)
                switch (gameBoard[i][j]) {
                    case OBST:
                        imageView.setImageResource(R.drawable.obstacle);
                        break;

                    case EXIT:
                        imageView.setImageResource(R.drawable.exit);
                        break;

                    case FREE:
                        imageView.setImageDrawable(null);
                        break;
                }
            }
        }

        //player starts at 1, 1
        player = new Player(PLAYER_ROW, PLAYER_COL);
        viewBoard[PLAYER_ROW][PLAYER_COL].setImageResource(R.drawable.male_player);

        zombie = new Zombie(ZOMBIE_ROW, ZOMBIE_COL);
        viewBoard[ZOMBIE_ROW][ZOMBIE_COL].setImageResource(R.drawable.zombie);
        //TODO: Loop through the viewBoard and initialize each of the ImageViews
        //TODO: to the children of the LinearLayouts
        //TODO: Use the gameBoard to determine which image to assign:

        //TODO: OBST = R.drawable.obstacle
        //TODO: EXIT = R.drawable.exit
        //TODO: FREE = null (no image to load)

        //TODO: Instantiate a new Player object at PLAYER_ROW, PLAYER_COL
        //TODO: Set the imageView at that position to R.drawable.player

        //TODO: Instantiate a new Zombie object at ZOMBIE_ROW, ZOMBIE_COL
        //TODO: Set the imageView at that position to R.drawable.zombie
    }

    private void movePlayer(float velocityX, float velocityY) {

        //TODO: Set the player's current image view drawable to null
        viewBoard[player.getRow()][player.getCol()].setImageDrawable(null);

        float absX = Math.abs(velocityX);
        float absY = Math.abs(velocityY);
        Direction direction;

        //logic for direction
        if(absY > absX)
        {
            if(velocityY < 0)
            {
                direction = UP;
            }
            else
            {
                direction = DOWN;
            }
        }
        else
        {
            if(velocityX < 0)
            {
                direction = LEFT;
            }
            else
            {
                direction = RIGHT;
            }
        }

        player.move(gameBoard, direction);

        viewBoard[player.getRow()][player.getCol()].setImageResource(R.drawable.male_player);
        //TODO: Determine the direction of the fling (based on velocityX and velocityY)
        //TODO: The velocity must exceed FLING_THRESHOLD to count (otherwise, it's not really a move)
        //TODO: Move the player
        //TODO: Set the player's current image view to R.drawable.player after the move
    }

    private void moveZombie() {
        //TODO: Set the zombie's current image view drawable to null
        viewBoard[zombie.getRow()][zombie.getCol()].setImageDrawable(null);
        //TODO: Move the zombie
        zombie.move(gameBoard, player.getRow(), player.getCol());
        //TODO: Set the zombie's current image view to R.drawable.zombie after the move
        viewBoard[zombie.getRow()][zombie.getCol()].setImageResource(R.drawable.zombie);
    }

    private void determineOutcome() {
        //TODO: Determine the outcome of the game (win or loss)
        //TODO: It's a win if the player's row/col is the same as the exit row/col
        //TODO: Call the handleWin() method

        //TODO: It's a loss if the player's row/col is the same as the zombie's row/col
        //TODO: Call the handleLoss() method

        //TODO: Otherwise, do nothing, just return.
    }

    private void handleWin()
    {
        //TODO: Implement the handleWin() method by accomplishing the following:
        //TODO: Increment the wins
        //TODO: Set the imageView (at the zombie's row/col) to the R.drawable.bunny
        //TODO: Start an animation
        //TODO: Wait 2 seconds, then start a new game
    }

    private void handleLoss()
    {
        //TODO: Implement the handleLoss() method by accomplishing the following:
        //TODO: Increment the losses
        //TODO: Set the imageView (at the player's row/col) to the R.drawable.blood
        //TODO: Start an animation
        //TODO: Wait 2 seconds, then start a new game
    }


    Runnable newGameRunnable = new Runnable() {
        @Override
        public void run() {
            startNewGame();
        }
    };

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        movePlayer(velocityX, velocityY);
        moveZombie();
        determineOutcome();

        return true;
    }
}
