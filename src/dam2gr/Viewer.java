package dam2gr;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Jumi
 *
 */
public class Viewer extends Canvas {

    public static final int IMG_ONE = 0;
    public static final int IMG_TWO = 1;
    public static final int IMG_THREE = 2;

    MyBufferedImage imgOriginal, imgMod[];
    Dam2Gr parent;

    // Publics -----------------------------------------------------------------  
    public Viewer(Dam2Gr m) {
        this.parent = m;
        this.imgMod = new MyBufferedImage[3];
        this.myPaint();
    }

    public int getBlueBright(int imgSelected) {
        return this.imgMod[imgSelected].getBlueBright();
    }

    public int getBright(int imgSelected) {
        return this.imgMod[imgSelected].getBright();
    }

    public boolean getGray(int imgSelected) {
        return this.imgMod[imgSelected].getGray();
    }

    public int getGreenBright(int imgSelected) {
        return this.imgMod[imgSelected].getGreenBright();
    }

    public int getImageHeight() {
        return this.imgOriginal.getHeight();
    }

    public String getImageName() {
        return this.imgOriginal.getImageName();
    }

    public int getImageWidth() {
        return this.imgOriginal.getWidth();
    }

    public int getRedBright(int imgSelected) {
        return this.imgMod[imgSelected].getRedBright();
    }

    public int getSquareBright(int imgSelected) {
        return this.imgMod[imgSelected].getSquareBright();
    }

    public float getSquareSize(int imgSelected) {
        return this.imgMod[imgSelected].getSquareSize();
    }

    public int getSquareGreenBright(int imgSelected) {
        return this.imgMod[imgSelected].getSquareGreenBright();
    }

    public int getSquareBlueBright(int imgSelected) {
        return this.imgMod[imgSelected].getSquareBlueBright();
    }

    public boolean getSquareGray(int imgSelected) {
        return this.imgMod[imgSelected].getSquareGray();
    }

    public int getSquareRedBright(int imgSelected) {
        return this.imgMod[imgSelected].getSquareRedBright();
    }

    public void resetBright(int imgSelected) {
        this.imgMod[imgSelected].resetBright();
        this.myPaint();
    }

    public void resetSquareBright(int imgSelected) {
        this.imgMod[imgSelected].resetBright();
        this.myPaint();
    }

    public void setBright(int brightLevel, int imgSelected) {
        if (this.imgMod[imgSelected].setBright(brightLevel)) {
            this.myPaint();
        }
    }

    public void setBlueBright(int brightLevel, int imgSelected) {
        if (this.imgMod[imgSelected].setBlueBright(brightLevel)) {
            this.myPaint();
        }
    }

    public void setGray(int imgSelected, boolean gray) {
        this.imgMod[imgSelected].setGray(gray);
        this.myPaint();
    }

    public void setGreenBright(int brightLevel, int imgSelected) {
        if (this.imgMod[imgSelected].setGreenBright(brightLevel)) {
            this.myPaint();
        }
    }

    public void setRedBright(int brightLevel, int imgSelected) {
        if (this.imgMod[imgSelected].setRedBright(brightLevel)) {
            this.myPaint();
        }
    }

    public void setSquareBlueBright(int brightLevel, int imgSelected) {
        if (this.imgMod[imgSelected].setSquareBlueBright(brightLevel)) {
            this.myPaint();
        }
    }

    public void setSquareBright(int brightLevel, int imgSelected) {
        if (this.imgMod[imgSelected].setSquareBright(brightLevel)) {
            this.myPaint();
        }
    }

    public void setSquareDimension(float percent, int imgSelected) {
        this.imgMod[imgSelected].setSquare(percent);
        this.myPaint();
    }

    public void setSquareGray(int imgSelected, boolean gray) {
        this.imgMod[imgSelected].setSquareGray(gray);
        this.myPaint();
    }

    public void setSquareGreenBright(int brightLevel, int imgSelected) {
        if (this.imgMod[imgSelected].setSquareGreenBright(brightLevel)) {
            this.myPaint();
        }
    }

    public void setSquareRedBright(int brightLevel, int imgSelected) {
        if (this.imgMod[imgSelected].setSquareRedBright(brightLevel)) {
            this.myPaint();
        }
    }

    public void loadImage(String imageURI) {
        float ratio;
        float w, h;

        this.imgOriginal = new MyBufferedImage(this.readImage(imageURI), imageURI);
        this.imgMod[Viewer.IMG_ONE] = new MyBufferedImage(this.readImage(imageURI), imageURI);
        this.imgMod[Viewer.IMG_TWO] = new MyBufferedImage(this.readImage(imageURI), imageURI);
        this.imgMod[Viewer.IMG_THREE] = new MyBufferedImage(this.readImage(imageURI), imageURI);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        ratio = (float) this.imgOriginal.getWidth() / (float) this.imgOriginal.getHeight();
        h = (float) d.height * 0.70f;
        w = h * ratio;

        d.setSize(w, h);
        this.setSize((int) w, (int) h);
        this.setPreferredSize(d);

        this.myPaint();
    }

    public void myPaint() {
        // System.out.println("My Paint");

        this.myPaint(this.getGraphics());
    }

    public void myPaint(Graphics g) {
        int w, h; // width & heigh

        h = this.getHeight() / 2;
        w = this.getWidth() / 2;

        if (g == null) {
            System.out.println("No manejador de graficos");
            return;
        }

        if (this.imgOriginal == null) {
            System.out.println("No hay imagen -> No se puede repintar el viewer");
            return;
        }

        g.drawImage(this.imgOriginal, 0, 0, w, h, null);
        g.drawImage(this.imgMod[Viewer.IMG_ONE], w, 0, w, h, null);
        g.drawImage(this.imgMod[Viewer.IMG_TWO], 0, h, w, h, null);
        g.drawImage(this.imgMod[Viewer.IMG_THREE], w, h, w, h, null);
    }

    @Override
    public void paint(Graphics g) {
        // System.err.println("Paint");

        this.myPaint(g);
    }

    public void setSquareFocusFilter(int imgSelected, int level) {
        if (this.imgMod[imgSelected].setSquareFocusLevel(level)) {
            this.myPaint();
        }
    }

    public void setFocusFilter(int imgSelected, int level) {
        if (this.imgMod[imgSelected].setFocusLevel(level)) {
            this.myPaint();
        }
    }

    // Privates ----------------------------------------------------------------
    private BufferedImage readImage(String name) {
        BufferedImage img;

        try {
            img = ImageIO.read(new File(name));
            return img; // ========= Image file found OK ======================>
        } catch (IOException e) {
            System.err.println("Img Error  (" + e + ")");
        }

        return null; // ============ Image file NOT found =====================>
    }
}
