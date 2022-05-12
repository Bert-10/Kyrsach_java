package IncorrectInput;

import javax.swing.*;
import java.util.Objects;

public class Proverka {

    static public boolean CheckNumber (String str)
    {
        boolean check=true;
        double n=1;
        try
        {
            n = Double.parseDouble(str);
        }catch(Exception r)
        {
            check=false;
            JOptionPane.showMessageDialog(null, "Было введено не число", "Некорректный ввод", JOptionPane.INFORMATION_MESSAGE);
        }
        if (n<0)
        {
            check=false;
            JOptionPane.showMessageDialog(null, "Число должно быть больше нуля", "Некорректный ввод", JOptionPane.INFORMATION_MESSAGE);

        }
        return check;
    }

    static public boolean CheckStroka(String str)
    {
        String checkStr="";
        boolean check=true;
        checkStr="АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЫЭЮЯабвгдеёжзийклмнопрстуфхцчшщэыюяьъ- ";
        if(Objects.equals(str, ""))
        {
            JOptionPane.showMessageDialog(null, "Некорректный ввод \nДопустимы только пробел и кириллица", "Некорректный ввод", JOptionPane.INFORMATION_MESSAGE);
            check=false;
        }
        for (int i = 0; i < str.length(); i++)
        {
            if ((checkStr.indexOf(str.charAt(i)) == -1))
            {
                JOptionPane.showMessageDialog(null, "Некорректный ввод \nДопустимы только пробел и кириллица", "Некорректный ввод", JOptionPane.INFORMATION_MESSAGE);
                check=false;
                break;
            }
        }
        return check;
    }
}
