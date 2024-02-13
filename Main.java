import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
   public static void main(String[] args) {
      int wordCount = 0;
      ArrayStack<String> stack = new ArrayStack<>(10000);
      int[] counts = new int[10000];
      String filePath = "C:/Users/seoji/Downloads/data (1)/data/tiny1.txt";
      try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
         String line;
         while ((line = reader.readLine()) != null) {
            // Use regular expression to find words from ChatGPT
            Pattern pattern = Pattern.compile("\\b\\w+\\b");
            Matcher matcher = pattern.matcher(line);

            // Process each word
            while (matcher.find()) {
               String word = matcher.group().toLowerCase();
               int index = indexOfWord(stack, word, wordCount);
               if (index == -1) {
                  stack.push(word);
                  counts[wordCount] = 1;
                  wordCount++;
               } else {
                  counts[index]++;
               }
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      String sentenceWithMaxOccurrences = "";
      int maxOccurrences = 0;

      try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
         StringBuilder textBuilder = new StringBuilder();
         String line;
         while ((line = reader.readLine()) != null) {
            textBuilder.append(line).append(" ");
         }

         String[] sentences = textBuilder.toString().split("[.!?]");

         Pattern pattern = Pattern.compile("\\bthe\\b", Pattern.CASE_INSENSITIVE);

         for (String sentence : sentences) {
            Matcher matcher = pattern.matcher(sentence);
            int occurrences = 0;
            while (matcher.find()) {
               occurrences++;
            }
            if (occurrences > maxOccurrences) {
               maxOccurrences = occurrences;
               sentenceWithMaxOccurrences = sentence.trim(); // Trim to remove leading/trailing whitespace
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }


      // Print the word counts
      System.out.println("Total words: " + stack.getSizeOfWords());
      int tempIndex = 0;
      for(int i = 1; i< wordCount; i++){
         //keeping the index that contains most frequent words in the file
         if(counts[i] > counts[tempIndex]){
            tempIndex = i;
         }
      }
      System.out.println("The most frequent words in the whole file is : " + stack.getIndex(tempIndex));
      System.out.println("It had the frequency of " + counts[tempIndex]);



      //Second question
      int firstHighestIndex = 0;
      int secondHightestIndex = 0;
      int thirdHighestIndex = 0;
      for(int i = 1; i< wordCount; i++) {
         if (counts[i] > counts[firstHighestIndex]) {
            thirdHighestIndex = secondHightestIndex;
            secondHightestIndex = firstHighestIndex;
            firstHighestIndex = i;
         } else if (counts[i] > counts[secondHightestIndex]) {
            thirdHighestIndex = secondHightestIndex;
            secondHightestIndex = i;
         } else if (counts[i] > counts[thirdHighestIndex]) {
            thirdHighestIndex = i;
         }
      }

      System.out.println("The 3rd most frequent words in the file is " + stack.getIndex(thirdHighestIndex));
      System.out.println("Its frequency is " + counts[thirdHighestIndex]);

      System.out.println("Sentence(s) with the maximum occurrences of 'the':");
      System.out.println(sentenceWithMaxOccurrences);
      System.out.println("Frequency: " + maxOccurrences);

      System.out.println("Sentence(s) with the maximum occurrences of 'of':");
      System.out.println(sentenceWithMaxOccurrences);
      System.out.println("Frequency: " + maxOccurrences);
   }
   public static void firstQuestion(){
      int wordCount = 0;

   }
   private static int indexOfWord(ArrayStack<String> words, String word, int length) {
      for (int i = 0; i < length; i++) {
         if (words.getIndex(i).equals(word)) {
            return i;
         }
      }
      return -1;
   }
}
