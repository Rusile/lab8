package Rusile.server;

import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.server.requestHandlers.RequestExecutor;
import Rusile.server.requestHandlers.RequestReader;
import Rusile.server.requestHandlers.ResponseSender;
import Rusile.server.util.ConsoleThread;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class App {
    private volatile Selector selector;

    private final ExecutorService fixedService = Executors.newFixedThreadPool(4);
    private final ExecutorService cachedService = Executors.newCachedThreadPool();
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(4);
    
    private final Set<SelectionKey> workingKeys =// new ConcurrentHashMap<>();
                Collections.synchronizedSet(new HashSet<>());
    private final ServerSocketChannel server;

    public App(ServerSocketChannel server) {
        this.server = server;
    }

    public void start() {
        ConsoleThread consoleThread = new ConsoleThread();
        if (ServerConfig.isRunning) {
            consoleThread.start();
            startServer();
        }
        fixedService.shutdown();
        cachedService.shutdown();
        fixedService.shutdown();
    }


    private void startServer() {
        ServerConfig.logger.info("Server started");
        try {
            selector = Selector.open();
            ServerSocketChannel server = initChannel(selector);
            startSelectorLoop(server);
        } catch (IOException e) {
            ServerConfig.logger.fatal("Some problems with IO. Try again");
            e.printStackTrace();
            ServerConfig.toggleStatus();
        } catch (ClassNotFoundException e) {
            ServerConfig.logger.error("Trying to serialize non-serializable object");
            ServerConfig.toggleStatus();
        }
    }


    private void startSelectorLoop(ServerSocketChannel channel) throws IOException, ClassNotFoundException {
        while (channel.isOpen() && ServerConfig.isRunning) {
            if (selector.select(1) != 0) {
                startIteratorLoop(channel);
            }
        }
    }

    private void startIteratorLoop(ServerSocketChannel channel) throws IOException {
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isValid() && !workingKeys.contains(key)) {
                if (key.isAcceptable()) {
                    accept(channel);
                } else if (key.isReadable()) {
                    workingKeys.add(key);
                    ServerConfig.logger.info("Client " + ((SocketChannel) key.channel()).getLocalAddress() + " sent message");
                    Supplier<Request> requestReader = new RequestReader(key);
                    Function<Request, Response> requestExecutor = new RequestExecutor();
                    Consumer<Response> responseSender = new ResponseSender(key, workingKeys);
                    CompletableFuture
                            .supplyAsync(requestReader, forkJoinPool)
                            .thenApplyAsync(requestExecutor, fixedService)
                            .thenAcceptAsync(responseSender, cachedService);
                }
            }
        }
    }

    private void accept(ServerSocketChannel channel) throws IOException {
        SocketChannel socketChannel = channel.accept();
        ServerConfig.logger.info("Server get connection from " + socketChannel.getLocalAddress());
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private ServerSocketChannel initChannel(Selector selector) throws IOException {
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        return server;
    }
}
