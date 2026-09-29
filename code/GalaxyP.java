import javax.swing.JPanel;
import java.awt.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GalaxyP extends JPanel {
    private List<Meteor> meteors;
    private Image[] meteorImages = new Image[Meteor.IMAGE_COUNT];
    private Image bombImg; // <-- 1. ประกาศตัวแปรเก็บรูประเบิดเพิ่มตรงนี้

    public GalaxyP(List<Meteor> meteors) {
        this.meteors = meteors;
        setBackground(Color.BLACK);
        loadImages();
    }

    public List<Meteor> getMeteors() {
        return meteors;
    }

    private void loadImages() {
        // โหลดรูปอุกกาบาตปกติ
        for (int i = 0; i < meteorImages.length; i++) {
            try {
                meteorImages[i] = ImageIO.read(new File("images/" + (i + 1) + ".png"));  
            } catch (IOException e) {
                System.out.println("โหลดรูปที่ " + (i + 1) + " ไม่ได้");
                meteorImages[i] = null;
            }
        }

        // <-- 2. โหลดรูปภาพระเบิดเพิ่มตรงนี้
        try {
            bombImg = ImageIO.read(new File("images/bomb.gif")); // ปรับชื่อ/นามสกุลไฟล์ให้ตรงกับในโฟลเดอร์ images นะคะ
        } catch (IOException e) {
            System.out.println("โหลดรูประเบิดไม่ได้");
            bombImg = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);                         
        for (Meteor m : meteors) {
            if (!m.isAlive()) continue;                      
            int r     = m.getRadius();
            int drawX = (int)(m.getX() - r);              
            int drawY = (int)(m.getY() - r);
            int size  = r * 2;                            
            
            Image img;
            
            if (m.isExploding()) {
                img = bombImg; 
            } else {
                img = meteorImages[m.getImageIndex()];
            }

            if (img != null) {
                g.drawImage(img, drawX, drawY, size, size, this);
            } else {
                g.setColor(m.getColor());
                g.fillOval(drawX, drawY, size, size);
            }
        }
    }
}