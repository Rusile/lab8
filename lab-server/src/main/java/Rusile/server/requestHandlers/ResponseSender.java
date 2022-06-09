package Rusile.server.requestHandlers;

import Rusile.common.util.Response;
import Rusile.common.util.Serializers;
import Rusile.server.ServerConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.function.Consumer;

public class ResponseSender implements Consumer<Response> {

    private final SelectionKey key;

    private final Set<SelectionKey> workingKeys;

    public ResponseSender(SelectionKey key, Set<SelectionKey> workingKeys) {
        this.key = key;
        this.workingKeys = workingKeys;
    }

    @Override
    public void accept(Response response) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (response == null) {
            response = new Response("Server couldn't handle your request :(");
        }
        try {
            ByteBuffer buffer = Serializers.serializeResponse(response);
            socketChannel.write(buffer);
        } catch (IOException e) {
            ServerConfig.logger.error("Problem with response serializing or sending");
            return;
        }
        ServerConfig.logger.info("Server wrote response to client");
        workingKeys.remove(key);
    }
}
