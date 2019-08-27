package pl.coderslab.programs.admin;

import pl.coderslab.dao.UserGroupDao;
import pl.coderslab.model.UserGroup;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GroupsAdministration {
    public static void main(String[] args) {
        UserGroupDao userGroupDao = new UserGroupDao();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            printUserGroups(userGroupDao);
            choice = getChoice(scanner);
            switch (choice) {
                case 1:
                    add(userGroupDao, scanner);
                    break;
                case 2:
                    edit(userGroupDao, scanner);
                    break;
                case 3:
                    delete(userGroupDao, scanner);
                    break;
            }
        } while (choice != 4);
        scanner.close();
    }

    private static void add(UserGroupDao userGroupDao, Scanner scanner) {
        String name;
        System.out.print("insert user group name: ");
        name = scanner.nextLine();
        userGroupDao.create(new UserGroup(name));
    }

    private static void edit(UserGroupDao userGroupDao, Scanner scanner) {
        UserGroup userGroup = new UserGroup();

        while (true) {
            try {
                System.out.print("insert id: ");
                userGroup.setId(scanner.nextInt());
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input data is incorrect");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        System.out.print("insert userGroup name: ");
        userGroup.setName(scanner.nextLine());

        userGroupDao.update(userGroup);
    }

    private static void delete(UserGroupDao userGroupDao, Scanner scanner) {
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
        userGroupDao.delete(id);
    }

    private static void printUserGroups(UserGroupDao userGroupDao) {
        List<UserGroup> userGroupList = userGroupDao.findAll();
        System.out.printf("%4s | %20s |%n", "ID", "USER GROUP NAME");
        for (UserGroup userGroup : userGroupList) {
            System.out.printf("%4d | %20s |%n", userGroup.getId(), userGroup.getName());
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
