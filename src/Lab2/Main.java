package Lab2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите значение координаты х первой точки");
        double ox = scan.nextDouble();
        System.out.println("Введите значение координаты y первой точки");
        double oy = scan.nextDouble();
        System.out.println("Введите значение координаты z первой точки");
        double oz = scan.nextDouble();
        Point3d onepoint = new Point3d(ox,oy,oz);
        System.out.println("Введите значение координаты х второй точки");
        double tx = scan.nextDouble();
        System.out.println("Введите значение координаты y второй точки");
        double ty = scan.nextDouble();
        System.out.println("Введите значение координаты z второй точки");
        double tz = scan.nextDouble();
        Point3d twopoint = new Point3d(tx,ty,tz);
        System.out.println("Введите значение координаты х третьей точки");
        double thx = scan.nextDouble();
        System.out.println("Введите значение координаты y третьей точки");
        double thy = scan.nextDouble();
        System.out.println("Введите значение координаты z третьей точки");
        double thz = scan.nextDouble();
        Point3d threepoint = new Point3d(thx,thy,thz);
        if ((onepoint.equals(twopoint))||(onepoint.equals(threepoint))||(twopoint.equals(threepoint)))
            System.out.println("Одна из точек равняется другой, расчет площади не будет выполнен.");
        else System.out.println("Площадь треугольника из трех точек = " + computeArea(onepoint,twopoint,threepoint));
    }
    public static double computeArea(Point3d one, Point3d two, Point3d three){
        double a = one.distanceTo(two);
        double b = two.distanceTo(three);
        double c = three.distanceTo(one);
        double p = (a+b+c)/2;
        return Math.sqrt(p*(p-a)*(p-b)*(p-c));
    }
}
