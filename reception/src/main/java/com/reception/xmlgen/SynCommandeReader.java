package com.reception.xmlgen;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cegim.commons.exception.util.StackTraceUtil;
import com.cegim.reception.commons.bean.ZSynComDet;
import com.cegim.reception.commons.entite.ZClient;
import com.cegim.reception.commons.xmlgen.AbstractObjectReader;
import com.cegim.reception.commons.xmlgen.GenericInputSource;
import com.reception.bean.SynCommande;
import com.reception.entite.Representant;
import com.reception.enumeration.TypeRepresentant;

/**
 * Parcours une commande synchronisee et genere les evenements SAX necessaires
 * en vue de la generation XML.
 * 
 * @author Laurent2403 
 *  
 */
public class SynCommandeReader extends AbstractObjectReader {

    /**
     * Logger de la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SynCommandeReader.class);

    /**
     * Formatter de date.
     */
    private final DateTimeFormatter dateTimeFmt;
    /**
     * Formatter de quantite.
     */
    private final DecimalFormat qteFmt;
    /**
     * Formatter de montant.
     */
    private final DecimalFormat mntFmt;
    /**
     * Formatter du PV net.
     */
    private final DecimalFormat pvNetFmt;
    /**
     * Chaine "null".
     */
    private static final String NULL_STRING = "null";

    /**
     * Constructeur.
     */
    public SynCommandeReader() {

        dateTimeFmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        qteFmt = new DecimalFormat("#,###.##");
        mntFmt = new DecimalFormat("#,###.##");
        pvNetFmt = new DecimalFormat("#,###.00");

    }

    /**
     * Parse la source passe en parametre.
     * 
     * @param pInput
     *            la source a parser
     * @see org.xml.sax.XMLReader#parse(InputSource)
     */
    public final void parse(final InputSource pInput) {
        if (pInput instanceof GenericInputSource<?>) {
            parse((SynCommande<ZSynComDet>) ((GenericInputSource<SynCommande<ZSynComDet>>) pInput).getObject());
        } else {
            LOGGER.error("La source specifiee n'est pas supportee, doit etre une SynCommande");
        }
    }

