package com.reception.gui.about;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;

import com.cegim.reception.commons.gui.about.AboutPresenter;
import com.cegim.reception.commons.gui.image.CommonsImage;
import com.cegim.reception.commons.gui.styleguide.CommonsStyleGuide;
import com.google.inject.Inject;
import com.reception.gui.main.MainPresenter;

/**
 * Vue A Propos.
 * 
 * @author Laurent2403 
 */
public class AboutView implements AboutPresenter.View {

    /**
     * La vue principale.
     */
    private final transient MainPresenter.View mainView;
    /**
     * Fenetre a propos.
     */
    private transient JDialog aboutDialog;
    /**
     * Le bouton ok.
     */
    private transient JXButton okButton;

    /**
     * Dimension de predilection de la fenetre.
     */
    private static final Dimension WINDOW_DIM = new Dimension(480, 340);

    /**
     * Constructeur.
     * 
     * @param pMainView
     *            la vue principale
     */
    @Inject
    public AboutView(final MainPresenter.View pMainView) {

        mainView = pMainView;
        initComponents();

    }

    /**
     * Cette methode est apelle depuis le constructeur et sert a intialiser les
     * composants du panneau.
     * 
     */
    private void initComponents() {

        // creation de la fenetre modale
        // et rattache a la fenetre de la vue principale
        aboutDialog = new JDialog(mainView.getFrame(), true);

        // definition de la mise en page
        final MigLayout contentPaneLayout = new MigLayout("", "[grow][]100[][grow]", "[][grow][][][][grow]");
        final Container contentPane = aboutDialog.getContentPane();
        contentPane.setLayout(contentPaneLayout);

        // definition du titre de la fenetre
        aboutDialog.setTitle("A propos");

        // definition du type de fermeture de la fenetre
        aboutDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        // definition de l'icone de la fenetre
        if (CommonsImage.RECEIVE_32 != null) {
            aboutDialog.setIconImage(((ImageIcon) CommonsImage.INFO_16).getImage());
        }

        // definition de la taille de preference de la fenetre
        aboutDialog.setPreferredSize(AboutView.WINDOW_DIM);

        // conditionne la fenetre
        aboutDialog.pack();

        // centre la fenetre par rapport a la fenetre parente
        aboutDialog.setLocationRelativeTo(mainView.getFrame());

        // titre du panneau
        final JXLabel titleLabel = new JXLabel("A propos");
        // generation d'une police plus grosse
        final Font defaultFont = titleLabel.getFont();
        final Font bigFont = defaultFont.deriveFont((float) (defaultFont.getSize() * CommonsStyleGuide.BIG_FONT_RATIO));
        titleLabel.setFont(bigFont);

        // creation du logo
        final JXLabel logoLbl = new JXLabel();
        logoLbl.setIcon(CommonsImage.LOGO_CEGIM);

        // creation du libelle du nom de l'application
        final JXLabel nomAppliLbl = new JXLabel("Réception");
        nomAppliLbl.setFont(bigFont);

        // creation du libelle du numero de version
        final String version = "Version : " + this.getClass().getPackage().getImplementationVersion();
        final JXLabel versionLbl = new JXLabel(version);

        // creation du nom de la societe
        final JXLabel societeLbl = new JXLabel("");

        // creation du bouton ok
        okButton = new JXButton("OK");
        okButton.setIcon(CommonsImage.OK_32);
        okButton.setToolTipText("Valider");

        contentPane.add(titleLabel, "center, span 4 1, wrap");
        contentPane.add(logoLbl, "newline, span 1 3, skip 1");
        contentPane.add(nomAppliLbl, "wrap");
        contentPane.add(versionLbl, "skip 1, wrap");
        contentPane.add(societeLbl, "skip 1, wrap");
        contentPane.add(okButton, "center, span 4 1");

    }

    /**
     * Retourne le bouton ok.
     * 
     * @return le bouton ok
     */
    @Override
    public final JXButton getOkButton() {
        return okButton;
    }

    /**
     * Affiche ou masque la fenetre.
     * 
     * @param pVisible
     *            true pour afficher, false pour masquer
     */
    @Override
    public final void setVisible(final boolean pVisible) {
        aboutDialog.setVisible(pVisible);
    }

    /**
     * Retourne le composant.
     * 
     * @return le composant
     */
    @Override
    public final Component asComponent() {
        return aboutDialog;
    }

}
