package src.CardPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import src.BankAccountPackage.BankAccount;
import src.UserPackage.User;

public class CardService {
	public static void insert(Connection connection, User user, Card card) throws SQLException {
		String query = "INSERT INTO card (card_number, cvc, userid, expiration_date) VALUES (?, ?, ?, ?)";

		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, card.getCardNumber());
		statement.setInt(2, card.getCvc());
		statement.setInt(3, user.getId());
		statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
		statement.executeUpdate();
	}

	public static void delete(Connection connection, Card card) throws SQLException {
		String query = "DELETE FROM card WHERE id = ?";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setInt(1, card.getId());
		statement.executeUpdate();
	}

	public static void update(Connection connection, Card card, User user, BankAccount ba) throws SQLException {
		String query = "UPDATE card SET card_number = ?, cvc = ?, userid = ?, bankaccountid = ? WHERE id = ?";

		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(5, card.getId());
		statement.setString(1, card.getCardNumber());
		statement.setInt(2, card.getCvc());
		statement.setInt(3, user.getId());
		statement.setInt(4, ba.getId());

		statement.executeUpdate();
	}

}
