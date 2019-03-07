package com.reception.gui.main.order.filter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.inject.Inject;

/**
 * Classe implementant l'action a realiser suite a l'appui sur le bouton
 * Filtrer.
 * 
 * @author Laurent2403 
 *  
 */
public class OrderFilterBtnAl implements ActionListener {

    /**
     * Le matcher editor de commandes synchronisees.
     */
    @Inject
    private transient OrderFilterMatcherEditor orderFilterMtchEdt;

    /**
     * Action a realiser.
     * 
     * @param actionEvent
     *            l'evenement a l'origine de l'action
     */
    @Override
    public final void actionPerformed(final ActionEvent actionEvent) {
        orderFilterMtchEdt.buildFilter();
    }
}
