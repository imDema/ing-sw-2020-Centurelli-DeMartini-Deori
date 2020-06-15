package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;

/**
 * Listener for all server generated events.
 * Intended to be used as a shortcut.
 */
public interface OnServerEventListener extends OnUserJoinedListener, OnGodChosenListener, OnActionsReadyListener,
        OnEliminationListener, OnGodsAvailableListener, OnRequestPlacePawnsListener, OnTurnChangeListener,
        OnWinListener, OnBuildListener, OnMoveListener, OnServerErrorListener, OnPawnPlacedListener,
        OnSizeSelectedListener {}
