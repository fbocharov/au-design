package ru.spbau.bocharov.m2u.network;

import ru.spbau.bocharov.m2u.protocol.Protocol;

public interface IMessageReceiver {

    void receive(long connectionId, Protocol.Message message);
}
