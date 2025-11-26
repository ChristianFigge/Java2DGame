package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Helper interface for changing key bindings easily.
 * (So we don't have to change them twice in keyPressed() and keyReleased())
 */
interface KeyBinds {
    int LEFT	= KeyEvent.VK_A;
    int RIGHT	= KeyEvent.VK_D;
    int UP		= KeyEvent.VK_W;
    int DOWN	= KeyEvent.VK_S;
    int SPRINT  = KeyEvent.VK_SHIFT;
}

/**
 * KeyListener class with public boolean attributes that represent
 * input operations which are modified by pressing and releasing keys.
 * This does not update the game state, because KeyEvents are not
 * in sync with our game loop. Instead, we can read out the booleans
 * within the game loop and call the according update methods from there.
 */
public class KeyboardInput implements KeyListener {
    public boolean left, right, up, down, sprint;

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyBinds.LEFT: left	= true; break;
            case KeyBinds.RIGHT: right	= true; break;
            case KeyBinds.UP: up		= true; break;
            case KeyBinds.DOWN: down	= true; break;
            case KeyBinds.SPRINT: sprint = true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyBinds.LEFT: left	= false; break;
            case KeyBinds.RIGHT: right	= false; break;
            case KeyBinds.UP: up		= false; break;
            case KeyBinds.DOWN: down	= false; break;
            case KeyBinds.SPRINT: sprint = false; break;
        }
    }
}
