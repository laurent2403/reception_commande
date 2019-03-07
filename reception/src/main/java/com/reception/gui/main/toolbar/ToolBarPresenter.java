package com.reception.gui.main.toolbar;

import org.jdesktop.swingx.JXButton;

import com.cegim.reception.commons.gui.about.AboutBtnAl;
import com.cegim.reception.commons.gui.legend.LegendBtnAl;
import com.cegim.reception.commons.gui.mvp.SwingPresenter;
import com.cegim.reception.commons.gui.mvp.SwingView;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.reception.gui.config.ConfigBtnAl;

/**
 * Presenter de la barre d'outils.
 * 
 * @author Laurent2403 
 * 
 */
public class ToolBarPresenter extends SwingPresenter<ToolBarPresenter.View> {

    /**
     * Action associee au bouton A propos.
     */
    private final transient AboutBtnAl aboutBtnAl;
    /**
     * Action associee au bouton Configuration.
     */
    private final transient ConfigBtnAl configBtnAl;
    /**
     * Action associee au bouton Legende.
     */
    private final transient LegendBtnAl legendBtnAl;

    /**
     * Constructeur.
     * 
     * @param pEventBus
     *            l'event bus
     * @param pView
     *            la vue
     * @param pAboutBtnAl
     *            l'action associee au bouton A propos
     * @param pConfigBtnAl
     *            l'action associee au bouton Configuration
     * @param pLegendBtnAl
     *            l'action associee au bouton Legende
     */
    @Inject
    public ToolBarPresenter(final EventBus pEventBus, final ToolBarPresenter.View pView, final AboutBtnAl pAboutBtnAl,
            final ConfigBtnAl pConfigBtnAl, final LegendBtnAl pLegendBtnAl) {

        super(pEventBus, pView);

        aboutBtnAl = pAboutBtnAl;
        configBtnAl = pConfigBtnAl;
        legendBtnAl = pLegendBtnAl;

        bind();

    }

    /**
     * Interface de la vue du presenter.
     */
    public interface View extends SwingView {

        /**
         * Retourne le bouton Legende.
         * 
         * @return le bouton Legende
         */
        JXButton getLegendButton();

        /**
         * Retourne le bouton Configuration.
         * 
         * @return le bouton Configuration
         */
        JXButton getConfigButton();

        /**
         * Retourne le bouton A propos.
         * 
         * @return le bouton A propos
         */
        JXButton getAboutButton();
    }

    /**
     * Effectue les liaisons.
     */
    protected final void bind() {

        // ajout d'un gestionnaire d'action au bouton Legende
        view.getLegendButton().addActionListener(legendBtnAl);

        // ajout d'un gestionnaire d'action au bouton Configuration
        view.getConfigButton().addActionListener(configBtnAl);

        // ajout d'un gestionnaire d'action au bouton a propos
        view.getAboutButton().addActionListener(aboutBtnAl);

    }
}
