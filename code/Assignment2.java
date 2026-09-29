import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

public class Assignment2 extends  JFrame{
     public Assignment2(){
            setTitle("Galaxy");
            setSize(800,600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

    }
    
public static void main(String[] args) {
    Assignment2 frame = new Assignment2();

    List<Meteor> meteors = new CopyOnWriteArrayList<>();
    
    GalaxyP panel = new GalaxyP(meteors);
    frame.add(panel);
    frame.setVisible(true);
   
   java.util.Random rand = new java.util.Random();

// เพิ่มส่วนนี้ก่อนจะถึง for loop
java.util.Scanner scanner = new java.util.Scanner(System.in);
System.out.print("Enter number of meteors: ");
int count = scanner.nextInt();

    // 2. สร้างอุกกาบาตตามจำนวนที่กรอก

    for (int i = 0; i < count; i++) {

        int startX = 100 + rand.nextInt(600);

        int startY = 100 + rand.nextInt(400);

        int radius = 30; 

        int imgIndex = rand.nextInt(10); // สุ่มรูป 0-9


        // สุ่มความเร็วเริ่มต้น (dx, dy) ให้ไม่เท่ากันและวิ่งแนวต่างๆ ได้

        double dx = (rand.nextDouble() * 4) - 2; 

        double dy = (rand.nextDouble() * 4) - 2;

        if (dx == 0 && dy == 0) dx = 1; 


        // สร้าง Object Meteor และส่ง panel เข้าไป

        Meteor m = new Meteor(i, startX, startY, radius, imgIndex, dx, dy, panel);

        meteors.add(m);


        // 3. เริ่ม Thread แยกกันอิสระทุกลูก

        new Thread(m).start();

    }
}
}
 
