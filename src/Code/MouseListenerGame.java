package Code;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class MouseListenerGame implements MouseListener {

	Hero hero;
	ArrayList<Paintable> sprites;

	public MouseListenerGame(Hero hero, ArrayList<Paintable> sprites) {
		this.hero = hero;
		this.sprites = sprites;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		hero.firing = true;
		hero.firstShotDone = false;
		hero.xDirection = e.getX();
		hero.yDirection = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		hero.firing = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
