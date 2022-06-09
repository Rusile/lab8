package Rusile.client;

import Rusile.client.CommandDispatcher.CommandListener;
import Rusile.client.CommandDispatcher.CommandToSend;
import Rusile.client.CommandDispatcher.CommandValidators;
import Rusile.client.NetworkManager.ClientSocketChannelIO;
import Rusile.client.NetworkManager.CommandRequestCreator;
import Rusile.client.NetworkManager.AuthorizationModule;
import Rusile.common.exception.IllegalSizeOfScriptException;
import Rusile.common.exception.WrongAmountOfArgumentsException;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.common.util.TextWriter;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.text.FieldPosition;
import java.util.*;

public final class Client {

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    private static int PORT = 45846;
    private static String HOST;
    private static final int maxPort = 65535;

    private static final Scanner SCANNER = new Scanner(System.in);
    private static Selector selector;
    private static SocketChannel clientChannel;

    private static final CommandRequestCreator COMMAND_REQUEST_CREATOR = new CommandRequestCreator();
    private static final AuthorizationModule authorizationModule = new AuthorizationModule(SCANNER);

    private static boolean reconnectionMode = false;
    private static int attempts = 0;

    public static String login;
    public static String password;


    public static void main(String[] args) {

        try {
            if (!reconnectionMode) {
                inputAddress();
            } else {
                Thread.sleep(8 * 1000);
                clientChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            }
            TextWriter.printSuccessfulMessage("Connected!");
            clientChannel.configureBlocking(false);
            selector = Selector.open();
            clientChannel.register(selector, SelectionKey.OP_WRITE);
            attempts = 0;
            startSelectorLoop(clientChannel, SCANNER, false);
        } catch (ClassNotFoundException e) {
            TextWriter.printErr("Trying to serialize non-serializable object");
        } catch (InterruptedException e) {
            TextWriter.printErr("Thread was interrupt while sleeping. Restart client");
        }  catch (IOException e) {
            TextWriter.printErr("Server is invalid. Trying to reconnect, attempt #" + (attempts + 1));
            reconnectionMode = true;
            if (attempts == 4) {
                TextWriter.printErr("Reconnection failed. Server is dead. Try later...");
                System.exit(1);
            }
            attempts++;
            main(args);
        } catch (NoSuchElementException e) {
            TextWriter.printErr("An invalid character has been entered, forced shutdown!");
            System.exit(1);
        }
    }

    private static void startSelectorLoop(SocketChannel channel, Scanner sc, boolean scriptMode) throws IOException, ClassNotFoundException, InterruptedException {
        do {
            selector.select();
        } while (startIteratorLoop(channel, sc, scriptMode));
    }

    private static boolean startIteratorLoop(SocketChannel channel, Scanner sc, boolean scriptMode) throws IOException, ClassNotFoundException, InterruptedException {
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isReadable()) {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                ClientSocketChannelIO socketChannelIO = new ClientSocketChannelIO(clientChannel);
                Response response = (Response) socketChannelIO.receive();
                if (response.getInfo() != null) {
                    authorizationModule.validateRegistration(response);
                } else {
                    TextWriter.printInfoMessage(response.getData());
                }
                clientChannel.register(selector, SelectionKey.OP_WRITE);

            } else if (key.isWritable()) {
                try {
                    if (!authorizationModule.isAuthorizationDone()) {
                        Request request = authorizationModule.askForRegistration();
                        SocketChannel client = (SocketChannel) key.channel();

                        ClientSocketChannelIO socketChannelIO = new ClientSocketChannelIO(client);
                        socketChannelIO.send(request);

                        client.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    CommandToSend commandToSend = CommandListener.readCommand(sc, scriptMode);
                    if (commandToSend == null) return false;
                    if (commandToSend.getCommandName().equalsIgnoreCase("execute_script")) {
                        CommandValidators.validateAmountOfArgs(commandToSend.getCommandArgs(), 1);
                        ScriptReader scriptReader = new ScriptReader(commandToSend);
                        startSelectorLoop(channel, new Scanner(scriptReader.getPath()), true);
                        scriptReader.stopScriptReading();
                        startSelectorLoop(channel, SCANNER, false);
                    }

                    Request request = COMMAND_REQUEST_CREATOR.createRequestOfCommand(commandToSend);
                    if (request == null) throw new NullPointerException("");
                    SocketChannel client = (SocketChannel) key.channel();

                    ClientSocketChannelIO socketChannelIO = new ClientSocketChannelIO(client);
                    request.setLogin(login);
                    request.setPassword(password);
                    socketChannelIO.send(request);

                    client.register(selector, SelectionKey.OP_READ);
                } catch (NullPointerException | IllegalArgumentException | WrongAmountOfArgumentsException | IllegalSizeOfScriptException e) {
                    TextWriter.printErr(e.getMessage());
                }


            }

        }
        return true;
    }


    private static void inputAddress() {
        TextWriter.printInfoMessage("Enter hostname:");
        try {
            HOST = SCANNER.nextLine();
        } catch (NoSuchElementException e) {
            TextWriter.printErr("An invalid character has been entered, forced shutdown!");
            System.exit(1);
        }
        TextWriter.printInfoMessage("Do you want to use a default port? [y/n]");
        try {
            String s = SCANNER.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("n".equals(s)) {
                TextWriter.printInfoMessage("Please enter the remote host port (1-65535)");
                String port = SCANNER.nextLine();
                try {
                    int portInt = Integer.parseInt(port);
                    if (portInt > 0 && portInt <= maxPort) {
                        PORT = portInt;
                        clientChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
                    } else {
                        TextWriter.printErr("The number did not fall within the limits, repeat the input");
                        inputAddress();
                    }
                } catch (IllegalArgumentException e) {
                    TextWriter.printErr("Error processing the number, repeat the input");
                    inputAddress();
                }
            } else if ("y".equals(s)) {
                clientChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            } else {
                TextWriter.printErr("You entered not valid symbol, try again");
                inputAddress();
            }
        } catch (NoSuchElementException e) {
            TextWriter.printErr("An invalid character has been entered, forced shutdown!");
            System.exit(1);
        } catch (UnresolvedAddressException e) {
            TextWriter.printErr("Server with this host not found. Try again");
            inputAddress();
        }catch (IOException e) {
            TextWriter.printErr("Server is invalid.");
            inputAddress();
        }
    }

}
