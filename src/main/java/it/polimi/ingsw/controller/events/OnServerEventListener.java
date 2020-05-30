package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;

/**
 * This interface is meant to be used as a shortcut to implement all listeners for server events
 */
public interface OnServerEventListener extends OnUserJoinedListener, OnGodChosenListener, OnActionsReadyListener,
        OnEliminationListener, OnGodsAvailableListener, OnRequestPlacePawnsListener, OnTurnChangeListener,
        OnWinListener, OnBuildListener, OnMoveListener, OnServerErrorListener, OnPawnPlacedListener,
        OnSizeSelectedListener {}
