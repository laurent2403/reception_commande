package com.reception.app;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.cegim.commons.database.param.DBCnxParam;
import com.cegim.reception.commons.config.AppConfigFile;
import com.cegim.reception.commons.config.IParamHandler;
import com.cegim.reception.commons.config.database.DBCnxParamRemote;
import com.cegim.reception.commons.config.database.handler.DBCnxParamPropHandler;
import com.cegim.reception.commons.config.database.handler.DBCnxParamRemoteHandler;
import com.cegim.reception.commons.config.database.key.DBCnxParamRemoteKey;
import com.cegim.reception.commons.config.general.ZGenParam;
import com.cegim.reception.commons.config.general.handler.ZGenParamPropHandler;
import com.cegim.reception.commons.config.general.key.ZGenParamKey;
import com.cegim.reception.commons.config.order.ZOrderParam;
import com.cegim.reception.commons.config.order.handler.ZOrderParamPropHandler;
import com.cegim.reception.commons.config.order.key.ZOrderParamKey;
import com.cegim.reception.commons.database.DBConnectionFactory;
import com.cegim.reception.commons.database.DBRemoteConnectionFactory;
import com.cegim.reception.commons.gui.about.AboutPresenter;
import com.cegim.reception.commons.gui.config.database.ConfigDataBasePresenter;
import com.cegim.reception.commons.gui.config.database.ConfigDataBaseView;
import com.cegim.reception.commons.gui.config.general.ConfigGeneralPresenter;
import com.cegim.reception.commons.gui.config.general.ConfigGeneralView;
import com.cegim.reception.commons.gui.config.order.ConfigOrderPresenter;
import com.cegim.reception.commons.gui.config.order.ConfigOrderView;
import com.cegim.reception.commons.gui.filter.OrderFilter;
import com.cegim.reception.commons.gui.legend.LegendPresenter;
import com.cegim.reception.commons.pdf.PDFFactory;
import com.cegim.reception.commons.pdf.PDFPrinter;
import com.cegim.reception.commons.thread.ThreadManager;
import com.cegim.reception.commons.thread.init.InitThreadMngr;
import com.cegim.reception.commons.thread.order.DisplayOrderThreadMngr;
import com.cegim.reception.commons.thread.order.PrintOrderThreadMngr;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.reception.gui.about.AboutView;
import com.reception.gui.business.display.DisplayOrderTask;
import com.reception.gui.business.display.DisplayOrderThread;
import com.reception.gui.business.init.InitTask;
import com.reception.gui.business.init.InitThread;
import com.reception.gui.business.print.PrintOrderTask;
import com.reception.gui.business.print.PrintOrderThread;
import com.reception.gui.config.ConfigPresenter;
import com.reception.gui.config.ConfigView;
import com.reception.gui.legend.LegendView;
import com.reception.gui.main.MainPresenter;
import com.reception.gui.main.MainView;
import com.reception.gui.main.filter.FilterPresenter;
import com.reception.gui.main.filter.FilterSubscriber;
import com.reception.gui.main.filter.FilterView;
import com.reception.gui.main.order.OrderPresenter;
import com.reception.gui.main.order.OrderView;
import com.reception.gui.main.order.filter.OrderFilterBtnAl;
import com.reception.gui.main.order.filter.OrderFilterMatcherEditor;
import com.reception.gui.main.statusbar.StatusBarPresenter;
import com.reception.gui.main.statusbar.StatusBarView;
import com.reception.gui.main.toolbar.ToolBarPresenter;
import com.reception.gui.main.toolbar.ToolBarView;
import com.reception.xmlgen.SynCommandeReader;

/**
 * Classe de configuration de guice.
 * 
 * @author Laurent2403 
 */
public class AppModule extends AbstractModule {

    /**
     * Nom du fichier de configuration.
     */
    private static final String APP_CONFIG_FILE = "appConfig.properties";

