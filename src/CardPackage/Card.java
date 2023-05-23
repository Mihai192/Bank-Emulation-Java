package src.CardPackage;

import java.sql.Timestamp;

public class Card {
	private int id;
	private String cardNumber;
	private int cvc;
	private Timestamp expiration_date;
	private Timestamp added_date;

	public Card(String cardNumber, int cvc) {
		this.cardNumber = cardNumber;
		this.cvc = cvc;
	}

	public Card(int id, String cardNumber, int cvc, Timestamp expiration_date, Timestamp added_date) {
		this(cardNumber, cvc);
		this.id = id;

		this.expiration_date = expiration_date;
		this.added_date = added_date;
	}

	public int getId() {
		return this.id;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public int getCvc() {
		return this.cvc;
	}

	public Timestamp getExpirationDate() {
		return new Timestamp(expiration_date.getTime());
	}

	public Timestamp getAddedDate() {
		return new Timestamp(added_date.getTime());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		Card temp = (Card) obj;

		if (temp.getCardNumber() == this.getCardNumber() && temp.getId() == this.getId()
				&& temp.getCvc() == this.getCvc() && temp.getExpirationDate() == this.getExpirationDate())
			return true;
		return false;
	}

	@Override
	public String toString() {
		return String.format("Card: number %s, cvc %s, expiration date %s",
				this.cardNumber,
				this.cvc,
				this.expiration_date);
	}
}
