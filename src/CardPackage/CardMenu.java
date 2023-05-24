package src.CardPackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import src.BankAccountPackage.BankAccount;
import src.UserPackage.User;
import src.UserPackage.UserService;

public class CardMenu {
	public static void cardOperationsMenu(String error, User currentUser) {
		System.out.println("Operatii card-uri");
		System.out.println(error);
		System.out.println("1. Adauga card");
		System.out.println("2. Sterge card");
		System.out.println("3. Vizualizeaza card-uri");
		System.out.println("4. Back");
		System.out.println("Introdu o comanda(de la 1 la 4): ");
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
}
