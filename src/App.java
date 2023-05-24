
package src;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Random;

import java.sql.*;

import src.BankAccountPackage.BankAccount;
import src.BankAccountPackage.BankAccountService;
import src.CardPackage.Card;
import src.CardPackage.CardService;
import src.Transfer.Transfer;
import src.Transfer.TransferService;
import src.UserPackage.User;
import src.UserPackage.UserService;

public class App {

	public static void clearScreen() {
		// System.out.print("\033[H\033[2J");
		// System.out.flush();
	}

	public static String generateRandomString(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		String characters = "abcdefghjklmnoprstzABCDEFGHJKLMNOPRSTZ123456789";

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			sb.append(randomChar);
		}

		return sb.toString();
	}

	public static void clearBuffer(Scanner scanner) {
		while (scanner.hasNextLine()) {
			scanner.nextLine();
		}
	}

	public static User initialMenu(Connection connection, Scanner scanner) {
		String error = "";
		int command = 0;
		// Scanner scanner = new Scanner(System.in);
		boolean loop = true;
		// if (scanner.hasNext())
		// scanner.nextLine();

		while (loop) {
			try {

				System.out.println("Bank App");

				System.out.println(error);

				System.out.println("1. Log in");
				System.out.println("2. Sign in");
				System.out.println("3. Forgot password");
				System.out.println("4. Exit");

				System.out.printf("Enter your command(1/2/3/4): ");
				command = scanner.nextInt();

				if (command < 1 || command > 4)
					throw new Exception("Ai introdus o valoarea gresita. Trebuie sa introduci un numar intre 1 si 4");

				loop = false;
			} catch (InputMismatchException e) {
				error = "Ai introdus o valoare gresita. Trebuie sa introduci un numar intre 1 si 4.";
				scanner.nextLine();
			} catch (Exception e) {
				// System.out.println(e);
				error = e.getMessage();

			}
		}

		switch (command) {
			case 1: {
				return login(connection, scanner);
			}
			case 2: {
				createAccount(connection, scanner);

				break;
			}
			// return initialMenu(connection);
			case 3: {
				changePassword(connection, scanner);

				break;
			}
			// return initialMenu(connection);

			case 4: {
				// scanner.close();
				System.exit(0);
				break;
			}
		}

		// scanner.close();
		return null;
	}

	public static void createAccount(Connection connection, Scanner scanner) {
		String error = "";
		String name = "";
		String pass = "";
		String email = "";
		String cnp = "";
		String adresa = "";
		// Scanner scanner = new Scanner(System.in);
		int role = 0;
		boolean loop = true;
		scanner.nextLine();
		while (loop) {
			try {
				// clearScreen();

				System.out.println("Sign up");

				System.out.println(error);

				System.out.printf("nume: ");
				name = scanner.nextLine();

				System.out.printf("email: ");
				email = scanner.nextLine();

				System.out.printf("pass: ");
				pass = scanner.nextLine();

				System.out.printf("cnp: ");
				cnp = scanner.nextLine();

				System.out.printf("adresa: ");
				adresa = scanner.nextLine();

				if (name.length() > 60)
					throw new Exception("Numele trebuie sa aiba o lungime mai mica de 60 de caractere");

				if (email.length() > 255)
					throw new Exception("Email-ul trebuie sa aiba mai putin de 255 de caractere");

				if (pass.length() > 60)
					throw new Exception("Parola trebuie sa aiba mai putin de 60 de caractere");

				if (cnp.length() > 16)
					throw new Exception("CNP trebuie sa aiba 16 caractere");

				if (adresa.length() > 255)
					throw new Exception("Adresa trebuie sa aiba mai putin de 255 de caractere");

				User new_user = new User(name, email, role, pass, cnp, adresa);

				// System.out.println(new_user);
				// System.out.println("chiar aici ???");
				if (UserService.existsNameEmail(connection, new_user))
					throw new Exception("Nume sau email deja folosit.");

				// System.out.println("inainte de eroare");
				UserService.insert(connection, new_user);

				// System.out.println("aici apare eroarea");
				// new_user = UserService.select(connection, "email",
				// new_user.getEmail()).get(0);
				loop = false;
			} catch (Exception e) {

				error = e.getMessage();
			}
		}
		// scanner.close();
		return;
	}

	public static void changePassword(Connection connection, Scanner scanner, User currentUser) {
		String error = "";

		String pass = "";
		String repeat_pass = "";
		String new_pass = "";
		String repeat_new_pass = "";

		boolean loop = true;
		scanner.nextLine();
		while (loop) {
			try {

				System.out.println("Schimbare parola");
				System.out.println(error);

				System.out.printf("Introdu parola contului: ");
				pass = scanner.nextLine();

				System.out.printf("Repeta parola: ");
				repeat_pass = scanner.nextLine();

				System.out.printf("Introdu noua parola: ");
				new_pass = scanner.nextLine();

				System.out.printf("Repeta noua parola: ");
				repeat_new_pass = scanner.nextLine();

				// ArrayList<User> users = UserService.getUsers(connection);

				if (!pass.equals(repeat_pass))
					throw new Exception("Parolele nu coincid.");

				if (!new_pass.equals(repeat_new_pass))
					throw new Exception("Parolele noi nu coincid");

				if (repeat_pass.length() > 60)
					throw new Exception("Parola noua trebuie sa fie mai scurta de 60 de caractere");

				if (!currentUser.getPass().equals(pass))
					throw new Exception("Nu ai introdus parola corecta a contului");

				currentUser.setPass(new_pass);

				UserService.update(connection, currentUser);
				loop = false;
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
	}

	public static void changePassword(Connection connection, Scanner scanner) {
		String error = "";
		String email = "";
		String pass = "";
		String repeat_pass = "";

		boolean loop = true;
		scanner.nextLine();
		while (loop) {
			try {
				// clearScreen();

				System.out.println("Schimbare parola");
				System.out.println(error);

				System.out.printf("Input email: ");

				email = scanner.nextLine();

				User user = null;
				ArrayList<User> users = UserService.select(connection, "email", email);
				if (users != null && users.size() > 0)
					user = users.get(0);
				else
					throw new Exception("Nu exista niciun user cu acest email");

				System.out.printf("Introdu noua parola: ");
				pass = scanner.nextLine();

				System.out.printf("Repeta parola: ");
				repeat_pass = scanner.nextLine();

				if (!pass.equals(repeat_pass))
					throw new Exception("Parolele nu coincid.");

				user.setPass(pass);

				UserService.update(connection, user);
				loop = false;
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
	}

	public static void changeAdress(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		String adresa = "";

		boolean loop = true;
		scanner.nextLine();
		while (loop) {
			try {
				// clearScreen();

				System.out.println("Schimbare adresa");
				System.out.println(error);

				System.out.printf("Adresa curenta: %s\n", currentUser.getAdresa());
				System.out.printf("Noua adresa: ");

				adresa = scanner.nextLine();

				currentUser.setAdresa(adresa);
				UserService.update(connection, currentUser);
				loop = false;
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
	}

	public static User login(Connection connection, Scanner scanner) {
		String error = "";
		String email = "";
		String pass = "";

		boolean loop = true;
		User u = null;
		scanner.nextLine();
		while (loop) {
			try {
				clearScreen();

				System.out.println("Login");

				System.out.println(error);

				System.out.printf("email: ");
				email = scanner.nextLine();

				System.out.printf("pass: ");
				pass = scanner.nextLine();

				if (UserService.existsEmailPass(connection, email, pass)) {
					u = UserService.select(connection, "email", email).get(0);
					loop = false;
				} else
					throw new Exception("Email-ul sau parola sunt gresite!");
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return u;
	}

	public static void displayUserMenu(String error, User currentUser) {
		// clearScreen();

		System.out.println("Administrare cont");

		System.out.println(error);
		System.out.println(currentUser);

		System.out.println("1. Detalii user");
		System.out.println("2. Schimba parola");
		System.out.println("3. Schimba adresa");
		System.out.println("4. Sterge cont");
		System.out.println("5. History");
		System.out.println("6. Operatii conturi bancare");
		System.out.println("7. Operatii card-uri");
		System.out.println("8. Manageriaza lista prieteni pentru tranzactii rapide");
		System.out.println("9. Transfer");
		System.out.println("10. Logout");
		System.out.println("11. Exit");
		System.out.printf("Introdu o comanda(de la 1 la 11): ");

	}

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

	public static void cardOperationsMenu(String error, User currentUser) {
		System.out.println("Operatii card-uri");
		System.out.println(error);
		System.out.println("1. Adauga card");
		System.out.println("2. Sterge card");
		System.out.println("3. Vizualizeaza card-uri");
		System.out.println("4. Back");
		System.out.println("Introdu o comanda(de la 1 la 4): ");
	}

	public static void promptToDelete(Connection connection, Scanner scanner, User currentUser) {
		String error = "";

		String pass = "";
		String repeat_pass = "";

		boolean loop = true;
		scanner.nextLine();
		while (loop) {
			try {

				System.out.println("Stergere cont");
				System.out.println(error);

				System.out.printf("Introdu parola contului: ");
				pass = scanner.nextLine();

				System.out.printf("Repeta parola: ");
				repeat_pass = scanner.nextLine();

				if (!pass.equals(repeat_pass))
					throw new Exception("Parolele nu coincid.");

				if (!currentUser.getPass().equals(pass))
					throw new Exception("Nu ai introdus parola corecta a contului");

				loop = false;
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
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

				BankAccount ba = new BankAccount(0, name, generateRandomString(6), currency);

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

				viewCards(connection, scanner, currentUser);

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

	public static void addCard(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		boolean loop = true;

		String cardNumber;
		int cvc;

		// scanner.nextLine();
		while (loop) {
			try {
				System.out.println("Adauga card");
				System.out.println(error);
				System.out.printf("Scrie card numar: ");

				cardNumber = scanner.nextLine();
				scanner.nextLine();

				System.out.printf("Scrie cvc: ");
				cvc = scanner.nextInt();

				ArrayList<Card> cards = new ArrayList<Card>();

				Card new_card = new Card(cardNumber, cvc);

				for (Card c : cards)
					if (c.equals(new_card))
						throw new Exception("acest card este deja folosit");

				CardService.insert(connection, currentUser, new_card);

				currentUser.addCard(new_card);

				loop = false;

			} catch (InputMismatchException e) {
				error = "CVC trebuie sa fie un numar";

			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		return;
	}

	public static void deleteCard(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		boolean loop = true;
		int comanda;
		scanner.nextLine();
		while (loop) {
			try {
				System.out.println("Sterge un card");
				System.out.println(error);

				viewCards(connection, scanner, currentUser);

				ArrayList<Card> cards = UserService.getCards(connection, currentUser);
				ArrayList<BankAccount> bankAccounts = UserService.getBankAccounts(connection, currentUser);

				if (cards == null || cards.size() == 0) {
					System.out.println("Nu ai niciun card adaugat");
					return;
				}

				System.out.printf("Alege un card intre 1 si %d:", cards.size());
				comanda = scanner.nextInt();

				if (comanda == cards.size() + 1)
					return;

				if (comanda < 1 || (cards != null && comanda > cards.size()))
					throw new Exception(
							String.format("Trebuie sa introduci o valoare intre 1 si %d", cards.size()));

				for (BankAccount ba : bankAccounts)
					if (ba.getCard() != null && ba.getCard().equals(cards.get(comanda - 1)))
						ba.setCard(null);

				// currentUser.setCards(cards);
				// currentUser.deleteCard(cards.get(comanda - 1));
				CardService.delete(connection, cards.get(comanda - 1));

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

	public static void viewCards(Connection connection, Scanner scanner, User currentUser) throws SQLException {
		System.out.println("Lista cu card-urile");
		ArrayList<Card> cards = UserService.getCards(connection, currentUser);

		if (cards != null) {
			for (int i = 0; i < cards.size(); ++i)
				System.out.println((i + 1) + "." + cards.get(i));
			System.out.printf("%d. Back\n", cards.size() + 1);
		} else {
			System.out.println("Nu ai niciun card adaugat");
			System.out.printf("%d. Back\n", 1);
		}

	}

	public static boolean cardOperations(Connection connection, Scanner scanner, User currentUser) throws SQLException {
		int comanda = 0;
		String error = "";
		boolean loop = true;
		ArrayList<Card> cards = UserService.getCards(connection, currentUser);
		while (loop) {
			try {
				cardOperationsMenu(error, currentUser);
				comanda = scanner.nextInt();

				if (comanda < 1 || comanda > 4)
					throw new InputMismatchException();

				if (comanda == 4)
					return false;

				loop = false;
			} catch (InputMismatchException e) {
				error = "Input-ul trebuie sa fie un numar intreg intre 1 si 4.";
				scanner.nextLine();
			}

			catch (Exception e) {
				error = e.getMessage();

			}
		}

		switch (comanda) {
			case 1: {
				addCard(connection, scanner, currentUser);
				return true;
			}

			case 2: {
				deleteCard(connection, scanner, currentUser);
				return true;
			}

			case 3: {
				// viewCards(connection, scanner, currentUser);
				System.out.println("Lista cu card-urile");
				// ArrayList<Card> cards = UserService.getCards(connection, currentUser);

				if (cards != null && cards.size() > 0) {
					for (int i = 0; i < cards.size(); ++i)
						System.out.println((i + 1) + "." + cards.get(i));
					System.out.printf("%d. Back\n", cards.size() + 1);
				} else {
					System.out.println("Nu ai niciun card adaugat");
					// System.out.printf("%d. Back\n", 1);
				}

				return true;
			}

			case 4: {

				break;
			}

			default:
				return true;
		}

		return false;
	}

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

	public static void transferMoney(Connection connection, Scanner scanner, User currentUser) {
		String error = "";
		int comanda;
		int suma;
		boolean loop = true;
		scanner.nextLine();
		while (loop) {
			try {
				// clearScreen();

				if (UserService.getBankAccounts(connection, currentUser).size() == 0)
					return;

				System.out.println("Transfera o suma");
				System.out.println(error);

				System.out.println("Alegi o persoana din lista de beneficiari sau introduci contul bancar ?");
				System.out.println("1. Lista de beneficiari");
				System.out.println("2. Introducere cont bancar");
				System.out.println("3. Back");
				System.out.println("Introdu comanda dorita(1/3): ");

				comanda = scanner.nextInt();

				if (comanda == 3)
					return;

				while (comanda < 1 || comanda > 2) {
					System.out.println("Transfera o suma");
					System.out.println(error);

					System.out.println("Alegi o persoana din lista de beneficiari sau introduci contul bancar ?");
					System.out.println("1. Lista de beneficiari");
					System.out.println("2. Introducere cont bancar");
					System.out.println("3. Back");
					System.out.println("Introdu comanda dorita(1/3): ");

					comanda = scanner.nextInt();

					if (comanda == 3)
						return;
				}

				switch (comanda) {
					case 1: {
						System.out.println("Lista de beneficiari.");
						int nr = viewBeneficiaries(connection, scanner, currentUser);
						if (nr == 0) {
							System.out.println("Lista de beneficiari este 0.");
							transferMoney(connection, scanner, currentUser);
							return;
						}

						System.out.printf("Alege persoana pentru transfer(1/%d): ", nr);
						comanda = scanner.nextInt();

						if (comanda == nr + 1)
							return;

						while (comanda < 1 || comanda > nr) {
							System.out.printf("Alege persoana pentru transfer(1/%d): ", nr);
							comanda = scanner.nextInt();
							if (comanda == nr + 1)
								return;
						}

						User user = UserService.getBeneficiaries(connection, currentUser).get(comanda - 1);

						System.out.println("Lista cu conturile bancare.");

						nr = viewBankAccounts(connection, scanner, currentUser);

						System.out.printf("Alege contul bancar pentru transfer(1/%d): ", nr);
						comanda = scanner.nextInt();

						while (comanda < 0 || comanda > nr) {
							System.out.printf("Alege contul bancar pentru transfer(1/%d): ", nr);
							comanda = scanner.nextInt();

							if (comanda == nr + 1)
								return;
						}

						BankAccount ba = UserService.getBankAccounts(connection, currentUser).get(0);

						System.out.println("Introdu suma: ");
						suma = scanner.nextInt();

						if (comanda == nr + 1)
							return;

						while (suma < 0 || suma > ba.getBalance()) {
							System.out.println("Introdu suma: ");
							suma = scanner.nextInt();
						}

						// currentUser
						// contul bancar
						// user de trimis
						// cont bancar de trimis;
						// am suma

						ba.setBalance(ba.getBalance() - suma);

						ArrayList<BankAccount> beneficiaryBankAccounts = UserService.getBankAccounts(connection, user);

						if (beneficiaryBankAccounts.size() == 0) {
							System.out.println("Beneficiarul ales nu are un cont bancar");
							return;
						}

						BankAccount toTransfer = null;

						for (int i = 0; i < beneficiaryBankAccounts.size(); ++i)
							if (beneficiaryBankAccounts.get(i).getCurrency().equals(ba.getCurrency())) {
								toTransfer = beneficiaryBankAccounts.get(i);
								break;
							}

						if (toTransfer == null) {
							System.out.println("Beneficiarul nu are un cont bancar in aceeasi valuta");
							return;
						}

						if (ba.equals(toTransfer)) {
							System.out.println("Conturile bancare coincid");
							return;
						}

						toTransfer.setBalance(toTransfer.getBalance() + suma);

						BankAccountService.update(connection, ba);
						BankAccountService.update(connection, toTransfer);

						TransferService.createTransfer(connection, suma, ba.getCurrency(), currentUser, user);
						return;
					}

					case 2: {
						int nr = viewBankAccounts(connection, scanner, currentUser);

						System.out.printf("Alege contul bancar pentru transfer(1/%d): ", nr);
						comanda = scanner.nextInt();

						while (comanda < 0 || comanda > nr) {
							System.out.printf("Alege contul bancar pentru transfer(1/%d): ", nr);
							comanda = scanner.nextInt();
						}

						ArrayList<BankAccount> banks = BankAccountService.getBanks(connection);
						BankAccount ba = banks.get(comanda - 1);
						BankAccount toTransfer = null;

						System.out.println("Introdu suma: ");
						suma = scanner.nextInt();

						while (suma < 0 || suma > ba.getBalance()) {
							System.out.println("Introdu suma: ");
							suma = scanner.nextInt();
						}
						scanner.nextLine();

						System.out.println("Introduce iban-ul contului bancar: ");
						String bankAccountIBAN = scanner.nextLine();

						for (int i = 0; i < banks.size(); ++i)
							if (banks.get(i).getIBAN().equals(bankAccountIBAN)) {
								toTransfer = banks.get(i);
								break;
							}

						if (toTransfer == null) {
							System.out.println("Nu exista acest cont bancar");
							return;
						}

						if (ba.equals(toTransfer)) {
							System.out.println("Conturile bancare coincid");
							return;
						}

						if (!ba.getCurrency().equals(toTransfer.getCurrency())) {
							System.out.println("Conturile bancare au valuta diferita");
							return;
						} else {
							ba.setBalance(ba.getBalance() - suma);
							toTransfer.setBalance(toTransfer.getBalance() + suma);

							BankAccountService.update(connection, ba);
							BankAccountService.update(connection, toTransfer);

							User user = BankAccountService.select(connection, toTransfer);

							TransferService.createTransfer(connection, suma, ba.getCurrency(), currentUser, user);
						}
					}
				}

				// loop = false;
			} catch (InputMismatchException e) {
				error = "Input incorrect";
				scanner.nextLine();
			} catch (Exception e) {
				error = e.getMessage();

			}
		}

		return;
	}

	public static void showHistory(Connection connection, Scanner scanner, User currentUser) throws SQLException {
		TreeSet<Transfer> ts = TransferService.getTransfers(connection, currentUser);

		if (ts.size() > 0)
			for (Transfer transfer : ts)
				System.out.println(transfer.toString());
		else {
			System.out.println("Momentan istoricul este gol.");
		}
	}

	public static User UserMenu(Connection connection, Scanner scanner, User currentUser) throws SQLException {
		int comanda = 0;
		String error = "";
		boolean loop = true;

		while (loop) {
			try {
				displayUserMenu(error, currentUser);
				comanda = scanner.nextInt();

				if (comanda < 1 || comanda > 11)
					throw new InputMismatchException();

				loop = false;
			} catch (InputMismatchException e) {
				error = "Input-ul trebuie sa fie o valoare intreaga intre 1 si 11";
				scanner.nextLine();
			} catch (Exception e) {
				error = e.getMessage();
			}
		}

		switch (comanda) {
			case 1: {
				showUserDetails(currentUser);
				scanner.nextLine();
				break;
			}

			case 2: {
				changePassword(connection, scanner, currentUser);
				System.out.println("Parola a fost schimbata!");

				break;
			}

			case 3: {
				changeAdress(connection, scanner, currentUser);
				System.out.println("Adresa a fost schimbata!");
				break;
			}

			case 4: {
				promptToDelete(connection, scanner, currentUser);
				UserService.delete(connection, currentUser);
				currentUser = null;
				break;
			}

			case 5: {
				showHistory(connection, scanner, currentUser);
				break;
			}

			case 6: {
				boolean operations = true;
				while (operations) {
					operations = bankAccountsOperations(connection, scanner, currentUser);
				}

				break;
			}

			case 7: {
				boolean operations = true;
				while (operations) {
					operations = cardOperations(connection, scanner, currentUser);
				}
				break;
			}

			case 8: {

				boolean operations = true;
				while (operations) {
					operations = ManageBeneficiaryOperations(connection, scanner, currentUser);
				}
				break;
			}

			case 9: {
				transferMoney(connection, scanner, currentUser);
				break;
			}

			case 10: {
				currentUser = null;
				break;
			}

			case 11: {
				connection.close();
				scanner.close();
				System.exit(0);
			}

		}

		return currentUser;
	}

	public static void showUserDetails(User user) {
		System.out.printf("User name: %s\n", user.getName());
		System.out.printf("User email: %s\n", user.getEmail());
		System.out.printf("User cnp: %s\n", user.getCNP());
		System.out.printf("User adresa: %s\n", user.getAdresa());
	}

	public static void main(String arg[]) {
		Connection connection = null;
		String url = "jdbc:mysql://localhost:3306/javaapp";
		String user = "root";
		String pass = "";
		Scanner scanner = new Scanner(System.in);
		try {
			connection = DriverManager.getConnection(url, user, pass);

			User currentUser = null;

			while (currentUser == null) {
				currentUser = initialMenu(connection, scanner);
				while (currentUser != null) {
					currentUser = UserMenu(connection, scanner, currentUser);
				}
			}

		} catch (Exception exception) {
			System.out.println("sunt aicii ???");
			System.out.println(exception);
		}
	}
}