package src.UserPackage;

import java.sql.Timestamp;
import java.util.ArrayList;

import src.BankAccountPackage.*;
import src.CardPackage.Card;

public class User {
	private int id;
	private String name;
	private String email;
	private String pass;
	private int role;
	private String cnp;
	private String adresa;
	private Timestamp registration_date;

	private ArrayList<BankAccount> bankAccounts;
	private ArrayList<Card> cards;

	public User(String name, String email, int role, String pass, String cnp, String adresa) {
		this.name = name;
		this.email = email;
		this.role = role;
		this.pass = pass;
		this.cnp = cnp;
		this.adresa = adresa;
	}

	public User(int id, String name, String email, int role, String pass, String cnp, String adresa,
			Timestamp registration_date) {
		this.id = id;
		this.name = name;
		this.role = role;
		this.email = email;
		this.pass = pass;
		this.cnp = cnp;
		this.adresa = adresa;
		this.registration_date = registration_date;
	}

	public User(User user) {
		this.id = user.id;
		this.name = user.name;
		this.email = user.email;
		this.role = user.role;
		this.pass = user.pass;
		this.cnp = user.cnp;
		this.adresa = user.adresa;
		this.registration_date = user.registration_date;
	}

	public int getId() {
		return this.id;
	}

	public int getRole() {
		return this.role;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCNP() {
		return this.cnp;
	}

	public void setCNP(String cnp) {
		this.cnp = cnp;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getAdresa() {
		return this.adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public void setBanks(ArrayList<BankAccount> bankList) {
		this.bankAccounts = new ArrayList<BankAccount>(bankList);
	}

	public void setCards(ArrayList<Card> cardList) {
		this.cards = new ArrayList<Card>(cardList);
	}

	public ArrayList<BankAccount> getBanks() {
		return new ArrayList<BankAccount>(this.bankAccounts);
	}

	public ArrayList<Card> getCards() {
		if (this.cards != null)
			return new ArrayList<Card>(this.cards);
		return null;
	}

	public void addBank(BankAccount bank) {
		this.bankAccounts.add(bank);
	}

	public void addCard(Card c) {
		if (this.cards != null)
			this.cards.add(c);
		else {
			this.cards = new ArrayList<Card>();
			this.cards.add(c);
		}
	}

	public void deleteBank(BankAccount bank) {
		for (int i = 0; i < this.bankAccounts.size(); ++i)
			if (this.bankAccounts.get(i).equals(bank)) {
				this.bankAccounts.remove(i);
				return;
			}
	}

	public void deleteCard(Card card) {
		for (int i = 0; i < this.bankAccounts.size(); ++i)
			if (this.cards.get(i).equals(card)) {
				this.cards.remove(i);
				return;
			}
	}

	public Timestamp getRegistrationDate() {
		return this.registration_date;
	}

	public void addMoneyToBank(int index, int sum) throws IndexOutOfBoundsException {
		if (index < 0 || index > this.bankAccounts.size())
			throw new IndexOutOfBoundsException();
		int oldBalance = this.bankAccounts.get(index).getBalance();
		this.bankAccounts.get(index).setBalance(oldBalance + sum);
	}

	public void addMoneyToBank(BankAccount b, int sum) {
		return;
	}

	public void withDrawMoneyFromBank(int index, int sum) throws IndexOutOfBoundsException {
		if (index < 0 || index > this.bankAccounts.size())
			throw new IndexOutOfBoundsException();

		int oldBalance = this.bankAccounts.get(index).getBalance();

		if (sum > oldBalance)
			throw new NumberFormatException("Suma depaseste balance-ul contului curent.");

		if (sum < 0)
			throw new NumberFormatException("Suma trebuie sa fie mai mare ca 0.");

		this.bankAccounts.get(index).setBalance(oldBalance - sum);
	}

	@Override
	public String toString() {
		return String.format("User(%s, %s)", this.name, this.email);
	}
}
