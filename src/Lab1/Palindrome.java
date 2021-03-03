package Lab1;

import java.util.Scanner;

public class Palindrome {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String s = scan.nextLine();
        System.out.println(isPalindrome(s));
    }
    public static String reverseString(String s){
        String rev = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            rev += s.charAt(i);
        }
        return rev;
    }
    public static boolean isPalindrome(String s){
        return s.equals(reverseString(s));
    }
}
