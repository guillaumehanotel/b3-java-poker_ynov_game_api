package GameAPI.engine.action;

public enum ActionType {
    FOLD,
    CALL,
    BET

    /*
     * ACTIONS DU JOUEUR :
     *
     * - FOLD : se coucher
     *
     * Si il est pas à jour de la mise :
     * - CALL : suivre
     * - RAISE : suivre et relancer d'une somme minimum ----> BET
     *
     * Si il est à jour :
     * - CHECK : passer (miser 0) ----> CALL
     * - BET : Miser une somme
     */
}
