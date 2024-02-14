import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		String directory = "C:/Users/seoji/Downloads/data (1)/data/tiny1.txt";
		getMostWordCounts(directory);
		getMostWordsForSpecificWord(directory,"the" );
		getMostWordsForSpecificWord(directory, "of");
		getMostWordsForSpecificWord(directory, "was");
		forThePhase(directory, "but the");
		forThePhase(directory,"it was");
		forThePhase(directory,"in my");
	}
	private static void getMostWordCounts(String fileDirectory) {
		int wordCount = 0;
		ArrayStack<String> stack = new ArrayStack<>(10000);
		ArrayStack<Integer> counts = new ArrayStack<>(10000);
		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			String line;
			while ((line = reader.readLine()) != null) {
				Pattern pattern = Pattern.compile("\\b\\w+\\b");
				Matcher matcher = pattern.matcher(line);

				while (matcher.find()) {
					String word = matcher.group().toLowerCase();
					int index = indexOfWord(stack, word, wordCount);
					if (index == -1) {
						stack.push(word);
						counts.push(1); // Initialize count for the new word
						wordCount++;
					} else {
						counts.setIndex(index, counts.getIndex(index) + 1); // Update count for existing word
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		int maxIndex = 0;
		for (int i = 1; i < wordCount; i++) {
			if (counts.getIndex(i) > counts.getIndex(maxIndex)) {
				maxIndex = i;
			}
		}

		//For the Second question
		int firstHighestIndex = 0;
		int secondHighestIndex = 0;
		int thirdHighestIndex = 0;
		for(int i = 1; i < wordCount; i++){
			if(counts.getIndex(i) > counts.getIndex(firstHighestIndex)){
				thirdHighestIndex = secondHighestIndex;
				secondHighestIndex = firstHighestIndex;
				firstHighestIndex = i;
			}
			else if(counts.getIndex(i) > counts.getIndex(secondHighestIndex)){
				thirdHighestIndex = secondHighestIndex;
				secondHighestIndex = i;
			}
			else if (counts.getIndex(i) > counts.getIndex(i)){
				thirdHighestIndex = i;
			}
		}

		System.out.println("The most frequent word in the file is: " + stack.getIndex(maxIndex));
		System.out.println("Frequency: " + counts.getIndex(maxIndex));
		System.out.println("*************************************************");
		System.out.println("The 3rd most frequent words in the file is " + stack.getIndex(thirdHighestIndex));
		System.out.println("Its frequency is " + counts.getIndex(thirdHighestIndex));
		System.out.println("*************************************************");

	}

	public static void getMostWordsForSpecificWord(String fileDirectory, String word) {
		ArrayStack<String> sentenceWithMaxOccurrences = new ArrayStack<>(10000);
		int maxOccurrences = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			StringBuilder textBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				textBuilder.append(line).append(" ");
			}

			String[] sentences = textBuilder.toString().split("[.!?\\n]");

			Pattern pattern = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);

			for (String sentence : sentences) {
				Matcher matcher = pattern.matcher(sentence);
				int occurrences = 0;
				while (matcher.find()) {
					occurrences++;
				}
				if (occurrences > maxOccurrences) {
					maxOccurrences = occurrences;
					sentenceWithMaxOccurrences.empty(); // Clear the stack
					sentenceWithMaxOccurrences.push(sentence.trim()); // Trim to remove leading/trailing whitespace
				} else if (occurrences == maxOccurrences) {
					sentenceWithMaxOccurrences.push(sentence.trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!sentenceWithMaxOccurrences.isEmpty()) {
			int count = 1; 
			System.out.println("The most sentence(s) with the most word '" + word + "' is/are:");
			while (!sentenceWithMaxOccurrences.isEmpty()) {
				System.out.println("############ Sentence " + count + " ############ ");
				System.out.println(sentenceWithMaxOccurrences.pop());
			}
			System.out.println("With the frequency of " + maxOccurrences);
			System.out.println("*************************************************");
		} else {
			System.out.println("Was unable to find the sentence with the word: " + word);
		}
	}
	public static void forThePhase(String fileDirectory, String word) {
		ArrayStack<String> sentenceWithMaxOccurrences = new ArrayStack<>(10000);
		int maxOccurrences = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			StringBuilder textBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				textBuilder.append(line).append(" ");
			}

			String[] sentences = textBuilder.toString().split("(?<=[.!?\\n])\\s+");

			Pattern pattern = Pattern.compile("\\b" + word.replace(" ", "\\s+") + "\\b", Pattern.CASE_INSENSITIVE);

			for (String sentence : sentences) {
				Matcher matcher = pattern.matcher(sentence);
				int occurrences = 0;
				while (matcher.find()) {
					occurrences++;
				}
				if (occurrences > maxOccurrences) {
					maxOccurrences = occurrences;
					sentenceWithMaxOccurrences.empty(); // Clear the stack
					sentenceWithMaxOccurrences.push(sentence.trim()); // Trim to remove leading/trailing whitespace
				} else if (occurrences == maxOccurrences) {
					sentenceWithMaxOccurrences.push(sentence.trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (maxOccurrences > 0) {
			int count = 1;
			System.out.println("The most sentence(s) with the most phrase '" + word + "' is/are:");
			while (!sentenceWithMaxOccurrences.isEmpty()) {
				System.out.println("############ Sentence " + count + " ############ ");
				System.out.println(sentenceWithMaxOccurrences.pop());
				count+=1;
			}
			System.out.println("With the frequency of " + maxOccurrences);
			System.out.println("*************************************************");
		} else {
			System.out.println("The phrase '" + word + "' is not found in the file.");
			System.out.println("*************************************************");
		}
	}



	private static int indexOfWord(ArrayStack<String> words, String word, int wordCount) {
		for (int i = 0; i < wordCount; i++) {
			if (words.getIndex(i).equals(word)) {
				return i;
			}
		}
		return -1;
	}
}
