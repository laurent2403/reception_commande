package com.reception.app;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cegim.commons.database.param.DBCnxParam;
import com.cegim.commons.exception.util.StackTraceUtil;
import com.cegim.reception.commons.config.IParamHandler;
import com.cegim.reception.commons.config.database.DBCnxParamRemote;
import com.cegim.reception.commons.config.database.handler.DBCnxParamRemoteHandler;
import com.cegim.reception.commons.config.general.ZGenParam;
import com.cegim.reception.commons.config.order.ZOrderParam;
import com.cegim.reception.commons.gui.lookandfeel.LookAndFeelUtil;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.reception.gui.main.MainPresenter;

/**
 * Classe principale de lancement de l'application.
 * 
 * @author Laurent2403 
 */
public final class App {

    /**
     * Logger de la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    /**
     * Constructeur privé.
     */
    private App() {

    }

    /**
     * Main principal.
     * 
     * @param args
     *            paramètre de lancement de l'application
     */
    public static void main(final String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    // creation d'un injecteur
                    final Injector injector = Guice.createInjector(new AppModule());

                    // chargement de la configuration de l'application
                    loadAppConfig(injector);

                    // changement du look and feel
                    LookAndFeelUtil.setLookAndFeel(LookAndFeelUtil.NIMBUS);

                    // creation du presenter principal
                    final MainPresenter mainPresenter = injector.getInstance(MainPresenter.class);
                    // affichage de la fenetre
                    mainPresenter.getView().displayWindow();
                    // demarre les thread
                    mainPresenter.start();

                } catch (Exception exception) {
                    LOGGER.error(StackTraceUtil.getStackMessage(exception));
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Charge la configuration de l'application.
     * 
     * @param pInjector
     *            l'injecteur
     */
    private static void loadAppConfig(final Injector pInjector) {

        // creation des parametres
        final ZGenParam genParam = pInjector.getInstance(ZGenParam.class);
        final DBCnxParam dBCnxParamRmt = pInjector.getInstance(Key.get(DBCnxParam.class, DBCnxParamRemote.class));
        final ZOrderParam orderParam = pInjector.getInstance(ZOrderParam.class);

        // creation des gestionnaires de parametres
        final IParamHandler<ZGenParam> genParamHdl = pInjector.getInstance(Key
                .get(new TypeLiteral<IParamHandler<ZGenParam>>() {
                }));
        final IParamHandler<DBCnxParam> dBCnxParamRmtHdl = pInjector.getInstance(Key.get(
                new TypeLiteral<IParamHandler<DBCnxParam>>() {
                }, DBCnxParamRemoteHandler.class));
        final IParamHandler<ZOrderParam> orderParamHdl = pInjector.getInstance(Key
                .get(new TypeLiteral<IParamHandler<ZOrderParam>>() {
                }));

        // chargement des parametres
        genParamHdl.load(genParam);
        dBCnxParamRmtHdl.load(dBCnxParamRmt);
        orderParamHdl.load(orderParam);

    }
}
