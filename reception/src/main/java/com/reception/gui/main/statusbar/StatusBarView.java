package com.reception.gui.main.statusbar;

import java.awt.Component;

import com.cegim.reception.commons.gui.statusbar.StatusBar;
import com.google.inject.Inject;

/**
 * Vue de la barre de statut.
 * 
 * @author Laurent2403 
 */
public class StatusBarView implements StatusBarPresenter.View {

    /**
     * Barre d'etat.
     */
    private transient StatusBar statusBar;

    /**
     * Cree une vue principale.
     * 
     */
    @Inject
    public StatusBarView() {

        initComponents();

    }

    /**
     * Initialise les composants.
     */
    private void initComponents() {

        // creation de la barre d'etat
        statusBar = new StatusBar();

    }

    /**
     * Modifie le texte de la barre d'etat.
     * 
     * @param pText
     *            le nouveau texte de la barre d'etat
     */
    @Override
    public final void setStatusBarText(final String pText) {
        statusBar.setStatusBarText(pText);
    }

    /**
     * Retourne le composant.
     * 
     * @return le composant
     */
    @Override
    public final Component asComponent() {
        return statusBar.asComponent();
    }
}
