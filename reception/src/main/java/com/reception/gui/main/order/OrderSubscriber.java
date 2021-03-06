package com.reception.gui.main.order;

import java.util.List;

import com.cegim.reception.commons.bean.ZSynCommande;
import com.cegim.reception.commons.gui.order.event.UpdateListSynCommandeEvent;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

/**
 * Classe a l'ecoute d'evenements.
 * 
 * @author Laurent2403 
 * 
 */
public class OrderSubscriber {

    /**
     * La vue.
     */
    private final transient OrderPresenter.View view;

    /**
     * Constructeur.
     * 
     * @param pView
     *            la vue
     * 
     */
    @Inject
    public OrderSubscriber(final OrderPresenter.View pView) {
        view = pView;
    }

    /**
     * Action effectuee suite a la reception d'un evenement.
     * 
     * @param pEvent
     *            l'evenement
     */
    @Subscribe
    public final void handleUpdateListSynCommandeEvent(final UpdateListSynCommandeEvent<ZSynCommande<?>> pEvent) {

        final List<ZSynCommande<?>> listSynCommande = pEvent.getList();
        view.setList(listSynCommande);

    }

}
