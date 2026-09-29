import java.util.List;
import java.util.Random;
import java.awt.Color;

public class Meteor implements Runnable {

    public static final int IMAGE_COUNT = 10;
    public static final double[] HIT_SCALE = { 0.80, 0.75, 0.90, 0.75, 0.90, 0.88, 0.88, 0.70, 0.85, 0.85 };

    // fields
    private int id;
    private double x, y;
    private int radius;
    private double hitRadius;
    private int imageIndex;
    private Color color;
    private volatile boolean alive = true;
    private boolean exploding = false;     // <-- เพิ่มตัวแปรนี้
    private long explosionTime = 0;        // <-- เพิ่มตัวแปรนี้
    private double dx = 0; 
    private double dy = 1; 
    GalaxyP panel;

    // constructor
    public Meteor(int id, double x, double y, int radius, int imageIndex, double dx, double dy, GalaxyP panel) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.imageIndex = imageIndex;
        this.dx = dx;
        this.dy = dy;
        this.panel = panel;
        this.hitRadius = radius * HIT_SCALE[imageIndex];
        Random rand = new Random();
        this.color = new Color(
            rand.nextInt(156) + 100,
            rand.nextInt(156) + 100,
            rand.nextInt(156) + 100
        );
    }

    // getters
    public int getId()         { return id; }
    public double getX()       { return x; }
    public double getY()       { return y; }
    public int getRadius()     { return radius; }
    public int getImageIndex() { return imageIndex; }
    public Color getColor()    { return color; }
    public boolean isAlive()   { return alive; }
    public boolean isExploding() { return exploding; }

    @Override 
    public void run() {
        while (alive) {
            if (!exploding) {
                x += dx;
                y += dy;

                // ชนขอบจอ แล้วเด้ง + เร็วขึ้น 10%
                if (x - radius < 0) {
                    x = radius;
                    dx = -dx * 1.1;
                } else if (x + radius > 800) {
                    x = 800 - radius;
                    dx = -dx * 1.1;
                }

                if (y - radius < 0) {
                    y = radius;
                    dy = -dy * 1.1;
                } else if (y + radius > 600) {
                    y = 600 - radius;
                    dy = -dy * 1.1;
                }

                // เรียกเช็กชนกับอุกกาบาตลูกอื่น
                if (panel != null && panel.getMeteors() != null) {
                    checkCollision(panel.getMeteors());
                }
            } else {
                // ถ้ากำลังระเบิด ให้แสดงผลสักพักแล้วเคลียร์ออก (เช่น 500ms)
                if (System.currentTimeMillis() - explosionTime > 500) {
                    alive = false;
                }
            }

            if (panel != null) {
                panel.repaint();
            }

            try {
                Thread.sleep(10); 
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    // เมธอดสำหรับให้ Thread ของลูกนี้ วนเช็กชนกับลูกอื่นในลิสต์
    public void checkCollision(List<Meteor> meteors) {
        if (!alive || exploding) return; 

        for (Meteor other : meteors) {
            if (other != this && other.isAlive() && !other.exploding) {
                
                // คำนวณระยะห่างระหว่างจุดศูนย์กลาง
                double dist = Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
                
                // ถ้าชนกัน (ระยะห่างน้อยกว่าผลรวมรัศมี)
                if (dist < (this.hitRadius + other.hitRadius)) {
                    this.exploding = true;
                    this.explosionTime = System.currentTimeMillis();
                    this.dx = 0; 
                    this.dy = 0;
                    break; 
                }
            }
        }
    }
}