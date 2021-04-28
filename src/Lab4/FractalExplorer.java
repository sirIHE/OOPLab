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
        JButton button = new JButton("Reset");
        JButton buttonSave = new JButton("Save image");//Lab5
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

    //Отрисовка фрактала
    private void drawFractal(){
        for (int x = 0; x < displaySize; x++){
            for (int y = 0; y < displaySize; y++){
                int counter = fractalGenerator.numIterations(FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, x),
                        fractalGenerator.getCoord(range.y, range.y + range.width, displaySize, y));
                if (counter == -1)
                    imageDisplay.drawPixel(x,y,0);
                else {
                    float hue = 0.7f + (float) counter / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    imageDisplay.drawPixel(x,y, rgbColor);
                }
            }
        }
        imageDisplay.repaint();
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
    public class MouseListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            double x = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());
            double y = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());
            fractalGenerator.recenterAndZoomRange(range, x, y, 0.5);
            drawFractal();
        }
    }
}
