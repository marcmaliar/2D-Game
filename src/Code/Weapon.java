package Code;

import java.awt.Color;

public class Weapon {

	private String name;
	boolean automatic;
	private double damage;
	private int clip;
	private int ammunition;
	private double secPerShot;
	private Color color;
	private double bulletSpeed;
	private int bulletRadius;
	long timeSinceLastShot;

	/**
	 * @param name
	 * @param automatic
	 * @param damage
	 * @param clip
	 * @param ammuntion
	 * @param framesPerShot
	 * @param color
	 * @param bulletSpeed
	 * @param bulletRadius
	 */
	public Weapon(String name, boolean automatic, double damage, int clip, int ammunition, double secPerShot,
			Color color, double bulletSpeed, int bulletRadius) {
		super();
		this.name = name;
		this.automatic = automatic;
		this.damage = damage;
		this.clip = clip;
		this.ammunition = ammunition;
		this.secPerShot = secPerShot;
		this.color = color;
		this.bulletSpeed = bulletSpeed;
		this.bulletRadius = bulletRadius;
		timeSinceLastShot = 0;
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
	 * @return the automatic
	 */
	public boolean isAutomatic() {
		return automatic;
	}

	/**
	 * @param automatic
	 *            the automatic to set
	 */
	public void setAutomatic(boolean automatic) {
		this.automatic = automatic;
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
	 * @return the clip
	 */
	public int getClip() {
		return clip;
	}

	/**
	 * @param clip
	 *            the clip to set
	 */
	public void setClip(int clip) {
		this.clip = clip;
	}

	/**
	 * @return the ammuntion
	 */
	public int getAmmunition() {
		return ammunition;
	}

	/**
	 * @param ammuntion
	 *            the ammunition to set
	 */
	public void setAmmuntion(int ammunition) {
		this.ammunition = ammunition;
	}

	/**
	 * @return the framesPerShot
	 */
	public double getSecPerShot() {
		return secPerShot;
	}

	/**
	 * @param framesPerShot
	 *            the framesPerShot to set
	 */
	public void setSecPerShot(double secPerShot) {
		this.secPerShot = secPerShot;
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

	/**
	 * @return the bulletSpeed
	 */
	public double getBulletSpeed() {
		return bulletSpeed;
	}

	/**
	 * @param bulletSpeed
	 *            the bulletSpeed to set
	 */
	public void setBulletSpeed(double bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	/**
	 * @return the bulletRadius
	 */
	public int getBulletRadius() {
		return bulletRadius;
	}

	/**
	 * @param bulletRadius
	 *            the bulletRadius to set
	 */
	public void setBulletRadius(int bulletRadius) {
		this.bulletRadius = bulletRadius;
	}
}
