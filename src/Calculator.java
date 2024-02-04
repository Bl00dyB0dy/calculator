import java.util.Arrays;
import java.util.Scanner;

public class Calculator {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.println(calc(scanner.nextLine()));
    }

    public static String calc(String string) throws Exception {
        int numArray1Count = 0, numArray2Count = 0, operationLen = 0, state = 0;
        char operation = ' ';
        char[] numArray1 = new char[4], numArray2 = new char[4];

        for (int i = 0; i < string.length(); i++) {
            char symbol = string.charAt(i);

            switch (state) {
                case 0 -> {
                    if (isValidSymbol(symbol)) {
                        if (symbol == '0' && numArray1Count == 0) {
                            throw new Exception("Некорректные данные");
                        }

                        numArray1[numArray1Count] = symbol;
                        numArray1Count++;
                    } else if (symbol == ' ' && numArray1Count > 0) {
                        state++;
                    } else {
                        throw new Exception("Некорректные данные");
                    }
                }
                case 1 -> {
                    if (isValidOperation(symbol) && operationLen == 0) {
                        operation = symbol;
                        operationLen++;
                    } else if (symbol == ' ' && operationLen > 0) {
                        state++;
                    } else {
                        throw new Exception("Некорректные данные");
                    }
                }
                case 2 -> {
                    if (isValidSymbol(symbol)) {
                        if (symbol == '0' && numArray2Count == 0) {
                            throw new Exception("Некорректные данные");
                        }

                        numArray2[numArray2Count] = symbol;
                        numArray2Count++;
                    } else {
                        throw new Exception("Некорректные данные");
                    }
                }
            }
        }

        if (numArray2Count == 0) {
            throw new Exception("Некорректные данные");
        }

        boolean isNum1Arabic = isArabic(numArray1, numArray1Count), isNum2Arabic = isArabic(numArray2, numArray2Count), isNum1Romanian = isRomanian(numArray1, numArray1Count), isNum2Romanian = isRomanian(numArray2, numArray2Count);

        if ((isNum1Arabic || isNum2Arabic) && (isNum1Romanian || isNum2Romanian)) {
            throw new Exception("Некорректные данные");
        }

        if (isNum1Arabic) {
            int number1 = parseArabic(numArray1, numArray1Count), number2 = parseArabic(numArray2, numArray2Count);

            if (number1 >= 10 || number2 >= 10) {
                throw new Exception("Некорректные данные");
            }

            return String.valueOf(applyOperation(number1, number2, operation));
        } else {
            int number1 = parseRomanian(numArray1, numArray1Count), number2 = parseRomanian(numArray2, numArray2Count);

            if (number1 >= 10 || number2 >= 10) {
                throw new Exception("Некорректные данные");
            }

            int result = applyOperation(number1, number2, operation);

            if (result <= 0) {
                throw new Exception("Невозможно вывести результат в виде римского числа");
            }

            return numToRomanian(result);
        }
    }

    public static int parseArabic(char[] number, int numberCount) {
        int result = 0;

        for (int i = 0; i < numberCount; i++) {
            result = result * 10 + number[i] - '0';
        }

        return result;
    }

    public static int parseRomanian(char[] number, int numberCount) {
        int prevValue = 0, result = 0;
        int[] values = {0, 1, 5, 10, 50, 100};
        String[] symbols = {"", "I", "V", "X", "L", "C"};


        for (int i = 0; i < numberCount; i++) {
            char symbol = number[i];
            int curIndex = Arrays.asList(symbols).indexOf("" + symbol), curValue = values[curIndex];

            if (i > 0 && curValue > prevValue) {
                result += curValue - 2 * prevValue;
            } else {
                result += curValue;
            }

            prevValue = curValue;
        }

        return result;
    }

    public static String numToRomanian(int number) {
        int[] values = {100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                result.append(symbols[i]);
                number -= values[i];
            }
        }

        return result.toString();
    }

    public static int applyOperation(int number1, int number2, char operation) {
        int result = 0;

        switch (operation) {
            case '+' -> result = number1 + number2;
            case '-' -> result = number1 - number2;
            case '*' -> result = number1 * number2;
            case '/' -> result = number1 / number2;
        }

        return result;
    }

    public static boolean isValidSymbol(char symbol) {
        return symbol >= '1' && symbol <= '9' || symbol == 'I' || symbol == 'V' || symbol == 'X';
    }

    public static boolean isValidOperation(char operation) {
        return operation == '+' || operation == '-' || operation == '*' || operation == '/';
    }

    public static boolean isArabic(char[] number, int numberCount) {
        boolean result = true;

        for (int i = 0; i < numberCount; i++) {
            if (number[i] < '1' || number[i] > '9') {
                result = false;
                break;
            }
        }

        return result;
    }

    public static boolean isRomanian(char[] number, int numberCount) {
        boolean result = true;
        String validSymbols = "IVXLCDM";

        for (int i = 0; i < numberCount; i++) {
            if (! validSymbols.contains(String.valueOf(number[i]))) {
                result = false;
                break;
            }
        }

        return result;
    }
}
