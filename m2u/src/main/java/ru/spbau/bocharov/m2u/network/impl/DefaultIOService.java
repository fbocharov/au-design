package ru.spbau.bocharov.m2u.network.impl;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.bocharov.m2u.network.IIOService;
import ru.spbau.bocharov.m2u.network.IMessageReceiver;
import ru.spbau.bocharov.m2u.protocol.Protocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultIOService implements IIOService {

    private final static Logger log = LogManager.getLogger(DefaultIOService.class);

    private static long nextConnectionId = 0;

    private final short port;
    private final List<IMessageReceiver> receivers = new CopyOnWriteArrayList<>();
    private final Map<Long, Connection> connections = new HashMap<>();

    public DefaultIOService(short p) {
        port = p;
    }
    
    @Override
    public void start() throws IOException {
        try (ServerSocket ss = new ServerSocket(port)) {
            log.info("started server on port " + port);
            while (!ss.isClosed()) {
                Socket s = ss.accept();
                log.info("accepted connection from " + s.getInetAddress().toString());

                long connectionId = nextConnectionId++;
                connections.put(connectionId, new Connection(connectionId, s));
            }
        }
    }

    @Override
    public void registerMessageReceiver(IMessageReceiver receiver) {
        receivers.add(receiver);
    }

    @Override
    public void send(long connectionId, Protocol.Message message) throws Exception {
        Connection connection = connections.get(connectionId);
        if (connection == null) {
            throw new Exception("no connection with id " + connectionId);
        }

        connection.send(message);
    }

    @Override
    public void close(long connectionId) throws Exception {
        Connection connection = connections.get(connectionId);
        if (connection == null) {
            throw new Exception("no connection with id " + connectionId);
        }

        connection.close();
        connections.remove(connectionId);
    }

    private class Receiver implements Runnable {

        private final long connectionId;
        private final InputStream stream;

        Receiver(long id, InputStream in) {
            connectionId = id;
            stream = in;
        }

        @Override
        public void run() {
            try (DataInputStream in = new DataInputStream(stream)) {
                while (!Thread.currentThread().isInterrupted()) {
                    int size = in.readInt();
                    byte[] bytes = new byte[size];
                    IOUtils.read(in, bytes);

                    Protocol.Message message = Protocol.Message.parseFrom(bytes);
                    for (IMessageReceiver receiver: receivers) {
                        receiver.receive(connectionId, message);
                    }
                }

            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private class Sender implements Runnable {

        private final OutputStream stream;
        private final Queue<Protocol.Message> messageQueue = new LinkedList<>();

        Sender(OutputStream s) {
            stream = s;
        }

        void send(Protocol.Message message) {
            synchronized (messageQueue) {
                messageQueue.add(message);
                messageQueue.notify();
            }
        }

        @Override
        public void run() {
            try (DataOutputStream out = new DataOutputStream(stream)) {
                while (!Thread.currentThread().isInterrupted()) {
                    Protocol.Message message;
                    synchronized (messageQueue) {
                        while (messageQueue.isEmpty()) {
                            messageQueue.wait();
                        }
                        message = messageQueue.poll();
                        messageQueue.notify();
                    }

                    byte[] bytes = message.toByteArray();
                    out.writeInt(bytes.length);
                    out.write(bytes);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Connection {

        private final Socket socket;
        private final Thread receiveThread;
        private final Sender sender;
        private final Thread sendThread;

        Connection(long id, Socket s) throws IOException {
            socket = s;
            receiveThread = new Thread(new Receiver(id, s.getInputStream()));
            sender = new Sender(s.getOutputStream());
            sendThread = new Thread(sender);

            receiveThread.start();
            sendThread.start();
        }

        void send(Protocol.Message message) {
            sender.send(message);
        }

        void close() throws InterruptedException, IOException {
            receiveThread.interrupt();
            sendThread.interrupt();
            receiveThread.join();
            sendThread.join();

            socket.close();
        }
    }
}
