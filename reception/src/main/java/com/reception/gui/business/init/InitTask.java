package com.reception.gui.business.init;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cegim.commons.database.util.DBUtil;
import com.cegim.commons.exception.util.StackTraceUtil;
import com.cegim.reception.commons.database.DBConnectionFactory;
import com.google.inject.Inject;
import com.reception.dao.RepresentantDao;
import com.reception.entite.Representant;

/**
 * Tache d'initialisation.
 * 
 * @author Laurent2403 
 * 
 */
public class InitTask {

    /**
     * Logger de la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InitTask.class);

    /**
     * Fabrique de connexions.
     */
    @Inject
    private transient DBConnectionFactory dbCnxFactory;
    /**
     * DAO des representants.
     */
    @Inject
    private transient RepresentantDao represDao;

    /**
     * Retourne la liste des representants pour le ou les codes societes passes
     * en parametre.
     * 
     * @param pCodeSociete
     *            le ou les codes societes
     * @return la liste des representants
     */
    public final List<Representant> loadRepresentant(final String pCodeSociete) {

        Connection cnx = null;
        List<Representant> listRepres = new ArrayList<Representant>();

        try {

            // cree un representant pour symboliser tous les representants
            final Representant allRepres = new Representant();
            // le filtre teste si le code representant est vide
            allRepres.setCode("");
            allRepres.setNom("Tous les représentants");

            // recuperation d'une connexion a la BDD
            cnx = dbCnxFactory.getRemoteConnection();
            // recuperation de la liste des representants
            listRepres = represDao.getList(cnx, pCodeSociete);

            // ajoute le representant generique au debut de la liste
            listRepres.add(0, allRepres);

        } catch (Exception exception) {
            LOGGER.error(StackTraceUtil.getStackMessage(exception));
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBUtil.closeConnection(cnx);
        }

        return listRepres;
    }
}
