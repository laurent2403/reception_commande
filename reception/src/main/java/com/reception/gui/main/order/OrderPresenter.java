package com.reception.gui.main.order;

import org.jdesktop.swingx.JXButton;

import com.cegim.reception.commons.bean.ZSynCommande;
import com.cegim.reception.commons.gui.filter.OrderFilter;
import com.cegim.reception.commons.gui.mvp.SwingPresenter;
import com.cegim.reception.commons.gui.mvp.SwingView;
import com.cegim.reception.commons.gui.table.HasFilterTable;
import com.cegim.reception.commons.gui.view.IListView;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.reception.gui.main.filter.FilterPresenter;

/**
 * Presenter des commandes synchronisees.
 * 
 * @author Laurent2403 
 * 
 */
public class OrderPresenter extends SwingPresenter<OrderPresenter.View> {

    /**
     * Action associee au bouton Imprimer.
     */
    private final transient PrintOrderBtnAl printBtnAl;
    /**
     * Actions a realiser suite au declenchement d'evenement de la souris.
     */
    private final transient SynCommandeTableMl synCmdTableMl;
    /**
     * Gestionnaire d'evenements.
     */
    private final transient OrderSubscriber orderSb;

    /**
     * Presenter du filtre.
     */
    @Inject
    @OrderFilter
    private transient FilterPresenter filterPres;

    /**
     * Constructeur.
     * 
     * @param pEventBus
     *            l'event bus
     * @param pView
     *            la vue
     * @param pPrintBtnAl
     *            l'action associee au bouton imprimer
     * @param pSynCmdTableMl
     *            actions a realiser suite au declenchement d'evenement de la
     *            souris
     * @param pOrderSb
     *            gestionnaire d'evt
     */
    @Inject
    public OrderPresenter(final EventBus pEventBus, final OrderPresenter.View pView, final PrintOrderBtnAl pPrintBtnAl,
            final SynCommandeTableMl pSynCmdTableMl, final OrderSubscriber pOrderSb) {

        super(pEventBus, pView);
        printBtnAl = pPrintBtnAl;
        synCmdTableMl = pSynCmdTableMl;
        orderSb = pOrderSb;

        bind();

    }

    /**
     * Interface de la vue du presenter.
     */
    public interface View extends SwingView, HasFilterTable<ZSynCommande<?>>, IListView<ZSynCommande<?>> {

        /**
         * Identifiant de la colonne etat.
         */
        String COL_ETAT_ID = "columnEtatId";
        /**
         * Identifiant de la colonne etat impression.
         */
        String COL_ETAT_IMPR_ID = "columnEtatImprId";

        /**
         * Retourne le bouton Imprimer.
         * 
         * @return le bouton Imprimer
         */
        JXButton getPrintButton();

    }

    /**
     * Effectue les liaisons.
     */
    protected final void bind() {

        // ajout d'un gestionnaire d'action au bouton Imprimer
        view.getPrintButton().addActionListener(printBtnAl);

        // associe le gestionnaire d'evenement souris a la table
        view.getFilterTable().getTable().addMouseListener(synCmdTableMl);

        // rattache le gestionnaire d'evt a l'event bus
        eventBus.register(orderSb);

    }
}
