package Lab2;

public class Point3d extends Point2d {

    /** координата Z **/
    private double zCoord;

    /** Конструктор инициализации **/
    public Point3d ( double x, double y, double z) {
        super(x,y);
        zCoord = z;
    }

    /** Конструктор по умолчанию. **/
    public Point3d () {
        this(0, 0, 0);
    }

    /** Возвращение координаты Z **/
    public double getZ () {
        return zCoord;
    }

    /** Установка значения координаты Z. **/
    public void setZ ( double val) {
        zCoord = val;
    }

    /** Метод сравнения двух точек класса Point3d. **/
    public boolean equals (Point3d twopoint) {
        if ((this.getX()== twopoint.getX())&&
                (this.getY()==twopoint.getY())&&
                (this.getZ()==twopoint.getZ())) return true;
        else return false;
    }

    /** Вычисление расстояния между двумя точками **/
    public double distanceTo (Point3d twopoint) {
        return (Math.sqrt(Math.pow((twopoint.getX() - this.getX()),2) +
                         Math.pow((twopoint.getY() - this.getY()),2) +
                         Math.pow((twopoint.getZ() - this.getZ()),2)));
    }
}
