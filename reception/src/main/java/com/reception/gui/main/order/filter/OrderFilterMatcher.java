package com.reception.gui.main.order.filter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import ca.odell.glazedlists.matchers.Matcher;

import com.cegim.reception.commons.bean.ZSynCommande;
import com.cegim.reception.commons.joda.JodaUtils;

/**
 * Definition du filtre dynamique de commandes synchronisees.
 * 
 * @author Laurent2403 
 *  
 */
public class OrderFilterMatcher implements Matcher<ZSynCommande<?>> {

    /**
     * Le critere du code representant.
     */
    private final transient String critCodeRepres;
    /**
     * Le critere de la date de debut.
     */
    private final transient DateTime critStartDate;
    /**
     * Le critere de la date de fin.
     */
    private final transient DateTime critEndDate;

    /**
     * Constructeur.
     * 
     * @param pCritCodeRepres
     *            le code du representant
     * @param pCritStartDate
     *            le critere de date de debut
     * @param pCritEndDate
     *            le critere de date de fin
     */
    public OrderFilterMatcher(final String pCritCodeRepres, final DateTime pCritStartDate, final DateTime pCritEndDate) {

        this.critCodeRepres = pCritCodeRepres;
        this.critStartDate = pCritStartDate;
        this.critEndDate = pCritEndDate;

    }

    /**
     * Retourne true si l'element passe en parametre correspond au filtre, false
     * sinon.
     * 
     * @param pElement
     *            l'element a filtrer
     * @return true si l'element passe en parametre correspond au filtre, false
     *         sinon
     */
    @Override
    public final boolean matches(final ZSynCommande<?> pElement) {

        boolean resultat = false;

        if (pElement == null) {
            resultat = false;
        } else {
            if ((StringUtils.isBlank(critCodeRepres)) && (critStartDate == null) && (critEndDate == null)) {
                // pas de critere de date, ni de critere de representant
                resultat = true;
            } else {
                // recupere la date de la commande et met l'heure a zero
                final DateTime dateCreation = pElement.getDateCreation();
                // recupere le code representant
                final String codeRepres = pElement.getRepresentant().getCode();

                if (dateCreation == null) {
                    resultat = true;
                } else {
                    if (StringUtils.isBlank(critCodeRepres)) {
                        // pas de critere de representant
                        resultat = JodaUtils.isDateBetween(dateCreation, critStartDate, critEndDate);
                    } else {
                        // critere sur le representant
                        resultat = (critCodeRepres.compareTo(codeRepres) == 0)
                                && JodaUtils.isDateBetween(dateCreation, critStartDate, critEndDate);
                    }
                }
            }
        }
        return resultat;
    }

}
