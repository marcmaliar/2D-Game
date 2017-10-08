package Code;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import JSON.JSONArray;
import JSON.JSONObject;

public class GameWorld extends JPanel {

	private static final long serialVersionUID = 541546558425377377L;

	private static final double UPDATE_RATE2MS = 50.0;
	private static final double UPDATE_RATE20MS = 50.0;
	private static final double TEST_UPDATE_RATE = 50.0;
	private static final double FRAME_CONSTANT2MS = TEST_UPDATE_RATE / UPDATE_RATE2MS;
	private static final double FRAME_CONSTANT20MS = TEST_UPDATE_RATE / UPDATE_RATE20MS;

	long stopWatch2MS = 0;
	long stopWatch20MS = 0;

	private int canvasWidth;
	private int canvasLength;

	private Hero hero;

	private KeyListenerGame klg;

	private int xShift = 0;
	private int yShift = 0;

	ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	ArrayList<Paintable> sprites = new ArrayList<Paintable>();

	ArrayList<Shot> shots = new ArrayList<Shot>();

	double frames = 0;

	Polygon SpawnPolygon;
	int xSpawnPolygonStart;
	int ySpawnPolygonStart;
	int xSpawnPolygonEnd;
	int ySpawnPolygonEnd;

	final static char UP='t', DOWN='g', LEFT='f', RIGHT='h', ATTACK='k';

	public GameWorld(int canvasWidth, int canvasLength) {

		this.canvasWidth = canvasWidth;
		this.canvasLength = canvasLength;
		initializeSpawnPolygon();
		panelInitialization();
		loadMap("KinoDerToten");
		createListeners();

		Thread thread = new Thread() {
			@Override
			public void run() {
				long previousTime = System.currentTimeMillis();
				long start = System.currentTimeMillis();
				stopWatch2MS = 0;
				stopWatch20MS = 0;
				long leftOverTime = 0;
				while (true) {

					if (System.currentTimeMillis() - start > 10000) {
						start = System.currentTimeMillis();
						Random rand = new Random();
						Enemy e;
						int x, y;
						do {
							x = rand.nextInt(xSpawnPolygonEnd - xSpawnPolygonStart) + xSpawnPolygonStart;
							y = rand.nextInt(ySpawnPolygonEnd - ySpawnPolygonStart) + ySpawnPolygonStart;
							e = new Enemy(new Rectangle(x, y, 64, 64), x, y, "zombie", 5, 100);
						} while (!e.checkPosition(sprites) || !SpawnPolygon.contains(new Point(x, y)));
						sprites.add(e);
						enemies.add(e);
					}

					while (stopWatch2MS > 20) {
						stopWatch2MS -= 20;
						update2MS();
					}
					while (stopWatch20MS > 20) {
						stopWatch20MS -= 20;
						update20MS();
					}

					// Calculate what happens next
					// update();
					// Refresh the display
					repaint();
					// Makes the program slow down so things can be drawn
					// properly
					try {
						if ((1000 / (int) UPDATE_RATE2MS) - (System.currentTimeMillis() - previousTime) < 0) {
							leftOverTime += (1000 / (int) UPDATE_RATE2MS) - (System.currentTimeMillis() - previousTime);
						} else {
						}
						Thread.sleep((1000 / (int) UPDATE_RATE2MS) - (System.currentTimeMillis() - previousTime));
					} catch (Exception ex) {
						leftOverTime += (1000 / (int) UPDATE_RATE2MS) - (System.currentTimeMillis() - previousTime);
					}
					hero.wep.timeSinceLastShot += (System.currentTimeMillis() - previousTime);
					stopWatch2MS += (System.currentTimeMillis() - previousTime);
					stopWatch20MS += (System.currentTimeMillis() - previousTime);
					previousTime = System.currentTimeMillis();
				}
			}

		};

		thread.start(); // start the thread for graphics

	}

	public void update20MS() {
		updateEnemyDirection(FRAME_CONSTANT20MS);
	}

