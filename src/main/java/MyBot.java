import org.telegram.telegrambots.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

public class MyBot extends TelegramLongPollingBot {
    private static final String[] filters = {"http://", "www.", "https://"};
    private static final String[] allowedUserNames = {"ibrahimGolshani","Najmeddini","s_maryam_ghasemi","Behshadmousavii","Rosahosseini","Matin_Shg","Royaros","Maarrjjaannnnn"};
    private static final String groupLink = "https://t.me/joinchat/AAAAAERRN6DmKseWKBVIHQ";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            // Set variables
            for (int i = 0; i < allowedUserNames.length; ++i) {
                if ( update.getMessage().getFrom().getUserName()!= null && update.getMessage().getFrom().getUserName().equals(allowedUserNames[i]))
                    return;
            }

            if (!isReplyToMessage(update) && isNotAllowedMessage(update)) {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(String.valueOf(update.getMessage().getChat().getId()));
                deleteMessage.setMessageId(update.getMessage().getMessageId());
                try {
                    execute(deleteMessage); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            //remove bot if it is added by a person
            boolean botAdded = false;
            if (update.getMessage().getNewChatMembers() != null) {
                List<User> users = update.getMessage().getNewChatMembers();
                for (int i = 0; i < users.size(); ++i) {
                    if (users.get(i).getBot()) {
                        botAdded = true;
                        KickChatMember kickChatMember = new KickChatMember();
                        kickChatMember.setChatId(update.getMessage().getChatId());
                        kickChatMember.setUserId(users.get(i).getId());
                        try {
                            execute(kickChatMember); // Sending our message object to user
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (botAdded) {
                    KickChatMember kickChatMember = new KickChatMember();
                    kickChatMember.setChatId(update.getMessage().getChatId());
                    kickChatMember.setUserId(update.getMessage().getFrom().getId());
                    try {
                        execute(kickChatMember); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

    private boolean isNotAllowedMessage(Update update) {
        String message_text = update.getMessage().hasText() ? update.getMessage().getText() : "";
        String caption = update.getMessage().getCaption() != null ? update.getMessage().getCaption() : "";
        for (int i = 0; i < filters.length; ++i) {
            if ((message_text.toLowerCase().contains(filters[i]) && !message_text.toLowerCase().contains(groupLink.toLowerCase())) ||
                    update.getMessage().getNewChatMembers() != null ||
                    update.getMessage().getLeftChatMember() != null ||
                    (caption.toLowerCase().contains(filters[i]) && !caption.toLowerCase().contains(groupLink.toLowerCase()))
                    )
                return true;
        }
        return false;
    }

    private boolean isReplyToMessage(Update update) {
        return update.getMessage().getReplyToMessage() != null;
    }

    @Override
    public String getBotUsername() {
        return "startupnetworkbot";
    }

    @Override
    public String getBotToken() {
        return "364641381:AAEmndPUow9d73uynPJBncsHHnMsuwyZq_Q";
    }
}
