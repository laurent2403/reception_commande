package com.reception.gui.main.order;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cegim.commons.database.util.DBUtil;
import com.cegim.commons.exception.util.StackTraceUtil;
import com.cegim.reception.commons.bean.ZSynCommande;
import com.cegim.reception.commons.database.DBConnectionFactory;
import com.cegim.reception.commons.pdf.PDFFactory;
import com.cegim.reception.commons.pdf.PDFPrinter;
import com.cegim.reception.commons.xmlgen.GenericInputSource;
import com.google.inject.Inject;
import com.reception.bean.SynCommande;
import com.reception.dao.SynCommandeDao;
import com.reception.xmlgen.SynCommandeReader;

/**
 * Classe implementant l'action a realiser suite a l'appui sur le bouton
 * Imprimer.
 * 
 * @author Laurent2403 
 *  
 */
public class PrintOrderBtnAl implements ActionListener {

    /**
     * Logger de la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintOrderBtnAl.class);

    /**
     * La vue des commandes.
     */
    private final transient OrderPresenter.View orderView;

    /**
     * Fabrique de connexions.
     */
    @Inject
    private transient DBConnectionFactory dbCnxFactory;
    /**
     * Fabrique de PDF.
     */
    @Inject
    private transient PDFFactory pDFFactory;
    /**
     * Impression de pdf.
     */
    @Inject
    private transient PDFPrinter pDFPrinter;
    /**
     * Generateur d'evenements SAX.
     */
    @Inject
    private transient SynCommandeReader xmlReader;
    /**
     * DAO des commandes synchronisees.
     */
    @Inject
    private transient SynCommandeDao synCommandeDao;

    /**
     * Constructeur.
     * 
     * @param pOrderView
     *            la vue des commandes
     */
    @Inject
    public PrintOrderBtnAl(final OrderPresenter.View pOrderView) {
        this.orderView = pOrderView;
    }

    /**
     * Action a realiser.
     * 
     * @param actionEvent
     *            l'evenement a l'origine de l'action
     */
    @Override
    public final void actionPerformed(final ActionEvent actionEvent) {

        // recupere sous forme de liste les lignes selectionnees
        final List<ZSynCommande<?>> listSelection = orderView.getFilterTable().getSelectionModel().getSelected();

        // verifie qu'il y a bien une ligne selectionnee
        if (listSelection.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Merci de sélectionner au moins une ligne", "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
        } else {

            Connection cnx = null;
            try {
                // recuperation d'une connexion a la BDD
                cnx = dbCnxFactory.getRemoteConnection();

                // flux d'entree du fichier xsl
                InputStream xslInputStream = null;
                // flux d'octets de sortie
                ByteArrayOutputStream outputStream = null;

                SynCommande<?> curSynCommande = null;
                // parcours de la selection
                for (ZSynCommande<?> curSelSynCommande : listSelection) {

                    try {

                        // recuperation de la commande en bdd
                        curSynCommande = synCommandeDao.getSynCommandePrint(cnx, curSelSynCommande.getCodeSociete(),
                                curSelSynCommande.getNumero());

                        if (curSynCommande == null) {
                            final String errorMsg = "Erreur lors de la récupération de la commande (code soc : "
                                    + curSelSynCommande.getCodeSociete() + ", " + curSelSynCommande.getNumero()
                                    + ") : vérifiez l'existence du client";
                            LOGGER.error(errorMsg);
                            JOptionPane.showMessageDialog(null, errorMsg, "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // lecture du fichier xsl
                            xslInputStream = getClass().getResourceAsStream("/xsl/commande.xsl");

                            // creation du flux de sortie
                            outputStream = new ByteArrayOutputStream();
                            // creation d'une source d'entree generique a partir
                            // de la commande a imprimer
                            final GenericInputSource<SynCommande<?>> orderInputSource = new GenericInputSource<SynCommande<?>>(
                                    curSynCommande);
                            // creation d'une source a partir de la source
                            // generique et du reader xml
                            final Source source = new SAXSource(xmlReader, orderInputSource);
                            // generation du pdf
                            pDFFactory.build(source, new StreamSource(xslInputStream), outputStream);
                            // conversion flux sortie pdf vers flux entre
                            // impression
                            final byte[] data = outputStream.toByteArray();
                            final ByteArrayInputStream istream = new ByteArrayInputStream(data);
                            // impression du pdf
                            pDFPrinter.print(istream, true);
                            // maj de l'etat d'impression
                            synCommandeDao.updatePrintState(cnx, curSynCommande.getNumero(), 1);
                        }
                    } catch (Exception exception) {
                        LOGGER.error(StackTraceUtil.getStackMessage(exception));
                        JOptionPane.showMessageDialog(null, "Erreur lors de l'impression de la commande "
                                + curSynCommande.getNumero(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        IOUtils.closeQuietly(xslInputStream);
                        IOUtils.closeQuietly(outputStream);
                    }
                }
            } catch (Exception exception) {
                // rien a faire, l'ouverture de connexion a deja ete loggee
            } finally {
                DBUtil.closeConnection(cnx);
            }
        }
    }
}
