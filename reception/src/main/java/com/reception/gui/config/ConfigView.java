package com.reception.gui.config;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;

import com.cegim.reception.commons.gui.image.CommonsImage;
import com.cegim.reception.commons.gui.panel.OkCancelApplyPanel;
import com.cegim.reception.commons.gui.tab.TabPanel;
import com.cegim.reception.commons.gui.tab.TabbedPane;
import com.google.inject.Inject;

/**
 * Vue de la configuration.
 * 
 * @author Laurent2403 
 *  
 */
public class ConfigView implements ConfigPresenter.View {

    /**
     * La fenetre.
     */
    private transient JDialog configDialog;
    /**
     * Le gestionnaire d'onglets.
     */
    private transient TabbedPane tabbedPane;
    /**
     * Panneau de boutons ok, annuler et appliquer.
     */
    private transient OkCancelApplyPanel okCclApplPanel;

    /**
     * Dimension de predilection de la fenetre.
     */
    private static final Dimension WINDOW_DIM = new Dimension(640, 480);

    /**
     * Constructeur.
     * 
     */
    @Inject
    public ConfigView() {
        initComponents();
    }

    /**
     * Cette methode est apelle depuis le constructeur et sert a intialiser les
     * composants du panneau.
     * 
     */
    private void initComponents() {

        // creation de la fenetre
        configDialog = new JDialog();
        configDialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);

        // definition de la mise en page
        final MigLayout contentPaneLayout = new MigLayout("", "[grow]", "[grow][]");
        final Container contentPane = configDialog.getContentPane();
        contentPane.setLayout(contentPaneLayout);

        // definition du titre de l'application
        configDialog.setTitle("Configuration");

        // definition du type de fermeture de la fenetre
        configDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        // definition de l'icone de la fenetre
        if (CommonsImage.CONFIG_16 != null) {
            configDialog.setIconImage(((ImageIcon) CommonsImage.CONFIG_16).getImage());
        }

        // definition de la taille de preference de la fenetre
        configDialog.setPreferredSize(ConfigView.WINDOW_DIM);

        // definition de l'icone de la fenetre
        configDialog.pack();

        // centre la fenetre a l'ecran
        configDialog.setLocationRelativeTo(null);

        // creation du gestionnaire d'onglets
        tabbedPane = new TabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        // creation du panneau de boutons
        okCclApplPanel = new OkCancelApplyPanel();

        // ajout de la table a la fentre
        contentPane.add(tabbedPane.getTabbedPane(), "grow, wrap");
        // ajout du panneau des boutons a la fenetre
        contentPane.add(okCclApplPanel.asComponent(), "center");

    }

    /**
     * Ajoute l'onglet General.
     * 
     * @param pComponent
     *            le panneau General
     */
    @Override
    public final void addGeneralTab(final Component pComponent) {

        // creation de l'entete de l'onglet
        final TabPanel generalTabPnl = new TabPanel("Général", CommonsImage.RECEIVE_32, "Général");
        // ajout du composant au gestionnaire d'onglets
        tabbedPane.addTab(pComponent, generalTabPnl);

    }

    /**
     * Ajoute l'onglet Base de donnees.
     * 
     * @param pComponent
     *            le panneau Base de donnees
     */
    @Override
    public final void addDataBaseTab(final Component pComponent) {

        // creation de l'entete de l'onglet
        final TabPanel dataBaseTabPnl = new TabPanel("BDD", CommonsImage.DATABASE_32, "Base De Données");
        // ajout du composant au gestionnaire d'onglets
        tabbedPane.addTab(pComponent, dataBaseTabPnl);

    }

    /**
     * Ajoute l'onglet Commande.
     * 
     * @param pComponent
     *            le panneau Commande
     */
    @Override
    public final void addOrderTab(final Component pComponent) {

        // creation de l'entete de l'onglet
        final TabPanel orderTabPnl = new TabPanel("Commande", CommonsImage.ORDER_32, "Commande");
        // ajout du composant au gestionnaire d'onglets
        tabbedPane.addTab(pComponent, orderTabPnl);

    }

    /**
     * Retourne le bouton ok.
     * 
     * @return le bouton ok
     */
    @Override
    public final JXButton getOkButton() {
        return okCclApplPanel.getOkButton();
    }

    /**
     * Retourne le bouton Annuler.
     * 
     * @return le bouton Annuler
     */
    @Override
    public final JXButton getCancelButton() {
        return okCclApplPanel.getCancelButton();
    }

    /**
     * Retourne le bouton Appliquer.
     * 
     * @return le bouton Appliquer
     */
    @Override
    public final JXButton getApplyButton() {
        return okCclApplPanel.getApplyButton();
    }

    /**
     * Affiche ou masque la fenetre.
     * 
     * @param pVisible
     *            true pour afficher, false pour masquer
     */
    @Override
    public final void setVisible(final boolean pVisible) {
        configDialog.setVisible(pVisible);
    }

    /**
     * Retourne le composant.
     * 
     * @return le composant
     */
    @Override
    public final Component asComponent() {
        return configDialog;
    }
}
