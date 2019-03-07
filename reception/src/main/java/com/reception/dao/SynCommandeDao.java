package com.reception.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cegim.commons.dao.exception.DaoException;
import com.cegim.commons.database.util.DBUtil;
import com.cegim.commons.exception.util.StackTraceUtil;
import com.cegim.reception.commons.bean.ZSynComDet;
import com.cegim.reception.commons.bean.ZSynCommande;
import com.cegim.reception.commons.entite.ZClient;
import com.reception.bean.SynCommande;
import com.reception.entite.Representant;

/**
 * Couche d'acces aux commandes synchronisees en BDD.
 * 
 * @author Laurent2403 
 *  
 */
public class SynCommandeDao {

    /**
     * Logger de la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SynCommandeDao.class);

    /**
     * Champs a recuperer pour l'affichage des commandes synchronisees.
     */
    public static final String DISPLAY_FIELDS = "sce_id, sce_soc, sce_nocde, sce_origine, sce_client, sce_nom,"
            + " sce_date_creation, sce_date_synchro, sce_etat, sce_etat_impr, sce_repres";

    /**
     * Nom alias table Representant pour le representant du client.
     */
    private static String represCliTbl = "represCliTbl";
    /**
     * Nom alias table Representant pour le chef de zone du client.
     */
    private static String cfzCliTbl = "cfzCliTbl";

    /**
     * Champs a recuperer pour l'impression des commandes synchronisees.
     */
    public static final StringBuilder PRINT_FIELDS = new StringBuilder();

    static {
        PRINT_FIELDS.append("sce_id, sce_soc, sce_nocde, sce_origine, sce_client, sce_nom, ");
        PRINT_FIELDS.append("sce_adr1, sce_adr2, sce_bp, sce_cp, sce_ville, sce_tel, ");
        PRINT_FIELDS.append("sce_mess, sce_date_creation, sce_date_synchro, sce_etat, sce_etat_impr, sce_repres, ");
        // champs representant auteur de la commande
        PRINT_FIELDS.append(RemoteTable.REPRESENTANT.getName() + ".re_nom, ");
        PRINT_FIELDS.append(RemoteTable.REPRESENTANT.getName() + ".re_type, ");
        // champs representant associe au client
        PRINT_FIELDS.append(represCliTbl + ".re_repres as repCliCode, ");
        PRINT_FIELDS.append(represCliTbl + ".re_nom as repCliNom, ");
        PRINT_FIELDS.append(represCliTbl + ".re_type as repCliType, ");
        // champs representant chef de zone associe au client
        PRINT_FIELDS.append(cfzCliTbl + ".re_repres as cfzCliCode, ");
        PRINT_FIELDS.append(cfzCliTbl + ".re_nom as cfzCliNom, ");
        PRINT_FIELDS.append(cfzCliTbl + ".re_type as cfzCliType, ");
        // autres champs de la commande
        PRINT_FIELDS.append("scd_ordre, scd_ref, scd_gencod, scd_desig, scd_qte, ");
        PRINT_FIELDS.append("scd_pv_client, scd_pc_rem_client, scd_total_ligne");
    }

    /**
     * Retourne une instance de l'objet <code>SynCommande</code> en vue de
     * l'afficher a partir du ResultSet passe en parametre.
     * 
     * @param resultSet
     *            le ResultSet
     * @return une instance de l'objet <code>SynCommande</code> a partir du
     *         ResultSet passe en parametre.
     * @throws SQLException
     *             si une erreur d'acces a la base survient
     */
    private ZSynCommande<?> getSynCommandeDisplayFromResultSet(final ResultSet resultSet) throws SQLException {

        final ZSynCommande<?> resultat = new ZSynCommande<ZSynComDet>();

        resultat.setIdentifiant(resultSet.getLong("sce_id"));
        resultat.setCodeSociete(StringUtils.trim(resultSet.getString("sce_soc")));
        resultat.setNumero(StringUtils.trim(resultSet.getString("sce_nocde")));
        resultat.setOrigine(StringUtils.trim(resultSet.getString("sce_origine")));

        ZClient client = new ZClient();
        client.setCode(StringUtils.trim(resultSet.getString("sce_client")));
        client.setNom(StringUtils.trim(resultSet.getString("sce_nom")));
        resultat.setClient(client);

        java.sql.Timestamp dateCreationTimestamp = resultSet.getTimestamp("sce_date_creation");
        if (dateCreationTimestamp != null) {
            resultat.setDateCreation(new DateTime(dateCreationTimestamp.getTime()));
        }
        java.sql.Timestamp dateSynchroTimestamp = resultSet.getTimestamp("sce_date_synchro");
        if (dateSynchroTimestamp != null) {
            resultat.setDateSynchro(new DateTime(dateSynchroTimestamp.getTime()));
        }
        resultat.setEtat(resultSet.getInt("sce_etat"));
        resultat.setEtatImpr(resultSet.getInt("sce_etat_impr"));

        Representant repres = new Representant();
        repres.setCode(resultSet.getString("sce_repres"));
        resultat.setRepresentant(repres);

        resultat.setTotal(resultSet.getBigDecimal("total"));

        return resultat;
    }

