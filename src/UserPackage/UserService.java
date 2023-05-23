package src.UserPackage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.sql.*;

import src.BankAccountPackage.BankAccount;
import src.CardPackage.Card;

public class UserService {

	public static ArrayList<User> getUsers(Connection connection) throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		String query = "SELECT * FROM user";

		ResultSet resultSet = statement.executeQuery(query);

		ArrayList<User> users = new ArrayList<>();

		while (resultSet.next()) {

			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String email = resultSet.getString("email");
			int role = resultSet.getInt("role");
			String pass_user = resultSet.getString("pass");
			String cnp = resultSet.getString("cnp");
			Timestamp timestamp = resultSet.getTimestamp("registration_date");

			User temp = new User(id, name, email, role, pass_user, cnp, cnp, timestamp);

			users.add(temp);
		}

		resultSet.close();
		statement.close();

		return users;
	}

	public static User select(Connection connection, int id) throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		String query = String.format("SELECT * FROM user WHERE id = %d", id);

		ResultSet resultSet = statement.executeQuery(query);

		ArrayList<User> users = new ArrayList<>();

		while (resultSet.next()) {

			int id_ = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String email = resultSet.getString("email");
			int role = resultSet.getInt("role");
			String pass_user = resultSet.getString("pass");
			String cnp = resultSet.getString("cnp");
			Timestamp timestamp = resultSet.getTimestamp("registration_date");

			User temp = new User(id_, name, email, role, pass_user, cnp, cnp, timestamp);

			users.add(temp);
		}

		resultSet.close();
		statement.close();

		if (users.isEmpty())
			return null;
		return users.get(0);
	}

	public static boolean existsNameEmail(Connection connection, User user) throws SQLException {
		ArrayList<User> users = UserService.getUsers(connection);
		final String tempName = user.getName();
		final String tempEmail = user.getEmail();

		users = users.stream().filter(u -> u.getName().equals(tempName))
				.filter(u -> u.getEmail().equals(tempEmail))
				.collect(Collectors.toCollection(ArrayList::new));

		return users.size() > 0;
	}

	public static boolean existsEmailPass(Connection connection, String email, String pass) throws SQLException {
		ArrayList<User> users = UserService.getUsers(connection);
		final String tempPass = pass;
		final String tempEmail = email;

		users = users.stream().filter(u -> u.getPass().equals(tempPass))
				.filter(u -> u.getEmail().equals(tempEmail))
				.collect(Collectors.toCollection(ArrayList::new));

		return users.size() > 0;
	}

	public static ArrayList<User> select(Connection connection, String propertyName, String propertyValue)
			throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		String query = String.format("SELECT * FROM user WHERE %s = '%s'", propertyName, propertyValue);

		ResultSet resultSet = statement.executeQuery(query);

		ArrayList<User> users = new ArrayList<>();

		while (resultSet.next()) {
			int id_ = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String email = resultSet.getString("email");
			int role = resultSet.getInt("role");
			String pass_user = resultSet.getString("pass");
			String cnp = resultSet.getString("cnp");
			Timestamp timestamp = resultSet.getTimestamp("registration_date");

			User temp = new User(id_, name, email, role, pass_user, cnp, cnp, timestamp);

			users.add(temp);
		}

		resultSet.close();
		statement.close();

		return users;
	}

	public static void insert(Connection connection, User u) throws SQLException {
		String query = "INSERT INTO user (name, email, role, pass, cnp, adresa) VALUES (?, ?, ?, ?, ?, ?)";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setString(1, u.getName());
		statement.setString(2, u.getEmail());
		statement.setInt(3, u.getRole());
		statement.setString(4, u.getPass());

		statement.setString(5, u.getCNP());
		statement.setString(6, u.getAdresa());

		statement.executeUpdate();
	}

	public static void update(Connection connection, User u) throws SQLException {
		String query = "UPDATE user SET name = ?, email = ?, role = ?,  pass = ?, cnp = ?, adresa = ? WHERE id = ?";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setString(1, u.getName());
		statement.setString(2, u.getEmail());
		statement.setInt(3, 0);
		statement.setString(4, u.getPass());

		statement.setString(5, u.getCNP());
		statement.setString(6, u.getAdresa());

		statement.setInt(7, u.getId());
		statement.executeUpdate();
	}

	public static void delete(Connection connection, User u) throws SQLException {
		String query = "DELETE FROM user where id = ?";
		PreparedStatement statement = connection.prepareStatement(query);

		statement.setInt(1, u.getId());
		statement.executeUpdate();
	}

	// one-to-many
	public static ArrayList<BankAccount> getBankAccounts(Connection connection, User user) throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		String query = String.format("SELECT * FROM bankaccount WHERE userid = %d", user.getId());

		ResultSet resultSet = statement.executeQuery(query);

		ArrayList<BankAccount> banks = new ArrayList<>();

		while (resultSet.next()) {
			int id_ = resultSet.getInt("id");
			int balance = resultSet.getInt("balance");
			String name = resultSet.getString("name");
			String iban = resultSet.getString("iban");
			String currency = resultSet.getString("currency");
			Timestamp registration_date = resultSet.getTimestamp("creation_date");

			BankAccount temp = new BankAccount(id_, balance, name, iban, currency, registration_date);
			banks.add(temp);
		}

		resultSet.close();
		statement.close();

		return banks;
	}

	// one-to-many
	public static ArrayList<Card> getCards(Connection connection, User user) throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		String query = String.format("SELECT * FROM card WHERE userid = %d", user.getId());

		ResultSet resultSet = statement.executeQuery(query);

		ArrayList<Card> cards = new ArrayList<>();

		while (resultSet.next()) {
			int id_ = resultSet.getInt("id");
			String card_number = resultSet.getString("card_number");
			int cvc = resultSet.getInt("cvc");
			Timestamp expiration_date = resultSet.getTimestamp("expiration_date");
			Timestamp registration_date = resultSet.getTimestamp("added_date");

			Card temp = new Card(id_, card_number, cvc, expiration_date, registration_date);
			cards.add(temp);
		}

		resultSet.close();
		statement.close();

		if (cards.size() == 0)
			return null;
		return cards;
	}

	public static ArrayList<User> getBeneficiaries(Connection connection, User user) throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		String query = String.format("SELECT * FROM beneficiary WHERE self_id = %d", user.getId());

		ResultSet resultSet = statement.executeQuery(query);

		ArrayList<Integer> IDs = new ArrayList<>();

		while (resultSet.next()) {
			IDs.add(resultSet.getInt("beneficiary_id"));
		}

		ArrayList<User> users = new ArrayList<User>();

		System.out.println(IDs);
		for (Integer value : IDs) {
			User u = select(connection, value);
			users.add(u);

		}

		resultSet.close();
		statement.close();
		return users;
	}

	public static void insertBeneficiary(Connection connection, User self, User beneficiary) throws SQLException {
		String query = "INSERT INTO beneficiary (self_id, beneficiary_id) VALUES (?, ?)";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setInt(1, self.getId());
		statement.setInt(2, beneficiary.getId());
		statement.executeUpdate();
	}

	public static void deleteBeneficiary(Connection connection, User self, User beneficiary) throws SQLException {
		String query = "DELETE FROM beneficiary WHERE self_id = ? AND beneficiary_id = ?";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setInt(1, self.getId());
		statement.setInt(2, beneficiary.getId());
		statement.executeUpdate();
	}
}
