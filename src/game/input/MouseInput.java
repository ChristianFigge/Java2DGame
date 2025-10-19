package game.input;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;

public class MouseInput implements MouseInputListener {
    public int cursorPosX, cursorPosY;
    public boolean isInPanel = false;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isInPanel = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isInPanel = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        updateCursorPos(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        updateCursorPos(e);
    }

    private void updateCursorPos(MouseEvent e) {
        cursorPosX = e.getX();
        cursorPosY = e.getY();
    }
}
