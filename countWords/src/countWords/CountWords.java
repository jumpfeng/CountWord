package countWords;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class CountWords {

	public static void main(String[] args) {	
		
		String fileName = "passage.txt"; 

		List<String> mostUsedWordsList = new ArrayList<String>();

		// read file to a string
		String dataString = readFile(fileName).toString();

		// count the words in the string
		int count = totalWordCount(dataString);

		System.out.println("The total number of words in the file of "+"\""+ fileName +"\" is "+  count);
		System.out.println();

		List<Entry<String, Integer>> countWordList = createSortedWordCountList(dataString);

		// identify the top 10 words used and display them in sorted order
		mostUsedWordsList = getFirstTenUsedWords(countWordList);

		System.out.println();
		// get the last sentence on the file that contains the most used word.
		getLastSentenceWithMostUsedWord(dataString, mostUsedWordsList.get(0));

	}
	
	//read the file
	public static StringBuilder readFile(String fileName) {

		StringBuilder data = new StringBuilder();

		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				data.append(myReader.nextLine());
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}		
		return data;
	}
	
	//get the word string array
	public static String[] getStringArr(String string) {
		String filterString = string.codePoints().mapToObj(Character::toChars).filter(
				a -> (a.length == 1 && (Character.isLetterOrDigit(a[0]) || Character.isSpaceChar(a[0]) || a[0] == '-')))
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();		

		String fiterAgainString = filterString;		

		String[] strings = fiterAgainString.trim().split("\\s+");

		return strings;
	}

	//get string with period
	public static String getStringWithPeriod(String string) {
		String filterString = string.codePoints().mapToObj(Character::toChars).filter(a -> (a.length == 1
				&& (Character.isLetterOrDigit(a[0]) || Character.isSpaceChar(a[0]) || a[0] == '.' || a[0] == '-')))
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
		return filterString;
	}
	
	//get last sentence for the whole string
	public static String getLastSentence(String string) {

		String resultString = "";
		StringBuilder stringBuilder = new StringBuilder(string);
		stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
		resultString = stringBuilder.substring(stringBuilder.lastIndexOf(".") + 1);
		return resultString;
	}

	//delete the last sentence for the whole string
	public static String deleteLastSentence(String string) {

		String resultString = "";
		StringBuilder stringBuilder = new StringBuilder(string);
		stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
		resultString = stringBuilder.delete(stringBuilder.lastIndexOf(".") + 1, stringBuilder.length()).toString();
		return resultString;
	}

	//count the total number of words
	public static int totalWordCount(String string) {

		String[] strings = getStringArr(string);
		return strings.length;
	}

	//create the list of sorted word used 
	public static List<Entry<String, Integer>> createSortedWordCountList(String string) {

		List<Entry<String, Integer>> resultList = new ArrayList<Map.Entry<String, Integer>>();
		String[] strings = getStringArr(string);
		Map<String, Integer> map = new HashMap<String, Integer>();

		for (String wordString : strings) {
			wordString = wordString.toLowerCase();
			map.put(wordString, map.containsKey(wordString) ? map.get(wordString) + 1 : 1);
		}

		LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
		map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

		resultList = new ArrayList<>(reverseSortedMap.entrySet());
		return resultList;

	}
	
	//get the top 10 used words in a list
	public static List<String> getFirstTenUsedWords(List<Entry<String, Integer>> list) {
		List<String> resultList = new ArrayList<String>();
		System.out.println("The top 10 words used in the file: ");		
		for (int i = 0; i < list.size(); i++) {
			if (i==9) {
				System.out.print(list.get(i).getKey() + " " + list.get(i).getValue());
			}else {
				System.out.print(list.get(i).getKey() + " " + list.get(i).getValue() + ", ");
			}			
			resultList.add(list.get(i).getKey());
			if (i >= 9) {
				break;
			}
		}
		System.out.println();
		return resultList;
	}

	//Find and display the last sentence on the file that contains the most used word
	public static void getLastSentenceWithMostUsedWord(String string, String mostUsedWord) {

		String lastSentenceString = getLastSentence(string);
		String stringWithLastSentenceDeleted = "";

		if (ifWordInSentence(lastSentenceString, mostUsedWord)) {
			System.out.println("The last sentence with most used word of " + "\"" + mostUsedWord + "\""+" is " + "\""
					+ lastSentenceString.trim() + "." + "\"");
		} else {
			stringWithLastSentenceDeleted = deleteLastSentence(string);
			getLastSentenceWithMostUsedWord(stringWithLastSentenceDeleted, mostUsedWord);
		}
	}

	//check if word in a sentence
	public static boolean ifWordInSentence(String sentenceString, String word) {

		String filterString = getStringWithPeriod(sentenceString);
		String[] strings = filterString.split("\\s+");

		for (String wordString : strings) {
			if (wordString.equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

}
