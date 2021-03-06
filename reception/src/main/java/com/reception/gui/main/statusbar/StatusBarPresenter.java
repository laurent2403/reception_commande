package com.reception.gui.main.statusbar;

import com.cegim.reception.commons.gui.mvp.SwingPresenter;
import com.cegim.reception.commons.gui.mvp.SwingView;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/**
 * Presenter de la barre de statut.
 * 
 * @author Laurent2403 
 * 
 */
public class StatusBarPresenter extends SwingPresenter<StatusBarPresenter.View> {

    /**
     * Gestionnaire d'evenements.
     */
    private final transient StatusBarSb statusBarSb;

    /**
     * Constructeur.
     * 
     * @param pEventBus
     *            l'event bus
     * @param pView
     *            la vue
     * @param pStatusBarSb
     *            subscriber de la barre de statut
     */
    @Inject
    public StatusBarPresenter(final EventBus pEventBus, final StatusBarPresenter.View pView,
            final StatusBarSb pStatusBarSb) {

        super(pEventBus, pView);
        statusBarSb = pStatusBarSb;

        bind();
    }

    /**
     * Interface de la vue du presenter.
     */
    public interface View extends SwingView {

        /**
         * Modifie le texte de la barre d'etat.
         * 
         * @param pText
         *            le nouveau texte de la barre d'etat
         */
        void setStatusBarText(String pText);
    }

    /**
     * Effectue les liaisons.
     */
    protected final void bind() {

        // rattache le gestionnaire d'evt a l'event bus
        eventBus.register(statusBarSb);

    }
}
