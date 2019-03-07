package com.reception.dao;

/**
 * Enumeration des tables de la base de donnees distante.
 * 
 * @author Laurent2403 
 *  
 */
public enum RemoteTable {

    /**
     * Table representant.
     */
    REPRESENTANT("repres"),
    /**
     * Table des commandes synchronisees.
     */
    SYN_COMMANDE("syn_commande"),
    /**
     * Table des details de commande synchronises.
     */
    SYN_COMDET("syn_comdet"),
    /**
     * Table client.
     */
    CLIENT("client");

    /**
     * Nom de la table.
     */
    private String name;

    /**
     * Constructeur prive.
     * 
     * @param pName
     *            le nom de la table
     */
    private RemoteTable(final String pName) {

        this.name = pName;

    }

    /**
     * Retourne le nom de la table.
     * 
     * @return le nom de la table
     */
    public final String getName() {

        return name;

    }

}
