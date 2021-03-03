package Lab1;

public class Primes {
    public static void main(String[] args) {
        //Перебор чисел от 2х до 100
        for (int n = 2; n < 101; n++){
            //Условие с проверкой методом isPrime числа
            if (isPrime(n))
                //Вывод чисел являющихся простыми
                System.out.println(n);
        }
    }
    //Метод на проверку числа, является ли оно простым
    public static boolean isPrime(int n) {
        //Перебор чисел от 2х до n
        for (int i = 2; i < n; i++){
            //Проверка делимости числа
            if (n % i == 0)
                //Если метод нашел число, на которое n делится без остатка
                return false;
        }
        //Если метод не нашел число, на которое n делится без остатка
        return true;
    }
}
