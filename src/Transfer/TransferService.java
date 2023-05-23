package src.Transfer;

import java.sql.Timestamp;
import java.util.TreeSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import src.UserPackage.User;
import src.UserPackage.UserService;

public class TransferService {
	public static void createTransfer(Connection connection, int amount, String currency, User sender, User recepient)
			throws SQLException {
		String query = "INSERT INTO transfer (sender_id, recipient_id, currency, amount) VALUES (?, ?, ?, ?)";

		PreparedStatement statement = connection.prepareStatement(query);

		statement.setInt(1, sender.getId());

		statement.setInt(2, recepient.getId());
		statement.setString(3, currency);
		statement.setInt(4, amount);

		statement.executeUpdate();
	}

	public static TreeSet<Transfer> getTransfers(Connection connection, User user) throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		String query = String.format("SELECT * FROM transfer WHERE sender_id = %d OR recipient_id = %d",
				user.getId(), user.getId());

		ResultSet resultSet = statement.executeQuery(query);

		TreeSet<Transfer> ts = new TreeSet<Transfer>();

		while (resultSet.next()) {
			int senderid = resultSet.getInt("sender_id");
			int recipientid = resultSet.getInt("recipient_id");
			String currency = resultSet.getString("currency");
			int amount = resultSet.getInt("amount");
			Timestamp action_date = resultSet.getTimestamp("action_date");

			Transfer transfer = new Transfer(UserService.select(connection, senderid),
					UserService.select(connection, recipientid),
					amount, currency, action_date);
			ts.add(transfer);
		}

		return ts;
	}
}
