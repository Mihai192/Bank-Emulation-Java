package src.utils;

import java.util.Random;

public class RandomStringGenerator {
	public static String generateRandomString(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		String characters = "abcdefghjklmnoprstzABCDEFGHJKLMNOPRSTZ123456789";

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			sb.append(randomChar);
		}

		return sb.toString();
	}
}
