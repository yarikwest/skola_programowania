package pl.coderslab.programs.admin;

import pl.coderslab.dao.ExerciseDao;
import pl.coderslab.dao.SolutionDao;
import pl.coderslab.dao.UserDao;
import pl.coderslab.model.Exercise;
import pl.coderslab.model.Solution;
import pl.coderslab.model.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SolutionsAdministration {
    public static void main(String[] args) {
        SolutionDao solutionDao = new SolutionDao();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            choice = getChoice(scanner);
            switch (choice) {
                case 1:
                    add(solutionDao, scanner);
                    break;
                case 2:
                    view(solutionDao, scanner);
                    break;
            }
        } while (choice != 3);
        scanner.close();
    }

    private static void add(SolutionDao solutionDao, Scanner scanner) {
        UserDao userDao = new UserDao();
        ExerciseDao exerciseDao = new ExerciseDao();
        Solution solution = new Solution();

        printUsers(userDao);
        while (true) {
            try {
                System.out.print("insert user id: ");
                solution.setUsersId(scanner.nextInt());
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.nextLine();
            }
        }

        printExercises(exerciseDao);
        while (true) {
            try {
                System.out.print("insert exercise id: ");
                solution.setExerciseId(scanner.nextInt());
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.nextLine();
            }
        }

        solution.setCreated(new Timestamp(System.currentTimeMillis()));
        solutionDao.create(solution);
    }

    private static void view(SolutionDao solutionDao, Scanner scanner) {
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
        printSolution(solutionDao.findAllByUserId(id));
    }

    private static void printSolution(List<Solution> solutionList) {
        System.out.println("------------------");
        System.out.println("TABLE OF SOLUTIONS");
        System.out.println("------------------");
        System.out.printf("%4s | %19s | %19s | %30s | %11s | %7s |%n", "ID", "CREATED", "UPDATED", "DESCRIPTION", "EXERCISE ID", "USER ID");
        for (Solution solution : solutionList) {
            String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(solution.getCreated());
            String updateDate = null;
            if (solution.getUpdated() != null)
                updateDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(solution.getUpdated());

            System.out.printf("%4d | %19s | %19s | %30s | %11d | %7d |%n", solution.getId(), createDate, updateDate,
                    solution.getDescription(), solution.getExerciseId(), solution.getUsersId());
        }
    }

    private static void printUsers(UserDao userDao) {
        User[] userList = userDao.findAll();
        System.out.println("--------------");
        System.out.println("TABLE OF USERS");
        System.out.println("--------------");
        System.out.printf("%4s | %20s | %30s | %60s | %8s |%n", "ID", "USER NAME", "EMAIL", "PASSWORD", "GROUP ID");
        for (User user : userList) {
            System.out.printf("%4d | %20s | %30s | %60s | %8d |%n", user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getGroupId());
        }
    }

    private static void printExercises(ExerciseDao exerciseDao) {
        List<Exercise> exerciseList = exerciseDao.findAll();
        System.out.println("------------------");
        System.out.println("TABLE OF EXERCISES");
        System.out.println("------------------");
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
                System.out.println("\t1.add \t2.view \t3.quit");
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= 3) {
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
