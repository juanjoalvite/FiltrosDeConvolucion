/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dam2gr;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.util.ArrayList;

/**
 *
 * @author Jumi
 */
public class MyBufferedImage extends BufferedImage {

    private static final int UNFOCUS_KERNEL[][] = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    private static final int UNFOCUS_GAUSSIAN_KERNEL[][] = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
    private static final int SHARP_KERNEL[][] = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
    private static final int EDGES_KERNEL[][] = {{1, 1, 1}, {1, -9, 1}, {1, 1, 1}};

    byte[] baDataRasterOriginal;
    boolean gray;
    boolean squareGray;
    Dimension diSquareBrightDimension;
    Rectangle square;
    String name;
    int bright;
    int blueBright;
    int greenBright;
    int pixelLength;
    int redBright;
    int squareBright;
    int squareRedBright;
    int squareGreenBright;
    int squareBlueBright;
    int focusLevel;
    int contrast;

    public MyBufferedImage(BufferedImage bi, String n) {
        super(
                bi.getColorModel(), bi.getRaster(),
                bi.getColorModel().isAlphaPremultiplied(), null);

        //        this.rasterOriginal = GraphicsUtil.copyRaster(bi.getRaster());
        this.baDataRasterOriginal
                = this.copyDataRasterToByteArray(bi.getRaster());

        this.resetBright();
        this.resetSquareBright();

        this.gray = false;
        this.name = n;
        this.focusLevel = 0;
        this.diSquareBrightDimension = new Dimension(0, 0);
        this.square = new Rectangle(0, 0, 0, 0);

        if (this.getAlphaRaster() != null) {
            this.pixelLength = 4;
        } else {
            this.pixelLength = 3;
        }
    }

    // Publics -----------------------------------------------------------------
    public int getBright() {
        return this.bright;
    }

    public int getBlueBright() {
        return this.blueBright;
    }

    public int getRedBright() {
        return this.redBright;
    }

    public boolean getGray() {
        return this.gray;
    }

    public int getGreenBright() {
        return this.greenBright;
    }

    public String getImageName() {
        return this.name;
    }

    public int getSquareBright() {
        return this.squareBright;
    }

    public int getSquareBlueBright() {
        return this.squareBlueBright;
    }

    public float getSquareSize() {
        float percent;

        percent = (float) this.square.width / (float) this.getWidth();

        return percent;
    }

    public int getFocusLevel() {
        return focusLevel;
    }

    public boolean getSquareGray() {
        return this.squareGray;
    }

    public int getSquareGreenBright() {
        return this.squareGreenBright;
    }

    public int getSquareRedBright() {
        return this.squareRedBright;
    }

    public void resetBright() {
        if (this.bright == 0 & this.redBright == 0
                & this.greenBright == 0 & this.blueBright == 0) {
            return; // ========= Brillo original -> Nada que hacer ============>
        }

        this.bright = 0;
        this.redBright = 0;
        this.greenBright = 0;
        this.blueBright = 0;

        this.brightAndGrayAndFocus();
    }

    public void resetSquareBright() {
        if (this.squareBright == 0 & this.squareRedBright == 0
                & this.squareGreenBright == 0 & this.squareBlueBright == 0) {
            return; // ========== Brillo original: nada que hacer =============>
        }

        this.squareBright = 0;
        this.squareRedBright = 0;
        this.squareGreenBright = 0;
        this.squareBlueBright = 0;

        this.brightAndGrayAndFocus();
    }

    public boolean setFocusLevel(int level) {
        if (this.getFocusLevel() == level) {
            return false;
        }

        this.focusLevel = level;
        this.brightAndGrayAndFocus();

        return true;
    }

    public boolean setSquareFocusLevel(int level) {
        if (this.getFocusLevel() == level) {
            return false;
        }

        this.focusLevel = level;
        this.squareBrightAndGrayAndFocus();

        return true;
    }

    public boolean setBright(int brightLevel) {
        if (this.bright == brightLevel) {
            return false;
        }

        this.bright = brightLevel;
        this.brightAndGrayAndFocus();

        return true;
    }

    public boolean setBlueBright(int brightLevel) {
        if (this.blueBright == brightLevel) {
            return false;
        }

        this.blueBright = brightLevel;
        this.brightAndGrayAndFocus();
        return true;
    }

    public void setGray(boolean g) {
        byte[] dataRasterTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();

        if (this.gray == g) {
            return; // ================== Nada que hacer ======================>
        }

        this.gray = g;
        this.brightAndGrayAndFocus();
    }

    public boolean setGreenBright(int brightLevel) {
        if (this.greenBright == brightLevel) {
            return false;
        }

        this.greenBright = brightLevel;
        this.brightAndGrayAndFocus();

        return true;
    }

