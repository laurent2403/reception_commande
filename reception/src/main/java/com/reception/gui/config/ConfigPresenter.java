package com.reception.gui.config;

import java.awt.Component;

import com.cegim.reception.commons.gui.config.IConfigView;
import com.cegim.reception.commons.gui.config.database.ConfigDataBasePresenter;
import com.cegim.reception.commons.gui.config.general.ConfigGeneralPresenter;
import com.cegim.reception.commons.gui.config.order.ConfigOrderPresenter;
import com.cegim.reception.commons.gui.mvp.SwingPresenter;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/**
 * Presenter de la configuration.
 * 
 * @author Laurent2403 
 */
public class ConfigPresenter extends SwingPresenter<ConfigPresenter.View> {

    /**
     * Presenter de la configuration : General.
     */
    private final transient ConfigGeneralPresenter configGeneralPres;
    /**
     * Presenter de la configuration : BDD.
     */
    private final transient ConfigDataBasePresenter configDataBasePres;
    /**
     * Presenter de la configuration : Commande.
     */
    private final transient ConfigOrderPresenter configOrderPres;
    /**
     * Actions associees aux boutons.
     */
    private final transient ConfigAl configAl;

    /**
     * Constructeur.
     * 
     * @param pEventBus
     *            l'event bus
     * @param pView
     *            la vue
     * @param pConfigAl
     *            actions associees aux boutons
     * @param pConfigGeneralPres
     *            le presenter de la configuration : General
     * @param pConfigDataBasePres
     *            le presenter de la configuration : BDD
     * @param pConfigOrderPres
     *            le presenter de la configuration : Commande
     */
    @Inject
    public ConfigPresenter(final EventBus pEventBus, final ConfigPresenter.View pView, final ConfigAl pConfigAl,
            final ConfigGeneralPresenter pConfigGeneralPres, final ConfigDataBasePresenter pConfigDataBasePres,
            final ConfigOrderPresenter pConfigOrderPres) {

        super(pEventBus, pView);
        configAl = pConfigAl;
        configGeneralPres = pConfigGeneralPres;
        view.addGeneralTab(pConfigGeneralPres.getView().asComponent());
        configDataBasePres = pConfigDataBasePres;
        view.addDataBaseTab(pConfigDataBasePres.getView().asComponent());
        configOrderPres = pConfigOrderPres;
        view.addOrderTab(pConfigOrderPres.getView().asComponent());
        bind();
    }

    /**
     * Interface de la vue du presenter.
     */
    public interface View extends IConfigView {

        /**
         * Ajoute l'onglet General.
         * 
         * @param pComponent
         *            le panneau General
         */
        void addGeneralTab(Component pComponent);

        /**
         * Ajoute l'onglet BDD.
         * 
         * @param pComponent
         *            le panneau BDD
         */
        void addDataBaseTab(Component pComponent);

        /**
         * Ajoute l'onglet Commande.
         * 
         * @param pComponent
         *            le panneau Commande
         */
        void addOrderTab(Component pComponent);

    }

    /**
     * Effectue les liaisons.
     */
    protected final void bind() {

        // association d'une action aux boutons
        view.getOkButton().addActionListener(configAl);
        view.getCancelButton().addActionListener(configAl);
        view.getApplyButton().addActionListener(configAl);

    }
}