    /**
     * Configure le framework d'injection de dependance google guice.
     */
    @Override
    protected void configure() {

        bind(ToolBarPresenter.View.class).to(ToolBarView.class).in(Singleton.class);
        bind(ToolBarPresenter.class).in(Singleton.class);

        bind(OrderPresenter.View.class).to(OrderView.class).in(Singleton.class);
        bind(OrderView.class).in(Singleton.class);

        bind(StatusBarPresenter.View.class).to(StatusBarView.class).in(Singleton.class);
        bind(StatusBarPresenter.class).in(Singleton.class);

        bind(MainPresenter.View.class).to(MainView.class).in(Singleton.class);
        bind(MainPresenter.class).in(Singleton.class);

        bind(FilterPresenter.View.class).annotatedWith(OrderFilter.class).to(FilterView.class).in(Singleton.class);
        bind(OrderFilterMatcherEditor.class).in(Singleton.class);

        bind(AboutPresenter.View.class).to(AboutView.class).in(Singleton.class);
        bind(AboutPresenter.class).in(Singleton.class);

        bind(LegendPresenter.View.class).to(LegendView.class).in(Singleton.class);
        bind(LegendPresenter.class).in(Singleton.class);

        bind(ConfigPresenter.View.class).to(ConfigView.class).in(Singleton.class);
        bind(ConfigPresenter.class).in(Singleton.class);

        bind(ConfigGeneralPresenter.View.class).to(ConfigGeneralView.class).in(Singleton.class);
        bind(ConfigGeneralPresenter.class).in(Singleton.class);

        bind(ConfigDataBasePresenter.View.class).to(ConfigDataBaseView.class).in(Singleton.class);
        bind(ConfigDataBasePresenter.class).in(Singleton.class);

        bind(ConfigOrderPresenter.View.class).to(ConfigOrderView.class).in(Singleton.class);
        bind(ConfigOrderPresenter.class).in(Singleton.class);

        bind(EventBus.class).in(Singleton.class);

        bind(DBConnectionFactory.class).to(DBRemoteConnectionFactory.class).in(Singleton.class);

        bind(InitTask.class).in(Singleton.class);
        bind(DisplayOrderTask.class).in(Singleton.class);
        bind(PrintOrderTask.class).in(Singleton.class);

        bind(PDFFactory.class).in(Singleton.class);
        bind(PDFPrinter.class).in(Singleton.class);
        bind(SynCommandeReader.class).in(Singleton.class);

        bind(ZGenParam.class).in(Singleton.class);
        bind(DBCnxParam.class).annotatedWith(DBCnxParamRemote.class).to(DBCnxParam.class).in(Singleton.class);
        bind(ZOrderParam.class).in(Singleton.class);

        bind(new TypeLiteral<ThreadManager<InitThread>>() {
        }).annotatedWith(InitThreadMngr.class).to(new TypeLiteral<ThreadManager<InitThread>>() {
        }).in(Singleton.class);

        bind(new TypeLiteral<ThreadManager<DisplayOrderThread>>() {
        }).annotatedWith(DisplayOrderThreadMngr.class).to(new TypeLiteral<ThreadManager<DisplayOrderThread>>() {
        }).in(Singleton.class);

        bind(new TypeLiteral<ThreadManager<PrintOrderThread>>() {
        }).annotatedWith(PrintOrderThreadMngr.class).to(new TypeLiteral<ThreadManager<PrintOrderThread>>() {
        }).in(Singleton.class);
    }

    /**
     * Fournit le presenter du filtre des commandes.
     * 
     * @param pEventBus
     *            l'event bus
     * @param pView
     *            la vue du filtre
     * @param pFilterBtnAl
     *            l'action du filtre
     * @return le presenter du filtre des commandes
     */
    @Provides
    @OrderFilter
    @Singleton
    final FilterPresenter provideOrderFilterPresenter(final EventBus pEventBus,
            @OrderFilter final FilterPresenter.View pView, final OrderFilterBtnAl pFilterBtnAl) {
        return new FilterPresenter(pEventBus, pView, new FilterSubscriber(pView), pFilterBtnAl);
    }

