package Code;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DragMouseListenerGame implements MouseMotionListener {
	Hero hero;

	public DragMouseListenerGame(Hero hero) {
		this.hero = hero;
		;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		hero.xDirection = e.getX();
		hero.yDirection = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		hero.xDirection = e.getX();
		hero.yDirection = e.getY();

	}

}
