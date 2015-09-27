package org.taptech.app;

 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.Properties;
 import javax.mail.*;
 import javax.mail.search.SearchTerm;

/**
 * This program demonstrates how to search for e-mail messages which satisfy
 * a search criterion.
 * @author www.codejava.net
 *
 */
public class EmailSearcher {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Searches for e-mail messages containing the specified keyword in
     * Subject field.
     * @param host
     * @param port
     * @param userName
     * @param password
     * @param keyword
     */
    public void searchEmail(String host, String port, String userName,
                            String password, final String keyword) {

        Properties properties = new Properties();

        // server setting
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", port);

        // SSL setting
        properties.setProperty("mail.imap.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.socketFactory.port",
                String.valueOf(port));

        Session session = Session.getDefaultInstance(properties);

        try {
            final Date searchDate = dateFormat.parse("2015-08-28");
            // connects to the message store
            Store store = session.getStore("imap");
            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // creates a search criterion
            SearchTerm searchCondition = new SearchTerm() {

                @Override
                public boolean match(Message message) {
                    try {
//                        Address[] fromAddress = message.getFrom();
//                        if (fromAddress != null && fromAddress.length > 0) {
//                            if (fromAddress[0].toString().contains("glawson6@gmail.com")) {
//                                return true;
//                            }
//                        }
                        if (message.getReceivedDate().after(searchDate)) {
                            return true;
                        }
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                    return false;
                }
            };

            // performs search through the folder
            Message[] foundMessages = folderInbox.search(searchCondition);

            for (int i = 0; i < foundMessages.length; i++) {
                Message message = foundMessages[i];
                String subject = message.getSubject();
                System.out.println("Found message #" + i + ": " + subject);
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store.");
            ex.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test this program with a Gmail's account
     */
    public static void main(String[] args) {
        String host = "imap.mail.yahoo.com";
        String port = "993";
        String userName = "glawson6";
        String password = "Bigg6dogg!";
        EmailSearcher searcher = new EmailSearcher();
        String keyword = "JavaMail";
        searcher.searchEmail(host, port, userName, password, keyword);
    }

}
