package Code;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Shot implements Paintable {

	private double damage, xSpeed, ySpeed;
	private Ellipse2D.Double o;
	private Color color;

	/**
	 * @param damage
	 * @param z
	 * @param s
	 * @param color
	 */
	public Shot(double xSpeed, double ySpeed, double damage, Ellipse2D.Double o, Color color) {
		super();
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.damage = damage;
		this.o = o;
		this.color = color;
	}

	/**
	 * @return the damage
	 */
	public double getDamage() {
		return damage;
	}

	/**
	 * @param damage
	 *            the damage to set
	 */
	public void setDamage(double damage) {
		this.damage = damage;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void draw(Graphics g, int xShift, int yShift, int alpha) {
		g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));

		g.fillOval((int) (o.x - xShift), (int) (o.y - yShift), (int) o.width, (int) o.height);

	}


	@Override
	public Shape getShape() {
		return o;
	}


	public boolean move(ArrayList<Paintable> sprites, ArrayList<Shot> shots, double xSpeed, double ySpeed) {

		Area areaA;
		o.x = o.x + xSpeed;
		o.y = o.y + ySpeed;

		for (int i = 0; i < sprites.size(); i++) {

			areaA = new Area(o);

			Paintable s = sprites.get(i);
			areaA.intersect(new Area(s.getShape()));

			if (!areaA.isEmpty() && s != this) {

				try {
					Shot sTemp = (Shot) s;
				} catch (Exception ex) {

					try {
						Player e = (Player) s;
						e.hp -= damage;
					} catch (Exception ex1) {
					}
					sprites.remove(this);
					shots.remove(this);
					return false;

				}

			}
		}

		return true;
	}

	public void checkPositionEliminateIfBad(ArrayList<Paintable> sprites, ArrayList<Shot> shots, Player player) {
		Area areaA;

		for (int i = 0; i < sprites.size(); i++) {

			areaA = new Area(o);

			Paintable s = sprites.get(i);
			areaA.intersect(new Area(s.getShape()));

			if (!areaA.isEmpty() && s != this && s != player) {

				try {
					Shot sTemp = (Shot) s;
				} catch (Exception ex) {

					try {
						Player e = (Player) s;
						e.hp -= damage;
					} catch (Exception ex1) {
					}
					sprites.remove(this);
					shots.remove(this);
					return;
				}


			}
		}

	}

	@Override
	public double findAngle() {
		return 0;
	}

	/**
	 * @return the xSpeed
	 */
	public double getxSpeed() {
		return xSpeed;
	}

	/**
	 * @param xSpeed
	 *            the xSpeed to set
	 */
	public void setxSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	/**
	 * @return the ySpeed
	 */
	public double getySpeed() {
		return ySpeed;
	}

	/**
	 * @param ySpeed
	 *            the ySpeed to set
	 */
	public void setySpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}

	/**
	 * @return the o
	 */
	public Ellipse2D.Double getO() {
		return o;
	}

	/**
	 * @param o
	 *            the o to set
	 */
	public void setO(Ellipse2D.Double o) {
		this.o = o;
	}


}
