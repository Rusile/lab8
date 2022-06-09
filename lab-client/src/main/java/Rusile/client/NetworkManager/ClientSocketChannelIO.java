package Rusile.client.NetworkManager;

import Rusile.common.interfaces.Data;
import Rusile.common.interfaces.IOController;
import Rusile.common.util.Serializers;
import Rusile.common.util.TextWriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientSocketChannelIO implements IOController {
    private final SocketChannel channel;

    public ClientSocketChannelIO(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void send(Data data) throws IOException {
        ByteBuffer buffer = Serializers.serializeRequest(data);
        channel.write(buffer);
    }

    @Override
    public Data receive() throws IOException, ClassNotFoundException {
        ByteBuffer readBuffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
        channel.read(readBuffer);
        return Serializers.deSerializeResponse(readBuffer.array());
    }
}
