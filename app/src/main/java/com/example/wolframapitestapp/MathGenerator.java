package com.example.wolframapitestapp;

import android.widget.Switch;

import java.util.Random;

public class MathGenerator {

    public enum operations{ add , subtract, multiple,divide}

    public static final int add=0;
    public static final int subtract=1;
    public static final int multiple=2;
    public static final int divide=3;

    private static final long theseed=(long)Math.random();
    private Random random;

    private double number1;
    private double number2;
    private double number3;

    private double fakenumber1;
    private double fakenumber2;
    private double fakenumber3;
    private int operation;

    private boolean isseeded = false;


    public  MathGenerator(){
        random.setSeed(theseed);

        number1 = random.nextInt() % 25;
        number2 = random.nextInt() % 25;

        operation = random.nextInt() % 4;

        problem();
    }

    public  MathGenerator(long seed){
        random = new Random(seed);

        number1 = random.nextInt() % 25;
        number2 = random.nextInt() % 25;

        operation = random.nextInt() % 4;

        isseeded = true;

        problem();
    }

    public void problem(){


        switch (operation) {
            case add:
                number3 = number1 + number2;
                fakenumber1 = number1 + number2 + random.nextInt()%10;
                fakenumber2 = number1 + number2 + random.nextInt()%10;
                fakenumber3 = number1 + number2 + random.nextInt()%10;
            case subtract:
                number3 = number1 - number2;
                fakenumber1 = (number1 - number2) + random.nextInt()%10;
                fakenumber2 = (number1 - number2) + random.nextInt()%10;
                fakenumber3 = (number1 - number2) + random.nextInt()%10;
            case multiple:
                number3 = number1 * number2;
                fakenumber1 = (number1 * number2) + random.nextInt()%10;
                fakenumber2 = (number1 * number2) + random.nextInt()%10;
                fakenumber3 = (number1 * number2) + random.nextInt()%10;
            case divide:
                number3 = number1 / number2;
                fakenumber1 = (number1 + number2) + random.nextInt()%10;
                fakenumber2 = (number1 + number2) + random.nextInt()%10;
                fakenumber3 = (number1 + number2) + random.nextInt()%10;


        }




    }
}
