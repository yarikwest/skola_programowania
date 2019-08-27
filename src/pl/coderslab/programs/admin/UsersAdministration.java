package pl.coderslab.programs.admin;

import pl.coderslab.dao.UserDao;
import pl.coderslab.dao.UserGroupDao;
import pl.coderslab.model.User;
import pl.coderslab.utils.PasswordUtil;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UsersAdministration {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            printUsers(userDao);
            choice = getChoice(scanner);
            switch (choice) {
                case 1:
                    add(userDao, scanner);
                    break;
                case 2:
                    edit(userDao, scanner);
                    break;
                case 3:
                    delete(userDao, scanner);
                    break;
            }
        } while (choice != 4);
        scanner.close();
    }

    private static void add(UserDao userDao, Scanner scanner) {
        String userName, email, password;
        int userGroupId;

        System.out.print("insert user name: ");
        userName = scanner.nextLine();
        System.out.print("insert email: ");
        email = scanner.nextLine();
        System.out.print("insert password: ");
        password = scanner.nextLine();
        while (true) {
            try {
                System.out.print("insert user group id: ");
                userGroupId = scanner.nextInt();
                if (new UserGroupDao().read(userGroupId) != null)
                    break;
                else
                    System.out.println("Input group doesn't exist");
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.next();
            }
        }
        userDao.create(new User(userName, email, password, userGroupId));
    }

    private static void edit(UserDao userDao, Scanner scanner) {
        User user = new User();

        while (true) {
            try {
                System.out.print("insert id: ");
                user.setId(scanner.nextInt());
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        System.out.print("insert user name: ");
        user.setUserName(scanner.nextLine());
        System.out.print("insert email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("insert password: ");
        user.setPassword(PasswordUtil.createHash(scanner.nextLine()));
        while (true) {
            try {
                System.out.print("insert user group id: ");
                user.setGroupId(scanner.nextInt());
                if (new UserGroupDao().read(user.getGroupId()) != null)
                    break;
                else
                    System.out.println("Input group doesn't exist");
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.nextLine();
            }
        }
        userDao.update(user);
    }

    private static void delete(UserDao userDao, Scanner scanner) {
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
        userDao.delete(id);
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