    /**
     * Retourne la liste a afficher des <code>ZSynCommande</code> pour la
     * societe passe en parametre et dont la date est superieure a J - le nombre
     * de jours d'historique.
     * 
     * @param cnx
     *            la connexion a la bdd
     * @param pCodeSociete
     *            le code societe
     * @param pNbHistDay
     *            le nombre de jours d'historique
     * @return la liste des <code>ZSynCommande</code>
     * @see ZSynCommande
     */
    public final List<ZSynCommande<?>> getListSynCommandeDisplay(final Connection cnx, final String pCodeSociete,
            final int pNbHistDay) {

        PreparedStatement selectPStmt = null;
        ResultSet resultSet = null;

        final StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("select ");
        selectQuery.append(SynCommandeDao.DISPLAY_FIELDS);
        selectQuery.append(", sum(scd_total_ligne) as total");
        selectQuery.append(" from ");
        selectQuery.append(RemoteTable.SYN_COMMANDE.getName() + ", ");
        selectQuery.append(RemoteTable.SYN_COMDET.getName());
        // jointure entete detail de commande
        selectQuery.append(" where scd_id_sce = sce_id");
        // restriction a l'historique
        selectQuery.append(" and sce_date_synchro > ?");
        // regroupement par commande
        selectQuery.append(" group by " + SynCommandeDao.DISPLAY_FIELDS);
        // affichage du plus recent au plus ancien
        selectQuery.append(" order by sce_date_synchro desc");

        final List<ZSynCommande<?>> listSynCommande = new ArrayList<ZSynCommande<?>>();
        try {
            selectPStmt = cnx.prepareStatement(selectQuery.toString());
            final DateTime now = new DateTime();
            final DateTime startDateTime = now.minusDays(pNbHistDay);
            final java.sql.Timestamp startTimestamp = new java.sql.Timestamp(startDateTime.getMillis());
            selectPStmt.setTimestamp(1, startTimestamp);
            resultSet = selectPStmt.executeQuery();
            while (resultSet.next()) {
                listSynCommande.add(getSynCommandeDisplayFromResultSet(resultSet));
            }

        } catch (SQLException sqle) {
            LOGGER.error(StackTraceUtil.getStackMessage(sqle));
            throw new DaoException(
                    "Erreur lors de la récupération des commandes synchronisées à afficher (codeSociete = "
                            + pCodeSociete + ", nbHistDay = " + pNbHistDay + ") en BDD", sqle);
        } finally {
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(selectPStmt);
        }
        return listSynCommande;

    }

