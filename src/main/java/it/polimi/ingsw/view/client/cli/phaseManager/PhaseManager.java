package it.polimi.ingsw.view.client.cli.phaseManager;

public class PhaseManager {
    private Phase currentPhase;

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public PhaseManager() {
        currentPhase = new LoginPhase();
    }
}
