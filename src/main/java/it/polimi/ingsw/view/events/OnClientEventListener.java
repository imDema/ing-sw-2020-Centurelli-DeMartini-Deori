package it.polimi.ingsw.view.events;
/**
 * This interface is meant to be used as a shortcut to implement all listeners for client events
 */
public interface OnClientEventListener extends OnAddUserListener, OnChooseGodListener, OnCheckActionListener,
        OnExecuteActionListener, OnPlacePawnsListener, OnSelectPlayerNumberListener, OnSelectGodsListener,
        OnChooseFirstPlayerListener {}
