package Code;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KeyListenerGame implements KeyListener {

	private GameWorld gw;

	// Set of currently pressed keys
	ConcurrentHashMap<Character, Boolean> pressed = new ConcurrentHashMap<Character, Boolean>();

	public KeyListenerGame(GameWorld gw) {
		this.gw = gw;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println("Key pressed");
		pressed.put(e.getKeyChar(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println("Key released");
		pressed.remove(e.getKeyChar());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		/* Not used */ }

	/**
	 * @return the gw
	 */
	public GameWorld getGw() {
		return gw;
	}

	/**
	 * @param gw
	 *            the gw to set
	 */
	public void setGw(GameWorld gw) {
		this.gw = gw;
	}

}