    /**
     * Retourne une instance de l'objet <code>SynCommande</code> en vue de
     * l'imprimer a partir du ResultSet passe en parametre.
     * 
     * @param resultSet
     *            le ResultSet
     * @return une instance de l'objet <code>SynCommande</code> a partir du
     *         ResultSet passe en parametre.
     * @throws SQLException
     *             si une erreur d'acces a la base survient
     */
    private SynCommande<ZSynComDet> getSynCommandePrintFromResultSet(final ResultSet resultSet) throws SQLException {

        final SynCommande<ZSynComDet> resultat = new SynCommande<ZSynComDet>();

        resultat.setIdentifiant(resultSet.getLong("sce_id"));
        resultat.setCodeSociete(StringUtils.trim(resultSet.getString("sce_soc")));
        resultat.setNumero(StringUtils.trim(resultSet.getString("sce_nocde")));
        resultat.setOrigine(StringUtils.trim(resultSet.getString("sce_origine")));

        final ZClient client = new ZClient();
        client.setCode(StringUtils.trim(resultSet.getString("sce_client")));
        client.setNom(StringUtils.trim(resultSet.getString("sce_nom")));
        client.setAdrLigne1(StringUtils.trim(resultSet.getString("sce_adr1")));
        client.setAdrLigne2(StringUtils.trim(resultSet.getString("sce_adr2")));
        client.setBoitePostale(StringUtils.trim(resultSet.getString("sce_bp")));
        client.setCodePostal(StringUtils.trim(resultSet.getString("sce_cp")));
        client.setVille(StringUtils.trim(resultSet.getString("sce_ville")));
        client.setTelephone(StringUtils.trim(resultSet.getString("sce_tel")));
        resultat.setClient(client);

        final Representant repres = new Representant();
        repres.setCode(StringUtils.trim(resultSet.getString("sce_repres")));
        repres.setNom(StringUtils.trim(resultSet.getString("re_nom")));
        repres.setType(resultSet.getInt("re_type"));
        resultat.setRepresentant(repres);

        final String sRepCliCode = resultSet.getString("repCliCode");
        if (sRepCliCode != null) {
            final Representant represCli = new Representant();
            represCli.setCode(StringUtils.trim(sRepCliCode));
            represCli.setNom(StringUtils.trim(resultSet.getString("repCliNom")));
            represCli.setType(resultSet.getInt("repCliType"));
            resultat.setRepresClient(represCli);
        }

        final String sCfzCliCode = resultSet.getString("cfzCliCode");
        if (sCfzCliCode != null) {
            final Representant chefZoneCli = new Representant();
            chefZoneCli.setCode(StringUtils.trim(sCfzCliCode));
            chefZoneCli.setNom(StringUtils.trim(resultSet.getString("cfzCliNom")));
            chefZoneCli.setType(resultSet.getInt("cfzCliType"));
            resultat.setChefZoneClient(chefZoneCli);
        }

        resultat.setMessage(StringUtils.trim(resultSet.getString("sce_mess")));

        java.sql.Timestamp dateCreationTimestamp = resultSet.getTimestamp("sce_date_creation");
        if (dateCreationTimestamp != null) {
            resultat.setDateCreation(new DateTime(dateCreationTimestamp.getTime()));
        }
        java.sql.Timestamp dateSynchroTimestamp = resultSet.getTimestamp("sce_date_synchro");
        if (dateSynchroTimestamp != null) {
            resultat.setDateSynchro(new DateTime(dateSynchroTimestamp.getTime()));
        }
        resultat.setEtat(resultSet.getInt("sce_etat"));
        resultat.setEtatImpr(resultSet.getInt("sce_etat_impr"));

        return resultat;
    }

    /**
     * Retourne une instance de l'objet <code>SynComDet</code> en vue de
     * l'imprimer a partir du ResultSet passe en parametre.
     * 
     * @param resultSet
     *            le ResultSet
     * @return une instance de l'objet <code>SynComDet</code> a partir du
     *         ResultSet passe en parametre.
     * @throws SQLException
     *             si une erreur d'acces a la base survient
     */
    private ZSynComDet getSynComDetPrintFromResultSet(final ResultSet resultSet) throws SQLException {

        final ZSynComDet resultat = new ZSynComDet();

        resultat.setReference(StringUtils.trim(resultSet.getString("scd_ref")));
        resultat.setGencod(StringUtils.trim(resultSet.getString("scd_gencod")));
        resultat.setDesignation(StringUtils.trim(resultSet.getString("scd_desig")));
        resultat.setQteCmd(resultSet.getBigDecimal("scd_qte"));
        resultat.setPvClient(resultSet.getBigDecimal("scd_pv_client"));
        resultat.setPcRemiseClient(resultSet.getBigDecimal("scd_pc_rem_client"));
        resultat.setTotalLigne(resultSet.getBigDecimal("scd_total_ligne"));

        return resultat;
    }