    public boolean setRedBright(int brightLevel) {
        if (this.redBright == brightLevel) {
            return false;
        }

        this.redBright = brightLevel;
        this.brightAndGrayAndFocus();

        return true;
    }

    public void setSquare(float percent) {
        float squareWidth, squareHeight;
        float xCentral, yCentral;
        int rIni, cIni;

        squareWidth = ((float) this.getWidth()) * percent / 100f;
        squareHeight = ((float) this.getHeight()) * percent / 100f;

        xCentral = this.getWidth() / 2;
        yCentral = this.getHeight() / 2;
        rIni = (int) (yCentral - squareHeight / 2);
        cIni = (int) (xCentral - squareWidth / 2);

        this.square.x = cIni;
        this.square.y = rIni;
        this.square.width = (int) squareWidth;
        this.square.height = (int) squareHeight;

        this.brightAndGrayAndFocus();
    }

    public boolean setSquareBlueBright(int brightLevel) {
        if (this.squareBlueBright == brightLevel) {
            return false;
        }

        this.squareBlueBright = brightLevel;
        this.squareBrightAndGrayAndFocus();

        return true;
    }

    public boolean setSquareBright(int brightLevel) {
        if (this.squareBright == brightLevel) {
            return false;
        }

        this.squareBright = brightLevel;
        this.squareBrightAndGrayAndFocus();

        return true;
    }

    public void setSquareGray(boolean gray) {
        byte[] dataRasterTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();

        if (this.squareGray == gray) {
            return; // ---------------- Nada que hacer ------------------------>
        }
        this.squareGray = gray;
        this.squareBrightAndGrayAndFocus();
    }

    public boolean setSquareGreenBright(int brightLevel) {
        if (this.squareGreenBright == brightLevel) {
            return false;
        }

        this.squareGreenBright = brightLevel;
        this.squareBrightAndGrayAndFocus();

        return true;
    }

    public boolean setSquareRedBright(int brightLevel) {
        if (this.squareRedBright == brightLevel) {
            return false;
        }

        this.squareRedBright = brightLevel;
        this.squareBrightAndGrayAndFocus();

        return true;
    }


