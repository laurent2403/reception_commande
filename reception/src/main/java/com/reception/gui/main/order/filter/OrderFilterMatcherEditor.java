package com.reception.gui.main.order.filter;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;

import com.cegim.reception.commons.bean.ZSynCommande;
import com.cegim.reception.commons.entite.ZRepresentant;
import com.cegim.reception.commons.gui.filter.OrderFilter;
import com.google.inject.Inject;
import com.reception.gui.main.filter.FilterPresenter;

/**
 * Implemente un filtre de commandes s'appuyant sur la vue d'un filtre.
 * 
 * @author Laurent2403 
 *  
 */
public class OrderFilterMatcherEditor extends AbstractMatcherEditor<ZSynCommande<?>> {

    /**
     * La vue du filtre.
     */
    private final transient FilterPresenter.View filterView;

    /**
     * Constructeur.
     * 
     * @param pFilterView
     *            la vue du filtre
     */
    @Inject
    public OrderFilterMatcherEditor(@OrderFilter final FilterPresenter.View pFilterView) {
        filterView = pFilterView;
    }

    /**
     * Construit et signale le changement de filtre.
     */
    public final void buildFilter() {

        // recupere le representant selectionnne
        final ZRepresentant repres = filterView.getRepresentant();
        // recupere la date de debut selectionnee
        final DateTime startDate = filterView.getStartDate();
        // recupere la date de fin selectionnee
        final DateTime endDate = filterView.getEndDate();
        // verification sur le representant
        if (repres == null) {
            JOptionPane.showMessageDialog(null, "Merci de choisir un représentant", "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            // construit le nouveau filtre
            final OrderFilterMatcher orderFilterMatcher = new OrderFilterMatcher(repres.getCode(), startDate, endDate);
            // signale la modification
            fireChanged(orderFilterMatcher);
        }

    }
}
