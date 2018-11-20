package GameAPI.entities;

public enum ActionType {
    FOLD,
    CALL,
    RAISE,
    BET,
    ALL_IN

    /**
     * ACTIONS DU JOUEUR :
     *
     * - FOLD : se coucher
     *
     * Si il est pas à jour de la mise :
     * - CALL : suivre
     * - RAISE : suivre et relancer d'une somme minimum
     *
     * Si il est à jour :
     * - CHECK : passer (miser 0)
     * - BET : Miser une somme
     *
     * ALL-IN : CALL / RAISE / BET qui irait >= à ses jetons
     */
}
