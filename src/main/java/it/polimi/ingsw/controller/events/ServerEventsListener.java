package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;

//Compact Listener used to represent all Board and Controller Listener
public interface ServerEventsListener extends OnUserJoinedListener, OnGodChosenListener, OnActionsReadyListener,
        OnEliminationListener, OnGodsAvailableListener, OnRequestPlacePawnsListener, OnTurnChangeListener,
        OnWinListener, OnBuildListener, OnMoveListener, OnServerErrorListener, OnPawnPlacedListener {}
