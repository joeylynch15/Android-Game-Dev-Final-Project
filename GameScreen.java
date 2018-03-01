package edu.angelo.finalprojectlynch;

import java.util.List;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;

public class GameScreen extends Screen {
    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver
    }

    GameState state = GameState.Ready;
    World world;
    int oldScore = 0;
    String score = "0";

    public GameScreen(Game game) {
        super(game);
        world = new World();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        if (state == GameState.Ready) {
            updateReady(touchEvents);
        }
        if (state == GameState.Running) {
            updateRunning(touchEvents, deltaTime);
        }
        if (state == GameState.Paused) {
            updatePaused(touchEvents);
        }
        if (state == GameState.GameOver) {
            updateGameOver(touchEvents);
        }
    }

    private void updateReady(List<TouchEvent> touchEvents) {
        if (touchEvents.size() > 0) {
            state = GameState.Running;
        }
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i += 1) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < 64 && event.y < 64) {
                    if (Settings.soundEnabled) {
                        Assets.paused.play(1); //Changed the sound played when the paused button is tapped from 'clicked' to 'paused'
                    }
                    state = GameState.Paused;
                    return;
                }
            }
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (event.x < 64 && event.y > 416) {
                    world.ship.angleVelocity = -5;
                }
                else if (event.x > 64 && event.x < 128 && event.y > 416) {
                    world.ship.angleVelocity = 5;
                }
                else if (event.x > 192 && event.x < 256 && event.y > 416) {
                    world.ship.thrust();
                }
                else if (event.x > 256  && event.y > 416) {
                    if (!world.ship.activeBullet)
                    {
                        world.ship.shoot();
                    }
                }
            }
        }

        world.update(deltaTime);
        if (world.gameOver) {
            if (Settings.soundEnabled) {
                Assets.bitten.play(1);
            }
            state = GameState.GameOver;
        }
        if (oldScore != world.score) {
            oldScore = world.score;
            score = "" + oldScore;
            if (Settings.soundEnabled) {
                Assets.eat.play(1);
            }
        }
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i += 1) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 80 && event.x <= 240) {
                    if (event.y > 100 && event.y <= 148) {
                        if (Settings.soundEnabled) {
                            Assets.click.play(1);
                        }
                        state = GameState.Running;
                        return;
                    }
                    if (event.y > 148 && event.y < 196) {
                        if (Settings.soundEnabled) {
                            Assets.click.play(1);
                        }
                        game.setScreen(new MainMenuScreen(game));
                        return;
                    }
                }
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i += 1) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x >= 128 && event.x <= 192 &&
                        event.y >= 200 && event.y <= 264) {
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.background, 0, 0);
        drawWorld(world);
        if (state == GameState.Ready) {
            drawReadyUI();
        }
        if (state == GameState.Running) {
            drawRunningUI();
        }
        if (state == GameState.Paused) {
            drawPausedUI();
        }
        if (state == GameState.GameOver) {
            drawGameOverUI();
        }

        drawText(g, score, g.getWidth() / 2 - score.length() * 20 / 2, g.getHeight() - 42);
    }

    private void drawWorld(World world) {
        Graphics g = game.getGraphics();
        Ship ship = world.ship;

        for (int i = 0; i < world.asteroidSet.asteroids.size(); i++)
        {
            g.drawPixmap(Assets.asteroidLarge, world.asteroidSet.asteroids.get(i).locationX - 16, world.asteroidSet.asteroids.get(i).locationY - 16);
        }

        if (ship.activeBullet)
        {
            g.drawLine(ship.bullet.x - 2, ship.bullet.y - 2, ship.bullet.x + 2, ship.bullet.y + 2, Color.BLACK);
            g.drawLine(ship.bullet.x - 2, ship.bullet.y + 2, ship.bullet.x + 2, ship.bullet.y - 2, Color.BLACK);
        }

        if (ship.angleTop == 0)
        {
            g.drawLine((double) ship.x, (double) ship.y - 10, ship.x + 10 * Math.cos(Math.toRadians(ship.angleRight - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleRight - 90)), Color.BLACK);
            g.drawLine((double) ship.x, (double) ship.y - 10, ship.x - 10 * Math.cos(Math.toRadians(ship.angleRight - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleRight - 90)), Color.BLACK);
        }
        else if (ship.angleTop > 0 && ship.angleTop < 45)
        {
            g.drawLine(ship.x + 10 * Math.sin(Math.toRadians(ship.angleTop)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleTop)), ship.x + 10 * Math.cos(Math.toRadians(ship.angleRight - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleRight - 90)), Color.BLACK);
            g.drawLine(ship.x + 10 * Math.sin(Math.toRadians(ship.angleTop)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleTop)), ship.x - 10 * Math.sin(Math.toRadians(ship.angleRight - 90)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleRight - 90)), Color.BLACK);
        }
        else if (ship.angleTop == 45)
        {
            g.drawLine(ship.x + 10 * Math.sin(Math.toRadians(ship.angleTop)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleTop)), (double) ship.x, (double) ship.y + 10, Color.BLACK);
            g.drawLine(ship.x + 10 * Math.sin(Math.toRadians(ship.angleTop)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleTop)), (double) ship.x - 10, (double) ship.y, Color.BLACK);
        }
        else if (ship.angleTop > 45 && ship.angleTop < 90)
        {
            g.drawLine(ship.x + 10 * Math.sin(Math.toRadians(ship.angleTop)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleTop)), ship.x - 10 * Math.sin(Math.toRadians(ship.angleRight - 180)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleRight - 180)), Color.BLACK);
            g.drawLine(ship.x + 10 * Math.sin(Math.toRadians(ship.angleTop)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleTop)), ship.x - 10 * Math.cos(Math.toRadians(ship.angleRight - 180)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleRight - 180)), Color.BLACK);
        }
        else if (ship.angleTop == 90)
        {
            g.drawLine((double) ship.x + 10, (double) ship.y, ship.x - 10 * Math.sin(Math.toRadians(ship.angleRight - 180)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleRight - 180)), Color.BLACK);
            g.drawLine((double) ship.x + 10, (double) ship.y, ship.x - 10 * Math.cos(Math.toRadians(ship.angleRight - 180)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleRight - 180)), Color.BLACK);
        }
        else if (ship.angleTop > 90 && ship.angleTop < 135)
        {
            g.drawLine(ship.x + 10 * Math.cos(Math.toRadians(ship.angleTop - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleTop - 90)), ship.x - 10 * Math.sin(Math.toRadians(ship.angleRight - 180)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleRight - 180)), Color.BLACK);
            g.drawLine(ship.x + 10 * Math.cos(Math.toRadians(ship.angleTop - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleTop - 90)), ship.x - 10 * Math.cos(Math.toRadians(ship.angleRight - 180)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleRight - 180)), Color.BLACK);
        }
        else if (ship.angleTop == 135)
        {
            g.drawLine(ship.x + 10 * Math.cos(Math.toRadians(ship.angleTop - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleTop - 90)), ship.x - 10, ship.y, Color.BLACK);
            g.drawLine(ship.x + 10 * Math.cos(Math.toRadians(ship.angleTop - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleTop - 90)), ship.x, ship.y - 10, Color.BLACK);
        }
        else if (ship.angleTop > 135 && ship.angleTop < 180)
        {
            g.drawLine(ship.x + 10 * Math.cos(Math.toRadians(ship.angleTop - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleTop - 90)), ship.x - 10 * Math.cos(Math.toRadians(ship.angleRight - 270)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleRight - 270)), Color.BLACK);
            g.drawLine(ship.x + 10 * Math.cos(Math.toRadians(ship.angleTop - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleTop - 90)), ship.x + 10 * Math.sin(Math.toRadians(ship.angleRight - 270)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleRight - 270)), Color.BLACK);
        }
        else if (ship.angleTop == 180)
        {
            g.drawLine(ship.x, ship.y + 10, ship.x - 10 * Math.cos(Math.toRadians(ship.angleRight - 270)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleRight - 270)), Color.BLACK);
            g.drawLine(ship.x, ship.y + 10, ship.x + 10 * Math.sin(Math.toRadians(ship.angleRight - 270)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleRight - 270)), Color.BLACK);
        }
        else if (ship.angleTop > 180 && ship.angleTop < 225)
        {
            g.drawLine(ship.x - 10 * Math.sin(Math.toRadians(ship.angleTop - 180)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleTop - 180)), ship.x - 10 * Math.cos(Math.toRadians(ship.angleRight - 270)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleRight - 270)), Color.BLACK);
            g.drawLine(ship.x - 10 * Math.sin(Math.toRadians(ship.angleTop - 180)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleTop - 180)), ship.x + 10 * Math.sin(Math.toRadians(ship.angleRight - 270)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleRight - 270)), Color.BLACK);
        }
        else if (ship.angleTop == 225)
        {
            g.drawLine(ship.x - 10 * Math.sin(Math.toRadians(ship.angleTop - 180)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleTop - 180)), ship.x, ship.y - 10, Color.BLACK);
            g.drawLine(ship.x - 10 * Math.sin(Math.toRadians(ship.angleTop - 180)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleTop - 180)), ship.x + 10, ship.y, Color.BLACK);
        }
        else if (ship.angleTop > 225 && ship.angleTop < 270)
        {
            g.drawLine(ship.x - 10 * Math.sin(Math.toRadians(ship.angleTop - 180)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleTop - 180)), ship.x + 10 * Math.sin(Math.toRadians(ship.angleRight)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleRight)), Color.BLACK);
            g.drawLine(ship.x - 10 * Math.sin(Math.toRadians(ship.angleTop - 180)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleTop - 180)), ship.x + 10 * Math.cos(Math.toRadians(ship.angleRight)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleRight)), Color.BLACK);
        }
        else if (ship.angleTop == 270)
        {
            g.drawLine(ship.x - 10, ship.y, ship.x + 10 * Math.sin(Math.toRadians(ship.angleRight)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleRight)), Color.BLACK);
            g.drawLine(ship.x - 10, ship.y, ship.x + 10 * Math.cos(Math.toRadians(ship.angleRight)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleRight)), Color.BLACK);
        }
        else if (ship.angleTop > 270 && ship.angleTop < 315)
        {
            g.drawLine(ship.x - 10 * Math.cos(Math.toRadians(ship.angleTop - 270)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleTop - 270)), ship.x + 10 * Math.sin(Math.toRadians(ship.angleRight)), ship.y - 10 * Math.cos(Math.toRadians(ship.angleRight)), Color.BLACK);
            g.drawLine(ship.x - 10 * Math.cos(Math.toRadians(ship.angleTop - 270)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleTop - 270)), ship.x + 10 * Math.cos(Math.toRadians(ship.angleRight)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleRight)), Color.BLACK);
        }
        else if (ship.angleTop == 315)
        {
            g.drawLine(ship.x - 10 * Math.cos(Math.toRadians(ship.angleTop - 270)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleTop - 270)), ship.x + 10, ship.y, Color.BLACK);
            g.drawLine(ship.x - 10 * Math.cos(Math.toRadians(ship.angleTop - 270)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleTop - 270)), ship.x, ship.y + 10, Color.BLACK);
        }
        else if (ship.angleTop > 315 && ship.angleTop < 360)
        {
            g.drawLine(ship.x - 10 * Math.cos(Math.toRadians(ship.angleTop - 270)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleTop - 270)), ship.x + 10 * Math.cos(Math.toRadians(ship.angleRight - 90)), ship.y + 10 * Math.sin(Math.toRadians(ship.angleRight - 90)), Color.BLACK);
            g.drawLine(ship.x - 10 * Math.cos(Math.toRadians(ship.angleTop - 270)), ship.y - 10 * Math.sin(Math.toRadians(ship.angleTop - 270)), ship.x - 10 * Math.sin(Math.toRadians(ship.angleRight - 90)), ship.y + 10 * Math.cos(Math.toRadians(ship.angleRight - 90)), Color.BLACK);
        }

        /*
        g.drawLine(ship.x, ship.y - 10, ship.x + 10, ship.y + 10, Color.BLACK);
        g.drawLine(ship.x, ship.y - 10, ship.x - 10, ship.y + 10, Color.BLACK);
        g.drawLine(ship.x, ship.y, ship.x + 10, ship.y + 10, Color.BLACK);
        g.drawLine(ship.x, ship.y, ship.x - 10, ship.y + 10, Color.BLACK);
        */
        //g.drawPixmap(Assets.ship, ship.x, ship.y);
    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.ready, 47, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
        g.drawPixmap(Assets.leftButton, 0, 416);
        g.drawPixmap(Assets.rightButton, 64, 416);
        g.drawPixmap(Assets.thrustButton, 192, 416);
        g.drawPixmap(Assets.shootButton, 256, 416);
    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.pause, 80, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.gameOver, 62, 100);
        g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    public void drawText(Graphics g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i += 1) {
            char character = line.charAt(i);

            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }

            g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

    @Override
    public void pause() {
        if (state == GameState.Running) {
            state = GameState.Paused;
        }
        if (world.gameOver) {
            Settings.addScore(world.score);
            Settings.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
