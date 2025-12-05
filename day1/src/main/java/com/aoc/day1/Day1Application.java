package com.aoc.day1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@SpringBootApplication
public class Day1Application implements CommandLineRunner {
    static int DIAL_MIN = 0;
    static int DIAL_MAX = 99;
    static int DIAL_NUMBER_OF_POSITIONS = 100;

    public static void main(String[] args) {
        SpringApplication.run(Day1Application.class, args);
    }

    @Override
    public void run(String... args) {
        try (InputStream is = Day1Application.class.getResourceAsStream("/input");
             Scanner scanner = new Scanner(is, StandardCharsets.UTF_8)) {
            int landedOnZero = 0;
            int rotatedOverZero  = 0;

            int currentPosition = Integer.parseInt(scanner.nextLine());
            System.out.println("The dial starts by pointing at " + currentPosition);

            if(currentPosition == 0) {
                landedOnZero++;
            }

            while (scanner.hasNextLine()) {
                String lineVal = scanner.nextLine();
                int moves = Integer.parseInt(lineVal.substring(1));
                char direction = lineVal.charAt(0);

                DialResult dialResult = direction == 'L' ? dialLeft(currentPosition, moves) : dialRight(currentPosition, moves);

                currentPosition = dialResult.newPosition;
                rotatedOverZero = rotatedOverZero + dialResult.clickedOverZero + dialResult.fullRotations;

                if(currentPosition == 0) {
                    landedOnZero++;
                }
            }

            System.out.println("Landed on zero " + landedOnZero + " times, rotated over zero " + rotatedOverZero + " times. Total zeros " + (landedOnZero + rotatedOverZero));
        } catch (IOException | NullPointerException e) {
            System.err.println("Error reading resource file: " + e.getMessage());
        }
    }

    private static DialResult dialRight(int initialPosition, int moves) {
        int fullRotations = moves / DIAL_NUMBER_OF_POSITIONS;
        int turnsWithoutFullRotations = moves % DIAL_NUMBER_OF_POSITIONS;

        int newPosition = initialPosition + turnsWithoutFullRotations;
        int clickedOverZero = 0;

        if(newPosition > DIAL_MAX) {
            newPosition = newPosition - DIAL_MAX - 1;
        }

        if(initialPosition != 0 && newPosition != 0 && newPosition < initialPosition) {
            clickedOverZero++;
        }

        DialResult dialResult = new DialResult(newPosition, clickedOverZero, fullRotations);
        logMove(initialPosition, "R", moves, dialResult.newPosition, dialResult.clickedOverZero, fullRotations);

        return dialResult;
    }

    private static DialResult dialLeft(int initialPosition, int moves) {
        int fullRotations = moves / DIAL_NUMBER_OF_POSITIONS;
        int turnsWithoutFullRotations = moves % DIAL_NUMBER_OF_POSITIONS;

        int newPosition = initialPosition - turnsWithoutFullRotations;
        int clickedOverZero = 0;

        if(newPosition < DIAL_MIN) {
            newPosition = newPosition + DIAL_MAX + 1;
        }

        if(initialPosition != 0 && newPosition != 0 && newPosition > initialPosition) {
            clickedOverZero++;
        }

        DialResult dialResult = new DialResult(newPosition, clickedOverZero, fullRotations);
        logMove(initialPosition, "L", moves, dialResult.newPosition, dialResult.clickedOverZero, fullRotations);
        
        return dialResult;
    }

    private static void logMove(int initialPosition, String direction, int moves, int newPostion, int clickedOverZero, int fullRotations) {
        System.out.println("The dial started at position " + initialPosition);
        System.out.println("The dial is rotated " + direction + " " + moves);
        System.out.println("The dial is pointing at " + newPostion);
        System.out.println("The dial clicked over zero " + clickedOverZero + " time(s)");
        System.out.println("The dial fully rotated " + fullRotations + " time(s)");
        System.out.println("***END OF MOVE***");
    }

    private static class DialResult {
        int newPosition;
        int clickedOverZero;
        int fullRotations;

        DialResult(int newPosition, int clickedOverZero, int fullRotations) {
            this.newPosition = newPosition;
            this.clickedOverZero = clickedOverZero;
            this.fullRotations = fullRotations;
        }
    }
}
