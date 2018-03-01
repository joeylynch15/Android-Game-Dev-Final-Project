package edu.angelo.finalprojectlynch;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class AsteroidsGame extends AndroidGame {

    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }
}
