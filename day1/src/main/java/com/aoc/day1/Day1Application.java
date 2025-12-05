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
    public void run(String... args) throws Exception {
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

                Dialer dialer = new Dialer(currentPosition, direction, moves);
                dialer.dial();

                currentPosition = dialer.newPosition;
                rotatedOverZero = rotatedOverZero + dialer.clickedOverZero + dialer.fullRotations;

                if(currentPosition == 0) {
                    landedOnZero++;
                }
            }

            System.out.println("Landed on zero " + landedOnZero + " times, rotated over zero " + rotatedOverZero + " times. Total zeros " + (landedOnZero + rotatedOverZero));
        } catch (IOException | NullPointerException e) {
            System.err.println("Error reading resource file: " + e.getMessage());
        }
    }

    private static class Dialer {
        final int initialPosition;
        final char direction;
        final int moves;

        int fullRotations;
        int turnsWithoutFullRotations;
        int clickedOverZero;
        int newPosition;

        Dialer(int initialPosition, char direction, int moves) {
            this.initialPosition = initialPosition;
            this.direction = direction;
            this.moves = moves;
        }

        public void dial() throws Exception {
            this.fullRotations = moves / DIAL_NUMBER_OF_POSITIONS;
            this.turnsWithoutFullRotations = moves % DIAL_NUMBER_OF_POSITIONS;
            this.clickedOverZero = 0;

            switch (direction) {
                case 'L':
                    this.dialLeft();
                    break;
                case 'R':
                    this.dialRight();
                    break;
                default:
                    throw new Exception("Unknown direction");
            }

            logMove();
        }

        private void dialLeft() {
            this.newPosition = initialPosition - turnsWithoutFullRotations;

            if(this.newPosition < DIAL_MIN) {
                this.newPosition = this.newPosition + DIAL_MAX + 1;
            }

            if(this.initialPosition != 0 
                && this.newPosition != 0 
                && this.newPosition > this.initialPosition) {
                
                this.clickedOverZero++;
            }
        }

        private void dialRight() {
            this.newPosition = initialPosition + turnsWithoutFullRotations;

            if(this.newPosition > DIAL_MAX) {
                this.newPosition = this.newPosition - DIAL_MAX - 1;
            }

            if(this.initialPosition != 0 
                && this.newPosition != 0
                && this.newPosition < this.initialPosition) {
                
                this.clickedOverZero++;
            }
        }

        private void logMove() {
            System.out.println("The dial started at position " + this.initialPosition);
            System.out.println("The dial is rotated " + this.direction + " " + this.moves);
            System.out.println("The dial is pointing at " + this.newPosition);
            System.out.println("The dial clicked over zero " + this.clickedOverZero + " time(s)");
            System.out.println("The dial fully rotated " + this.fullRotations + " time(s)");
            System.out.println("***END OF MOVE***");
        }
    }
}
