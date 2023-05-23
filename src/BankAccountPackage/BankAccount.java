package src.BankAccountPackage;

import java.sql.Timestamp;

import src.CardPackage.Card;

public class BankAccount {
	private int id;
	private int balance;
	private String name;
	private String iban;
	private String currency;
	private Card card;
	private Timestamp registration_date;

	public BankAccount(int balance, String name, String iban, String currency) {

		this.balance = balance;
		this.name = name;
		this.iban = iban;
		this.currency = currency;
	}

	public BankAccount(int id, int balance, String name, String iban, String currency) {
		this.id = id;
		this.balance = balance;
		this.name = name;
		this.iban = iban;
		this.currency = currency;
	}

	public BankAccount(int id, int balance, String name, String iban, String currency, Timestamp registration_date) {
		this.id = id;
		this.balance = balance;
		this.name = name;
		this.iban = iban;
		this.currency = currency;
		this.registration_date = registration_date;
	}

	public int getId() {
		return this.id;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getBalance() {
		return this.balance;
	}

	public String getName() {
		return name;
	}

	public String getIBAN() {
		return this.iban;
	}

	public String getCurrency() {
		return this.currency;
	}

	public Card getCard() {
		return this.card;
	}

	public void setCard(Card c) {
		this.card = c;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (obj == null)
			return false;
		BankAccount temp = (BankAccount) obj;

		if (temp.getIBAN() == this.getIBAN())
			return true;

		return false;
	}

	@Override
	public String toString() {
		return String.format("Bank account, name: %s, balance: %d, iban: %s, currency: %s", this.name, this.balance,
				this.iban,
				this.currency);
	}
}
