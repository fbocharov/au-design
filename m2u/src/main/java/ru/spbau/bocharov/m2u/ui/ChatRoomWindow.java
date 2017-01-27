package ru.spbau.bocharov.m2u.ui;

import ru.spbau.bocharov.m2u.core.Chat;
import ru.spbau.bocharov.m2u.core.ChatRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.*;

class ChatRoomWindow extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private String username;
    private int historySize = 0;
    private JTextArea chatArea = new JTextArea();
    private JTextField messageField = new JTextField();

    ChatRoomWindow(String title, long roomId, String name, Chat chat) {
        username = name;

        setTitle(title);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());


        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("SEND");
        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                chat.sendMessage(roomId, message);
                messageField.setText("");
                chatArea.append(createChatAreaText(username, message));
            }
        });
        messagePanel.add(sendButton, BorderLayout.LINE_END);
        add(messagePanel, BorderLayout.PAGE_END);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {
                chat.closeRoom(roomId);
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }

    void usernameChanged(String newUsername) {
        chatArea.append(createUsernameChangeMessate(username, newUsername));
        username = newUsername;
    }

    void roomChanged(ChatRoom model) {
        String name = getTitle();
        String newName = model.getUsername();
        if (!Objects.equals(name, newName)) {
            setTitle(newName);
            chatArea.append(createUsernameChangeMessate(name, newName));
            name = newName;
        }

        java.util.List<String> history = model.getHistory();
        for (int i = historySize; i < history.size(); ++i) {
            chatArea.append(createChatAreaText(name, history.get(i)));
        }
        historySize = history.size();
    }

    private static String createUsernameChangeMessate(String oldName, String newName) {
        return String.format("%s changed name to %s\n", oldName, newName);
    }

    private static String createChatAreaText(String user, String text) {
        return String.format("%s: %s\n", user, text);
    }
}
