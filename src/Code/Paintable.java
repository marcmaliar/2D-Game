package Code;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Shape;

public interface Paintable {

	public void draw(Graphics g, int xShift, int yShift, int alpha);

	public double findAngle();

	//public double getZ();

	public Shape getShape();

	//public double getHeight();
}
