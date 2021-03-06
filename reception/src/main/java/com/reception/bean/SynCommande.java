package com.reception.bean;

import java.io.Serializable;

import com.cegim.reception.commons.bean.ZSynComDet;
import com.cegim.reception.commons.bean.ZSynCommande;
import com.reception.entite.Representant;

/**
 * Classe bean <code>SynCommande</code>.
 * 
 * @param <E>
 *            le type des details de commande
 * 
 *  
 */
public class SynCommande<E> extends ZSynCommande<ZSynComDet> implements Serializable {

    /**
     * Numero de version de la classe.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representant associe au client.
     */
    private Representant represClient;
    /**
     * Chef de zone associe au client.
     */
    private Representant chefZoneClient;

    /**
     * Retourne le representant associe au client.
     * 
     * @return le representant associe au client
     */
    public final Representant getRepresClient() {
        return this.represClient;
    }

    /**
     * Modifie le representant associe au client.
     * 
     * @param pRepresClient
     *            le nouveau representant associe au client
     */
    public final void setRepresClient(final Representant pRepresClient) {
        this.represClient = pRepresClient;
    }

    /**
     * Retourne le chef de zone associe au client.
     * 
     * @return le chef de zone associe au client
     */
    public final Representant getChefZoneClient() {
        return this.chefZoneClient;
    }

    /**
     * Modifie le chef de zone associe au client.
     * 
     * @param pChefZoneClient
     *            le nouveau chef de zone associe au client
     */
    public final void setChefZoneClient(final Representant pChefZoneClient) {
        this.chefZoneClient = pChefZoneClient;
    }

}
