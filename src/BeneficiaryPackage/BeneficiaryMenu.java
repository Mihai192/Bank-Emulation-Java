package src.BeneficiaryPackage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import src.UserPackage.User;
import src.UserPackage.UserService;

public class BeneficiaryMenu {
	public static void addBeneficiaryUser(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		boolean loop = true;

		String beneficiary = "";

		scanner.nextLine();
		while (loop) {
			try {
				System.out.println("Adauga beneficiar");
				System.out.println(error);
				System.out.printf("Scrie email-ul beneficiarului: ");
				beneficiary = scanner.nextLine();

				if (beneficiary.equals(currentUser.getEmail())) {
					System.out.println("Nu te poti adauga pe tine insuti.");
				} else {
					ArrayList<User> users = UserService.getUsers(connection);

					for (User user : users)
						if (user.getEmail().equals(beneficiary)) {
							UserService.insertBeneficiary(connection, currentUser, user);
							return;
						}
				}

			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
	}

	public static void deleteBeneficiaryUser(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		boolean loop = true;

		String beneficiary = "";

		scanner.nextLine();
		while (loop) {
			try {
				System.out.println("Sterge beneficiar");
				System.out.println(error);
				System.out.printf("Scrie email-ul beneficiarului: ");
				beneficiary = scanner.nextLine();

				if (beneficiary.equals(currentUser.getEmail())) {
					System.out.println("Nu te poti sterge pe tine insuti.");
				} else {
					ArrayList<User> users = UserService.getUsers(connection);

					for (User user : users)
						if (user.getEmail().equals(beneficiary)) {
							UserService.deleteBeneficiary(connection, currentUser, user);
							return;
						}
				}

			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
	}

	public static int viewBeneficiaries(Connection connection, Scanner scanner, User currentUser) {
		try {
			ArrayList<User> users = UserService.getBeneficiaries(connection, currentUser);

			for (int i = 0; i < users.size(); ++i) {
				System.out.printf("%d. %s\n", i + 1, users.get(i));
			}
			if (users != null)
				return users.size();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// System.out.println("A aparut o exceptie");
		}
		return 0;
	}

	public static void ManageBeneficiaryOperationsMenu(String error, User currentUser) {
		System.out.println("Manage beneficiaries");
		System.out.println("1. Add beneficiary");
		System.out.println("2. Remove beneficiary");
		System.out.println("3. View Beneficiary");
		System.out.println("4. Back");
		System.out.printf("Enter your command(1-4): ");
	}

	public static boolean ManageBeneficiaryOperations(Connection connection, Scanner scanner, User currentUser) {
		int comanda = 0;
		String error = "";
		boolean loop = true;

		while (loop) {
			try {
				ManageBeneficiaryOperationsMenu(error, currentUser);
				comanda = scanner.nextInt();

				if (comanda < 1 || comanda > 4)
					throw new InputMismatchException();

				if (comanda == 4)
					return false;

				loop = false;
			} catch (InputMismatchException e) {
				error = "Input-ul trebuie sa fie un numar intreg intre 1 si 4.";
				scanner.nextLine();
			} catch (Exception e) {
				error = e.getMessage();

			}
		}

		switch (comanda) {
			case 1: {
				addBeneficiaryUser(connection, scanner, currentUser);
				System.out.println("Beneficiar adaugat.");
				return true;
			}

			case 2: {
				deleteBeneficiaryUser(connection, scanner, currentUser);
				System.out.println("Beneficiar sters.");
				return true;
			}

			case 3: {

				viewBeneficiaries(connection, scanner, currentUser);

				return true;
			}

			case 4: {
				loop = false;
			}
		}

		return false;
	}

}
