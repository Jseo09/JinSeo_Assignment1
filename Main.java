import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
   System.out.println("Enter your file directory");
    String directory = scanner.nextLine();
    System.out.println("Enter directory for the desire location (REMEMBER, when we are typing the file directory, please add \\ in the end)");
    System.out.println("EX: C:\\Users\\Example\\2024 Spring\\");
    String final_Directory = scanner.nextLine();
    System.out.println("Enter the desire output file name");
    String outPut_name = scanner.nextLine();
	 //C:\Users\seoji\Downloads\data\data\tiny1.txt
		//C:\Users\seoji\OneDrive\바탕 화면\College\2023 Fall\2024 Spring\
		int question_number = 4;
		ArrayStack<String> words = new ArrayStack<>(10000);
		ArrayStack<String> tempStack = new ArrayStack<>(10000);
		ArrayStack<String> tempStack2 = new ArrayStack<>(10000);
		ArrayStack<String> tempStack3 = new ArrayStack<>(10000);
		ArrayStack<String> tempStack4 = new ArrayStack<>(10000);
		ArrayStack<String> tempStack5 = new ArrayStack<>(10000);

		try (BufferedReader reader = new BufferedReader(new FileReader(directory))) {
			String line;
			while ((line = reader.readLine()) != null) {
				words.push(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (!words.isEmpty()) {
			String word = words.pop();
			tempStack.push(word);
			tempStack2.push(word);
			tempStack3.push(word);
			tempStack4.push(word);
			tempStack5.push(word);

		}
		getMostWordCount(tempStack, final_Directory, outPut_name);
		question_number = getMostWordsForSpecificWord(tempStack, final_Directory,outPut_name,"the", question_number);
		question_number = getMostWordsForSpecificWord(tempStack, final_Directory,outPut_name,"of", question_number);
		question_number = getMostWordsForSpecificWord(tempStack, final_Directory,outPut_name,"was", question_number);
		question_number = forThePhase(tempStack5, final_Directory, outPut_name, "but the",question_number);
		question_number = forThePhase(tempStack2, final_Directory, outPut_name, "it was", question_number);
		question_number = forThePhase(tempStack3, final_Directory, outPut_name,"in my",  question_number);
		highestFrequencyInSentence(tempStack4, final_Directory, outPut_name);

	}
	public static void highestFrequencyInSentence(ArrayStack<String> stack, String finalDirectory, String outPutname) {
		ArrayStack<String> mostFrequentWords = new ArrayStack<>(1000);
		ArrayStack<String> sentencesWithFrequent = new ArrayStack<>(100);
		ArrayStack<String> sentencesWithoutDuplicates = new ArrayStack<>(50);
		ArrayStack<String> wordsWithoutDuplicates = new ArrayStack<>(50);
		ArrayStack<String> print = new ArrayStack<>(1000);
		ArrayStack<String> temp2 = new ArrayStack<>(1000);

		int maxFrequency = 0;
		String line;
		while (!stack.isEmpty()) {
			line = stack.pop();
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
		while (!sentencesWithFrequent.isEmpty()) {
			String sentence = sentencesWithFrequent.pop();
			if (!sentencesWithFrequent.isEmpty()) {
				String nextSentence = sentencesWithFrequent.peek();
				if (!nextSentence.equalsIgnoreCase(sentence)) {
					sentencesWithoutDuplicates.push(sentence);
				}
			} else {
				sentencesWithoutDuplicates.push(sentence);
			}
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
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(finalDirectory+ outPutname+"-3.txt"), "utf-8"))) {
				while (!temp2.isEmpty()) {
					writer.write(temp2.pop().replace(".", "").toLowerCase() + "\n");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
	private static void getMostWordCount(ArrayStack<String> stack, String outputDirectory, String output_name) {
		int wordCount = 0;
		ArrayStack<String> words = new ArrayStack<>(10000);
		ArrayStack<Integer> counts = new ArrayStack<>(10000);
		ArrayStack<String> temp = new ArrayStack<>(10000);

		while (!stack.isEmpty()) {
			String line = stack.pop().toLowerCase();
			temp.push(line);
			Pattern pattern = Pattern.compile("\\b\\w+\\b");
			Matcher matcher = pattern.matcher(line);

			while (matcher.find()) {
				String word = matcher.group().toLowerCase();
				int index = indexOfWord(words, word, wordCount);

				if (index == -1) {
					words.push(word);
					counts.push(1);
					wordCount++;
				} else
					counts.setIndex(index, counts.getIndex(index) + 1);
			}
		}

		while (!temp.isEmpty())
			stack.push(temp.pop());

		ArrayStack<Integer> indexKeeper = new ArrayStack<>(100);

		// Find max count and update indexKeeper
		int maxCount = counts.getIndex(0);
		for (int i = 1; i < wordCount; i++) {
			int count = counts.getIndex(i);
			if (count > maxCount) {
				maxCount = count;
				indexKeeper.empty();
				indexKeeper.push(i);
			} else if (count == maxCount) {
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
		for (int i = 0; i < wordCount; i++) {
			if (counts.getIndex(i) == counts.getIndex(thirdHighestIndex)) {
				indexForThird.push(i);
			}
		}

		while (!indexForThird.isEmpty()) {
			int index = indexForThird.pop();
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDirectory+ output_name + "-2.txt"), "utf-8"))) {
				writer.write(words.getIndex(index).toLowerCase() + ":" + counts.getIndex(index));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}



		//PRINT
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDirectory+output_name+"-1.txt"), "utf-8"))) {
			for (int i = 0; i < indexKeeper.getSizeOfWords(); i++) {
				int index = indexKeeper.getIndex(i);
				writer.write(words.getIndex(index) + ":" + maxCount+"\n");

			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	public static int getMostWordsForSpecificWord(ArrayStack<String> stack, String outputDirectory, String outputName, String word, int question_number) {
		ArrayStack<String> sentenceWithMaxOccurrences = new ArrayStack<>(10000);
		ArrayStack<Integer> maxOccurrencesStack = new ArrayStack<>(10000);
		ArrayStack<String> temp = new ArrayStack<>(10000);
		int maxOccurrences = 0;
		StringBuilder textBuilder = new StringBuilder();

		while (!stack.isEmpty()) {
			String line = stack.pop();
			temp.push(line);
			textBuilder.append(line).append("\n");
		}

		while (!temp.isEmpty())
			stack.push(temp.pop());

		String[] paragraphs = textBuilder.toString().split("(?m)(?=\\n\\s*\\n)");

		for (String paragraph : paragraphs) {
			String[] sentences = paragraph.trim().split("(?<=[.!?\\n])\\s+");
			Pattern pattern = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);

			for (String sentence : sentences) {
				Matcher matcher = pattern.matcher(sentence);
				int occurrences = 0;
				while (matcher.find()) {
					occurrences++;
				}
				if (occurrences > maxOccurrences) {
					maxOccurrences = occurrences;
					sentenceWithMaxOccurrences.empty();
					sentenceWithMaxOccurrences.push(sentence.trim());
					maxOccurrencesStack.empty();
					maxOccurrencesStack.push(occurrences);
				} else if (occurrences == maxOccurrences) {
					sentenceWithMaxOccurrences.push(sentence.trim());
					maxOccurrencesStack.push(occurrences);
				}
			}
		}
		while(!sentenceWithMaxOccurrences.isEmpty()){
			temp.push(sentenceWithMaxOccurrences.pop());
		}
		if (!temp.isEmpty()) {
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDirectory+outputName+"-"+question_number+".txt"), "utf-8"))) {
				while (!temp.isEmpty()) {
					writer.write(word + ":" + maxOccurrences + ":" + temp.pop().replace(".", "").toLowerCase() + "\n");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		question_number++;
		return question_number;
	}
	public static int forThePhase(ArrayStack<String> stack, String outputDirectory, String outputName, String phrase, int question_number) {
		ArrayStack<String> sentenceWithMaxOccurrences = new ArrayStack<>(10000);
		ArrayStack<String> sentences_All = new ArrayStack<>(1000);
		int maxOccurrences = 0;

		StringBuilder textBuilder = new StringBuilder();
		String line;

		while (!stack.isEmpty()) {
			line = stack.pop();
			textBuilder.append(line).append("\n");
		}
		String[] sentences = textBuilder.toString().split("(?<=[.!?\\n]|^)(?!\\s*$)");
		for (String sentence : sentences) {
			sentences_All.push(sentence);
			// Check if the sentence contains the phrase
			if (sentence.toLowerCase().contains(phrase.toLowerCase())) {
				// Count occurrences of the phrase within the sentence
				int occurrences = countOccurrences(sentence.toLowerCase(), phrase.toLowerCase());
				if (occurrences > maxOccurrences) {
					maxOccurrences = occurrences;
					sentenceWithMaxOccurrences.empty();
					sentenceWithMaxOccurrences.push(sentence.trim()); // Trim to remove leading/trailing whitespace
				} else if (occurrences == maxOccurrences) {
					sentenceWithMaxOccurrences.push(sentence.trim());
				}
			}
		}

		ArrayStack<String> temp = new ArrayStack<>(1000);
		while (!sentenceWithMaxOccurrences.isEmpty()) {
			temp.push(sentenceWithMaxOccurrences.pop());
		}


		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDirectory + outputName+"-" + question_number + ".txt"), "utf-8"))) {
			if(temp.isEmpty()){
				ArrayStack<String> temp2 = new ArrayStack<>(1000);
				while(!sentences_All.isEmpty()){
					temp2.push(sentences_All.pop().trim().toLowerCase());
				}
				while(!temp2.isEmpty()){
					String sentence = temp2.pop().trim();
					if(!sentence.isEmpty()){
						writer.write(phrase.trim() + ":" + maxOccurrences + ":" + sentence.replaceAll("[.!?]$", "")+"\n");
					}
				}
			}
			while (!temp.isEmpty()) {
				String sentence = temp.pop().trim();
				if (!sentence.isEmpty()) {
					writer.write(phrase.trim() + ":" + maxOccurrences + ":" + sentence.replaceAll("[.!?]$", "").toLowerCase() + "\n");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		question_number++;
		return question_number;
	}
	public static int countOccurrences(String sentence, String phrase) {
		int count = 0;
		int index = sentence.indexOf(phrase);
		while (index != -1) {
			count++;
			index = sentence.indexOf(phrase, index + 1);
		}
		return count;
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
