package Code;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player implements Paintable {

	private String name;

	private double x;
	private double y;

	private double speed;

	private Rectangle r;

	double xDirection;
	double yDirection;

	double hp;

	int currentImage = 0;
	int animationChangeCounter = 0;

	ArrayList<Image> currentAnimations = new ArrayList<Image>();

	public Player(Rectangle r, double x, double y, String name, double speed, double hp) {
		this.r = r;
		setName(name);
		setX(x);
		setY(y);
		setSpeed(speed);
		this.hp = hp;
		xDirection = 0;
		yDirection = 0;

	}

	@Override
	public void draw(Graphics g, int xShift, int yShift, int alpha) {

		r.translate(-xShift, -yShift);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (alpha / 255.0)));
		AffineTransform trans = new AffineTransform();

		trans.rotate(-findAngle() + Math.PI / 2, r.getCenterX(), r.getCenterY());
		trans.translate(r.x, r.y);

		g2d.drawImage(currentAnimations.get(currentImage), trans, null);
		r.translate(xShift, yShift);
	}

	@Override
	public double findAngle() {
		try {
			double diffX = xDirection - r.getCenterX();
			double diffY = yDirection - r.getCenterY();
			if (diffX < 0 && diffY < 0) {
				return Math.atan(diffY / -diffX) + Math.PI;
			} else if (diffX > 0 && diffY > 0) {
				return Math.atan(diffY / -diffX);
			} else if (diffX < 0 && diffY > 0) {
				return Math.atan(diffY / -diffX) + Math.PI;
			} else {
				return Math.atan(diffY / -diffX);
			}
		} catch (Exception ex) {
			return 0;
		}

	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}


	public boolean move(double x, double y, ArrayList<Paintable> sprites) {

		Area areaA;
		r.translate((int) (x - getX()), (int) (y - getY()));
		for (int i = 0; i < sprites.size(); i++) {

			areaA = new Area(r);

			Paintable s = sprites.get(i);
			areaA.intersect(new Area(s.getShape()));

			if (!areaA.isEmpty() && sprites.get(i) != this) {
				//if (z + height > s.getZ() && z < s.getZ() + s.getHeight() - getHeight() / 2) {
					r.translate((int) (getX() - x), (int) (getY() - y));
					return false;
				//}

			}
		}
		r.translate((int) (getX() - x), (int) (getY() - y));
		setX(x);
		setY(y);
		r.setBounds((int) x, (int) y, width(), length());
		return true;
	}


	@Override
	public Shape getShape() {
		return r;
	}

	public int centerX() {
		return r.x + r.width / 2;
	}

	public int centerY() {
		return r.y + r.height / 2;
	}

	public int width() {
		return r.width;
	}

	public int length() {
		return r.height;
	}

	public double halfDiagonal() {
		return r.width * Math.sqrt(2) / 2;
	}

	/**
	 * @return the r
	 */
	public Rectangle getR() {
		return r;
	}

	/**
	 * @param r
	 *            the r to set
	 */
	public void setR(Rectangle r) {
		this.r = r;
	}

	/**
	 * @return the hp
	 */
	public double getHp() {
		return hp;
	}

	/**
	 * @param hp
	 *            the hp to set
	 */
	public void setHp(double hp) {
		this.hp = hp;
	}

	public boolean checkPosition(ArrayList<Paintable> sprites) {

		Area areaA;

		for (int i = 0; i < sprites.size(); i++) {

			areaA = new Area(r);

			Paintable s = sprites.get(i);
			areaA.intersect(new Area(s.getShape()));

			if (!areaA.isEmpty() && s != this) {

				//if (z + getHeight() > s.getZ() && z < s.getZ() + s.getHeight()) {

					return false;

				//}

			}
		}
		return true;
	}

}
