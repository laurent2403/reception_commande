package com.reception.entite;

import com.cegim.reception.commons.entite.ZRepresentant;

/**
 * Classe entite <code>Representant</code>.
 * 
 * @author Laurent2403 
 *  
 */
public class Representant extends ZRepresentant {

    /**
     * Numero de version de la classe.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Type du representant (0: representant, 1: chef de zone).
     */
    private int type;

    /**
     * Retourne le type du representant.
     * 
     * @return le type de representant
     */
    public final int getType() {
        return this.type;
    }

    /**
     * Modifie le type du representant.
     * 
     * @param pType
     *            le nouveau type du representant
     */
    public final void setType(final int pType) {
        this.type = pType;
    }

}