    // Privates ----------------------------------------------------------------
    private void applyBrigthAndGrayAndKernel(byte[] baSource, byte[] baTarget, int i,
            int redBright, int greenBright, int blueBright,
            boolean gray, int focusLevel, ArrayList<int[][]> matSource) {

        int newR, newG, newB;

        newB = Byte.toUnsignedInt(baSource[i]) + blueBright;
        newG = Byte.toUnsignedInt(baSource[i + 1]) + greenBright;
        newR = Byte.toUnsignedInt(baSource[i + 2]) + redBright;

        newB = (newB > 255 ? 255 : newB);
        newB = (newB < 0 ? 0 : newB);
        newG = (newG > 255 ? 255 : newG);
        newG = (newG < 0 ? 0 : newG);
        newR = (newR > 255 ? 255 : newR);
        newR = (newR < 0 ? 0 : newR);

        if (gray) {
            newR = newG = newB = (byte) ((newR + newG + newB) / 3);
        }

        int matB[][] = matSource.get(0);
        int matG[][] = matSource.get(1);
        int matR[][] = matSource.get(2);

        for (int p = 0; p < Math.abs(focusLevel); p++) {
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    if(focusLevel > 0){
                        newB += (matB[x][y] * UNFOCUS_KERNEL[x][y]);
                        newG += (matG[x][y] * UNFOCUS_KERNEL[x][y]);
                        newR += (matR[x][y] * UNFOCUS_KERNEL[x][y]);
                    }else{
                        newB += (matB[x][y] * SHARP_KERNEL[x][y]);
                        newG += (matG[x][y] * SHARP_KERNEL[x][y]);
                        newR += (matR[x][y] * SHARP_KERNEL[x][y]);
                    }

                }
            }
        }

        baTarget[i] = (byte) newB;
        baTarget[i + 1] = (byte) newG;
        baTarget[i + 2] = (byte) newR;
    }

    // Modifica el brillo para la imagen completa
    private void brightAndGrayAndFocus() {
        byte[] baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();
        int redB, greenB, blueB;

        redB = this.bright + this.redBright;
        greenB = this.bright + this.greenBright;
        blueB = this.bright + this.blueBright;

        this.brightAndGrayAndFocus(this.baDataRasterOriginal, baTarget,
                0, 0, this.getHeight(), this.getWidth(),
                redB, greenB, blueB, this.gray, this.getFocusLevel());

        if ((this.square.width > 0) && (this.square.height >0)) {
            this.squareBrightAndGrayAndFocus();
        }
    }

    private int[][] generateChannelMatrix(int offset, int row, int col) {
        int pixelWidth = this.pixelLength * this.getWidth();
        int p1 = 0, p2 = 0, p3 =0,  p4 =0,  p5 =0,  p6 =0,  p7 =0,  p8 =0,  p9 = 0;

        try {
            p1 = Byte.toUnsignedInt(this.baDataRasterOriginal[((row - 1) * pixelWidth) + ((col - 1) * this.pixelLength) + offset]);
            p2 = Byte.toUnsignedInt(this.baDataRasterOriginal[((row - 1) * pixelWidth) + ((col) * this.pixelLength) + offset]);
            p3 = Byte.toUnsignedInt(this.baDataRasterOriginal[((row - 1) * pixelWidth) + ((col + 1) * this.pixelLength) + offset]);
            p4 = Byte.toUnsignedInt(this.baDataRasterOriginal[((row) * pixelWidth) + ((col - 1) * this.pixelLength) + offset]);
            p5 = Byte.toUnsignedInt(this.baDataRasterOriginal[((row) * pixelWidth) + ((col) * this.pixelLength) + offset]);
            p6 = Byte.toUnsignedInt(this.baDataRasterOriginal[((row) * pixelWidth) + ((col + 1) * this.pixelLength) + offset]);
            p7 = Byte.toUnsignedInt(this.baDataRasterOriginal[((row + 1) * pixelWidth) + ((col - 1) * this.pixelLength) + offset]);
            p8 = Byte.toUnsignedInt(this.baDataRasterOriginal[((row + 1) * pixelWidth) + ((col) * this.pixelLength) + offset]);
            p9 = Byte.toUnsignedInt(this.baDataRasterOriginal[((row + 1) * pixelWidth) + ((col + 1) * this.pixelLength) + offset]);

            int[][] matrix = {
                    {p1, p2, p3},
                    {p4, p5, p6},
                    {p7, p8, p9}};

            return matrix;
        } catch (IndexOutOfBoundsException e) {

            if (row == 0 || col == 0) {
                int[][] matrix = {
                        {p5, p5, p5},
                        {p5, p5, p6},
                        {p7, p8, p9}};
                return matrix;
            } else {
                int[][] matrix = {
                        {p1, p2, p3},
                        {p4, p5, p5},
                        {p5, p5, p5}};

                return matrix;
            }
        }

    }

    // Modifica el brillo para un area especificada de la imagen
    private void brightAndGrayAndFocus(byte[] baSource, byte[] baTarget,
                                       int rowIni, int colIni, int height, int width,
                                       int redBright, int greenBright, int blueBright,
                                       boolean gray, int focusLevel) {
        int i;

        for (int row = rowIni; row < rowIni + height; row++) {
            for (int col = colIni; col < colIni + width; col++) {

                i = row * this.getWidth() * this.pixelLength
                        + col * this.pixelLength;

                ArrayList<int[][]> matRgbSource = new ArrayList<>();

                // Canal B
                matRgbSource.add(generateChannelMatrix(0, row, col));

                // Canal G
                matRgbSource.add(generateChannelMatrix(1, row, col));

                // Canal R
                matRgbSource.add(generateChannelMatrix(2, row, col));

                this.applyBrigthAndGrayAndKernel(baSource, baTarget, i,
                        redBright, greenBright, blueBright, gray, focusLevel, matRgbSource);
            }
        }
    }

    private byte[] copyByteArray(byte[] baSource) {
        byte[] baCopy;

        if (baSource == null) {
            throw new IllegalArgumentException("Byte array is null");
        }

        baCopy = new byte[baSource.length];
        System.arraycopy(baSource, 0, baCopy, 0, baSource.length);

        return baCopy;
    }

    private byte[] copyDataRasterToByteArray(Raster ras) {
        byte[] baDataRasterSource;

        if (ras.getDataBuffer().getDataType() != DataBuffer.TYPE_BYTE) {
            throw new IllegalArgumentException("RGB data type is not BYTE");
        }

        baDataRasterSource = ((DataBufferByte) ras.getDataBuffer()).getData();
        return this.copyByteArray(baDataRasterSource);
    }

    private void squareBrightAndGrayAndFocus() {
        int redB, greenB, blueB;
        byte[] baTarget = ((DataBufferByte) this.getRaster().getDataBuffer()).getData();

        redB = this.squareBright + this.squareRedBright;
        greenB = this.squareBright + this.squareGreenBright;
        blueB = this.squareBright + this.squareBlueBright;

        this.brightAndGrayAndFocus(this.baDataRasterOriginal, baTarget,
                this.square.y, this.square.x,
                this.square.height, this.square.width,
                redB, greenB, blueB, this.squareGray, this.getFocusLevel());
    }
}
