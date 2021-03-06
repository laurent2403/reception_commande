package com.reception.gui.business.print;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cegim.commons.database.util.DBUtil;
import com.cegim.commons.exception.util.StackTraceUtil;
import com.cegim.reception.commons.database.DBConnectionFactory;
import com.cegim.reception.commons.pdf.PDFFactory;
import com.cegim.reception.commons.pdf.PDFPrinter;
import com.cegim.reception.commons.xmlgen.GenericInputSource;
import com.google.inject.Inject;
import com.reception.bean.SynCommande;
import com.reception.dao.SynCommandeDao;
import com.reception.xmlgen.SynCommandeReader;

/**
 * Impression des commandes.
 * 
 * @author Laurent2403 
 *  
 */
public class PrintOrderTask {

    /**
     * Logger de la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintOrderTask.class);

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
     * Imprime les commandes.
     * 
     * @param pCodeSociete
     *            le code societe
     * @param pNbHistDay
     *            le nombre de jours d'historique
     * @param pNbCopy
     *            le nombre de copies
     */
    public final void printOrder(final String pCodeSociete, final int pNbHistDay, final int pNbCopy) {

        Connection cnx = null;

        try {
            // recuperation d'une connexion a la BDD
            cnx = dbCnxFactory.getRemoteConnection();

            // recuperation des commandes a imprimer
            final List<SynCommande<?>> listSynCommande = synCommandeDao.getListSynCommandePrint(cnx, pCodeSociete,
                    pNbHistDay);

            // flux d'entree du fichier xsl
            InputStream xslInputStream = null;
            // flux d'octets de sortie
            ByteArrayOutputStream outputStream = null;
            // OutputStream outputStream = null;

            // parcours de la liste des commandes a imprimer
            for (SynCommande<?> curSynCommande : listSynCommande) {

                try {
                    // lecture du fichier xsl
                    xslInputStream = getClass().getResourceAsStream("/xsl/commande.xsl");

                    // creation du flux de sortie
                    outputStream = new ByteArrayOutputStream();
                    // outputStream = new BufferedOutputStream(new
                    // FileOutputStream(new File("cmd_"
                    // + curSynCommande.getNumero() + ".pdf")));
                    // creation d'une source d'entree generique a partir de la
                    // commande a imprimer
                    final GenericInputSource<SynCommande<?>> orderInputSource = new GenericInputSource<SynCommande<?>>(
                            curSynCommande);
                    // creation d'une source a partir de la source generique et
                    // du reader xml
                    final Source source = new SAXSource(xmlReader, orderInputSource);
                    // generation du pdf
                    pDFFactory.build(source, new StreamSource(xslInputStream), outputStream);

                    final byte[] data = outputStream.toByteArray();
                    final ByteArrayInputStream istream = new ByteArrayInputStream(data);
                    // impression du pdf
                    pDFPrinter.print(istream, true, pNbCopy);
                    // maj de l'etat d'impression
                    synCommandeDao.updatePrintState(cnx, curSynCommande.getNumero(), 1);
                } catch (Exception exception) {
                    LOGGER.debug(StackTraceUtil.getStackMessage(exception));
                } finally {
                    IOUtils.closeQuietly(xslInputStream);
                    IOUtils.closeQuietly(outputStream);
                }
            }

        } catch (Exception exception) {
            LOGGER.debug(StackTraceUtil.getStackMessage(exception));
        } finally {
            DBUtil.closeConnection(cnx);
        }
    }
}
