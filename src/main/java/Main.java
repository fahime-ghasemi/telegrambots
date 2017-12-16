import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.BotSession;

public class Main {
    static BotSession botSession = null;

    public static void main(String[] args) {
        // Initialize Api Context

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {

            botSession = telegramBotsApi.registerBot(new MyBot());
        }catch (Exception e)
        {

        }
//        startTelegramBot();

    }

    private static boolean isTelegramBotRunning() {
        return botSession != null && botSession.isRunning();
    }

    private static synchronized void startTelegramBot() {
        try {
            if (isTelegramBotRunning())
                stopTelegramBot();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            botSession = telegramBotsApi.registerBot(new MyBot());
            botSession.stop();
            System.out.println("Common Telegram bot started.");
        } catch (TelegramApiException e) {
            System.out.println("Catch TelegramApiException");
        }
    }

    private static void stopTelegramBot() {
        try {
            if (botSession != null) {
                botSession.stop();
                botSession = null;
            }
        } catch (Exception e) {
        }
    }
}