	public void initializeSpawnPolygon() {
		int[] xPointsSpawnPolygon = new int[] { 0, 1050, 1050, 0 };
		int[] yPointsSpawnPolygon = new int[] { 0, 0, 1050, 1050 };
		int nPointsSpawnPolygon = 4;
		xSpawnPolygonStart = 0;
		ySpawnPolygonStart = 0;
		xSpawnPolygonEnd = 1050;
		ySpawnPolygonEnd = 1050;

		SpawnPolygon = new Polygon(xPointsSpawnPolygon, yPointsSpawnPolygon, nPointsSpawnPolygon);
	}

	public void panelInitialization() {
		setPreferredSize(new Dimension(canvasWidth, canvasLength));
		setFocusable(true);
		setBackground(Color.BLACK);
	}

	public void createListeners() {
		klg = new KeyListenerGame(this);
		this.addKeyListener(klg);

		MouseListener mlg = new MouseListenerGame(hero, sprites);
		this.addMouseListener(mlg);

		MouseMotionListener mml = new DragMouseListenerGame(hero);
		this.addMouseMotionListener(mml);
	}

	public void loadMap(String mapName) {
		// The name of the file to open.
		String fileName = "Maps/"+mapName;

		String line;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				JSONObject obj = new JSONObject(line);
				JSONObject geometry = obj.getJSONObject("geometry");

				String type = obj.getString("type");
				String shapeType = geometry.getString("type");

				if (type.equals("hero")) {
					createHero(obj, geometry);
				} else if (type.equals("obstacle") && shapeType.equals("rectangle")) {
					createObstacleRectangle(obj, geometry);
				} else if (type.equals("obstacle") && shapeType.equals("polygon")) {
					createObstaclePolygon(obj, geometry);
				}

			}
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

	public void createHero(JSONObject obj, JSONObject geometry) {

		int x = geometry.getInt("x");
		int y = geometry.getInt("y");

		hero = new Hero(new Rectangle(x, y, geometry.getInt("width"), geometry.getInt("length")), x, y,
				obj.getString("name"), obj.getDouble("speed"),
				obj.getDouble("hp"), findWeaponWithName("handgun"));
		sprites.add(hero);
	}

	public void createObstacleRectangle(JSONObject obj, JSONObject geometry) {
		int[] xPoints = new int[4];
		int[] yPoints = new int[4];

		int x = geometry.getInt("x");
		int y = geometry.getInt("y");
		int width = geometry.getInt("width");
		int length = geometry.getInt("length");

		xPoints[0] = x;
		xPoints[1] = x + width;
		xPoints[2] = x + width;
		xPoints[3] = x;

		yPoints[0] = y;
		yPoints[1] = y;
		yPoints[2] = y + length;
		yPoints[3] = y + length;
		addPolygonObstacle(obj, geometry, xPoints, yPoints, 4);
	}

	public void createObstaclePolygon(JSONObject obj, JSONObject geometry) {
		JSONArray coors = geometry.getJSONArray("coordinates");
		int[] xPoints = new int[coors.length()];
		int[] yPoints = new int[coors.length()];
		int nPoints = coors.length();
		for (int i = 0; i < coors.length(); i++) {
			JSONArray pair = coors.getJSONArray(i);
			xPoints[i] = pair.getInt(0);
			yPoints[i] = pair.getInt(1);
		}
		addPolygonObstacle(obj, geometry, xPoints, yPoints, nPoints);
	}

	public void addPolygonObstacle(JSONObject obj, JSONObject geometry, int[] xPoints, int[] yPoints, int nPoints) {
		sprites.add(new Obstacle(new Polygon(xPoints, yPoints, nPoints), geometry.getInt("x"), geometry.getInt("y"),
				 0,
				new Color(obj.getInt("red"), obj.getInt("green"), obj.getInt("blue"))));
	}

