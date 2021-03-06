package com.reception.gui.main.filter;

import java.util.List;

import com.cegim.reception.commons.entite.ZRepresentant;
import com.cegim.reception.commons.gui.repres.event.UpdateListRepresentantEvent;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

/**
 * Classe a l'ecoute d'evenements.
 * 
 * @author Laurent2403 
 *  
 */
public class FilterSubscriber {

    /**
     * La vue.
     */
    private final transient FilterPresenter.View view;

    /**
     * Constructeur.
     * 
     * @param pView
     *            la vue
     * 
     */
    @Inject
    public FilterSubscriber(final FilterPresenter.View pView) {
        view = pView;
    }

    /**
     * Action effectuee suite a la reception d'un evenement.
     * 
     * @param pEvent
     *            l'evenement
     */
    @Subscribe
    public final void handleUpdateListRepresentantEvent(final UpdateListRepresentantEvent<ZRepresentant> pEvent) {

        final List<ZRepresentant> listRepresentant = pEvent.getList();
        view.setListRepresentant(listRepresentant);

    }
}
