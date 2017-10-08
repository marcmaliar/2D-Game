package Code;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Obstacle implements Paintable {

	private double x;
	private double y;

	private Color color;
	private Polygon p;
	double rotateAngle;

	public Obstacle(Polygon p, double x, double y, double rotateAngle, Color color) {

		setX(x);
		setY(y);
		setColor(color);
		this.p = p;
		this.rotateAngle = rotateAngle;
	}

	@Override
	public void draw(Graphics g, int xShift, int yShift, int alpha) {
		p.translate(-xShift, -yShift);
		g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
		// Polygon tempP = new Polygon(p.xpoints, p.ypoints, p.npoints);
		// tempP.translate(-xShift, -yShift);
		// g.fillPolygon(tempP);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (alpha / 255.0)));
		AffineTransform trans = new AffineTransform();

		trans.rotate(-findAngle() + Math.PI / 2, centerX(), centerY());
		trans.translate(x, y);
		g2d.fill(p);
		p.translate(xShift, yShift);

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
	public Shape getShape() {
		return p;
	}

	@Override
	public double findAngle() {
		return rotateAngle;
	}

	public int centerX() {
		double centroidX = 0;

		for (int x : p.xpoints) {
			centroidX += x;
		}
		return (int) (centroidX / p.xpoints.length);
	}

	public int centerY() {
		double centroidY = 0;

		for (int y : p.ypoints) {
			centroidY += y;
		}
		return (int) (centroidY / p.ypoints.length);
	}

}
