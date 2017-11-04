package dam2gr;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

public class Dam2Gr
        extends JFrame
        implements ComponentListener, ActionListener, ChangeListener {

    private static final int POS_PIXELS_TOTAL = 0;
    private static final int POS_BYTES = 1;
    private static final int POS_PIXELS_WIDTH = 2;
    private static final int POS_PIXELS_HEIGHT = 3;
    private static final int POS_CANAL_ALPHA = 4;
    private static final int BRIGHT_MIN = -10;
    private static final int BRIGHT_MAX = 10;
    private static final int BRIGHT_INI = 0;
    private static final int BRIGHT_MULTIPLIER = 255 / Dam2Gr.BRIGHT_MAX;

    private JSlider jsBrightLevel;
    private JSlider jsRedBrightLevel;
    private JSlider jsGreenBrightLevel;
    private JSlider jsBlueBrightLevel;
    private JSlider jsZoneSize;
    private JSlider jsFocusFilter;
    private JSlider jsEdgesFilter;
    private JSlider jsUnfocusLevel;

    private JButton bLoad;
    private JButton bResetBright;

    private JToggleButton tbGrey;
    private JToggleButton tbImg1Selected;
    private JToggleButton tbImg2Selected;
    private JToggleButton tbImg3Selected;
    private JToggleButton tbAllZone;
    private JToggleButton tbCentralZone;
    private Viewer viewer;
    private JTable jtImageDetails;
    private JLabel jlImageName;

    public Dam2Gr() {
        createFrame();
    }

    public static void main(String[] args) {
        Dam2Gr grDesktop = new Dam2Gr();
    }

    // Privates ----------------------------------------------------------------
    private void addBrigthTools(Container panel, GridBagConstraints c) {
        JLabel label;

        c.ipady = 5;
        c.gridy += 1;
        c.gridwidth = 3;
        c.gridx = 0;
        label = new JLabel(" BRILLO");
        label.setFont(new Font("Arial Black", 0, 14));
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.ipady = 5;
        c.gridwidth = 3;
        c.gridx = 1;
        this.bResetBright = new JButton("Reset Brillo");
        this.bResetBright.addActionListener(this);
        panel.add(this.bResetBright, c);

        c.ipady = 15;
        c.gridy += 1;
        c.gridwidth = 1;
        c.gridx = 0;
        label = new JLabel("  Total");
        label.setForeground(Color.gray);
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridwidth = 3;
        c.gridx = 1;
        this.jsBrightLevel = new JSlider(Dam2Gr.BRIGHT_MIN, Dam2Gr.BRIGHT_MAX, Dam2Gr.BRIGHT_INI);

        this.jsBrightLevel.setName("Bright");
        this.jsBrightLevel.setMajorTickSpacing(1);
        this.jsBrightLevel.setPaintTicks(true);
        this.jsBrightLevel.addChangeListener(this);
        panel.add(this.jsBrightLevel, c);

        c.ipady = 0;
        c.gridy += 1;
        c.gridwidth = 1;
        c.gridx = 0;
        label = new JLabel("  Canal Rojo");
        label.setForeground(Color.gray);
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridwidth = 3;
        c.gridx = 1;
        this.jsRedBrightLevel = new JSlider(Dam2Gr.BRIGHT_MIN, Dam2Gr.BRIGHT_MAX, Dam2Gr.BRIGHT_INI);

        this.jsRedBrightLevel.setName("Bright Red");
        this.jsRedBrightLevel.setBackground(Color.red);
        this.jsRedBrightLevel.setMajorTickSpacing(1);
        this.jsRedBrightLevel.setPaintTicks(true);
        this.jsRedBrightLevel.addChangeListener(this);
        panel.add(this.jsRedBrightLevel, c);

        c.gridy += 1;
        c.gridwidth = 1;
        c.gridx = 0;
        label = new JLabel("  Canal Verde");
        label.setForeground(Color.gray);
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridwidth = 3;
        c.gridx = 1;
        this.jsGreenBrightLevel = new JSlider(Dam2Gr.BRIGHT_MIN, Dam2Gr.BRIGHT_MAX, Dam2Gr.BRIGHT_INI);

        this.jsGreenBrightLevel.setName("Bright Green");
        this.jsGreenBrightLevel.setBackground(Color.green);
        this.jsGreenBrightLevel.setMajorTickSpacing(1);
        this.jsGreenBrightLevel.setPaintTicks(true);
        this.jsGreenBrightLevel.addChangeListener(this);
        panel.add(this.jsGreenBrightLevel, c);

        c.gridy += 1;
        c.gridwidth = 1;
        c.gridx = 0;
        label = new JLabel("  Canal Azul");
        label.setForeground(Color.gray);
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridwidth = 3;
        c.gridx = 1;
        this.jsBlueBrightLevel = new JSlider(Dam2Gr.BRIGHT_MIN, Dam2Gr.BRIGHT_MAX, Dam2Gr.BRIGHT_INI);

        this.jsBlueBrightLevel.setName("Bright Blue");
        this.jsBlueBrightLevel.setBackground(Color.blue);
        this.jsBlueBrightLevel.setMajorTickSpacing(1);
        this.jsBlueBrightLevel.setPaintTicks(true);
        this.jsBlueBrightLevel.addChangeListener(this);
        panel.add(this.jsBlueBrightLevel, c);

        c.gridy += 1;
        c.gridx = 0;
        c.gridwidth = 4;
        label = new JLabel(" ");
        panel.add(label, c);
    }

    private void addColorTools(Container panel, GridBagConstraints c) {
        JLabel label;

        c.ipady = 0;
        c.gridy += 1;
        c.gridx = 0;
        c.gridwidth = 4;
        label = new JLabel(" ");
        panel.add(label, c);

        c.gridy += 1;
        c.gridwidth = 3;
        c.gridx = 0;
        label = new JLabel(" COLOR");
        label.setFont(new Font("Arial Black", 0, 14));
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridwidth = 3;
        c.gridx = 1;
        this.tbGrey = new JToggleButton("Convertir a Gris");
        this.tbGrey.addActionListener(this);
        panel.add(this.tbGrey, c);

        c.gridy += 1;
        c.gridx = 0;
        c.gridwidth = 4;
        label = new JLabel(" ");
        panel.add(label, c);
    }

    private void addFilterTools(Container panel, GridBagConstraints c) {
        c.ipady = 5;
        c.gridy += 1;
        c.gridx = 0;
        c.gridwidth = 4;
        JLabel label = new JLabel(" FILTROS");
        label.setFont(new Font("Arial Black", 0, 14));
        label.setHorizontalAlignment(2);
        label.setVerticalAlignment(3);
        panel.add(label, c);

        c.ipady = 15;
        c.gridy++;
        c.gridwidth = 1;
        c.gridx = 0;
        label = new JLabel("  Focus");
        label.setForeground(Color.gray);
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridwidth = 2;
        c.gridx = 1;
        this.jsUnfocusLevel = new JSlider(-5, 5, 0);

        this.jsUnfocusLevel.setName("Focus");
        this.jsUnfocusLevel.setMinorTickSpacing(1);
        this.jsUnfocusLevel.setMajorTickSpacing(5);
        this.jsUnfocusLevel.setPaintTicks(true);
        this.jsUnfocusLevel.addChangeListener(this);
        panel.add(this.jsUnfocusLevel, c);

        c.ipady = 15;
        c.gridwidth = 1;
        c.gridx = 3;
        label = new JLabel("  Unfocus");
        label.setForeground(Color.gray);
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridy += 1;
        c.gridx = 0;
        c.gridwidth = 4;
        label = new JLabel(" ");
        panel.add(label, c);
    }

    private void addImageSelectors(Container panel, GridBagConstraints c) {
        c.ipady = 5;
        c.gridy += 1;
        c.gridx = 0;
        c.gridwidth = 4;
        JLabel label = new JLabel(" IMAGEN");
        label.setFont(new Font("Arial Black", 0, 14));
        label.setHorizontalAlignment(2);
        label.setVerticalAlignment(3);
        panel.add(label, c);

        c.ipady = 5;
        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 0.1D;
        this.tbImg1Selected = new JToggleButton("1");
        this.tbImg1Selected.addActionListener(this);
        panel.add(this.tbImg1Selected, c);

        c.gridx += 1;
        this.tbImg2Selected = new JToggleButton("2");
        this.tbImg2Selected.addActionListener(this);
        panel.add(this.tbImg2Selected, c);

        c.gridx += 1;
        this.tbImg3Selected = new JToggleButton("3");
        this.tbImg3Selected.addActionListener(this);
        panel.add(this.tbImg3Selected, c);
        c.weightx = 0.0D;

        ButtonGroup bg = new ButtonGroup();
        bg.add(this.tbImg1Selected);
        bg.add(this.tbImg2Selected);
        bg.add(this.tbImg3Selected);

        c.gridy += 1;
        c.gridx = 0;
        c.gridwidth = 4;
        label = new JLabel(" ");
        panel.add(label, c);
    }

    private void addImageDataTable(Container panel, GridBagConstraints c) {
        c.gridy += 1;
        c.gridx = 1;
        c.gridwidth = 3;
        c.anchor = 18;
        this.jtImageDetails = createImageDataTable();
        panel.add(this.jtImageDetails.getTableHeader(), c);

        c.weighty = 0.01D;
        c.gridy += 1;
        c.ipady = 5;
        panel.add(this.jtImageDetails, c);
        c.weighty = 0.0D;

        c.ipady = 0;
        c.gridy += 1;
        c.gridx = 0;
        c.weighty = 0.1D;
        c.gridwidth = 4;
        JLabel label = new JLabel(" ");
        panel.add(label, c);
    }

    private void addLoadImageSection(Container panel, GridBagConstraints c) {
        c.gridy += 1;
        c.gridwidth = 3;
        c.gridx = 0;
        JLabel label = new JLabel(" ARCHIVO");
        label.setFont(new Font("Arial Black", 0, 14));
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridx = 1;
        c.gridwidth = 3;

        this.bLoad = new JButton("Cargar Imagen");
        this.bLoad.addActionListener(this);
        panel.add(this.bLoad, c);

        c.gridy += 1;
        c.gridx = 1;
        c.gridwidth = 3;
        this.jlImageName = new JLabel("No name");
        panel.add(this.jlImageName, c);
    }

    private void addUIComponents(Container panel) {
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.anchor = 18;
        c.fill = 2;
        c.ipadx = 0;
        c.ipady = 5;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.0D;
        c.weighty = 0.0D;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.gridx = 1;
        this.addLoadImageSection(panel, c);

        c.ipadx = 0;
        c.ipady = 5;
        c.gridwidth = 4;
        c.gridx = 1;
        c.weighty = 0.0D;

        this.addImageDataTable(panel, c);

        c.ipadx = 0;
        c.ipady = 5;
        c.gridwidth = 4;
        c.gridx = 1;
        c.weighty = 0.0;
        this.addImageSelectors(panel, c);

        c.ipadx = 0;
        c.ipady = 5;
        c.gridwidth = 4;
        c.gridx = 1;
        c.weighty = 0.0D;

        this.addZoneSelector(panel, c);

        c.ipadx = 0;
        c.ipady = 5;
        c.gridwidth = 4;
        c.gridx = 1;
        c.weighty = 0.0D;

        this.addBrigthTools(panel, c);

        c.ipadx = 0;
        c.ipady = 5;
        c.gridwidth = 4;
        c.gridx = 1;
        c.weighty = 0.0D;
        this.addColorTools(panel, c);

        c.ipadx = 0;
        c.ipady = 5;
        c.gridwidth = 4;
        c.gridx = 1;
        c.weighty = 0.0;
        this.addFilterTools(panel, c);

        c.fill = 1;
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        c.gridheight = 26;
        c.gridwidth = 1;
        c.gridx = 5;
        c.gridy = 0;
        this.addViewerSection(panel, c);
    }

    private void addViewerSection(Container panel, GridBagConstraints c) {
        this.viewer = new Viewer(this);
        panel.add(this.viewer, c);
    }

    private void addZoneSelector(Container panel, GridBagConstraints c) {
        JLabel label;

        c.gridy += 1;
        c.gridx = 0;
        c.gridwidth = 3;
        c.ipadx = 40;
        label = new JLabel(" ZONA");
        label.setFont(new Font("Arial Black", 0, 14));
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridx = 1;
        c.gridwidth = 1;
        this.tbAllZone = new JToggleButton("Todo");
        this.tbAllZone.addActionListener(this);
        panel.add(this.tbAllZone, c);
        this.tbAllZone.setSelected(true);

        c.gridx += 1;
        c.gridwidth = 2;
        this.tbCentralZone = new JToggleButton("Recuadro");
        this.tbCentralZone.addActionListener(this);
        panel.add(this.tbCentralZone, c);

        ButtonGroup bg = new ButtonGroup();
        bg.add(this.tbCentralZone);
        bg.add(this.tbAllZone);

        c.ipady = 15;
        c.gridy += 1;
        c.gridwidth = 1;
        c.gridx = 0;
        label = new JLabel(" Tamaño");
        label.setForeground(Color.gray);
        label.setHorizontalAlignment(2);
        panel.add(label, c);

        c.gridwidth = 3;
        c.gridx = 1;
        this.jsZoneSize = new JSlider(0, 100, 50);
        this.jsZoneSize.setName("Size");
        this.jsZoneSize.setMajorTickSpacing(25);
        this.jsZoneSize.setPaintTicks(true);
        this.jsZoneSize.addChangeListener(this);
        panel.add(this.jsZoneSize, c);

        c.gridy += 1;
        c.gridx = 0;
        c.gridwidth = 4;
        label = new JLabel(" ");
        panel.add(label, c);
    }

    private void createFrame() {
        this.setDefaultCloseOperation(3);
        this.setTitle("2on. DAM · Efectos Gráficos");
        this.setLayout(new GridBagLayout());

        this.addUIComponents(getContentPane());
        this.loadTrialImage();

        this.addComponentListener(this);
        this.pack();
        this.setVisible(true);

        this.viewer.myPaint();
    }

    private JTable createImageDataTable() {
        String[] columnHeaders = {"Descripción", "Valor"};
        Object[][] dataRows = {
            {"KPixels Totals", ""}, {"KBytes Totals", ""},
            {"Pixels Amplada", ""}, {"Pixels Al�ada", ""},
            {"Canal Alpha", ""}};

        JTable table = new JTable(dataRows, columnHeaders);
        table.getColumnModel().getColumn(0).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(60);

        return table;
    }

    private int getSelectedImage() {
        if (this.tbImg1Selected.isSelected()) {
            return Viewer.IMG_ONE; // ================= Imagen 1 ==============>
        }
        if (this.tbImg2Selected.isSelected()) { // ==== Imagen 2 ==============>
            return Viewer.IMG_TWO;
        }
        return Viewer.IMG_THREE; // =================== Imagen 3 ==============>
    }

    private boolean getSelectedZone() {
        return this.tbAllZone.isSelected();
    }

    private void iniImageSettings() {
        this.tbImg1Selected.setSelected(true);
        this.tbGrey.setSelected(false);

        this.tbAllZone.setSelected(true);
        this.jsZoneSize.setValue(0);

        this.jsBrightLevel.setValue(0);
        this.jsRedBrightLevel.setValue(0);
        this.jsGreenBrightLevel.setValue(0);
        this.jsBlueBrightLevel.setValue(0);
    }

    private void loadTrialImage() {
        this.viewer.loadImage("kratos.jpg");
        showImageData();

        this.iniImageSettings();
    }

    private void resetBright() {
        this.viewer.resetBright(getSelectedImage());

        this.jsBrightLevel.setValue(0);
        this.jsRedBrightLevel.setValue(0);
        this.jsGreenBrightLevel.setValue(0);
        this.jsBlueBrightLevel.setValue(0);
    }

    private void resetSquareBright() {
        this.viewer.resetSquareBright(getSelectedImage());

        this.jsBrightLevel.setValue(0);
        this.jsRedBrightLevel.setValue(0);
        this.jsGreenBrightLevel.setValue(0);
        this.jsBlueBrightLevel.setValue(0);
    }

    private void selectImage(int imgSelected) {
        float percentage;

        percentage = this.viewer.getSquareSize(imgSelected) * 100;
        this.jsZoneSize.setValue((int) percentage);
        this.selectZone(imgSelected, this.getSelectedZone());
    }

    private void selectZone(int imgSelected, boolean all) {
        if (all) {
            this.tbGrey.setSelected(this.viewer.getGray(imgSelected));
            this.jsBrightLevel.setValue(this.viewer.getBright(imgSelected) / Dam2Gr.BRIGHT_MULTIPLIER);
            this.jsRedBrightLevel.setValue(this.viewer.getRedBright(imgSelected) / Dam2Gr.BRIGHT_MULTIPLIER);
            this.jsGreenBrightLevel.setValue(this.viewer.getGreenBright(imgSelected) / Dam2Gr.BRIGHT_MULTIPLIER);
            this.jsBlueBrightLevel.setValue(this.viewer.getBlueBright(imgSelected) / Dam2Gr.BRIGHT_MULTIPLIER);
        } else {
            this.tbGrey.setSelected(this.viewer.getSquareGray(imgSelected));
            this.jsBrightLevel.setValue(this.viewer.getSquareBright(imgSelected) / Dam2Gr.BRIGHT_MULTIPLIER);
            this.jsRedBrightLevel.setValue(this.viewer.getSquareRedBright(imgSelected) / Dam2Gr.BRIGHT_MULTIPLIER);
            this.jsGreenBrightLevel.setValue(this.viewer.getSquareGreenBright(imgSelected) / Dam2Gr.BRIGHT_MULTIPLIER);
            this.jsBlueBrightLevel.setValue(this.viewer.getSquareBlueBright(imgSelected) / Dam2Gr.BRIGHT_MULTIPLIER);
        }

        System.out.println("Imagen: " + imgSelected + "   Brillo: " + this.jsBrightLevel.getValue());
    }

    private void showImageData() {
        int w = this.viewer.getImageWidth();
        int h = this.viewer.getImageHeight();

        this.jlImageName.setText(" " + this.viewer.getImageName());

        TableModel model = this.jtImageDetails.getModel();
        model.setValueAt((int) w * h / 1000, 0, 1);
        model.setValueAt((int) w * h * 3 / 1000, 1, 1);
        model.setValueAt((int) w, 2, 1);
        model.setValueAt((int) h, 3, 1);
        model.setValueAt("No", 4, 1);
    }

    // Listeners ---------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        switch (str) {
            case "1":
                this.selectImage(Viewer.IMG_ONE);
                break;
            case "2":
                this.selectImage(Viewer.IMG_TWO);
                break;
            case "3":
                this.selectImage(Viewer.IMG_THREE);
                break;
            case "Cargar Imagen":
                this.viewer.loadImage("kratos.jpg");
                this.showImageData();
                this.iniImageSettings();
                break;
            case "Reset Brillo":
                if (getSelectedZone()) {
                    this.resetBright();
                } else {
                    this.resetSquareBright();
                }
                break;
            case "Convertir a Gris":
                if (getSelectedZone()) {
                    this.viewer.setGray(this.getSelectedImage(), this.tbGrey.isSelected());
                } else {
                    this.viewer.setSquareGray(this.getSelectedImage(), this.tbGrey.isSelected());
                }
                break;
            case "Recuadro":
            case "Todo":
                this.selectZone(this.getSelectedImage(), this.getSelectedZone());
                break;
            default:
                System.out.println("Acción NO tratada: " + e);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider sl = (JSlider) e.getSource();
        String slName = sl.getName();
        if (sl.getValueIsAdjusting()) {
            return;
        }

        switch (slName) {
            case "Bright":
                if (this.getSelectedZone()) {
                    this.viewer.setBright(sl.getValue() * Dam2Gr.BRIGHT_MULTIPLIER, getSelectedImage());
                } else {
                    this.viewer.setSquareBright(sl.getValue() * Dam2Gr.BRIGHT_MULTIPLIER, getSelectedImage());
                }
                break;
            case "Bright Red":
                if (this.getSelectedZone()) {
                    this.viewer.setRedBright(sl.getValue() * Dam2Gr.BRIGHT_MULTIPLIER, getSelectedImage());
                } else {
                    this.viewer.setSquareRedBright(sl.getValue() * Dam2Gr.BRIGHT_MULTIPLIER, getSelectedImage());
                }
                break;
            case "Bright Green":
                if (this.getSelectedZone()) {
                    this.viewer.setGreenBright(sl.getValue() * Dam2Gr.BRIGHT_MULTIPLIER, getSelectedImage());
                } else {
                    this.viewer.setSquareGreenBright(sl.getValue() * Dam2Gr.BRIGHT_MULTIPLIER, getSelectedImage());
                }
                break;
            case "Bright Blue":
                if (this.getSelectedZone()) {
                    this.viewer.setBlueBright(sl.getValue() * Dam2Gr.BRIGHT_MULTIPLIER, getSelectedImage());
                } else {
                    this.viewer.setSquareBlueBright(sl.getValue() * Dam2Gr.BRIGHT_MULTIPLIER, getSelectedImage());
                }
                break;
            case "Size":
                this.viewer.setSquareDimension(sl.getValue(), getSelectedImage());

                break;
            case "Focus":
                System.out.println("Level: " + sl.getValue());
                if (this.getSelectedZone()) {
                    this.viewer.setSquareFocusFilter(getSelectedImage(), sl.getValue());
                } else {
                    this.viewer.setFocusFilter(getSelectedImage(), sl.getValue());
                }

                break;
            default:
                System.err.println("SLIDER Acción no tratada: " + e);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        //System.out.println("Resized");
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        //System.out.println("Moved");
    }

    @Override
    public void componentShown(ComponentEvent e) {
        //System.out.println("Shown");
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        //System.out.println("Hidden");
    }
}
