package Rusile.server.util;

import Rusile.common.interfaces.Data;
import Rusile.common.interfaces.IOController;
import Rusile.common.util.Request;

import java.io.*;
import java.net.Socket;

public class ServerSocketIO implements IOController {
    private final Socket socket;

    public ServerSocketIO(Socket socket) {
        this.socket = socket;
    }

    @Override
    public Data receive() throws IOException, ClassNotFoundException {
        ObjectInput in = new ObjectInputStream(socket.getInputStream());
        return (Request) in.readObject();
    }

    @Override
    public void send(Data data) throws IOException {
        OutputStream outputStream = socket.getOutputStream();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(data);

        objectOutputStream.flush();
        byteArrayOutputStream.writeTo(outputStream);
        byteArrayOutputStream.close();
    }
}