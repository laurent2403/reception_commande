package com.reception.gui.business.print;

import javax.swing.SwingWorker;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Swing Worker d'impression des commandes synchronisees.
 * 
 * @author Laurent2403 
 * 
 */
public class PrintOrderThread extends SwingWorker<Void, Void> {

    /**
     * Logger de la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintOrderThread.class);

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
     * Periode de scrutation.
     */
    @Inject
    private final transient int inspectionPeriod;
    /**
     * Nombre de copies.
     */
    @Inject
    private final transient int nbCopy;
    /**
     * Tache d'impression.
     */
    @Inject
    private final transient PrintOrderTask printOrderTask;

    /**
     * Constructeur.
     * 
     * @param pCodeSociete
     *            le code societe
     * @param pNbHistDay
     *            le nombre de jours d'historique
     * @param pInspectionPeriod
     *            la periode de scrutation
     * @param pNbCopy
     *            le nombre de copies
     * @param pPrintOrderTask
     *            la tache d'impression
     */
    public PrintOrderThread(final String pCodeSociete, final int pNbHistDay, final int pInspectionPeriod,
            final int pNbCopy, final PrintOrderTask pPrintOrderTask) {

        codeSociete = pCodeSociete;
        nbHistDay = pNbHistDay;
        inspectionPeriod = pInspectionPeriod;
        nbCopy = pNbCopy;
        printOrderTask = pPrintOrderTask;

    }

    /**
     * Tâche executee en arriere plan (dans un thread distinct).
     * 
     * @return une map contenant les différents objets recuperes
     */
    @Override
    protected final Void doInBackground() {

        LOGGER.info("Lancement du thread d'impression des commandes synchronisees");

        try {

            while (!isCancelled()) {

                try {
                    // lancement de la tache d'impression des commandes
                    printOrderTask.printOrder(codeSociete, nbHistDay, nbCopy);

                } catch (Exception exception) {
                    // on ignore les exceptions
                }

                Thread.sleep(inspectionPeriod * DateUtils.MILLIS_PER_SECOND);
            }
        } catch (InterruptedException interEx) {
            LOGGER.info("Interruption du thread d'impression des commandes synchronisees");
        }
        return null;
    }
}
