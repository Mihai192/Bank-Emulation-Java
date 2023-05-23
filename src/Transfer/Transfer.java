package src.Transfer;

import java.sql.Timestamp;

import src.UserPackage.User;

public class Transfer implements Comparable<Transfer> {
	Timestamp dateOfIssurance;
	int amount;
	User sender;
	User recipient;
	String currency;

	public Transfer(User sender, User recipient, int amount, String currency, Timestamp dateOfIssurance) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
		this.currency = currency;
		this.dateOfIssurance = dateOfIssurance;
	}

	@Override
	public int compareTo(Transfer o) {
		return this.dateOfIssurance.compareTo(o.dateOfIssurance);
	}

	@Override
	public String toString() {
		return String.format("Transfer[ sender: %s, recipient: %s, amount: %d, currency: %s]",
				this.sender, this.recipient, this.amount, this.currency);
	}
}