    /**
     * Retourne la liste a imprimer des <code>SynCommande</code> pour la societe
     * passe en parametre et dont la date est superieure a J - le nombre de
     * jours d'historique.
     * 
     * @param cnx
     *            la connexion a la bdd
     * @param pCodeSociete
     *            le code societe
     * @param pNbHistDay
     *            le nombre de jours d'historique
     * @return la liste des <code>SynCommande</code>
     * @see SynCommande
     */
    public final List<SynCommande<?>> getListSynCommandePrint(final Connection cnx, final String pCodeSociete,
            final int pNbHistDay) {

        PreparedStatement selectPStmt = null;
        ResultSet resultSet = null;

        final StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("select ");
        selectQuery.append(SynCommandeDao.PRINT_FIELDS);
        selectQuery.append(" from ");
        selectQuery.append(RemoteTable.SYN_COMMANDE.getName() + ", ");
        selectQuery.append(RemoteTable.SYN_COMDET.getName() + ", ");
        selectQuery.append(RemoteTable.CLIENT.getName());
        selectQuery.append(", outer ");
        selectQuery.append(RemoteTable.REPRESENTANT.getName());
        selectQuery.append(", outer ");
        selectQuery.append(RemoteTable.REPRESENTANT.getName() + " " + represCliTbl);
        selectQuery.append(", outer ");
        selectQuery.append(RemoteTable.REPRESENTANT.getName() + " " + cfzCliTbl);
        // jointure entete detail de commande
        selectQuery.append(" where scd_id_sce = sce_id");
        // jointure representant
        selectQuery.append(" and sce_repres = " + RemoteTable.REPRESENTANT.getName() + ".re_repres");
        // jointure client
        selectQuery.append(" and sce_client = c_client");
        // jointure representant du client
        selectQuery.append(" and c_repres = " + represCliTbl + ".re_repres");
        // jointure chef de zone du client
        selectQuery.append(" and c_chefzone = " + cfzCliTbl + ".re_repres");
        // restriction a l'historique
        selectQuery.append(" and sce_date_synchro > ?");
        // commande pas encore imprime
        selectQuery.append(" and sce_etat_impr = 0");
        // et a l'etat synchronisee
        selectQuery.append(" and sce_etat = 0");
        // tri par commande puis par ordre
        selectQuery.append(" order by sce_nocde, scd_ordre");

        final List<SynCommande<?>> listSynCommande = new ArrayList<SynCommande<?>>();
        try {
            selectPStmt = cnx.prepareStatement(selectQuery.toString());
            final DateTime now = new DateTime();
            final DateTime startDateTime = now.minusDays(pNbHistDay);
            final java.sql.Timestamp startTimestamp = new java.sql.Timestamp(startDateTime.getMillis());
            selectPStmt.setTimestamp(1, startTimestamp);
            resultSet = selectPStmt.executeQuery();
            String orderNum = "";
            String oldOrderNum = "";
            SynCommande<ZSynComDet> curSynCommande = null;
            ZSynComDet curSynComDet = null;
            while (resultSet.next()) {
                orderNum = StringUtils.trim(resultSet.getString("sce_nocde"));
                if (orderNum.compareTo(oldOrderNum) != 0) {
                    // il s'agit d'une nouvelle commande
                    curSynCommande = getSynCommandePrintFromResultSet(resultSet);
                    curSynCommande.setListSynComDet(new ArrayList<ZSynComDet>());
                    listSynCommande.add(curSynCommande);
                }
                curSynComDet = getSynComDetPrintFromResultSet(resultSet);
                curSynCommande.getListSynComDet().add(curSynComDet);
                curSynCommande.setTotal(curSynCommande.getTotal().add(curSynComDet.getTotalLigne()));
                oldOrderNum = new String(orderNum);
            }

        } catch (SQLException sqle) {
            LOGGER.error(StackTraceUtil.getStackMessage(sqle));
            throw new DaoException(
                    "Erreur lors de la récupération des commandes synchronisées à imprimer (codeSociete = "
                            + pCodeSociete + ", nbHistDay = " + pNbHistDay + ") en BDD", sqle);
        } finally {
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(selectPStmt);
        }
        return listSynCommande;

    }

