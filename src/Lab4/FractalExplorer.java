package Lab4;

import Lab5.BurningShip;
import Lab5.Tricorn;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class FractalExplorer {
    private int displaySize; //Размер приложения
    private JImageDisplay imageDisplay; //Ссылка на изображение
    private FractalGenerator fractalGenerator; //Выбор фрактала для отрисовки
    private Rectangle2D.Double range; //Область отрисовки
    private JComboBox comboBox; //Lab5 Комбобокс
    //Lab 6
    private int rowsRemaining;
    private JButton button; //Для метода enableGUI
    private JButton buttonSave; //Для метода enableGUI


    //Конструктор
    public FractalExplorer (int displaySize){
        this.displaySize = displaySize;
        this.fractalGenerator = new Mandelbrot();
        this.range = new Rectangle2D.Double(0,0,0,0);
        fractalGenerator.getInitialRange(this.range);
    }

    //Настройки окна приложения
    public void createAndShowGUI(){
        JFrame frame = new JFrame("Fractal Generation");
        button = new JButton("Reset");
        buttonSave = new JButton("Save image");//Lab5
        JPanel jPanel1 = new JPanel(); //Lab5
        JPanel jPanel2 = new JPanel(); // Lab5
        JLabel label = new JLabel("Fractal:");//Lab5
        imageDisplay = new JImageDisplay(displaySize, displaySize);
        imageDisplay.addMouseListener(new MouseListener());
        //ComboBox Lab5
        comboBox = new JComboBox();
        comboBox.addItem(new Mandelbrot());
        comboBox.addItem(new Tricorn());
        comboBox.addItem(new BurningShip());
        comboBox.addActionListener(new ActionHandler());
        //Reset Lab5
        button.setActionCommand("Reset");
        button.addActionListener(new ActionHandler());
        //Save image Lab5
        buttonSave.setActionCommand("Save");
        buttonSave.addActionListener(new ActionHandler());
        //Panel Lab5
        jPanel1.add(label, BorderLayout.CENTER);
        jPanel1.add(comboBox, BorderLayout.CENTER);
        jPanel2.add(button, BorderLayout.CENTER);
        jPanel2.add(buttonSave, BorderLayout.CENTER);
        //
        frame.setLayout(new BorderLayout());
        frame.add(imageDisplay, BorderLayout.CENTER);
        frame.add(jPanel1, BorderLayout.NORTH);
        frame.add(jPanel2, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    //Lab6 Отрисовка фрактала
    private void drawFractal(){
        enableGUI(false);
        rowsRemaining = displaySize;
        for (int i = 0; i < displaySize; i++){
            //Заруск отрисовки
            FractalWorker drawRow = new FractalWorker(i);
            drawRow.execute();
        }
    }

    public static void main(String[] args){
        FractalExplorer fractalExplorer = new FractalExplorer(600);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    //Lab 5
    public class ActionHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (e.getActionCommand().equals("Reset")){
                fractalGenerator.getInitialRange(range);
                drawFractal();
            }
            else if (e.getActionCommand().equals("Save")) {
                JFileChooser fileChooser = new JFileChooser(); //Класс для выбора в какой файл сохраняем
                //Гарантия перезаписи только в пнг формате
                FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("PNG Image", "png");
                fileChooser.setFileFilter(fileNameExtensionFilter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                //
                int t = fileChooser.showSaveDialog(imageDisplay); //Окно сохранения
                if (t == JFileChooser.APPROVE_OPTION) { //Подтверждение операции сохранения
                    try {
                        ImageIO.write(imageDisplay.getImage(), "png", fileChooser.getSelectedFile());
                    } catch (NullPointerException | IOException ee) { //Какие ошибки отлавливаем
                        JOptionPane.showMessageDialog(imageDisplay, ee.getMessage(), "Cannot save image", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            //Отрисовка по выбору в комбобоксе
            else {
                    fractalGenerator = (FractalGenerator) comboBox.getSelectedItem();
                    range = new Rectangle2D.Double(0,0,0,0);
                    fractalGenerator.getInitialRange(range);
                    drawFractal();
            }
        }
    }

    //Меняем область отрисовки фрактала
    public class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            //Lab 6
            if (rowsRemaining == 0) {
                double x = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());
                double y = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());
                fractalGenerator.recenterAndZoomRange(range, x, y, 0.5);
                drawFractal();
            }
        }
    }

    //Lab 6
    public class FractalWorker extends SwingWorker<Object,Object> {
        private int y_coord;
        private int[] rgb;
        public FractalWorker (int y_coord){
            this.y_coord = y_coord;
        }
        @Override
        //Вычисление одной строки фрактала
        protected Object doInBackground() throws Exception{
            rgb = new int[displaySize];
            for (int i = 0; i < displaySize; i++){
                int count = fractalGenerator.numIterations(FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, i),
                        FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, y_coord));
                if (count == -1) rgb [i] = 0;
                else {
                    float hue = 0.7f + (float) count / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    rgb[i] = rgbColor;
                }
            }
            return null;
        }
        //Перерисовка области
        @Override
        protected void done(){
            for (int i = 0; i < displaySize; i++){
                imageDisplay.drawPixel(i, y_coord, rgb[i]);
            }
            imageDisplay.repaint(0,0,y_coord,displaySize,1);
            rowsRemaining--;
            if (rowsRemaining == 0) enableGUI(true);
        }
    }

    //Lab 6 ON.OFF GUI
    public void enableGUI(boolean b){
        button.setEnabled(b);
        buttonSave.setEnabled(b);
        comboBox.setEnabled(b);
    }
}
