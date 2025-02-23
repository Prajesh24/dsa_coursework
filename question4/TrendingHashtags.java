package question4;
import java.util.*;
import java.util.regex.*;

public class TrendingHashtags {
    public static void main(String[] args) {
        String[][] tweetsTable = {
            {"12", "14", "2024-02-15", "I am Niraj #HappyDay and I am going to the holiday #Holiday"},
            {"15", "18", "2024-02-16", "Enjoying the sunshine! #HappyDay"},
            {"20", "22", "2024-02-17", "Let's celebrate together! #Festival #HappyDay"},
            {"25", "30", "2024-02-18", "Traveling is fun! #Holiday #Adventure"},
            {"30", "35", "2024-02-19", "Exploring new places! #Adventure #Travel"}
        };
        
        Map<String, Integer> hashtagCount = new HashMap<>();
        Pattern pattern = Pattern.compile("#\\w+");
        
        for (String[] row : tweetsTable) {
            String tweet = row[3];
            Matcher matcher = pattern.matcher(tweet);
            while (matcher.find()) {
                String hashtag = matcher.group();
                hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
            }
        }
        
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet());
        sortedHashtags.sort((a, b) -> b.getValue().equals(a.getValue()) ? a.getKey().compareTo(b.getKey()) : b.getValue() - a.getValue());
        
        System.out.println("+------------+-------+");
        System.out.println("| Hashtag    | Count |");
        System.out.println("+------------+-------+");
        for (int i = 0; i < Math.min(3, sortedHashtags.size()); i++) {
            System.out.printf("| %-10s | %-5d |%n", sortedHashtags.get(i).getKey(), sortedHashtags.get(i).getValue());
        }
        System.out.println("+------------+-------+");
    }
}