	public Weapon findWeaponWithName(String name) {
		// FileReader reads text files in the default encoding.
		try {
			FileReader fileReaderWeapon = new FileReader("Weapons");

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReaderWeapon = new BufferedReader(fileReaderWeapon);
			JSONObject w = new JSONObject(bufferedReaderWeapon.readLine());
			while (!w.getString("name").equals(name)) {
				w = new JSONObject(bufferedReaderWeapon.readLine());
			}
			bufferedReaderWeapon.close();
			return new Weapon(w.getString("name"), w.getBoolean("automatic"), w.getDouble("damage"), w.getInt("clip"),
					w.getInt("ammunition"), w.getDouble("framesPerShot"),
					new Color(w.getInt("red"), w.getInt("green"), w.getInt("blue")), w.getDouble("bulletSpeed"),
					w.getInt("bulletRadius"));
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return null;
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Map<Integer, ArrayList<Paintable>> map = new TreeMap<Integer, ArrayList<Paintable>>();

		for (int i = 0; i < sprites.size(); i++) {
			Paintable p = sprites.get(i);
			int alpha = 255;//calculateAlpha(p);
			addPaintableToMap(map, alpha, p);

		}
		drawPaintables(map, g);

	}

	/*public int calculateAlpha(Paintable p) {
		int alpha = 255;
		if (p != hero && !(hero.getZ() >= p.getZ() && hero.getZ() <= p.getZ() + p.getHeight())
				&& !(p.getZ() >= hero.getZ() && p.getZ() <= hero.getZ() + hero.getHeight())) {
			alpha = (int) (255 * Math.pow(Math.E,
					-Math.pow(Math.min(Math.abs(hero.getZ() + hero.getHeight() - p.getZ() - p.getHeight()),
							Math.abs(hero.getZ() + hero.getHeight() - p.getZ())) / 10, 2)));
		}
		return alpha;
	}*/

	public void addPaintableToMap(Map<Integer, ArrayList<Paintable>> map, int alpha, Paintable p) {
		ArrayList<Paintable> newList = map.get(alpha);
		if (map.get(alpha) == null) {
			newList = new ArrayList<Paintable>();
		}
		newList.add(p);
		map.put(alpha, newList);
	}

	public void drawPaintables(Map<Integer, ArrayList<Paintable>> map, Graphics g) {
		for (Entry<Integer, ArrayList<Paintable>> entry : map.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				entry.getValue().get(i).draw(g, xShift, yShift, entry.getKey());
			}
		}
	}

	public void update2MS() {
		updateShots(FRAME_CONSTANT2MS);
		newShots(FRAME_CONSTANT2MS);
		updatePositionAndState(FRAME_CONSTANT2MS);
		hero.nextImage();
		updateEnemyRegular(FRAME_CONSTANT2MS);
	}

	public void updateShots(double FRAME_CONSTANT) {
		for (int i = 0; i < shots.size(); i++) {
			shots.get(i).move(sprites, shots, shots.get(i).getxSpeed() * FRAME_CONSTANT,
					shots.get(i).getySpeed() * FRAME_CONSTANT);
		}
	}

	public void newShots(double FRAME_CONSTANT) {

		if (hero.firing) {
			double deltaX = hero.xDirection - hero.centerX() + xShift;
			double deltaY = hero.yDirection - hero.centerY() + yShift;
			double mag = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			Weapon w = hero.wep;
			if (!hero.firstShotDone) {
				Shot s = new Shot(FRAME_CONSTANT * w.getBulletSpeed() * deltaX / mag,
						FRAME_CONSTANT * w.getBulletSpeed() * deltaY / mag, w.getDamage(),
						new Ellipse2D.Double(hero.centerX() + hero.width() * deltaX / mag,
								hero.centerY() + hero.length() * deltaY / mag, w.getBulletRadius(),
								w.getBulletRadius()),
						w.getColor());
				sprites.add(s);
				shots.add(s);
				s.checkPositionEliminateIfBad(sprites, shots, hero);
				hero.firstShotDone = true;
			} else if (w.timeSinceLastShot > 0.02 && w.automatic) {
				w.timeSinceLastShot -= 0.02;
				Shot s = new Shot(FRAME_CONSTANT * w.getBulletSpeed() * deltaX / mag,
						FRAME_CONSTANT * w.getBulletSpeed() * deltaY / mag, w.getDamage(),
						new Ellipse2D.Double(hero.centerX() + hero.width() * deltaX / mag,
								hero.centerY() + hero.length() * deltaY / mag, w.getBulletRadius(),
								w.getBulletRadius()),
						w.getColor());
				sprites.add(s);
				shots.add(s);
				s.checkPositionEliminateIfBad(sprites, shots, hero);
			}
		}
	}