    /**
     * Retourne la commande a imprimer <code>SynCommande</code> pour la societe
     * et le numero passes en parametre.
     * 
     * @param cnx
     *            la connexion a la bdd
     * @param pCodeSociete
     *            le code societe
     * @param pNumCmd
     *            le numero de commande
     * @return la commande <code>ZSynCommande</code>
     * @see SynCommande
     */
    public final SynCommande<ZSynComDet> getSynCommandePrint(final Connection cnx, final String pCodeSociete,
            final String pNumCmd) {

        PreparedStatement selectPStmt = null;
        ResultSet resultSet = null;

        final StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("select ");
        selectQuery.append(SynCommandeDao.PRINT_FIELDS);
        selectQuery.append(" from ");
        selectQuery.append(RemoteTable.SYN_COMMANDE.getName() + ", ");
        selectQuery.append(RemoteTable.SYN_COMDET.getName() + ", ");
        selectQuery.append(RemoteTable.CLIENT.getName());
        selectQuery.append(", outer ");
        selectQuery.append(RemoteTable.REPRESENTANT.getName());
        selectQuery.append(", outer ");
        selectQuery.append(RemoteTable.REPRESENTANT.getName() + " " + represCliTbl);
        selectQuery.append(", outer ");
        selectQuery.append(RemoteTable.REPRESENTANT.getName() + " " + cfzCliTbl);
        // jointure entete detail de commande
        selectQuery.append(" where scd_id_sce = sce_id");
        // jointure representant
        selectQuery.append(" and sce_repres = " + RemoteTable.REPRESENTANT.getName() + ".re_repres");
        // jointure client
        selectQuery.append(" and sce_client = c_client");
        // jointure representant du client
        selectQuery.append(" and c_repres = " + represCliTbl + ".re_repres");
        // jointure chef de zone du client
        selectQuery.append(" and c_chefzone = " + cfzCliTbl + ".re_repres");
        // critere sur le numero de commande
        selectQuery.append(" and sce_nocde = ?");
        // tri par commande puis par ordre
        selectQuery.append(" order by sce_nocde, scd_ordre");

        SynCommande<ZSynComDet> synCommande = null;
        try {
            selectPStmt = cnx.prepareStatement(selectQuery.toString());
            selectPStmt.setString(1, pNumCmd);
            resultSet = selectPStmt.executeQuery();
            String orderNum = "";
            String oldOrderNum = "";

            ZSynComDet curSynComDet = null;
            while (resultSet.next()) {
                orderNum = StringUtils.trim(resultSet.getString("sce_nocde"));
                if (orderNum.compareTo(oldOrderNum) != 0) {
                    // il s'agit d'une nouvelle commande
                    synCommande = getSynCommandePrintFromResultSet(resultSet);
                    synCommande.setListSynComDet(new ArrayList<ZSynComDet>());
                }
                curSynComDet = getSynComDetPrintFromResultSet(resultSet);
                synCommande.getListSynComDet().add(curSynComDet);
                synCommande.setTotal(synCommande.getTotal().add(curSynComDet.getTotalLigne()));
                oldOrderNum = new String(orderNum);
            }

        } catch (SQLException sqle) {
            LOGGER.error(StackTraceUtil.getStackMessage(sqle));
            throw new DaoException(
                    "Erreur lors de la récupération de la commande synchronisée à imprimer (codeSociete = "
                            + pCodeSociete + ", numero = " + pNumCmd + ") en BDD", sqle);
        } finally {
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(selectPStmt);
        }
        return synCommande;

    }

    /**
     * Met a jour l'etat d'impression de la commande passee en parametre.
     * 
     * @param cnx
     *            la connexion a la bdd
     * @param pNumCmd
     *            le numero de la commande a mettre a jour
     * @param pPrintState
     *            l'etat de l'impression
     * @return le nombre d'enregistrements mis a jour
     */
    public final int updatePrintState(final Connection cnx, final String pNumCmd, final int pPrintState) {

        int nbUpdate = 0;

        PreparedStatement updatePStmt = null;

        final StringBuilder updateQuery = new StringBuilder();
        updateQuery.append("update ");
        updateQuery.append(RemoteTable.SYN_COMMANDE.getName());
        updateQuery.append(" set sce_etat_impr = ?");
        updateQuery.append(" where sce_nocde = ?");

        try {
            updatePStmt = cnx.prepareStatement(updateQuery.toString());
            updatePStmt.setInt(1, pPrintState);
            updatePStmt.setString(2, pNumCmd);
            nbUpdate = updatePStmt.executeUpdate();
        } catch (SQLException sqle) {
            LOGGER.error(StackTraceUtil.getStackMessage(sqle));
            throw new DaoException("Erreur lors de la mise a jour de l'etat de l'impression de la commande " + pNumCmd,
                    sqle);
        } finally {
            DBUtil.closePreparedStatement(updatePStmt);
        }

        return nbUpdate;
    }
}
