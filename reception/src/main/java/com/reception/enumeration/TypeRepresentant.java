package com.reception.enumeration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration des types de representant.
 * 
 * @author Laurent2403 
 *  
 */
public enum TypeRepresentant {

    /**
     * Representant.
     */
    REPRESENTANT(0, "Représentant"),
    /**
     * Chef de zone.
     */
    CHEF_ZONE(1, "Chef de zone");

    /**
     * Table associative enum - valeur.
     */
    private static final Map<Integer, TypeRepresentant> LOOKUP = new HashMap<Integer, TypeRepresentant>();

    /**
     * Remplissage de la map.
     */
    static {
        for (TypeRepresentant s : EnumSet.allOf(TypeRepresentant.class)) {
            LOOKUP.put(s.getCode(), s);
        }
    }

    /**
     * Code du type de representant.
     */
    private int code;
    /**
     * Description du type de representant.
     */
    private String description;

    /**
     * Constructeur.
     * 
     * @param pCode
     *            le code du type de representant
     * @param pDescription
     *            la description du type de representant
     */
    private TypeRepresentant(final int pCode, final String pDescription) {
        this.code = pCode;
        this.description = pDescription;
    }

    /**
     * Retourne le code du type de representant.
     * 
     * @return le code du type de representant
     */
    public final int getCode() {
        return code;
    }

    /**
     * Retourne la description du type de representant.
     * 
     * @return la description du type de representant
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Retourne l'enum a partir du code correspondant.
     * 
     * @param pCode
     *            le code du type de representant
     * @return l'enum a partir du code correspondant
     */
    public static TypeRepresentant get(final int pCode) {
        return LOOKUP.get(pCode);
    }

}
