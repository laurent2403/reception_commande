package com.reception.gui.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.cegim.reception.commons.gui.config.event.ChangeConfigurationEvent;
import com.cegim.reception.commons.gui.panel.OkCancelApplyPanel;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/**
 * Classe implementant l'action a realiser suite a l'appui sur les boutons de la
 * fenetre de configuration.
 * 
 * @author Laurent2403 
 * 
 */
public class ConfigAl implements ActionListener {

    /**
     * Event Bus.
     */
    private final transient EventBus eventBus;
    /**
     * Vue a propos.
     */
    private ConfigPresenter.View view;

    /**
     * Constructeur.
     * 
     * 
     * @param pEventBus
     *            l'event bus
     * @param pView
     *            la vue
     */
    @Inject
    public ConfigAl(final EventBus pEventBus, final ConfigPresenter.View pView) {

        eventBus = pEventBus;
        view = pView;

    }

    /**
     * Action a realiser.
     * 
     * @param pActionEvent
     *            l'evenement a l'origine de l'action
     */
    @Override
    public final void actionPerformed(final ActionEvent pActionEvent) {

        // appui sur le bouton ok ou appliquer
        if ((OkCancelApplyPanel.OK_ACT.compareTo(pActionEvent.getActionCommand()) == 0)
                || (OkCancelApplyPanel.APPLY_ACT.compareTo(pActionEvent.getActionCommand()) == 0)) {

            // envoie un evt de changement de configuration
            eventBus.post(new ChangeConfigurationEvent(this));

        }

        // appui sur le bouton ok ou annuler
        if ((OkCancelApplyPanel.OK_ACT.compareTo(pActionEvent.getActionCommand()) == 0)
                || (OkCancelApplyPanel.CANCEL_ACT.compareTo(pActionEvent.getActionCommand()) == 0)) {

            // masque la fenetre
            view.setVisible(false);

        }

    }
}
