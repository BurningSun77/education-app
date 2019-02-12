package com.example.wolframapitestapp;

import android.widget.Switch;

import java.util.Random;

public class MathGenerator {

    private enum operations{ add , subtract, multiple,divide}

    public static final int add=0;
    public static final int subtract=1;
    public static final int multiple=2;
    public static final int divide=3;

    //private static final long theseed=(long)Math.random();
    private Random random;
    private long theotherseed;

    private int number1;
    private int number2;
    private int number3;

    private int fakenumber1;
    private int fakenumber2;
    private int fakenumber3;
    private int operation;

    private boolean isseeded = false;


    public  MathGenerator(){
        random = new Random();
        //random.setSeed(theseed);

        number1 = random.nextInt(25) ;
        number2 = (random.nextInt(25)+1);

        operation = random.nextInt(4);

        problem();
    }

    public  MathGenerator(long seed){

        random = new Random(seed);
        theotherseed = seed;

        number1 = random.nextInt(25) ;
        number2 = (random.nextInt(25)+1);

        operation = random.nextInt(4);

        isseeded = true;

        problem();
    }

    public long getTheseed(){

        if(isseeded!=true){

            return theotherseed;
        } else {

            //return random
            return 10;
        }

    }

    public void problem() {

        switch (operation) {

            case add:

                number3 = number1 + number2;
                fakenumber1 = number1 + number2 + random.nextInt(10);
                fakenumber2 = number1 + number2 + random.nextInt(10);
                fakenumber3 = number1 + number2 - random.nextInt(10);
                break;
            case subtract:

                number3 = number1 - number2;
                fakenumber1 = (number1 - number2) + random.nextInt(10);
                fakenumber2 = (number1 - number2) + random.nextInt(10);
                fakenumber3 = (number1 - number2) - random.nextInt(10);
                break;
            case multiple:

                number3 = number1 * number2;
                fakenumber1 = (number1 * number2) + random.nextInt(10);
                fakenumber2 = (number1 * number2) + random.nextInt(10);
                fakenumber3 = (number1 * number2) + random.nextInt(10);
                break;
            case divide:

                number3 = number1 / number2;
                fakenumber1 = (number1 / number2) + random.nextInt(10);
                fakenumber2 = (number1 / number2) + random.nextInt(10);
                fakenumber3 = (number1 / number2) - random.nextInt(10);
                break;
        }

    }

    public int getNumber1() {

        return number1;
    }
    public int getNumber2() {

        return number2;
    }
    public int getanswer() {

        return number3;
    }
    public int getf1answer() {

        return fakenumber1;
    }
    public int getf2answer() {

        return fakenumber2;
    }
    public int getf3answer() {

        return fakenumber3;
    }
    public char getoperator() {

        switch (operation) {

            case add:

                return '+';
            case subtract:

                return '-';
            case multiple:

                return '*';
            case divide:

               return '/';
            default:

                return ' ';
        }
    }
}
