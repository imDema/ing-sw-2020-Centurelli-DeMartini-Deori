package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;
//Compact Listener used to represent all Board and Controller Listener
public interface ServerEventsListener extends OnActionsReadyListener, OnEliminationListener, OnGodsAvailableListener, OnRequestPlacePawnsListener, OnServerErrorListener, OnTurnChangeListener, OnWinListener, OnBuildListener, OnMoveListener {
}
