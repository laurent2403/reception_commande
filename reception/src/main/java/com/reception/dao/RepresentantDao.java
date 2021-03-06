package com.reception.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cegim.commons.dao.exception.DaoException;
import com.cegim.commons.database.util.DBUtil;
import com.cegim.commons.exception.util.StackTraceUtil;
import com.reception.entite.Representant;

/**
 * Couche d'acces aux representants en BDD.
 * 
 * @author Laurent2403 
 *  
 */
public class RepresentantDao {

    /**
     * Logger de la classe.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RepresentantDao.class);

    /**
     * Champs.
     */
    public static final String FIELDS = "re_repres, re_nom, re_type";

    /**
     * Retourne une instance de l'objet <code>Representant</code> a partir du
     * ResultSet passe en parametre.
     * 
     * @param resultSet
     *            le ResultSet
     * @return une instance de l'objet <code>Representant</code> a partir du
     *         ResultSet passe en parametre.
     * @throws SQLException
     *             si une erreur d'acces a la base survient
     */
    private Representant getRepresentantFromResultSet(final ResultSet resultSet) throws SQLException {

        final Representant resultat = new Representant();

        resultat.setCode(StringUtils.trim(resultSet.getString("re_repres")));
        resultat.setNom(StringUtils.trim(resultSet.getString("re_nom")));
        resultat.setType(resultSet.getInt("re_type"));

        return resultat;
    }

    /**
     * Retourne la liste des <code>Representant</code>.
     * 
     * @param cnx
     *            la connexion a la BDD
     * @param pCodeSociete
     *            le code societe
     * @return la liste des <code>Representant</code>
     * @see Representant
     */
    public final List<Representant> getList(final Connection cnx, final String pCodeSociete) {

        PreparedStatement selectPStmt = null;
        ResultSet resultSet = null;
        final StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("select ");
        selectQuery.append(RepresentantDao.FIELDS);
        selectQuery.append(" from ");
        selectQuery.append(RemoteTable.REPRESENTANT.getName());
        if (StringUtils.isNotBlank(pCodeSociete)) {
            // si multi societe
        }

        final List<Representant> listRepresentant = new ArrayList<Representant>();

        try {
            selectPStmt = cnx.prepareStatement(selectQuery.toString());
            if (StringUtils.isNotBlank(pCodeSociete)) {
                // si multi societe
            }

            resultSet = selectPStmt.executeQuery();
            while (resultSet.next()) {
                listRepresentant.add(getRepresentantFromResultSet(resultSet));
            }
        } catch (SQLException sqle) {
            LOGGER.error(StackTraceUtil.getStackMessage(sqle));
            throw new DaoException("Erreur lors de la récupération des représentants en BDD", sqle);
        } finally {
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(selectPStmt);
        }
        return listRepresentant;
    }

}
