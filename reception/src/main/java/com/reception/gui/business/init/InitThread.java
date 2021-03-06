package com.reception.gui.business.init;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import com.cegim.reception.commons.gui.repres.event.UpdateListRepresentantEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.reception.entite.Representant;

/**
 * Swing Worker d'initialisation.
 * 
 * @author Laurent2403 
 *  
 */
public class InitThread extends SwingWorker<Void, AbstractMap.SimpleEntry<String, Object>> {

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
     * Tache d'initialisation.
     */
    @Inject
    private final transient InitTask initTask;

    /**
     * Cle de la liste des commandes synchronisees a afficher.
     */
    private static final String KEY_LIST_REPRESENTANT = "listRepresentant";

    /**
     * Constructeur.
     * 
     * @param pEventBus
     *            l'event bus
     * @param pCodeSociete
     *            le code societe
     * @param pInitTask
     *            la tache d'initialisation
     */
    @Inject
    public InitThread(final EventBus pEventBus, final String pCodeSociete, final InitTask pInitTask) {

        super();
        eventBus = pEventBus;
        codeSociete = pCodeSociete;
        initTask = pInitTask;

    }

    /**
     * Tâche executee en arriere plan (dans un thread distinct).
     * 
     * @return une map contenant les différents objets recuperes
     */
    @Override
    protected final Void doInBackground() {

        List<Representant> listRepres = null;

        try {
            // appel a la tache d'initialisation
            listRepres = initTask.loadRepresentant(codeSociete);
            // publication de la liste des commandes synchronisees
            publish(new AbstractMap.SimpleEntry<String, Object>(KEY_LIST_REPRESENTANT, listRepres));

        } catch (Exception exception) {
            // publication d'une liste vide
            publish(new AbstractMap.SimpleEntry<String, Object>(KEY_LIST_REPRESENTANT, new ArrayList<Representant>()));
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

            if (KEY_LIST_REPRESENTANT.compareTo(entry.getKey()) == 0) {

                // recuperation de la liste des representants
                final List<Representant> listRepres = (List<Representant>) entry.getValue();
                // envoi d'un evenement portant la nouvelle liste
                eventBus.post(new UpdateListRepresentantEvent(this, listRepres));

            }
        }
    }
}
