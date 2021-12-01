package com.company;

import java.util.Stack;

/**
 *  Класс для подсчета значения выражения и записи его в постфиксной форме
 *
 */

public class Calculator {
    /**
     * Данное выражение необходимо для хранения числа, операторов(+,-,/,*), скобок, пробелов и мусора
     */
    private String str = "";

    /**
     * Метод, предназначенный для удаления пробелов в выражении
     */
    private void delSpace()
    {
        String newstr = "";
        for(int pos = 0; pos < str.length(); pos++)
            if(str.charAt(pos) != ' ')
            {
                newstr += str.charAt(pos);
            }
        str = newstr;
    }

    /**
     * Метод, который определяет приоритеты в выражении
     * @param ch - символ, нужный для считывании лексемы
     * @return - возвращает приоритет символа
     */
    private int typeChar(char ch) {
        if (ch == '*' || ch == '/')
            return 3;
        else if (ch == '+' || ch == '-')
            return 2;
        else if (ch == '(')
            return 1;
        else if (ch == ')')
            return -1;
        return 0; // Означает число
    }

    /**
     * Метод, который выполняет постфиксную запись выражения и проверяет правильность написания скобок ( и )
     * @return - возвращает true, если удалось построить выражение, если нет, то false
     */
    private boolean postfixNotation() {

        boolean b = false;

        if(str.isEmpty()) b = false;
        else {
            delSpace();//удаляем пробелы
            //смотрим за скобками, если встретили, например: ( и )) bracket = -1 это ошибка
            int bracket = 0;

            for (int pos = 0; pos < str.length(); pos++) {
                if (bracket >= 0) {
                    switch (str.charAt(pos)) {
                        case '+': //Проверка на то, что 2 знака операции подряд стоять не могут
                        case '-':
                        case '*':
                        case '/': {
                            if (pos == 0 || pos == str.length() - 1)
                                return false;//выражение не может начинаться или заканчиваться знаком операции
                            else if (str.charAt(pos + 1) == '+' || str.charAt(pos + 1) == '-' || str.charAt(pos + 1) == '*' || str.charAt(pos + 1) == '/' || str.charAt(pos + 1) == ')')
                                b = false;
                            break;
                        }

                        case '(': //Проверка на то, что после знака ( операции стоять не могут
                        {
                            bracket++;
                            if (str.charAt(pos + 1) == '+' || str.charAt(pos + 1) == '-' || str.charAt(pos + 1) == '*' || str.charAt(pos + 1) == '/' || str.charAt(pos + 1) == ')')
                                b = false;
                            else if (pos == str.length() - 1) b = false;
                            break;
                        }


                        case ')'://Перед закрывающейся скобкой не может стоять знак операции
                        {
                            bracket--;
                            if (pos == 0) b = false;
                            else if (str.charAt(pos - 1) == '+' || str.charAt(pos - 1) == '-' || str.charAt(pos - 1) == '*' || str.charAt(pos - 1) == '/' || str.charAt(pos - 1) == '(')
                                b = false;
                            break;
                        }

                        default:
                            if (str.charAt(pos) >= '0' && str.charAt(pos) <= '9') {   //Проверка на такое: "9(" || ")9" - некорректно
                                if (pos != 0)
                                    if (str.charAt(pos - 1) == ')')
                                        b = false;
                                if (pos != str.length() - 1)
                                    if (str.charAt(pos + 1) == '(')
                                        b = false;
                            } else b = false;
                    }
                } else b = false;
            }
            if (bracket != 0) b = false;
            else b = true;
        }



        if(!b || str.isEmpty()) //Не заходит, если не прошел проверку на корректность или пуст
            return false;
        else{
            Stack<Character> charstack = new Stack<Character>(); //Примитивные типы, такие как char, не могут использоваться в качестве параметров типа в Java. Нам нужно использовать тип оболочки:
            String newstr = ""; //Строка для постфиксной записи выражения

            for(int pos = 0; pos < str.length(); pos++)
            {
                //приоритет оператора
                int typeoper = typeChar(str.charAt(pos));

                if (typeoper == 0) newstr += str.charAt(pos); //не оператор, помещаем в новую строку
                else if (typeoper == 1) charstack.push(str.charAt(pos)); // если ( , помещаем в стек
                else if (typeoper > 1) //если + - * /
                {
                    newstr += ' ';
                    while (!charstack.empty())
                    {
                        if (typeChar(charstack.peek()) >= typeoper) //если приоритет верх. эл. стека >= текущ.
                            newstr += charstack.pop();
                        else break;
                    }
                    charstack.push(str.charAt(pos)); //помещаем в стек
                }
                else if (typeoper == -1)  // если ) , переписываем все до ( в строку
                {
                    newstr += ' ';
                    while (typeChar(charstack.peek()) != 1)
                        newstr += charstack.pop();
                    charstack.pop();
                }
            }
            while (!charstack.empty()) newstr += charstack.pop();
            // str = "";
            str = newstr;
            return true;
        }
    }

    /**
     * Метод для инициализации поля str
     * @param str - выражение
     */
    public void setStr(String str) {
        this.str = str;
    }

    /**
     * Метод, который подсчитывает значения выражения в постфиксной форме записи и записи результата в поле str
     * @return - Возвращает true, если удалось посчитать, есои нет - false
     */
    public boolean calculate() throws IndexOutOfBoundsException
    {
        boolean t = postfixNotation(); //Для проверки того, не споткнулись ли мы на предыдущем методе
        if ( !t ) return false;
        else
        {
            String res = "";
            Stack<Double> st = new Stack<Double>();

            for(int pos = 0;pos < str.length(); pos++)
            {
                if (str.charAt(pos) == ' ') continue;
                if (typeChar(str.charAt(pos)) == 0)// Если число
                {
                    while (str.charAt(pos) != ' ' && typeChar(str.charAt(pos)) == 0)
                    {
                        res += str.charAt(pos++);
                        if (pos == str.length()) break;
                    }
                    st.push(Double.parseDouble(res)); // возвращает новый тип double, инициализированный значением, представленным указанной строкой
                    res = "";
                }
                if (typeChar(str.charAt(pos)) > 1)// + - * /
                {
                    double num1 = st.pop();
                    double num2 = st.pop();

                    if (str.charAt(pos) == '+')
                        st.push(num2 + num1);

                    if (str.charAt(pos) == '-')
                        st.push(num2 - num1);

                    if (str.charAt(pos) == '*')
                        st.push(num2 * num1);

                    if (str.charAt(pos) == '/')
                        st.push(num2 / num1);
                }
            }
            str = Double.toString(st.pop());
            return true;
        }
    }


    /**
     * Метод, который преобразует выражение в строку для тестов
     * @return поле str
     */
    public String getStr() { return str;}

    @Override public String toString() {
        String line = new String("");
        line += this.str;
        return line;

    }
}
