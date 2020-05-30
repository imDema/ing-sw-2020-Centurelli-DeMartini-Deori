package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;
import it.polimi.ingsw.view.events.*;

import java.util.List;

public class MessageDispatcher {
    // Server messages
    private OnGodsAvailableListener godsAvailableListener = null;
    private OnGodChosenListener godChosenListener = null;
    private OnActionsReadyListener actionsReadyListener = null;
    private OnEliminationListener eliminationListener = null;
    private OnRequestPlacePawnsListener requestPlacePawnsListener = null;
    private OnServerErrorListener serverErrorListener = null;
    private OnTurnChangeListener turnChangeListener = null;
    private OnWinListener winListener = null;
    private OnUserJoinedListener userJoinedListener = null;
    private OnResultListener resultListener = null;
    private OnMoveListener moveListener = null;
    private OnBuildListener buildListener = null;
    private OnPawnPlacedListener pawnPlacedListener = null;
    private OnSizeSelectedListener sizeSelectedListener = null;
    // Client messages
    private OnAddUserListener addUserListener = null;
    private OnCheckActionListener checkActionListener = null;
    private OnChooseGodListener chooseGodListener = null;
    private OnChooseFirstPlayerListener chooseFirstPlayerListener = null;
    private OnExecuteActionListener executeActionListener = null;
    private OnPlacePawnsListener placePawnsListener = null;
    private OnSelectGodsListener selectGodsListener = null;
    private OnSelectPlayerNumberListener selectPlayerNumberListener = null;

    public void setOnGodsAvailableListener(OnGodsAvailableListener godsAvailableListener) {
        this.godsAvailableListener = godsAvailableListener;
    }

    public void setOnGodChosenListener(OnGodChosenListener godChosenListener) {
        this.godChosenListener = godChosenListener;
    }

    public void setOnActionsReadyListener(OnActionsReadyListener actionsReadyListener) {
        this.actionsReadyListener = actionsReadyListener;
    }

    public void setOnEliminationListener(OnEliminationListener eliminationListener) {
        this.eliminationListener = eliminationListener;
    }

    public void setOnRequestPlacePawnsListener(OnRequestPlacePawnsListener requestPlacePawnsListener) {
        this.requestPlacePawnsListener = requestPlacePawnsListener;
    }

    public void setOnServerErrorListener(OnServerErrorListener serverErrorListener) {
        this.serverErrorListener = serverErrorListener;
    }

    public void setOnTurnChangeListener(OnTurnChangeListener turnChangeListener) {
        this.turnChangeListener = turnChangeListener;
    }

    public void setOnWinListener(OnWinListener winListener) {
        this.winListener = winListener;
    }

    public void setOnUserJoinedListener(OnUserJoinedListener userJoinedListener) {
        this.userJoinedListener = userJoinedListener;
    }

