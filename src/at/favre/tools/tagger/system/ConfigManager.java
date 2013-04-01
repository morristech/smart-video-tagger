package at.favre.tools.tagger.system;

import at.favre.tools.tagger.io.filereader.WordListFileReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PatrickF
 * @since 23.03.13
 */
public class ConfigManager {
	private static final String ignoreWordsFilePath = "/ignore-words.txt";

	private static ConfigManager ourInstance = new ConfigManager();
	private Logger log = LogManager.getLogger(this.getClass().getSimpleName());

	private final List<String> extensions;
	private final List<FilterWord> ignoreWords;

	public static ConfigManager getInstance() {
		return ourInstance;
	}

	private ConfigManager() {
		extensions = readExtensions();
		ignoreWords = readIgnoreWords();
	}

	private List<String> readExtensions() {
		return new WordListFileReader("/extensions.txt").getAllItemsInTextFile();
	}

	private List<FilterWord> readIgnoreWords() {
		List<FilterWord> ignoreWordList = new ArrayList<FilterWord>();
		List<String> list =  new WordListFileReader(ignoreWordsFilePath).getAllItemsInTextFile();

		for(String ignoreWord:list) {
			if(ignoreWord.startsWith("\"") && ignoreWord.endsWith("\"")) {
				ignoreWordList.add(new FilterWord(ignoreWord.substring(1,ignoreWord.length()-1), FilterWord.CaseSensitivity.KEEP_CASE));
			} else {
				ignoreWordList.add(new FilterWord(ignoreWord, FilterWord.CaseSensitivity.IGNORE_CASE));
			}
		}

		return ignoreWordList;
	}

	public List<String> getExtensions() {
		return extensions;
	}

	public List<FilterWord> getIgnoreWords() {
		return ignoreWords;
	}
}
