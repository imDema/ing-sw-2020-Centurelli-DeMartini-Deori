package it.polimi.ingsw.view.client;

import it.polimi.ingsw.controller.events.OnResultListener;
import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.serialization.Serializer;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.events.ClientEventsListener;
import it.polimi.ingsw.view.messages.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerHandler implements Runnable, ClientEventsListener {
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Socket socket;
    private ServerEventsListener serverEventsListener = null;

    private OnResultListener resultListener = null;

    public ServerHandler(Scanner socketIn, PrintWriter socketOut, Socket socket) {
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.socket = socket;
    }

    // Result listener can be changed based on necessity
    public void setOnResultListener(OnResultListener onResultListener) {
        this.resultListener = onResultListener;
    }

    public void setServerEventsListener(ServerEventsListener serverEventsListener) {
        this.serverEventsListener = serverEventsListener;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = Serializer.deserializeMessage(socketIn.nextLine());
                MessageId id = message.getSerializationId();

                if(id.serverMessage()){
                    if (id == MessageId.RESULT) {
                        ResultMessage msg = (ResultMessage) message;
                        resultListener.onResult(msg.getValue());
                    } else {
                        ((ServerMessage)message).visit(serverEventsListener);
                    }
                }
            } catch (NoSuchElementException e) {
                serverEventsListener.onServerError("Server connection lost", "");
                break;
            }
        }
        socketOut.close();
        socketIn.close();
        try {
            socket.close();
        } catch (IOException e) {
            CLI.error("Exception thrown while closing socket");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSelectPlayerNumber(int size) {
        Message message = new SelectPlayerNumberMessage(size);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onAddUser(User user) {
        Message message = new AddUserMessage(user);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onChooseGod(User user, GodIdentifier god) {
        Message message = new ChooseGodMessage(user, god);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onPlacePawns(User user, Coordinate c1, Coordinate c2) {
        Message message = new PlacePawnsMessage(user, c1, c2);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        Message message = new CheckActionMessage(user, pawnId, actionIdentifier, coordinate);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onExecuteAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        Message message = new ExecuteActionMessage(user, pawnId, actionIdentifier, coordinate);
        sendMessage(message);
        return true;
    }

    public void sendMessage(Message message ){
        String serialized = Serializer.serializeMessage(message);
        synchronized (socketOut){
            socketOut.println(serialized);
            socketOut.flush();
        }
    }

}
