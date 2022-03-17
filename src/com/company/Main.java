package com.company;
import java.lang.*;
import java.util.Scanner;
import java.lang.String;


public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = 0;
        while (n != 2) {
            System.out.println("    ");
            System.out.println("1. Посчитать значение выражения:  ");
            System.out.println("2. Завершить выполнение программы:  ");

            if (scanner.hasNextInt()) {
                n = scanner.nextInt();
            } else {
                System.out.println("Вы ввели не целочисленное значение операции!  ");
                break;
            }

            switch (n) {
                case 1: {
                    boolean ExprCorrect;
                    Calculator expText = new Calculator();


                    Scanner in = new Scanner(System.in);
                    System.out.print("Пожалуйста, введите выражение: ");
                    String ourString = in.nextLine();
                    try {
                        expText.setStr(ourString);
                        ExprCorrect = expText.count();
                        if (!ExprCorrect) {
                            System.out.print("Выражение некорректно, повторите попытку");
                        } else {
                            System.out.print(ourString + " = ");
                            expText.printExp();
                        }
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
                ;
                break;

                case 2: break;
                default:
                    System.out.println("Вы ввели неверный номер операции! ");
                    break;

            }

        }
    }
}
