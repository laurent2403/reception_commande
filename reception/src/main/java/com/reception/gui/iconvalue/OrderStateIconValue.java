package com.reception.gui.iconvalue;

import javax.swing.Icon;

import org.jdesktop.swingx.renderer.IconValue;

import com.cegim.reception.commons.gui.image.CommonsImage;
import com.cegim.reception.commons.state.OrderState;

/**
 * Cette classe retourne l'icone utilisee pour symboliser l'etat d'une commande.
 * 
 * @author Laurent2403 
 */
public class OrderStateIconValue implements IconValue {

    /**
     * Numero de version de la classe.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Retourne l'icone en fonction de la valeur de la cellule passee en
     * parametre.
     * 
     * @param pValue
     *            la valeur de la cellule
     * @return l'icone l'icone en fonction de la valeur de la cellule passee en
     *         parametre
     */
    @Override
    public final Icon getIcon(final java.lang.Object pValue) {

        Icon resultat;

        if ((pValue instanceof Integer)) {

            final Integer nOrderState = (Integer) pValue;
            final OrderState orderState = OrderState.get(nOrderState);
            if (orderState == null) {
                resultat = CommonsImage.QUESTION_24;
            } else {
                switch (orderState) {
                case SYNCHRONIZED:
                    resultat = CommonsImage.OK_24;
                    break;
                case COPYING:
                    resultat = CommonsImage.EXCLAMATION_24;
                    break;
                default:
                    resultat = CommonsImage.QUESTION_24;
                    break;
                }
            }
        } else {
            resultat = CommonsImage.QUESTION_24;
        }

        return resultat;
    }
}
