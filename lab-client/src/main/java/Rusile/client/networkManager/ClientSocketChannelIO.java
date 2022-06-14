package Rusile.client.networkManager;

import Rusile.common.interfaces.Data;
import Rusile.common.interfaces.IOController;
import Rusile.common.util.Serializers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientSocketChannelIO implements IOController {
    private SocketChannel channel;
    private InetSocketAddress address;
    private boolean isConnected;


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

    public SocketChannel getChannel() {
        return channel;
    }

    public InetSocketAddress getInetAddress() {
        return address;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public void closeChannel() throws IOException {
        channel.close();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void reconnect() throws IOException {
        channel = SocketChannel.open(address);
    }
}

