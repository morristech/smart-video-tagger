package at.favre.tools.tagger.analyzer.util;

import at.favre.tools.tagger.analyzer.config.ConfigManager;
import at.favre.tools.tagger.analyzer.config.FilterWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author PatrickF
 * @since 24.03.13
 */
public class TitleCleaner {

	public static String cleanTitle(String fileName) {
		String s = removeBrackets(fileName);
		s = removeWhiteSpaceSubstitutes(s);

		List<String> chunkList = new ArrayList<String>();
		for(String chunk : Arrays.asList(s.split(" "))) {
			chunk=removeIgnoreWords(chunk);
			chunk=removeNonWords(chunk);
			chunk=removeSpecialChars(chunk);
			chunk=chunk.trim();
			chunk=chunk.toLowerCase(Locale.US);

			if(chunk.length() > 0) {
				chunkList.add(chunk);
			}
		}

		StringBuilder sb = new StringBuilder();
		for(String part: chunkList) {
			sb.append(part+" ");
		}

		return sb.toString();
	}

	public static String removeWhiteSpaceSubstitutes(String s) {
		s = s.replaceAll("_"," ");
		s = s.replaceAll("\\."," ");
		s = s.replaceAll("%20"," ");
		return s;
	}

	private static String removeIgnoreWords(String s) {
		for(FilterWord ignoreWord : ConfigManager.getInstance().getIgnoreWords()) {
			if(ignoreWord.isIgnoreCase() && s.matches("(?i)" + ignoreWord.getWord())) {
				return "";
			} else if(s.matches(ignoreWord.getWord())){
				return "";
			}
		}
		return s;
	}

	private static String removeBrackets(String s) {
		s = s.replaceAll("\\[.*\\]","");
		s = s.replaceAll("\\(.*\\)","");
		s = s.replaceAll("\\{.*\\}","");

		return s;
	}

	private static String removeNonWords(String s) {
		s = s.replaceAll("[^\\w\\d][^\\s]+\\w*",""); //start with special char
		s = s.replaceAll("^\\D+([0-9]|-)+\\w*",""); //start with letter but has digits
		s = s.replaceAll("^\\d+\\D+(\\d|\\D)*",""); //start with digit but has letter
		return s;
	}

	private static String removeSpecialChars(String s) {
		s = s.replaceAll("[^\\w\\s\\d]","");
		return s;
	}

}
