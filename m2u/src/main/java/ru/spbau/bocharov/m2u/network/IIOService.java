package ru.spbau.bocharov.m2u.network;

import ru.spbau.bocharov.m2u.protocol.Protocol;

import java.io.IOException;

public interface IIOService {

    void start() throws IOException;

    void registerMessageReceiver(IMessageReceiver receiver);

    void send(long connectionId, Protocol.Message message) throws Exception;

    void close(long connectionId) throws Exception;
}
