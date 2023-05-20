import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws InterruptedException, Exception {
        String[] texts = new String[25];
        List<Future> futureList = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(25);
        Callable logic;
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        for (String text : texts) {
            logic = () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
                return maxSize;

            };
            Future<Integer> task = threadPool.submit(logic);
            futureList.add(task);

        }
        threadPool.shutdown();
        Integer result = 0;
        for (int i = 0; i < futureList.size(); i++) {
            if ((int) futureList.get(i).get() > result) {
                result = (int) futureList.get(i).get();
            }

        }

        System.out.println(result);

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
