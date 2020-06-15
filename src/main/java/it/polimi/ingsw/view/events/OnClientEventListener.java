package it.polimi.ingsw.view.events;
/**
 * Listener for all client generated events.
 * Intended to be used as a shortcut.
 */
public interface OnClientEventListener extends OnAddUserListener, OnChooseGodListener, OnCheckActionListener,
        OnExecuteActionListener, OnPlacePawnsListener, OnSelectPlayerNumberListener, OnSelectGodsListener,
        OnChooseFirstPlayerListener {}
