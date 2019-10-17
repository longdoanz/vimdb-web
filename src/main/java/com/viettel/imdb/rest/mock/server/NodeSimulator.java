package com.viettel.imdb.rest.mock.server;

public interface NodeSimulator {
    boolean start();
    boolean stop();

    String getHost();
    void setHost(String host);
    int getPort();
    void setPort(int port);

    String getAddress();
}
