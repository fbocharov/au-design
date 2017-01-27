package ru.spbau.bocharov.m2u.ui;

import ru.spbau.bocharov.m2u.core.Chat;
import ru.spbau.bocharov.m2u.core.ChatRoom;
import ru.spbau.bocharov.m2u.core.IChatRoomListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainWindow extends JFrame implements IChatRoomListener {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 300;

    private final Chat chat;
    private final Map<Long, ChatRoomWindow> chatRooms = new ConcurrentHashMap<>();

    public MainWindow(String title, Chat c) {
        chat = c;

        setTitle(title);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setJMenuBar(createMenuBar());

        chat.registerChatRoomListener(this);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenuItem startChating = new JMenuItem("Start chat");
        startChating.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(2, 2));

            JTextField ipField = new JTextField();
            panel.add(new JLabel("IP:"));
            panel.add(ipField);

            JTextField portField = new JTextField();
            panel.add(new JLabel("Port:"));
            panel.add(portField);

            int option = JOptionPane.showConfirmDialog(null, panel, "User ip and port",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String ip = ipField.getText();
                String port = portField.getText();
                if (ip.isEmpty() || port.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "IP and port shouldn't be empty!",
                            "Bad address", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        chat.createRoom(ip, Short.valueOf(port));
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Something went wrong: " + e1.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });

        JMenuItem changeUsername = new JMenuItem("Change username");
        changeUsername.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(1, 2));
            JTextField usernameField = new JTextField();

            panel.add(new JLabel("Username: "));
            panel.add(usernameField);

            int option = JOptionPane.showConfirmDialog(null, panel, "Change username", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username can't be empty!",
                            "Bad username", JOptionPane.ERROR_MESSAGE);
                } else {
                    chat.setUsername(username);
                }
            }
        });
        JMenu options = new JMenu("Options");
        options.add(startChating);
        options.add(changeUsername);

        menuBar.add(options);

        return menuBar;
    }

    @Override
    public void processUsernameChange(String newUsername) {
        for (ChatRoomWindow window: chatRooms.values()) {
            window.usernameChanged(newUsername);
        }
    }

    @Override
    public void processRoomChange(ChatRoom room) {
        long roomId = room.getId();
        if (!chatRooms.containsKey(roomId)) {
            ChatRoomWindow window = new ChatRoomWindow(room.getUsername(), roomId, chat.getUsername(), chat);
            chatRooms.put(roomId, window);
            window.setVisible(true);
        }

        ChatRoomWindow window = chatRooms.get(roomId);
        if (window != null) {
            window.roomChanged(room);
        }
    }
}
