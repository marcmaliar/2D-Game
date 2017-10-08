package Code;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Enemy extends Player {

	int moveToX;
	int moveToY;

	ArrayList<Image> walkAnimations = new ArrayList<Image>();
	ArrayList<Image> attackAnimations = new ArrayList<Image>();

	public enum State {
		WALKING, ATTACKING
	}

	State currentState;

	public Enemy(Rectangle r, double x, double y, String name, double speed, double hp) {
		super(r, x, y, name, speed, hp);
		moveToX = (int) x;
		moveToY = (int) y;

		try {
			attackAnimations.add(ImageIO.read(new File("animations/" + name + "_attack_0001.png")));
			attackAnimations.add(ImageIO.read(new File("animations/" + name + "_attack_0002.png")));
			walkAnimations.add(ImageIO.read(new File("animations/" + name + "_move_0001.png")));
			walkAnimations.add(ImageIO.read(new File("animations/" + name + "_move_0002.png")));
			walkAnimations.add(ImageIO.read(new File("animations/" + name + "_move_0003.png")));
			walkAnimations.add(ImageIO.read(new File("animations/" + name + "_move_0004.png")));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		currentAnimations = walkAnimations;
	}

	public void nextImage() {
		if (currentState == State.ATTACKING && currentImage == currentAnimations.size() - 1)
			setState(State.WALKING);
		else
			currentImage = (currentImage + 1) % currentAnimations.size();
	}

	public void setState(State state) {
		if (state == currentState) {
			return;
		}
		this.currentState = state;
		currentImage = 0;
		switch (state) {
		case WALKING:
			currentAnimations = walkAnimations;
			break;
		case ATTACKING:
			currentAnimations = attackAnimations;
			break;
		}
	}

}
