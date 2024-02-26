import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		int current_question = 4;

		//EDIT THIS DIRECTORY DEPENDING ON THE LOCATION OF THE FILE
		String directory = "C:/Users/seoji/Downloads/data/data/tiny1.txt";
		getMostWordCounts(directory);
		current_question = getMostWordsForSpecificWord(directory, "the", current_question);
		current_question = getMostWordsForSpecificWord(directory, "of", current_question);
		current_question = getMostWordsForSpecificWord(directory, "was", current_question);
		current_question = forThePhase(directory, "but the", current_question);
		current_question = forThePhase(directory, "it was", current_question);
		current_question = forThePhase(directory, "in my", current_question);
		highestFrequencyInSentence(directory);
	}

	/**
	 * ---------------------------------------------------------------
	 * AUTHOR:         JIN SEO
	 * CREATED DATE:   2024/2/15
	 * PURPOSE:        To find the most frequent words from the file
	 * PRECONDITIONS:  N/A
	 * POST-CONDITIONS: N/A
	 * ARGUMENTS:      Requires the file Directory(String) from the user
	 * DEPENDENCIES:   N/A
	 * ---------------------------------------------------------------
	 */
	private static void getMostWordCounts(String fileDirectory) {
		int wordCount = 0;
		ArrayStack<String> stack = new ArrayStack<>(10000);
		ArrayStack<Integer> counts = new ArrayStack<>(10000);
		//Start reading the line
		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			String line;
			while ((line = reader.readLine()) != null) {
				//Finding this pattern of the words
				Pattern pattern = Pattern.compile("\\b\\w+\\b");
				Matcher matcher = pattern.matcher(line);

				//When the pattern is matched
				while (matcher.find()) {
					//Lowercase(case-free)
					String word = matcher.group().toLowerCase();
					int index = indexOfWord(stack, word, wordCount);

					//If the word is not found/ Is not existing
					if (index == -1) {
						stack.push(word);
						counts.push(1);
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
		ArrayStack<Integer> indexKeeper = new ArrayStack<>(100);
		for (int i = 1; i < wordCount; i++) {
			if (counts.getIndex(i) > counts.getIndex(maxIndex)) {
				maxIndex = i;
				indexKeeper.empty();
			}
			if (counts.getIndex(i).equals(counts.getIndex(maxIndex))) {
				indexKeeper.push(i);
			}

		}

		ArrayStack<Integer> indexForThird = new ArrayStack<>(100);
		//For the Second question
		int firstHighestIndex = 0;
		int secondHighestIndex = 0;
		int thirdHighestIndex = 0;
		for (int i = 1; i < wordCount; i++) {
			if (counts.getIndex(i) > counts.getIndex(firstHighestIndex)) {
				thirdHighestIndex = secondHighestIndex;
				secondHighestIndex = firstHighestIndex;
				firstHighestIndex = i;
			} else if (counts.getIndex(i) > counts.getIndex(secondHighestIndex)) {
				thirdHighestIndex = secondHighestIndex;
				secondHighestIndex = i;
			} else if (counts.getIndex(i) > counts.getIndex(i)) {
				thirdHighestIndex = i;
			}
		}
		//For getting same amount
		for (int i = 0; i < wordCount; i++) {
			if (counts.getIndex(i) == counts.getIndex(thirdHighestIndex)) {
				indexForThird.push(i);
			}
		}


		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Question1.txt"), "utf-8"))) {
			if (indexKeeper.getSizeOfWords() >= 1) {
				for (int i = 0; i < indexKeeper.getSizeOfWords(); i++) {
					writer.write(stack.getIndex(indexKeeper.getIndex(i)) + " : " + counts.getIndex(maxIndex));

				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		while (!indexForThird.isEmpty()) {
			int index = indexForThird.pop();
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Question2.txt"), "utf-8"))) {
				writer.write(stack.getIndex(index).toLowerCase() + " : " + counts.getIndex(index));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}


	}

	/**
	 * ---------------------------------------------------------------
	 * AUTHOR:         JIN SEO
	 * CREATED DATE:   2024/2/15
	 * PURPOSE:        To find the sentence that uses user's input mostly.
	 * PRECONDITIONS:  N/A
	 * POST-CONDITIONS: N/A
	 * ARGUMENTS:      Requires the file Directory(String) from the user and target word
	 * DEPENDENCIES:   N/A
	 * ---------------------------------------------------------------
	 */
	public static int getMostWordsForSpecificWord(String fileDirectory, String word, int question_number) {
		ArrayStack<String> sentenceWithMaxOccurrences = new ArrayStack<>(10000);
		ArrayStack<String> sentencesWithNoOccurence = new ArrayStack<>(10000);
		int maxOccurrences = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			StringBuilder textBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				textBuilder.append(line).append("\n"); // Ensure newline characters are retained
			}

			// Split text into paragraphs based on consecutive newline characters
			String[] paragraphs = textBuilder.toString().split("(?m)(?=\\n\\s*\\n)");

			for (String paragraph : paragraphs) {
				// Split paragraph into sentences
				String[] sentences = paragraph.trim().split("(?<=[.!?\\n])\\s+");

				Pattern pattern = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);

				for (String sentence : sentences) {
					sentencesWithNoOccurence.push(sentence);
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayStack<String> temp = new ArrayStack<>(1000);
		while(!sentenceWithMaxOccurrences.isEmpty()){
			temp.push(sentenceWithMaxOccurrences.pop());
		}
		if (!temp.isEmpty()) {
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Question" + question_number + ".txt"), "utf-8"))) {
				while (!temp.isEmpty()) {
					writer.write(word + ":" + maxOccurrences + ":" + temp.pop().replace(".", "").toLowerCase() + "\n");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		question_number += 1;
		return question_number;
	}

	/**
	 * ---------------------------------------------------------------
	 * AUTHOR:         JIN SEO
	 * CREATED DATE:   2024/2/16
	 * PURPOSE:        To find the sentence that uses user's input mostly.
	 * PRECONDITIONS:  N/A
	 * POST-CONDITIONS: N/A
	 * ARGUMENTS:      Requires the file Directory(String) from the user and target phase
	 * DEPENDENCIES:   N/A
	 * ---------------------------------------------------------------
	 */
	public static int forThePhase(String fileDirectory, String word, int question_number) {
		ArrayStack<String> sentenceWithMaxOccurrences = new ArrayStack<>(10000);
		int maxOccurrences = 0;

		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			StringBuilder textBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				textBuilder.append(line).append("\n"); // Ensure newline characters are retained
			}

			// Split text into sentences based on periods, exclamation marks, question marks, and newlines occurring together
			String[] sentences = textBuilder.toString().split("(?<=[.!?\\n]|^)(?!\\s*$)");

			Pattern pattern = Pattern.compile("\\b" + word.trim() + "\\b", Pattern.CASE_INSENSITIVE);

			for (String sentence : sentences) {
				Matcher matcher = pattern.matcher(sentence);
				int occurrences = 0;
				while (matcher.find()) {
					occurrences++;
				}
				if (occurrences > maxOccurrences) {
					maxOccurrences = occurrences;
					sentenceWithMaxOccurrences.empty();
					sentenceWithMaxOccurrences.push(sentence.trim()); // Trim to remove leading/trailing whitespace
				} else if (occurrences == maxOccurrences) {
					sentenceWithMaxOccurrences.push(sentence.trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayStack<String> temp = new ArrayStack<>(1000);
		while(!sentenceWithMaxOccurrences.isEmpty()){
			temp.push(sentenceWithMaxOccurrences.pop());
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Question" + question_number + ".txt"), "utf-8"))) {
			while (!temp.isEmpty()) {
				String sentence = temp.pop().trim();
				if (!sentence.isEmpty()) {
					writer.write(word.trim() + ":" + maxOccurrences + ":" + sentence.replaceAll("[.!?]$", "").toLowerCase() + "\n");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		question_number += 1;
		return question_number;
	}

	public static void highestFrequencyInSentence(String fileDirectory) {
		ArrayStack<String> mostFrequentWords = new ArrayStack<>(1000);
		ArrayStack<String> sentencesWithFrequent = new ArrayStack<>(100);
		ArrayStack<String> sentencesWithoutDuplicates = new ArrayStack<>(50);
		ArrayStack<String> wordsWithoutDuplicates = new ArrayStack<>(50);
		ArrayStack<String> print = new ArrayStack<>(100);
		ArrayStack<String> temp2 = new ArrayStack<>(100);

		int maxFrequency = 0;

		try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
			String line;

			while ((line = reader.readLine()) != null) {
				String[] sentences = line.split("(?<=[.!?\\n])\\s+(?!$)");
				for (String sentence : sentences) {
					String[] words = sentence.split("\\s+");
					for (String word : words) {
						word = word.toLowerCase();
						int count = countWordFrequency(word, sentence);
						if (count > maxFrequency) {
							maxFrequency = count;
							mostFrequentWords.empty();
							sentencesWithFrequent.empty();
							mostFrequentWords.push(word);
							if (!sentencesWithFrequent.contains(sentence)) {
								sentencesWithFrequent.push(sentence);
							}
						} else if (count == maxFrequency) {
							mostFrequentWords.push(word);
							sentencesWithFrequent.push(sentence);
						}
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Print the most frequent words, along with their frequency and corresponding sentences
		while (!sentencesWithFrequent.isEmpty()) {
			String sentence = sentencesWithFrequent.pop();
			if (!sentencesWithFrequent.isEmpty()){
				String nextSentence = sentencesWithFrequent.peek();
				if (!nextSentence.equalsIgnoreCase(sentence)){
					sentencesWithoutDuplicates.push(sentence);
					}
			}
			else{
				sentencesWithoutDuplicates.push(sentence);
			}
			// Print each occurrence of the most frequent word along with its frequency and corresponding sentence
		}
		while (!mostFrequentWords.isEmpty()) {
			String word = mostFrequentWords.pop();
			ArrayStack<String> tempStack = new ArrayStack<>();

			// Compare the current word with every other word in the stack
			while (!mostFrequentWords.isEmpty()) {
				String nextWord = mostFrequentWords.pop();
				if (!word.equalsIgnoreCase(nextWord)) {
					tempStack.push(nextWord); // Push non-duplicate words to the temporary stack
				}
			}

			// Restore the words back to the original stack, excluding duplicates
			while (!tempStack.isEmpty()) {
				mostFrequentWords.push(tempStack.pop());
			}
			// Print the unique word
			wordsWithoutDuplicates.push(word);
		}
		while (!sentencesWithoutDuplicates.isEmpty()) {
			ArrayStack<String> temp = new ArrayStack<>(100);

			String sentence = sentencesWithoutDuplicates.pop();
			while (!wordsWithoutDuplicates.isEmpty()) {
				String word = wordsWithoutDuplicates.pop();
				// Push the word into a temporary stack
				temp.push(word);
				// Check if the word's frequency in the sentence matches the max frequency
				if (countWordFrequency(word, sentence) == maxFrequency) {
					print.push(word + ":" + maxFrequency + ":" + sentence);
				}
			}
			// Restore the words back to the original stack
			while (!temp.isEmpty()) {
				wordsWithoutDuplicates.push(temp.pop());
			}


		}
		while(!print.isEmpty()){
			temp2.push(print.pop());
		}
		if (!temp2.isEmpty()) {
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Question3.txt"), "utf-8"))) {
				while (!temp2.isEmpty()) {
					writer.write(temp2.pop().replace(".", "").toLowerCase() + "\n");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}






	}

	private static int countWordFrequency(String word, String text) {
		Pattern pattern = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
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