	public Set<Character> copyPressedCharacters(ConcurrentHashMap<Character, Boolean> pressed) {
		Set<Character> copy = new HashSet<Character>();

		for (Entry<Character, Boolean> c : pressed.entrySet()) {
			copy.add(new Character(c.getKey()));
		}

		if (copy.contains(UP) && copy.contains(DOWN)) {
			copy.remove(UP);
			copy.remove(DOWN);
		}
		if (copy.contains(LEFT) && copy.contains(RIGHT)) {
			copy.remove(LEFT);
			copy.remove(RIGHT);
		}
		return copy;
	}

	public void updatePositionAndState(double FRAME_CONSTANT) {

		Set<Character> copy = copyPressedCharacters(klg.pressed);
		Iterator<Character> iterator = copy.iterator();
		String command = "standing";
		keyLoop: while (iterator.hasNext()) {
			Character c = iterator.next();
			boolean canMove = false;
			switch (c) {
			case UP:
				canMove = hero.move(hero.getX(),
						hero.getY() - FRAME_CONSTANT * hero.getSpeed() / Math.sqrt(copy.size()), sprites);
				if (canMove && hero.getY() - yShift < canvasLength / 4) {
					yShift -= hero.getSpeed() / Math.sqrt(copy.size());
				}
				if (canMove) {
					command = "walking";
				}
				break;
			case LEFT:
				canMove = hero.move(hero.getX() - FRAME_CONSTANT * hero.getSpeed() / Math.sqrt(copy.size()),
						hero.getY(), sprites);
				if (canMove && hero.getX() - xShift < canvasWidth / 4) {
					xShift -= hero.getSpeed() / Math.sqrt(copy.size());
				}
				if (canMove) {
					command = "walking";
				}
				break;
			case DOWN:
				canMove = hero.move(hero.getX(),
						hero.getY() + FRAME_CONSTANT * hero.getSpeed() / Math.sqrt(copy.size()), sprites);
				if (canMove && hero.getY() - yShift > (canvasLength * 3) / 4) {
					yShift += hero.getSpeed() / Math.sqrt(copy.size());
				}
				if (canMove) {
					command = "walking";
				}
				break;
			case RIGHT:
				canMove = hero.move(hero.getX() + FRAME_CONSTANT * hero.getSpeed() / Math.sqrt(copy.size()),
						hero.getY(), sprites);
				if (canMove && hero.getX() - xShift > (canvasWidth * 3) / 4) {
					xShift += hero.getSpeed() / Math.sqrt(copy.size());
				}
				if (canMove) {
					command = "walking";
				}
				break;
			}
		}
		if (copy.contains(ATTACK)) command = "attacking";
		else if (command.equals("walking")) hero.setState(Hero.State.WALKING);
		else if (command.equals("attacking")) hero.setState(Hero.State.ATTACKING);
		else hero.setState(Hero.State.STANDING);
	}

	public void updateEnemyRegular(double FRAME_CONSTANT) {
		for (int i = 0; i < enemies.size(); i++) {

			Enemy e = enemies.get(i);

			e.nextImage();

			if (e.getHp() < 0) {
				sprites.remove(e);
				enemies.remove(e);
				i--;
				continue;
			}

			double diffX = e.moveToX - e.centerX();
			double diffY = e.moveToY - e.centerY();

			double mag = Math.sqrt(diffX * diffX + diffY * diffY);

			e.move(e.getX() + (FRAME_CONSTANT * diffX / mag) * e.getSpeed(), e.getY(), sprites);
			e.move(e.getX(), e.getY() + (FRAME_CONSTANT * diffY / mag) * e.getSpeed(), sprites);

			e.xDirection = e.moveToX - xShift;
			e.yDirection = e.moveToY - yShift;
		}

	}

