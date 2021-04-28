package Lab4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int displaySize; //Размер приложения
    private JImageDisplay imageDisplay; //Ссылка на изображение
    private FractalGenerator fractalGenerator; //Выбор фрактала для отрисовки
    private Rectangle2D.Double range; //Область отрисовки

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
        imageDisplay = new JImageDisplay(displaySize, displaySize);
        imageDisplay.addMouseListener(new MouseListener());
        button.addActionListener(new ActionHandler());
        frame.setLayout(new java.awt.BorderLayout());
        frame.add(imageDisplay, BorderLayout.CENTER);
        frame.add(button, BorderLayout.SOUTH);
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

    //Возвращение к изначальной отрисовке фрактала
    public class ActionHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            fractalGenerator.getInitialRange(range);
            drawFractal();
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
