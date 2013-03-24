package at.favre.tools.tagger.analyzer.matcher;

import at.favre.tools.tagger.system.ConfigManager;
import at.favre.tools.tagger.system.FilterWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author PatrickF
 * @since 24.03.13
 */
public class TitleAnalyser {

	public static List<String> analyseTitle(String fileName) {
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


		return chunkList;
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


	private static List<String> tryToExtractTitleFromChunks(List<String> chunks) {
		List<String> cleanedChunks = new ArrayList<String>();
		int emptyChunks = 0;

		for(int i=0,ii=chunks.size();i<ii;i++) {
			if(chunks.get(i).length() == 0) {
				emptyChunks++;
			} else {
				cleanedChunks.add(chunks.get(i).trim());
			}
		}
		return cleanedChunks;
	}
}
