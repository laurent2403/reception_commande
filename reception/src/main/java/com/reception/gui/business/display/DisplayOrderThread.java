package com.reception.gui.business.display;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cegim.reception.commons.bean.ZSynCommande;
import com.cegim.reception.commons.gui.order.event.UpdateListSynCommandeEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/**
 * Swing Worker d'affichage des commandes synchronisees.
 * 
 * @author Laurent2403 
 * 
 */
public class DisplayOrderThread extends SwingWorker<Void, AbstractMap.SimpleEntry<String, Object>> {

    /**
     * Logger de la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayOrderThread.class);

    /**
     * Event Bus.
     */
    private final transient EventBus eventBus;
    /**
     * Code societe.
     */
    @Inject
    private final transient String codeSociete;
    /**
     * Nombre de jours d'historique.
     */
    @Inject
    private final transient int nbHistDay;
    /**
     * Periode de rafraichissement.
     */
    private final int refreshPeriod;
    /**
     * Tache d'affichage.
     */
    @Inject
    private final transient DisplayOrderTask displayOrderTask;

    /**
     * Cle de la liste des commandes synchronisees a afficher.
     */
    private static final String KEY_LIST_SYN_COMMANDE = "listSynCommande";

    /**
     * Constructeur.
     * 
     * @param pEventBus
     *            l'event bus
     * @param pCodeSociete
     *            le code societe
     * @param pNbHistDay
     *            le nombre de jours d'historique
     * @param pRefreshPeriod
     *            la periode de rafraichissement
     * @param pDisplayOrderTask
     *            la tache d'affichage
     */
    @Inject
    public DisplayOrderThread(final EventBus pEventBus, final String pCodeSociete, final int pNbHistDay,
            final int pRefreshPeriod, final DisplayOrderTask pDisplayOrderTask) {

        super();
        eventBus = pEventBus;
        codeSociete = pCodeSociete;
        nbHistDay = pNbHistDay;
        refreshPeriod = pRefreshPeriod;
        displayOrderTask = pDisplayOrderTask;

    }

    /**
     * Tâche executee en arriere plan (dans un thread distinct).
     * 
     * @return une map contenant les différents objets recuperes
     */
    @Override
    protected final Void doInBackground() {

        LOGGER.info("Lancement du thread d'affichage des commandes synchronisees");

        List<ZSynCommande<?>> listSynCommande = null;

        try {

            while (!isCancelled()) {

                try {
                    // lancement de la tache d'affichage des commandes
                    listSynCommande = displayOrderTask.getDisplayOrderList(codeSociete, nbHistDay);
                    // publication de la liste des commandes synchronisees
                    publish(new AbstractMap.SimpleEntry<String, Object>(KEY_LIST_SYN_COMMANDE, listSynCommande));

                } catch (Exception exception) {
                    // publication d'une liste vide
                    publish(new AbstractMap.SimpleEntry<String, Object>(KEY_LIST_SYN_COMMANDE,
                            new ArrayList<ZSynCommande<?>>()));
                }

                Thread.sleep(refreshPeriod * DateUtils.MILLIS_PER_SECOND);
            }
        } catch (InterruptedException interEx) {
            LOGGER.info("Interruption du thread d'affichage des commandes synchronisees");
        }
        return null;

    }

    /**
     * Traitement des publications.
     * 
     * @param pListChunk
     *            la liste des publications
     */
    @Override
    protected final void process(final List<AbstractMap.SimpleEntry<String, Object>> pListChunk) {

        for (AbstractMap.SimpleEntry<String, Object> entry : pListChunk) {

            if (isCancelled()) {
                break;
            }

            if (KEY_LIST_SYN_COMMANDE.compareTo(entry.getKey()) == 0) {

                // recuperation de la liste des commandes synchronisees
                final List<ZSynCommande<?>> listSynCommande = (List<ZSynCommande<?>>) entry.getValue();
                // envoi d'un evenement portant la nouvelle liste
                eventBus.post(new UpdateListSynCommandeEvent<ZSynCommande<?>>(this, listSynCommande));

            }
        }
    }
}