	public void updateEnemyDirection(double FRAME_CONSTANT) {
		sprites.remove(hero);
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			boolean seehero = false;

			double diffX = hero.centerX() - e.centerX();
			double diffY = hero.centerY() - e.centerY();
			Point e1, e2, p1, p2;
			Rectangle er = e.getR();
			int maxXE = (int) er.getMaxX();
			int maxYE = (int) er.getMaxY();
			int minXE = (int) er.getMinX();
			int minYE = (int) er.getMinY();

			Rectangle pr = hero.getR();
			int maxXP = (int) pr.getMaxX();
			int maxYP = (int) pr.getMaxY();
			int minXP = (int) pr.getMinX();
			int minYP = (int) pr.getMinY();
			if (diffX * diffY > 0) {
				e1 = new Point(maxXE + 2, minYE - 2);
				e2 = new Point(minXE - 2, maxYE + 2);
				p1 = new Point(maxXP + 2, minYP - 2);
				p2 = new Point(minXP - 2, maxYP + 2);
			} else {
				e1 = new Point(maxXE + 2, maxYE + 2);
				e2 = new Point(minXE - 2, minYE - 2);
				p1 = new Point(maxXP + 2, maxYP + 2);
				p2 = new Point(minXP - 2, minYP - 2);
			}
			int[] xPoints = new int[] { e1.x, e2.x, p2.x, p1.x };
			int[] yPoints = new int[] { e1.y, e2.y, p2.y, p1.y };

			for (int j = 0; j < sprites.size(); j++) {

				Area areaA = new Area(new Polygon(xPoints, yPoints, 4));

				Paintable s = sprites.get(j);
				areaA.intersect(new Area(s.getShape()));
				Enemy eTest = new Enemy(pr, pr.x, pr.y, "zombie", 0, 0);

				if ((!eTest.checkPosition(sprites) || !areaA.isEmpty()) && s != hero && s != e) {

					if (Math.sqrt(Math.pow(e.moveToX - e.centerX(), 2) + Math.pow(e.moveToY - e.centerY(), 2)) > e
							.getSpeed() * FRAME_CONSTANT) {
						break;
					}

					Random rand = new Random();

					boolean works = false;

					int moveToX, moveToY;
					do {

						moveToX = rand.nextInt(xSpawnPolygonEnd - xSpawnPolygonStart) + xSpawnPolygonStart;
						moveToY = rand.nextInt(ySpawnPolygonEnd - ySpawnPolygonStart) + ySpawnPolygonStart;

						Area areaATest;

						diffX = moveToX - e.centerX();
						diffY = moveToY - e.centerY();
						er = e.getR();
						maxXE = (int) er.getMaxX();
						maxYE = (int) er.getMaxY();
						minXE = (int) er.getMinX();
						minYE = (int) er.getMinY();

						pr = (Rectangle) er.clone();
						pr.translate((int) diffX, (int) diffY);
						maxXP = (int) pr.getMaxX();
						maxYP = (int) pr.getMaxY();
						minXP = (int) pr.getMinX();
						minYP = (int) pr.getMinY();

						if (diffX * diffY > 0) {
							e1 = new Point(maxXE, minYE);
							e2 = new Point(minXE, maxYE);
							p1 = new Point(maxXP, minYP);
							p2 = new Point(minXP, maxYP);
						} else {
							e1 = new Point(maxXE, maxYE);
							e2 = new Point(minXE, minYE);
							p1 = new Point(maxXP, maxYP);
							p2 = new Point(minXP, minYP);
						}
						xPoints = new int[] { e1.x, e2.x, p2.x, p1.x };
						yPoints = new int[] { e1.y, e2.y, p2.y, p1.y };

						for (int k = 0; k < sprites.size(); k++) {

							s = sprites.get(k);

							areaATest = new Area(new Polygon(xPoints, yPoints, 4));

							areaATest.intersect(new Area(s.getShape()));
							eTest = new Enemy(pr, pr.x, pr.y, "zombie", 0, 0);

							if (!areaATest.isEmpty()) {
								break;
							}
							if (k == sprites.size() - 1 && eTest.checkPosition(sprites)) {
								works = true;
							}
						}

					} while (!works);

					e.moveToX = moveToX + pr.width / 2;
					e.moveToY = moveToY + pr.width / 2;
					break;

				}
				if (j == sprites.size() - 1) {
					seehero = true;
				}

			}

			if (seehero) {
				e.moveToX = hero.centerX();
				e.moveToY = hero.centerY();
			}
		}
		sprites.add(hero);
	}

	/**
	 * @return the c
	 */
	public Hero gethero() {
		return hero;
	}

	/**
	 * @param c
	 *            the c to set
	 */
	public void setHero(Hero hero) {
		this.hero = hero;
	}

}
