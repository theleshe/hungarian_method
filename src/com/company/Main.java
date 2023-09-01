package com.company;

import java.util.Scanner;

public class Main {



    public static void main(String[] args) {


        Scanner scanner1 = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);
/*
        int n;

        do {
            System.out.println("Введите число, определяющее размерность квадратной матрицы назначений: ");
             n = scanner1.nextInt();
        } while (n < 2);

        Cell [][] matrix = new Cell[n][n];                                                                                            //ЭТО ВСЕ ВВОД

        System.out.println("Заполнение матрицы: ");
        for (int i = 0; i < n; i++) {
            System.out.println("Введите элементы " + i + "-ой строки через пробел: ");
            String [] strRow = scanner2.nextLine().split(" ");           //запихиваем ввод в строку, сплитим ее по пробелу
            for (int j = 0; j < n; j++)
            {
                matrix[i][j] = new Cell(Integer.parseInt (strRow[j]));          //заполняем матрицу значениями с той строки
            }
        }

 */

        //ЭТО ТЕСТОВЫЕ ДАННЫЕ, ЧТОБ НЕ ПАРИТЬСЯ С ВВОДОМ
        int n = 7;
        Cell [][] matrix = new Cell[n][n];

        String [][] strRows = new String[n][n];
        strRows[0] = "43 25 39 46 41 39 40".split(" ");
        strRows[1] = "41 19 29 45 23 37 39".split(" ");
        strRows[2] = "30 21 25 42 42 36 27".split(" ");
        strRows[3] = "45 27 41 47 40 38 39".split(" ");
        strRows[4] = "37 26 40 44 22 26 41".split(" ");
        strRows[5] = "33 27 42 52 54 49 52".split(" ");
        strRows[6] = "53 37 50 30 40 38 42".split(" ");
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                matrix[i][j] = new Cell(Integer.parseInt (strRows[i][j]));
            }
        }
        //КОНЕЦ ТЕСТОВЫХ ДАННЫХ ЧТОБ НЕ ПАРИТЬСЯ С ВВОДОМ

        Cell [][] startMatrix = copyMatrix(matrix, n);      //сохраняем начальную матрицу, т.к. она понадобится при подсчете оптимального решения

        int choose;
        do {
            System.out.println(" 0 - Задача о назначениях\n 1 - Задача о Невестах");
            choose = scanner1.nextInt();
        } while (choose < 0 || choose > 1);

        System.out.println("Исходная матрица: ");
        printMatrix(matrix, n);

        if (choose == 0) {      //если задача о назначениях
            System.out.println("Вычитаем из каждой строки минимальный элемент этой строки: ");
            int minRate;
            for (int i = 0; i < n; i++) {
                minRate = 100000;
                for (int j = 0; j < n; j++) {
                    if (matrix[i][j].value < minRate)           //находим мин элемент в строке
                        minRate = matrix[i][j].value;
                }

                for (int j = 0; j < n; j++) {
                    matrix[i][j].value -= minRate;          //вычитаем мин элемент в строке
                }
            }
            printMatrix(matrix, n);

            System.out.println("Вычитаем из каждого столбца минимальный элемент этого столбца: ");
            for (int j = 0; j < n; j++) {
                minRate = 100000;
                for (int i = 0; i < n; i++) {
                    if (matrix[i][j].value < minRate)           //находим мин элемент в столбце
                        minRate = matrix[i][j].value;
                }

                for (int i = 0; i < n; i++) {
                    matrix[i][j].value -= minRate;          //вычитаем мин элемент в столбце
                }
            }
            printMatrix(matrix, n);

        } else {            //если о невестах
            System.out.println("Находим максимальный элемент, умножаем значения элементов матрицы на -1, и прибавляем максимальный элемент: ");
            int maxRate = -1;
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    if (matrix[i][j].value > maxRate)
                        maxRate = matrix[i][j].value;
                }
            }

            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                   matrix[i][j].value = maxRate - matrix[i][j].value;
                }
            }
            printMatrix(matrix, n);
        }
        boolean isOptimality = false;

        do  {
            isOptimality = checkToOptimality(matrix, n);
            if (isOptimality)
            {
                System.out.println("Оптимальное решение достигнуто.");
                System.out.println("Выбранные клетки: " + printSelectedCells(matrix, n));
                System.out.println("Ответ: " + findAnswer(startMatrix, matrix, n)); //выводим ответ
            } else
            {
                clearCheckAndState(matrix, n);
                System.out.println("Оптимальное решение не достигнуто.");
                System.out.println("Строим прямые, вычеркивая все нули.");
                System.out.println("*c - перечеркнуто один раз, dc - перечеркнуто 2 раза*\n");
                buildLines(matrix, n);
                printMatrix(matrix, n);
                System.out.println("-min к неперечеркнутым, +min к находящимся на пересечении.\n");
                rebuildMatrix(matrix, n);
                printMatrix(matrix, n);
            }

        } while (!isOptimality);
   }

   public static Cell [][] copyMatrix (Cell[][] matrix, int n)
   {
       Cell [][] newMatrix = new Cell[n][n];
       for (int i = 0; i < n; i++)
       {
           for (int j = 0; j < n; j++)
           {
               newMatrix[i][j] = new Cell(matrix[i][j].value);
           }
       }
       return newMatrix;
   }


    public static void printMatrix(Cell[][] matrix, int size)       //вывод матрицы
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                switch (matrix[i][j].state) {
                    case 0 -> System.out.print(matrix[i][j].value + "   \t");
                    case 1 -> System.out.print(matrix[i][j].value + "(c)\t");
                    case 2 -> System.out.print(matrix[i][j].value + "(dc)\t");
                }
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    public static boolean checkToOptimality(Cell[][] matrix, int n)
    {
        int countOfZero;
        boolean isOptimality = false;

        for (int counter = 0; counter < 10; counter++) {
            for (int i = 0; i < n; i++) {       //здесь проставляю каждой строке/столбцу свой нуль
                countOfZero = 0;
                for (int j = 0; j < n; j++)
                {
                    if (matrix[i][j].value == 0 && !matrix[i][j].isChecked)            //определяем количество нулей в строке
                        countOfZero++;
                }

                if (countOfZero == 1)       //если в строке один 0
                {
                    for (int j = 0; j < n; j++) {
                        if (matrix[i][j].value == 0 && !matrix[i][j].isChecked)     //ищем его
                        {
                            matrix[i][j].isSelected = true;             //помечаем его как необходимый 0
                            for (int row = 0; row < n; row++) {
                                matrix[row][j].isChecked = true;            //помечаем столбец как проверенную
                            }
                            for (int colum = 0; colum < n; colum++) {
                                matrix[i][colum].isChecked = true;          //помчем строку как проверенную
                            }
                        }
                    }
                }
            }

            isOptimality = true;
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    if (!matrix[i][j].isChecked) {
                        isOptimality = false;
                        break;
                    }
                }
            }
        }
        return isOptimality;
    }

    public static void clearCheckAndState (Cell[][] matrix, int size)       //поставить isChecked = false и state = 0 и isSelected = false
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                matrix[i][j].isChecked = false;
                matrix[i][j].state = 0;
                matrix[i][j].isSelected = false;
            }
        }
    }

    public static void buildLines(Cell[][] matrix, int size)
    {
        int countOfZero;
        for (int i = 0; i < size; i++)
        {
            countOfZero = 0;

            for (int j = 0; j < size; j++)
            {
                if (matrix[i][j].value == 0)            //считаем нули в строке
                    countOfZero++;
            }

            if (countOfZero > 1)
            {
                for (int j = 0; j < size; j++)          //если нулей больше 1, то перечеркиваем всю строку
                    matrix[i][j].state++;
            } else if (countOfZero == 1)
            {
                for (int colum = 0; colum < size; colum++)
                {
                    if (matrix[i][colum].value == 0 && matrix[i][colum].state == 0)
                        for (int row = 0; row < size; row++)
                            matrix[row][colum].state++;             //если нуль один, вычеркиваем строку
                }
            }
        }

    }

    public static void rebuildMatrix(Cell[][] matrix, int size)
    {

        int minRate = 10000;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (matrix[i][j].value < minRate && matrix[i][j].state == 0)
                    minRate = matrix[i][j].value;               //находим минимальное значение
            }
        }

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                switch (matrix[i][j].state)
                {
                    case 0:
                        matrix[i][j].value -= minRate;      //если не перечеркнут, то вычитаем мин. элемент
                        break;
                    case 2:
                        matrix[i][j].value += minRate;      //если перечеркнут 2 раза, то добавляем мин. элемент
                }
            }
        }
    }

    public static int findAnswer (Cell[][] startMatrix, Cell[][] matrix, int size)
    {
        int answer = 0;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (matrix[i][j].isSelected)
                {
                    answer += startMatrix[i][j].value;
                }
            }
        }
        return answer;
    }

    public static String printSelectedCells(Cell[][] matrix, int n)
    {
        String answer = "";
        for(int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (matrix[i][j].isSelected)
                    answer += "(" + i + "," + j + ") ";
            }
        }
        return answer;
    }

}
