package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.serialization.Serializer;
import it.polimi.ingsw.view.events.ClientEventsListener;
import it.polimi.ingsw.view.messages.GodsAvailableMessage;
import it.polimi.ingsw.view.messages.Message;
import it.polimi.ingsw.view.messages.MessageId;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class ServerHandler implements Runnable, ClientEventsListener {
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Socket socket;
    private final Map<MessageId, Function<Message, Boolean>> map = Map.ofEntries(
            new SimpleImmutableEntry<>(MessageId.GODS_AVAILABLE, this::onGodsAvailable));
    private ServerEventsListener serverEventsListener = null;

    private Boolean onGodsAvailable (Message message){
        GodsAvailableMessage msg = (GodsAvailableMessage) message;
        serverEventsListener.onGodsAvailable(msg.getGods());
        return true;
    }

    public void setServerEventsListener(ServerEventsListener serverEventsListener) {
        this.serverEventsListener = serverEventsListener;
    }

    public ServerHandler(Scanner socketIn, PrintWriter socketOut, Socket socket) {
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = Serializer.deserializeMessage(socketIn.nextLine());
                MessageId id = message.getSerializationId();
                Function<Message, Boolean> handler = map.get(id);
                if(handler != null){
                    handler.apply(message);
                } else {
                    System.err.println("No handler for MessageId: " + id);
                }
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }

    //Serialization and Synchronize
    @Override
    public Optional<User> onAddUser(String username) {
        return Optional.empty();
    }

    @Override
    public boolean onChooseGod(User user, GodIdentifier god) {
        return false;
    }

    @Override
    public void onChoosePawn(User user, int id) {

    }

    @Override
    public boolean onExecuteAction(ActionIdentifier actionIdentifier, Coordinate coordinate) {
        return false;
    }

    @Override
    public boolean onPlacePawns(User user, Coordinate c1, Coordinate c2) {
        return false;
    }
}
