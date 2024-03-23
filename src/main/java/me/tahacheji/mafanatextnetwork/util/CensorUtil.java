package me.tahacheji.mafanatextnetwork.util;

import java.util.List;

public class CensorUtil {

    public String censorMessage(String message, List<String> starredWords) {
        String[] words = message.split("\\s+");
        StringBuilder censoredMessage = new StringBuilder();

        for (String word : words) {
            String censoredWord = censorWord(word, starredWords);
            censoredMessage.append(censoredWord).append(" ");
        }

        return censoredMessage.toString().trim();
    }

    private String censorWord(String word, List<String> starredWords) {
        for (String starredWord : starredWords) {
            if (word.equalsIgnoreCase(starredWord)) {
                return starOutWord(word);
            }
        }
        return word;
    }

    private String starOutWord(String word) {
        StringBuilder starredWord = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            starredWord.append("*");
        }
        return starredWord.toString();
    }

    public boolean containsBlacklistedWord(String message, List<String> blacklistedWords) {
        String[] words = message.split("\\s+");

        for (String word : words) {
            if (blacklistedWords.contains(word.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
