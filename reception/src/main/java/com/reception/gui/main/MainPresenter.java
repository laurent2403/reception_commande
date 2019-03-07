package com.reception.gui.main;

import javax.swing.JFrame;

import com.cegim.reception.commons.gui.mvp.SwingPresenter;
import com.cegim.reception.commons.gui.mvp.SwingView;
import com.cegim.reception.commons.thread.ThreadManager;
import com.cegim.reception.commons.thread.init.InitThreadMngrSb;
import com.cegim.reception.commons.thread.order.OrderThreadMngrSb;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.reception.gui.business.display.DisplayOrderThread;
import com.reception.gui.business.init.InitThread;
import com.reception.gui.business.print.PrintOrderThread;
import com.reception.gui.main.order.OrderPresenter;
import com.reception.gui.main.statusbar.StatusBarPresenter;
import com.reception.gui.main.toolbar.ToolBarPresenter;

/**
 * Presenter principal.
 * 
 * @author Laurent2403 
 *  
 */
public class MainPresenter extends SwingPresenter<MainPresenter.View> {

    /**
     * Window listener de la fenetre principale.
     */
    private final transient MainWl mainWl;

    /**
     * Presenter de la barre d'outils.
     */
    @Inject
    private transient ToolBarPresenter toolBarPres;
    /**
     * Presenter de la commande.
     */
    @Inject
    private transient OrderPresenter orderPres;
    /**
     * Presenter de la barre de statut.
     */
    @Inject
    private transient StatusBarPresenter statusBarPres;

    /**
     * Gestionnaire de thread d'initialisation.
     */
    @Inject
    private transient ThreadManager<InitThread> initThreadMngr;
    /**
     * Gestionnaire de thread d'affichage des commandes synchronisees.
     */
    @Inject
    private transient ThreadManager<DisplayOrderThread> displayOrderThreadMngr;
    /**
     * Gestionnaire de thread d'impression des commandes synchronisees.
     */
    @Inject
    private transient ThreadManager<PrintOrderThread> printOrderThreadMngr;

    /**
     * Cree un presenter principal.
     * 
     * @param pView
     *            la vue
     * @param pEventBus
     *            l'event bus
     * @param pMainWl
     *            le window listener de la fenetre principale
     */
    @Inject
    public MainPresenter(final EventBus pEventBus, final MainPresenter.View pView, final MainWl pMainWl) {

        super(pEventBus, pView);

        mainWl = pMainWl;

        bind();

    }

    /**
     * Interface de la vue du presenter.
     */
    public interface View extends SwingView {

        /**
         * Affiche la fenetre.
         */
        void displayWindow();

        /**
         * Retourne la fenetre.
         * 
         * @return la fenetre
         */
        JFrame getFrame();

    }

    /**
     * Effectue les liaisons.
     */
    protected final void bind() {

        view.getFrame().addWindowListener(mainWl);

    }

    /**
     * Demarre les threads.
     */
    public final void start() {

        final InitThreadMngrSb initThreadMngrSb = new InitThreadMngrSb(initThreadMngr);
        eventBus.register(initThreadMngrSb);
        // lance un thread d'initialisation
        initThreadMngr.createAndExecuteThread();

        final OrderThreadMngrSb displayOrderThreadMngrSb = new OrderThreadMngrSb(displayOrderThreadMngr);
        eventBus.register(displayOrderThreadMngrSb);
        // lance un thread d'affichage des commandes
        displayOrderThreadMngr.createAndExecuteThread();

        final OrderThreadMngrSb printOrderThreadMngrSb = new OrderThreadMngrSb(printOrderThreadMngr);
        eventBus.register(printOrderThreadMngrSb);
        // lance un thread d'impression des commandes
        printOrderThreadMngr.createAndExecuteThread();

    }
}
