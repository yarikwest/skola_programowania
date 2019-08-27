package pl.coderslab.programs.admin;

import pl.coderslab.dao.ExerciseDao;
import pl.coderslab.model.Exercise;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ExercisesAdministration {
    public static void main(String[] args) {
        ExerciseDao exerciseDao = new ExerciseDao();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            printUsers(exerciseDao);
            choice = getChoice(scanner);
            switch (choice) {
                case 1:
                    add(exerciseDao, scanner);
                    break;
                case 2:
                    edit(exerciseDao, scanner);
                    break;
                case 3:
                    delete(exerciseDao, scanner);
                    break;
            }
        } while (choice != 4);
        scanner.close();
    }

    private static void add(ExerciseDao exerciseDao, Scanner scanner) {
        String title, description;

        System.out.print("insert title: ");
        title = scanner.nextLine();
        System.out.print("insert description: ");
        description = scanner.nextLine();

        exerciseDao.create(new Exercise(title, description));
    }

    private static void edit(ExerciseDao exerciseDao, Scanner scanner) {
        Exercise exercise = new Exercise();

        while (true) {
            try {
                System.out.print("insert id: ");
                exercise.setId(scanner.nextInt());
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        System.out.print("insert title: ");
        exercise.setTitle(scanner.nextLine());
        System.out.print("insert description: ");
        exercise.setDescription(scanner.nextLine());

        exerciseDao.update(exercise);
    }

    private static void delete(ExerciseDao exerciseDao, Scanner scanner) {
        int id;

        while (true) {
            try {
                System.out.print("insert id: ");
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.nextLine();
            }
        }
        exerciseDao.delete(id);
    }

    private static void printUsers(ExerciseDao exerciseDao) {
        List<Exercise> exerciseList = exerciseDao.findAll();
        System.out.printf("%4s | %20s | %30s |%n", "ID", "TITLE", "DESCRIPTION");
        for (Exercise exercise : exerciseList) {
            System.out.printf("%4d | %20s | %30s |%n", exercise.getId(), exercise.getTitle(), exercise.getDescription());
        }
    }

    private static int getChoice(Scanner scanner) {
        int choice = 0;

        while (true) {
            try {
                System.out.println("Choose one of variants");
                System.out.println("\t1.add \t2.edit \t3.delete \t4.quit");
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= 4) {
                    scanner.nextLine();
                    return choice;
                } else
                    System.out.println("Input data is incorrect");
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.nextLine();
            }
        }
    }
}
