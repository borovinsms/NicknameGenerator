package org.example;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Main {
    public static AtomicInteger threeDigitNickname = new AtomicInteger(0);
    public static AtomicInteger fourDigitNickname = new AtomicInteger(0);
    public static AtomicInteger fiveDigitNickname = new AtomicInteger(0);

    public static boolean isPalindrome(String str) {
        int left = 0, right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static boolean isOneLetterString(String str) {
        char[] chars = str.toCharArray();
        char currentLetter = chars[0];
        for (char ch : chars) {
            if (ch != currentLetter) {
                return false;
            } else {
                currentLetter = ch;
            }
        }
        return true;
    }

    public static boolean isSortedString(String str) {
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);
        return sorted.equals(str);
    }

    public static void incrementCounters(String str) {
        switch (str.length()) {
            case 3:
                threeDigitNickname.incrementAndGet();
                break;
            case 4:
                fourDigitNickname.incrementAndGet();
                break;
            case 5:
                fiveDigitNickname.incrementAndGet();
                break;
            default:
                System.out.println("Непредвиденное количество символов в переданной строке");
                break;
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void validateStrings(String[] strings, Function<String, Boolean> validator) {
        for (String str : strings) {
            if (validator.apply(str)) {
                incrementCounters(str);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        Thread palindromeThread = new Thread(() -> {
            validateStrings(texts, Main::isPalindrome);
        });
        Thread oneLetterThread = new Thread(() -> {
            validateStrings(texts, Main::isOneLetterString);
        });
        Thread sortedThread = new Thread(() -> {
            validateStrings(texts, Main::isSortedString);
        });
        palindromeThread.start();
        oneLetterThread.start();
        sortedThread.start();

        palindromeThread.join();
        oneLetterThread.join();
        sortedThread.join();

        System.out.format("Красивых слов с длиной 3: %d шт \n", threeDigitNickname.get());
        System.out.format("Красивых слов с длиной 4: %d шт \n", fourDigitNickname.get());
        System.out.format("Красивых слов с длиной 5: %d шт \n", fiveDigitNickname.get());
    }
}