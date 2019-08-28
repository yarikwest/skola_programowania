package pl.coderslab.programs.user;

import pl.coderslab.dao.ExerciseDao;
import pl.coderslab.dao.SolutionDao;
import pl.coderslab.dao.UserDao;
import pl.coderslab.model.Exercise;
import pl.coderslab.model.Solution;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserProgram {

    public static void main(String[] args) {
        int userId = 0, choice;
        SolutionDao solutionDao = new SolutionDao();
        Scanner scanner = new Scanner(System.in);

        try {
            if (args.length == 1) {
                userId = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Program excepts only one argument and only number");
        }

        checkUserExist(userId);

        do {
            choice = getChoice(scanner);
            switch (choice) {
                case 1:
                    add(solutionDao, scanner, userId);
                    break;
                case 2:
                    view(solutionDao, userId);
                    break;
            }
        } while (choice != 3);
        scanner.close();
    }

    private static void add(SolutionDao solutionDao, Scanner scanner, int userId) {
        ExerciseDao exerciseDao = new ExerciseDao();
        Solution solution = new Solution();
        List<Integer> exerciseIdList;

        exerciseIdList = printExercises(exerciseDao, userId);
        while (true) {
            try {
                int id;
                System.out.print("insert exercise id: ");
                id = scanner.nextInt();
                if (exerciseIdList.indexOf(id) != -1) {
                    solution.setExerciseId(id);
                    scanner.nextLine();
                    break;
                }
                System.out.println("You can add solution only for given exercises");
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.nextLine();
            }
        }
        System.out.print("insert description: ");
        solution.setDescription(scanner.nextLine());
        solution.setCreated(new Timestamp(System.currentTimeMillis()));
        solution.setUsersId(userId);

        solutionDao.create(solution);
    }

    private static void view(SolutionDao solutionDao, int userId) {
        printSolution(solutionDao.findAllByUserId(userId));
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

    private static List<Integer> printExercises(ExerciseDao exerciseDao, int userId) {
        List<Exercise> exerciseList = exerciseDao.findAllWhereUserNotHaveSolution(userId);
        List<Integer> exerciseIdList = new ArrayList<>();
        System.out.println("------------------");
        System.out.println("TABLE OF EXERCISES");
        System.out.println("------------------");
        System.out.printf("%4s | %20s | %30s |%n", "ID", "TITLE", "DESCRIPTION");
        for (Exercise exercise : exerciseList) {
            System.out.printf("%4d | %20s | %30s |%n", exercise.getId(), exercise.getTitle(), exercise.getDescription());
            exerciseIdList.add(exercise.getId());
        }
        return exerciseIdList;
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

    private static void checkUserExist(int userId){
        UserDao userDao = new UserDao();
        if (userDao.read(userId) == null) {
            System.out.println("Input user doesn't exist");
            System.exit(0);
        }
    }
}