    /**
     * Parse la commande synchronisee.
     * 
     * @param pSynCommande
     *            la commande synchronisee a parser
     */
    public final void parse(final SynCommande<ZSynComDet> pSynCommande) {

        if (handler == null) {
            LOGGER.error("ContentHandler not set");
        } else {

            try {
                // debute le document
                handler.startDocument();

                handler.startElement("commande");

                handler.element("numeroCmd", pSynCommande.getNumero());

                final DateTime dateCreation = pSynCommande.getDateCreation();
                if (dateCreation == null) {
                    handler.element("dateCreationCmd", NULL_STRING);
                } else {
                    handler.element("dateCreationCmd", dateTimeFmt.print(dateCreation));
                }

                final DateTime dateSynchro = pSynCommande.getDateSynchro();
                if (dateSynchro == null) {
                    handler.element("dateSynchroCmd", NULL_STRING);
                } else {
                    handler.element("dateSynchroCmd", dateTimeFmt.print(dateSynchro));
                }

                final DateTime datePrint = new DateTime();
                handler.element("datePrintCmd", dateTimeFmt.print(datePrint));

                final ZClient client = pSynCommande.getClient();
                if (client == null) {
                    handler.element("codeClient", "");
                    handler.element("nomClient", "");
                    handler.element("AdrLigne1Client", "");
                    handler.element("AdrLigne2Client", "");
                    handler.element("bpClient", "");
                    handler.element("cpClient", "");
                    handler.element("villeClient", "");
                    handler.element("telClient", "");
                } else {
                    handler.element("codeClient", client.getCode());
                    handler.element("nomClient", client.getNom());
                    handler.element("AdrLigne1Client", client.getAdrLigne1());
                    handler.element("AdrLigne2Client", client.getAdrLigne2());
                    handler.element("bpClient", client.getBoitePostale());
                    handler.element("cpClient", client.getCodePostal());
                    handler.element("villeClient", client.getVille());
                    handler.element("telClient", client.getTelephone());
                }

                final Representant representant = (Representant) pSynCommande.getRepresentant();
                if (representant == null) {
                    handler.element("codeRepresentant", "");
                    handler.element("nomRepresentant", "");
                    handler.element("typeRepresentant", "");
                } else {
                    handler.element("codeRepresentant", representant.getCode());
                    handler.element("nomRepresentant", representant.getNom());
                    TypeRepresentant typeRepres = TypeRepresentant.get(representant.getType());
                    if (typeRepres == null) {
                        handler.element("typeRepresentant", "");
                    } else {
                        handler.element("typeRepresentant", typeRepres.getDescription());
                    }
                }

                final Representant represClient = (Representant) pSynCommande.getRepresClient();
                if (represClient == null) {
                    handler.element("typeRepresClient", "Représentant client inconnu");
                    handler.element("codeRepresClient", "");
                    handler.element("nomRepresClient", "");
                } else {
                    if (StringUtils.isNotBlank(represClient.getCode())) {
                        handler.element("codeRepresClient", represClient.getCode());
                    } else {
                        handler.element("codeRepresClient", "?");
                    }
                    if (StringUtils.isNotBlank(represClient.getNom())) {
                        handler.element("nomRepresClient", represClient.getNom());
                    } else {
                        handler.element("nomRepresClient", "?");
                    }
                    TypeRepresentant typeRepres = TypeRepresentant.get(represClient.getType());
                    if (typeRepres == null) {
                        handler.element("typeRepresClient", "?");
                    } else {
                        handler.element("typeRepresClient", typeRepres.getDescription());
                    }
                }

                final Representant chefZoneClient = (Representant) pSynCommande.getChefZoneClient();
                if (chefZoneClient == null) {
                    handler.element("typeChefZoneClient", "Chef zone client inconnu");
                    handler.element("codeChefZoneClient", "");
                    handler.element("nomChefZoneClient", "");
                } else {
                    if (StringUtils.isNotBlank(chefZoneClient.getCode())) {
                        handler.element("codeChefZoneClient", chefZoneClient.getCode());
                    } else {
                        handler.element("codeChefZoneClient", "?");
                    }
                    if (StringUtils.isNotBlank(chefZoneClient.getNom())) {
                        handler.element("nomChefZoneClient", chefZoneClient.getNom());
                    } else {
                        handler.element("nomChefZoneClient", "?");
                    }
                    TypeRepresentant typeRepres = TypeRepresentant.get(chefZoneClient.getType());
                    if (typeRepres == null) {
                        handler.element("typeChefZoneClient", "?");
                    } else {
                        handler.element("typeChefZoneClient", typeRepres.getDescription());
                    }
                }

                handler.element("message", pSynCommande.getMessage());

                if (pSynCommande.getListSynComDet() != null) {
                    handler.startElement("listeComDet");
                    for (ZSynComDet curSynComDet : pSynCommande.getListSynComDet()) {
                        handler.startElement("comDet");
                        handler.element("reference", curSynComDet.getReference());
                        handler.element("gencod", curSynComDet.getGencod());
                        handler.element("designation", curSynComDet.getDesignation());
                        handler.element("qteCmd", qteFmt.format(curSynComDet.getQteCmd()));
                        handler.element("pvClient", pvNetFmt.format(curSynComDet.getPvClient()));
                        handler.element("pcRemiseClient", String.valueOf(curSynComDet.getPcRemiseClient()));
                        handler.element("totalLigne", mntFmt.format(curSynComDet.getTotalLigne()));
                        handler.endElement("comDet");
                    }
                    handler.endElement("listeComDet");
                }

                if (pSynCommande.getListSynComDet() == null) {
                    handler.element("nbLigne", "0");
                } else {
                    handler.element("nbLigne", String.valueOf(pSynCommande.getListSynComDet().size()));
                }

                handler.element("totalNet", mntFmt.format(pSynCommande.getTotal()));

                handler.endElement("commande");

                // termine le document
                handler.endDocument();

            } catch (SAXException saxe) {
                // mis en debug ici car cette fonction est executee dans un
                // thread, si y'a un probleme faudrait pas saturer le disque
                // avec les logs de la stacktrace de l'exception
                LOGGER.debug(StackTraceUtil.getStackMessage(saxe));
            }
        }

    }
}
