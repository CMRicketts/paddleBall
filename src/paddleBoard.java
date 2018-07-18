/*
* Breakout for Java.
* Caleb Ricketts
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;

public class paddleBoard extends GraphicsProgram {

    private GRect paddle;
    private GOval ball;
    private double dx, dy;

    // Width and height of application window in pixels
    private static final int APPLICATION_WIDTH = 800;
    private static final int APPLICATION_HEIGHT = 1200;

    // Dimensions of game board (usually the same)
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

    // Dimensions of the paddle
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 10;

    // Offset of the paddle up from the bottom
    private static final int PADDLE_Y_OFFSET = 190;

    // Number of bricks per row
    private static final int NBRICKS_PER_ROW = 5;

    // Number of rows of bricks
    private static final int NBRICK_ROWS = 5;

    // Separation between bricks
    private static final int BRICK_SEP = 4;

    // Width of a brick
    private static final int BRICK_WIDTH =
            (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    // Height of a brick
    private static final int BRICK_HEIGHT = 8;

    // Radius of the ball in pixels
    private static final int BALL_RADIUS = 10;

    // Offset of the top brick row from the top
    private static final int BRICK_Y_OFFSET = 80;

    // Number of turns
    private static final int NTURNS = 3;

    private void addBlocks(){
        for(int i = 0; i < NBRICK_ROWS; i++){
            for(int j = 0; j < NBRICKS_PER_ROW;j++){
                GRect rect = new GRect(i * (BRICK_WIDTH + BRICK_SEP), j * (BRICK_HEIGHT + BRICK_SEP), BRICK_WIDTH, BRICK_HEIGHT);
                if(j < 2){
                    rect.setFillColor(Color.red);
                } else if( j < 4){
                    rect.setFillColor(Color.orange);
                } else {
                    rect.setFillColor(Color.PINK);
                }
                rect.setFilled(true);
                add(rect);
            }
        }
    }

    private void removeBrick(GObject g){
        remove(g);
    }

    private void removePaddle() {
        if (paddle != null) {
            remove(paddle);
        }
    }

    private GObject getCollidingObject(double x, double y){
        GObject item = getElementAt(x, y);
        if(item != null){
            return(item);
        }else{
            return(null);
        }
    }

    private void makePaddle(){
        paddle = new GRect(30, APPLICATION_HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH,PADDLE_HEIGHT);
        paddle.setFilled(true);
        add(paddle);
    }

    private void moveBall(){
        while(true){
            GObject item = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2);
            ball.move(dx, dy);
            pause(1000/60.0);
            if(ball.getY() < 0){
                dy = -dy;
            }
            if(ball.getX() > WIDTH - 2*BALL_RADIUS || ball.getX() < 0){
                dx = -dx;
            }
            if(ball.getY() > HEIGHT - 2*BALL_RADIUS){
                break;
            }
            item = getCollidingObject(ball.getX(),ball.getY());
            if(item == null){
                item = getCollidingObject(ball.getX() + BALL_RADIUS * 2, ball.getY());
            }
            if(item == null){
                item = getCollidingObject(ball.getX(), ball.getY() + BALL_RADIUS * 2);
            }
            if(item == null){
                item = getCollidingObject(ball.getX() + BALL_RADIUS * 2, ball.getY() + BALL_RADIUS * 2);
            }
            if(item != null){
                if (item == paddle && dy > 0) {
                    dy = -dy;
                } else {
                    if (item != paddle) {
                        dy = -dy;
                        removeBrick(item);
                    }
                }
            }
        }
    }

    private void makeBall(){
        ball = new GOval(getWidth()/2, getHeight()/2, BALL_RADIUS * 2, BALL_RADIUS *2);
        ball.setFilled(true);
        add(ball);

    }

    public void init() {
        setSize(APPLICATION_WIDTH,APPLICATION_HEIGHT);
    }

    @Override public void mouseMoved(MouseEvent e){
        double x = e.getX() - paddle.getWidth() / 2.0;

        if(e.getX()< paddle.getWidth()/2 ){
            x = 0;
        }
        if(e.getX() > getWidth() - paddle.getWidth()/2){
            x = getWidth()-paddle.getWidth();
        }

        paddle.setLocation(x, APPLICATION_HEIGHT - PADDLE_Y_OFFSET);

    }

    @Override public void mousePressed(MouseEvent e){
        RandomGenerator rgen = RandomGenerator.getInstance();
        dx = rgen.nextDouble(1.0, 3.0);
        dy = 10;
        if (rgen.nextBoolean(0.5)) dx = -dx;


    }

    public void run() {
        for(int i = 0; i < NTURNS; i++) {
            removePaddle();
            pause(200);
            addBlocks();
            makePaddle();
            addMouseListeners();
            makeBall();
            moveBall();
        }
    }

    public static void main(String[] args) {
            new paddleBoard().start(args);

    }

}
