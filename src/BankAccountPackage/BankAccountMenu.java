package src.BankAccountPackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import src.UserPackage.User;
import src.UserPackage.UserService;
import src.utils.RandomStringGenerator;

import src.CardPackage.*;

public class BankAccountMenu {

	public static void bankAccountsOperationsMenu(String error, User currentUser) {
		System.out.println("Operatii bancare");
		System.out.println(error);
		System.out.println("1. Adauga cont bancar");
		System.out.println("2. Sterge cont bancar");
		System.out.println("3. Vizualizeaza conturi bancare");
		System.out.println("4. Adauga bani ");
		System.out.println("5. Retrage bani");
		System.out.println("6. Ataseaza card la un cont bancar");
		System.out.println("7. Back");
		System.out.printf("Introdu o comanda(de la 1 la 7): ");
	}

	public static void addBankAccount(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		boolean loop = true;

		String name;
		String currency;

		scanner.nextLine();
		while (loop) {
			try {
				System.out.println("Adauga cont bancar");
				System.out.println(error);
				System.out.printf("Alege numele contului bancar: ");
				name = scanner.nextLine();
				System.out.printf("Alege valuta: ");
				currency = scanner.nextLine();

				BankAccount ba = new BankAccount(0, name, RandomStringGenerator.generateRandomString(6), currency);

				// currentUser.addBank(ba);
				BankAccountService.insert(connection, currentUser, ba);

				loop = false;
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
	}

	public static void deleteBankAccount(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		boolean loop = true;
		int comanda;
		scanner.nextLine();
		while (loop) {
			try {
				System.out.println("Sterge un cont bancar");
				System.out.println(error);

				System.out.println("Lista cu conturile bancare");

				ArrayList<BankAccount> bankAccounts = UserService.getBankAccounts(connection, currentUser);

				if (bankAccounts == null || bankAccounts.size() == 0) {
					System.out.println("Nu ai conturi bancare pe care sa le stergi.");
					return;
				}

				for (int i = 0; i < bankAccounts.size(); ++i)
					System.out.println((i + 1) + "." + bankAccounts.get(i));

				System.out.printf("%d. Back\n", bankAccounts.size() + 1);

				System.out.printf("Alege un cont bancar intre 1 si %d:", bankAccounts.size());
				comanda = scanner.nextInt();

				if (comanda == bankAccounts.size() + 1)
					return;

				if (comanda < 1 || comanda > bankAccounts.size())
					throw new Exception(
							String.format("Trebuie sa introduci o valoare intre 1 si %d", bankAccounts.size()));

				// currentUser.deleteBank(bankAccounts.get(comanda - 1));
				BankAccountService.delete(connection, bankAccounts.get(comanda - 1));
				loop = false;
			} catch (InputMismatchException e) {
				error = "Trebuie sa introduci o valoare intre intervalele specificate.";
				scanner.nextLine();
			} catch (Exception e) {
				error = e.getMessage();

			}
		}

		return;
	}

	public static int viewBankAccounts(Connection connection, Scanner scanner, User currentUser) throws SQLException {
		System.out.println("Lista cu conturile bancare");

		ArrayList<BankAccount> bankAccounts = UserService.getBankAccounts(connection, currentUser);

		if (bankAccounts != null && bankAccounts.size() > 0) {
			for (int i = 0; i < bankAccounts.size(); ++i)
				System.out.println((i + 1) + "." + bankAccounts.get(i));
			System.out.printf("%d. Back\n", bankAccounts.size() + 1);
			return bankAccounts.size();
		} else {
			System.out.println("Nu ai niciun cont bancar.");
			System.out.printf("%d. Back\n", 1);
			return 0;
		}
	}

	public static void viewBankAccounts(ArrayList<BankAccount> bankAccounts) throws SQLException {
		System.out.println("Lista cu conturile bancare");

		if (bankAccounts != null && bankAccounts.size() > 0) {
			for (int i = 0; i < bankAccounts.size(); ++i)
				System.out.println((i + 1) + "." + bankAccounts.get(i));
			System.out.printf("%d. Back\n", bankAccounts.size() + 1);
			// return bankAccounts.size();
		} else {
			System.out.println("Nu ai niciun cont bancar.");
			System.out.printf("%d. Back\n", 1);
			// return 0;
		}
	}

	public static void addMoneyToBankAccount(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		int comanda;
		int suma;
		boolean loop = true;
		scanner.nextLine();
		while (loop) {
			try {
				// clearScreen();

				System.out.println("Adauga Suma");
				System.out.println(error);

				viewBankAccounts(connection, scanner, currentUser);
				System.out.println("Alege contul bancar in care vrei sa depui suma: ");
				comanda = scanner.nextInt();
				System.out.println("Introdu suma dorita: ");
				suma = scanner.nextInt();

				if (suma < 0)
					throw new Exception("Suma trebuie sa fie pozitiva");

				// currentUser.addMoneyToBank(comanda - 1, suma);
				ArrayList<BankAccount> bankAccounts = UserService.getBankAccounts(connection, currentUser);

				if (comanda == bankAccounts.size() + 1)
					return;

				if (comanda < 1 || comanda > bankAccounts.size())
					throw new Exception(String.format("Trebuie sa introduci o valoare intre intervalele 1 si %d",
							bankAccounts.size()));

				bankAccounts.get(comanda - 1).setBalance(bankAccounts.get(comanda - 1).getBalance() + suma);
				BankAccountService.update(connection, bankAccounts.get(comanda - 1));
				loop = false;
			} catch (InputMismatchException e) {
				error = "Trebuie sa introduci un numar intreg";
				scanner.nextLine();
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
	}

	public static void withdrawMoneyFromBankAccount(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		int comanda;
		int suma;
		boolean loop = true;
		scanner.nextLine();
		while (loop) {
			try {
				// clearScreen();

				System.out.println("Retrage Suma");
				System.out.println(error);

				viewBankAccounts(connection, scanner, currentUser);

				System.out.println("Alege contul bancar din care vrei sa retragi suma: ");
				comanda = scanner.nextInt();

				ArrayList<BankAccount> bankAccounts = UserService.getBankAccounts(connection, currentUser);

				if (comanda == bankAccounts.size() + 1)
					return;

				System.out.println("Introdu suma dorita de retras: ");
				suma = scanner.nextInt();

				if (suma < 0)
					throw new Exception("Suma trebuie sa fie pozitiva");

				BankAccount ba = bankAccounts.get(comanda - 1);

				if (BankAccountService.getCard(connection, ba) != null) {
					// System.out.println("Here");
					// currentUser.withDrawMoneyFromBank(comanda - 1, suma);

					if (ba.getBalance() - suma < 0)
						throw new Exception("Suma este mai mare decat balanta contului");

					ba.setBalance(ba.getBalance() - suma);

					BankAccountService.update(connection, ba);
				} else
					throw new Exception("Contul bancar selectat nu are un card atasat");
				loop = false;
			} catch (InputMismatchException e) {
				error = e.getMessage();
				scanner.nextLine();
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
	}

	public static void attachCardToBankAccount(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		int comanda1;
		int comanda2;
		boolean loop = true;
		scanner.nextLine();
		while (loop) {
			try {
				// clearScreen();

				System.out.println("Ataseaza card la un cont bancar");
				System.out.println(error);

				ArrayList<BankAccount> bankAccounts = UserService.getBankAccounts(connection, currentUser);
				ArrayList<Card> cards = UserService.getCards(connection, currentUser);

				ArrayList<BankAccount> listToConsider = new ArrayList<BankAccount>();

				for (BankAccount ba : bankAccounts)
					if (BankAccountService.getCard(connection, ba) == null)
						listToConsider.add(ba);

				bankAccounts = listToConsider;

				if (bankAccounts == null || bankAccounts.size() == 0) {
					System.out.println("Nu ai vreun cont bancar ca sa atasezi card-uri");
					return;
				}
				viewBankAccounts(bankAccounts);

				System.out.println("Alege contul bancar: ");
				comanda1 = scanner.nextInt();

				if (bankAccounts == null || bankAccounts.size() == 0)
					return;

				if (comanda1 == bankAccounts.size() + 1)
					return;

				if (cards == null) {
					System.out.println("Nu ai card-uri sa atasezi la vreun cont bancar");
					return;
				}

				CardMenu.viewCards(connection, scanner, currentUser);

				System.out.println("Alege card-ul care vrei sa-l atasezi: ");
				comanda2 = scanner.nextInt();

				if (comanda2 == cards.size() + 1)
					return;

				if (comanda1 <= 0 || comanda1 > bankAccounts.size())
					throw new Exception(
							String.format("Contul bancar trebuie sa fie intre 1 si %d", bankAccounts.size()));

				if (comanda2 <= 0 || comanda2 > cards.size())
					throw new Exception(
							String.format("Card-urile trebuie sa fie intre 1 si %d", cards.size()));

				bankAccounts.get(comanda1 - 1).setCard(cards.get(comanda2 - 1));
				currentUser.setBanks(bankAccounts);
				// BankAccountService.update(connection, bankAccounts.get(comanda1 - 1));
				CardService.update(connection, cards.get(comanda2 - 1), currentUser, bankAccounts.get(comanda1 - 1));
				loop = false;
			} catch (InputMismatchException e) {
				error = e.getMessage();
				scanner.nextLine();
			} catch (Exception e) {
				error = e.getMessage();
			}
		}
	}

	public static boolean bankAccountsOperations(Connection connection, Scanner scanner, User currentUser)
			throws SQLException {
		int comanda = 0;
		String error = "";
		boolean loop = true;
		ArrayList<BankAccount> bankAccounts = UserService.getBankAccounts(connection, currentUser);

		while (loop) {
			try {
				bankAccountsOperationsMenu(error, currentUser);
				comanda = scanner.nextInt();

				if (comanda == 7)
					return false;

				if (comanda < 1 || comanda > 7)
					throw new InputMismatchException();
				loop = false;

			} catch (InputMismatchException e) {
				error = "Input-ul trebuie sa fie un numar intreg si intre valorile specificate.";
				scanner.nextLine();
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		switch (comanda) {
			case 1: {
				addBankAccount(connection, scanner, currentUser);
				return true;
			}

			case 2: {
				deleteBankAccount(connection, scanner, currentUser);
				return true;
			}

			case 3: {
				// viewBankAccounts(connection, scanner, currentUser);
				// System.out.println("Press 1 or any key to continue...");
				// scanner.nextLine();

				System.out.println("Lista cu conturile bancare");

				// ArrayList<BankAccount> bankAccounts = UserService.getBankAccounts(connection,
				// currentUser);

				if (bankAccounts != null && bankAccounts.size() > 0) {
					for (int i = 0; i < bankAccounts.size(); ++i)
						System.out.println((i + 1) + "." + bankAccounts.get(i));
					// System.out.printf("%d. Back\n", bankAccounts.size() + 1);

				} else {
					System.out.println("Nu ai niciun cont bancar.");
				}

				return true;
			}

			case 4: {
				addMoneyToBankAccount(connection, scanner, currentUser);
				return true;
			}

			case 5: {
				withdrawMoneyFromBankAccount(connection, scanner, currentUser);
				return true;
			}

			case 6: {
				attachCardToBankAccount(connection, scanner, currentUser);
				return true;
			}

			case 7: {
				// loop = false;
				break;
			}
			default: {
				return true;
			}
		}

		return false;
	}
}