    public void setOnResultListener(OnResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setOnMoveListener(OnMoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public void setOnBuildListener(OnBuildListener buildListener) {
        this.buildListener = buildListener;
    }

    public void setOnPawnPlacedListener(OnPawnPlacedListener pawnPlacedListener) {
        this.pawnPlacedListener = pawnPlacedListener;
    }

    public void setOnSizeSelectedListener(OnSizeSelectedListener sizeSelectedListener) {
        this.sizeSelectedListener = sizeSelectedListener;
    }

    public void setOnAddUserListener(OnAddUserListener addUserListener) {
        this.addUserListener = addUserListener;
    }

    public void setOnCheckActionListener(OnCheckActionListener checkActionListener) {
        this.checkActionListener = checkActionListener;
    }

    public void setOnChooseGodListener(OnChooseGodListener chooseGodListener) {
        this.chooseGodListener = chooseGodListener;
    }

    public void setOnExecuteActionListener(OnExecuteActionListener executeActionListener) {
        this.executeActionListener = executeActionListener;
    }

    public void setOnPlacePawnsListener(OnPlacePawnsListener placePawnsListener) {
        this.placePawnsListener = placePawnsListener;
    }

    public void setOnSelectGodsListener(OnSelectGodsListener selectGodsListener) {
        this.selectGodsListener = selectGodsListener;
    }

    public void setOnChooseFirstPlayerListener(OnChooseFirstPlayerListener chooseFirstPlayerListener) {
        this.chooseFirstPlayerListener = chooseFirstPlayerListener;
    }

    public void setOnSelectPlayerNumberListener(OnSelectPlayerNumberListener selectPlayerNumberListener) {
        this.selectPlayerNumberListener = selectPlayerNumberListener;
    }

    public void setOnServerEventListener(OnServerEventListener serverEventListener) {
        godsAvailableListener = serverEventListener;
        godChosenListener = serverEventListener;
        actionsReadyListener = serverEventListener;
        eliminationListener = serverEventListener;
        requestPlacePawnsListener = serverEventListener;
        serverErrorListener = serverEventListener;
        turnChangeListener = serverEventListener;
        winListener = serverEventListener;
        userJoinedListener = serverEventListener;
        moveListener = serverEventListener;
        buildListener = serverEventListener;
        pawnPlacedListener = serverEventListener;
        // resultListener = serverEventListener;
    }

    public void setOnClientEventListener(OnClientEventListener clientEventListener) {
        addUserListener = clientEventListener;
        checkActionListener = clientEventListener;
        chooseGodListener = clientEventListener;
        chooseFirstPlayerListener = clientEventListener;
        executeActionListener = clientEventListener;
        placePawnsListener = clientEventListener;
        selectGodsListener = clientEventListener;
        selectPlayerNumberListener = clientEventListener;
    }

    public boolean onActionsReady(User user, List<ActionIdentifier> actions) {
        if (actionsReadyListener != null) {
            actionsReadyListener.onActionsReady(user, actions);
            return true;
        }
        return false;
    }

    public boolean onElimination(User user) {
        if (eliminationListener != null) {
            eliminationListener.onElimination(user);
            return true;
        }
        return false;
    }

    public boolean onGodChosen(User user, GodIdentifier godIdentifier) {
        if (godChosenListener != null) {
            godChosenListener.onGodChosen(user, godIdentifier);
            return true;
        }
        return false;
    }

    public boolean onGodsAvailable(List<GodIdentifier> gods) {
        if (godsAvailableListener != null) {
            godsAvailableListener.onGodsAvailable(gods);
            return true;
        }
        return false;
    }

    public boolean onSizeSelected(int size) {
        if (sizeSelectedListener != null) {
            sizeSelectedListener.onSizeSelected(size);
            return true;
        }
        return false;
    }

    public boolean onPawnPlaced(User owner, int pawnId, Coordinate coordinate) {
        if (pawnPlacedListener != null) {
            pawnPlacedListener.onPawnPlaced(owner, pawnId, coordinate);
            return true;
        }
        return false;
    }

    public boolean onRequestPlacePawns(User user) {
        if (requestPlacePawnsListener != null) {
            requestPlacePawnsListener.onRequestPlacePawns(user);
            return true;
        }
        return false;
    }

    public boolean onServerError(String type, String description) {
        if (serverErrorListener != null) {
            serverErrorListener.onServerError(type, description);
            return true;
        }
        return false;
    }

    public boolean onTurnChange(User currentUser, int turn) {
        if (turnChangeListener != null) {
            turnChangeListener.onTurnChange(currentUser, turn);
            return true;
        }
        return false;
    }

    public boolean onUserJoined(User user) {
        if (userJoinedListener != null) {
            userJoinedListener.onUserJoined(user);
            return true;
        }
        return false;
    }

    public boolean onWin(User user) {
        if (winListener != null) {
            winListener.onWin(user);
            return true;
        }
        return false;
    }

    public boolean onBuild(Building building, Coordinate coordinate) {
        if (buildListener != null) {
            buildListener.onBuild(building, coordinate);
            return true;
        }
        return false;
    }

    public boolean onMove(Coordinate from, Coordinate to) {
        if (moveListener != null) {
            moveListener.onMove(from, to);
            return true;
        }
        return false;
    }

    public boolean onResult(boolean value) {
        if (resultListener != null) {
            resultListener.onResult(value);
            return true;
        }
        return false;
    }

    public boolean onAddUser(User user) {
        if (addUserListener != null) {
            return addUserListener.onAddUser(user);
        }
        return false;
    }

    public boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        if (checkActionListener != null) {
            return checkActionListener.onCheckAction(user, pawnId, actionIdentifier, coordinate);
        }
        return false;
    }

    public boolean onChooseGod(User user, GodIdentifier god) {
        if (chooseGodListener != null) {
            return chooseGodListener.onChooseGod(user, god);
        }
        return false;
    }

    public boolean onExecuteAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        if (executeActionListener != null) {
            return executeActionListener.onExecuteAction(user, pawnId, actionIdentifier, coordinate);
        }
        return false;
    }

    public boolean onPlacePawns(User user, Coordinate c1, Coordinate c2) {
        if (placePawnsListener != null) {
            return placePawnsListener.onPlacePawns(user, c1, c2);
        }
        return false;
    }

    public boolean onSelectGods(User user, List<GodIdentifier> selectedGods) {
        if (selectGodsListener != null) {
            return selectGodsListener.onSelectGods(user, selectedGods);
        }
        return false;
    }

    public boolean onChooseFirstPlayer(User self, User firstPlayer) {
        if (chooseFirstPlayerListener != null) {
            return chooseFirstPlayerListener.onChooseFirstPlayer(self, firstPlayer);
        }
        return false;
    }

    public boolean onSelectPlayerNumber(int size) {
        if (selectPlayerNumberListener != null) {
            return selectPlayerNumberListener.onSelectPlayerNumber(size);
        }
        return false;
    }
}