    /**
     * Fournit un thread d'initialisation.
     * 
     * @param pEventBus
     *            l'event bus
     * @param pGenParam
     *            les parametres generaux
     * @param pInitTask
     *            la tache d'initialisation
     * @return le thread d'initialisation
     */
    @Provides
    final InitThread provideInitThread(final EventBus pEventBus, final ZGenParam pGenParam, final InitTask pInitTask) {
        return new InitThread(pEventBus, pGenParam.getCodeSociete(), pInitTask);
    }

    /**
     * Fournit un thread d'affichage des commandes synchronisees.
     * 
     * @param pEventBus
     *            l'event bus
     * @param pGenParam
     *            les parametres generaux
     * @param pOrderParam
     *            les parametres des commandes
     * @param pDisplayOrderTask
     *            la tache d'affichage des commandes
     * @return le thread d'affichage des commandes synchronisees
     */
    @Provides
    final DisplayOrderThread provideDisplayOrderThread(final EventBus pEventBus, final ZGenParam pGenParam,
            final ZOrderParam pOrderParam, final DisplayOrderTask pDisplayOrderTask) {

        DisplayOrderThread thread = null;
        if (pOrderParam.isDisplay()) {
            thread = new DisplayOrderThread(pEventBus, pGenParam.getCodeSociete(), pOrderParam.getDisplayNbHistDay(),
                    pOrderParam.getDisplayPeriod(), pDisplayOrderTask);
        }
        return thread;

    }

    /**
     * Fournit un thread d'impression des commandes synchronisees.
     * 
     * @param pGenParam
     *            les parametres generaux
     * @param pOrderParam
     *            les parametres des commandes
     * @param pPrintOrderTask
     *            la tache d'impression des commandes
     * @return le thread d'impression des commandes synchronisees
     */
    @Provides
    final PrintOrderThread providePrintOrderThread(final ZGenParam pGenParam, final ZOrderParam pOrderParam,
            final PrintOrderTask pPrintOrderTask) {

        PrintOrderThread thread = null;
        if (pOrderParam.isPrint()) {
            thread = new PrintOrderThread(pGenParam.getCodeSociete(), pOrderParam.getPrintNbHistDay(),
                    pOrderParam.getPrintPeriod(), pOrderParam.getPrintNbCopy(), pPrintOrderTask);
        }
        return thread;

    }

    /**
     * Fournit le gestionnaire de configuration de l'application.
     * 
     * @return le gestionnaire de configuration de l'application
     * @throws ConfigurationException
     *             en cas de probleme d'ouverture
     */
    @Provides
    @AppConfigFile
    @Singleton
    final PropertiesConfiguration provideAppConfig() throws ConfigurationException {
        return new PropertiesConfiguration(APP_CONFIG_FILE);
    }

    /**
     * Fournit le gestionnaire des parametres generaux.
     * 
     * @param pPropConfig
     *            le gestionnaire de configuration
     * @return le gestionnaire des parametres generaux
     */
    @Provides
    @Singleton
    final IParamHandler<ZGenParam> provideGenParamHandler(@AppConfigFile final PropertiesConfiguration pPropConfig) {
        return new ZGenParamPropHandler(pPropConfig, new ZGenParamKey());
    }

    /**
     * Fournit le gestionnaire des parametres de connexion a la bdd distante.
     * 
     * @param pPropConfig
     *            le gestionnaire de configuration
     * @return le gestionnaire des parametres de connexion a la bdd distante
     */
    @Provides
    @DBCnxParamRemoteHandler
    @Singleton
    final IParamHandler<DBCnxParam> provideDBCnxParamRemoteHandler(
            @AppConfigFile final PropertiesConfiguration pPropConfig) {
        return new DBCnxParamPropHandler(pPropConfig, new DBCnxParamRemoteKey());
    }

    /**
     * Fournit le gestionnaire des parametres des commandes.
     * 
     * @param pPropConfig
     *            le gestionnaire de configuration
     * @return le gestionnaire des parametres des commandes
     */
    @Provides
    @Singleton
    final IParamHandler<ZOrderParam> provideOrderParamHandler(@AppConfigFile final PropertiesConfiguration pPropConfig) {
        return new ZOrderParamPropHandler(pPropConfig, new ZOrderParamKey());
    }

}
