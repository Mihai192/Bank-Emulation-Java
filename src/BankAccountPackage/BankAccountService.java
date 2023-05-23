package src.BankAccountPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import src.CardPackage.Card;
import src.UserPackage.User;
import src.UserPackage.UserService;

public class BankAccountService {

	public static ArrayList<BankAccount> getBanks(Connection connection) throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		String query = "SELECT * FROM bankaccount";

		ResultSet resultSet = statement.executeQuery(query);

		ArrayList<BankAccount> bankAccounts = new ArrayList<>();

		while (resultSet.next()) {

			int id = resultSet.getInt("id");
			int balance = resultSet.getInt("balance");
			String name = resultSet.getString("name");
			String iban = resultSet.getString("iban");
			String currency = resultSet.getString("currency");
			Timestamp timestamp = resultSet.getTimestamp("creation_date");

			BankAccount temp = new BankAccount(id, balance, name, iban, currency, timestamp);
			bankAccounts.add(temp);
		}

		resultSet.close();
		statement.close();

		return bankAccounts;
	}

	public static User select(Connection connection, BankAccount ba) throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		String query = String.format("SELECT * FROM bankaccount WHERE iban = '%s'", ba.getIBAN());

		System.out.println(query);
		ResultSet resultSet = statement.executeQuery(query);

		while (resultSet.next()) {
			int id = resultSet.getInt("userid");

			return UserService.select(connection, id);
		}
		return null;
	}

	public static void insert(Connection connection, User user, BankAccount ba) throws SQLException {
		String query = "INSERT INTO bankaccount (balance, name, iban, currency, userid) VALUES (?, ?, ?, ?, ?)";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setInt(1, ba.getBalance());
		statement.setString(2, ba.getName());
		statement.setString(3, ba.getIBAN());
		statement.setString(4, ba.getCurrency());
		statement.setInt(5, user.getId());
		statement.executeUpdate();
	}

	public static void update(Connection connection, BankAccount ba) throws SQLException {
		String query = "UPDATE bankaccount SET balance = ? WHERE iban = ?";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setInt(1, ba.getBalance());
		statement.setString(2, ba.getIBAN());
		statement.executeUpdate();
	}

	public static void delete(Connection connection, BankAccount ba) throws SQLException {
		String query = "DELETE FROM bankaccount WHERE iban = ?";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setString(1, ba.getIBAN());
		statement.executeUpdate();
	}

	public static Card getCard(Connection connection, BankAccount ba) throws SQLException {
		String query = "SELECT * FROM card where bankaccountid = ?";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setInt(1, ba.getId());
		ResultSet resultSet = statement.executeQuery();

		Card card = null;

		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String card_number = resultSet.getString("card_number");
			int cvc = resultSet.getInt("cvc");
			Timestamp expiration_date = resultSet.getTimestamp("expiration_date");
			Timestamp added_date = resultSet.getTimestamp("added_date");

			card = new Card(id, card_number, cvc, expiration_date, added_date);
		}

		resultSet.close();
		statement.close();
		return card;
	}
}
