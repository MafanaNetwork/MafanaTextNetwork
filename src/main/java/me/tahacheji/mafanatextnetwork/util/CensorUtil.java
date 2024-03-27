package me.tahacheji.mafanatextnetwork.util;

import me.tahacheji.mafanatextnetwork.data.MutedPlayer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class CensorUtil {

    public String getChat(List<String> x) {
        for(String s : x) {
            if(s.contains("CHAT")) {
                return s;
            }
        }
        return "";
    }

    public String getBubble(List<String> x) {
        for(String s : x) {
            if(s.contains("BUBBLE")) {
                return s;
            }
        }
        return "";
    }

    public String getStarred(List<String> x) {
        for(String s : x) {
            if(s.contains("STARRED")) {
                return s;
            }
        }
        return "";
    }

    public MutedPlayer getMutedPlayer(UUID uuid, List<MutedPlayer> mutedPlayers) {
        for(MutedPlayer mutedPlayer : mutedPlayers) {
            if(mutedPlayer.getUser().equalsIgnoreCase(uuid.toString())) {
                return mutedPlayer;
            }
        }
        return null;
    }

    public boolean isEndDateTodayOrBefore(String endDate) {
        LocalDate now = LocalDate.now();
        LocalDate endDateParsed = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("M/d/yyyy"));
        return endDateParsed.isBefore(now) || endDateParsed.isEqual(now);
    }


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
            if (word.contains(starredWord)) {
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
