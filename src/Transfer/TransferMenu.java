package src.Transfer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import src.BankAccountPackage.BankAccount;
import src.BankAccountPackage.BankAccountMenu;
import src.BankAccountPackage.BankAccountService;
import src.BeneficiaryPackage.BeneficiaryMenu;
import src.UserPackage.User;
import src.UserPackage.UserService;

public class TransferMenu {
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
						int nr = BeneficiaryMenu.viewBeneficiaries(connection, scanner, currentUser);
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

						nr = BankAccountMenu.viewBankAccounts(connection, scanner, currentUser);

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
						int nr = BankAccountMenu.viewBankAccounts(connection, scanner, currentUser);

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

}