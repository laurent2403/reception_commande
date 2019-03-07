package com.reception.gui.main.filter;

import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.joda.time.DateTime;

import com.cegim.reception.commons.entite.ZRepresentant;
import com.cegim.reception.commons.gui.image.CommonsImage;
import com.cegim.reception.commons.gui.renderer.ZRepresentantRenderer;

/**
 * Vue du filtre.
 * 
 * @author Laurent2403 
 *  
 */
public class FilterView implements FilterPresenter.View {

    /**
     * Le panneau conteneur.
     */
    private transient JXPanel panel;
    /**
     * Contrôle choix de date de debut.
     */
    private transient JXDatePicker startDatePicker;
    /**
     * Contrôle choix de date de fin.
     */
    private transient JXDatePicker endDatePicker;
    /**
     * La combo box de choix d'un representant.
     */
    private transient JComboBox<ZRepresentant> represComboBox;
    /**
     * Bouton filtrer.
     */
    private transient JXButton filterButton;

    /**
     * Cree une nouvelle vue du filtre.
     * 
     */
    public FilterView() {

        super();
        initComponents();
    }

    /**
     * Cette methode est apelle depuis le constructeur et sert a intialiser les
     * composants du panneau.
     * 
     */
    private void initComponents() {

        // creation du libelle de la date de debut
        final JXLabel startDateLabel = new JXLabel("Du ", SwingConstants.LEFT);

        // creation du calendrier date debut
        startDatePicker = new JXDatePicker();
        startDatePicker.setToolTipText("Date de début");

        // creation du libelle de la date de fin
        final JXLabel endDateLabel = new JXLabel("au ", SwingConstants.LEFT);

        // creation du calendrier date fin
        endDatePicker = new JXDatePicker();
        endDatePicker.setToolTipText("Date de fin");

        // creation du libelle representant
        final JXLabel represLabel = new JXLabel("Représ. ", SwingConstants.LEFT);
        represLabel.setIcon(CommonsImage.DB_USER_16);

        // creation de la combobox des representants
        represComboBox = new JComboBox<ZRepresentant>();
        represComboBox.setToolTipText("Choix du représentant");
        // creation d'un representant servant de prototype a la representation
        final ZRepresentant represProto = new ZRepresentant();
        represProto.setCode("CC");
        represProto.setNom("NNNNNNNNNNNNNNNNNNNNNNNNN");
        represComboBox.setPrototypeDisplayValue(represProto);

        final ZRepresentantRenderer renderer = new ZRepresentantRenderer();
        represComboBox.setRenderer(renderer);

        final Object compPopupMenu = represComboBox.getUI().getAccessibleChild(represComboBox, 0);
        if (compPopupMenu instanceof JPopupMenu) {
            final JPopupMenu popup = (JPopupMenu) compPopupMenu;
            final Component compScrollPane = popup.getComponent(0);
            if (compScrollPane instanceof JScrollPane) {
                final JScrollPane scrollPane = (JScrollPane) compScrollPane;
                scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            }
        }

        // association du libelle au combobox
        represLabel.setLabelFor(represComboBox);

        // creation du bouton filtrer
        filterButton = new JXButton("Filtrer");
        filterButton.setIcon(CommonsImage.FUNNEL_16);
        filterButton.setToolTipText("Filtrer l'affichage");

        // panneau principal
        panel = new JXPanel();
        // definition de la mise en page
        final MigLayout filterPnlLayout = new MigLayout("", "[][][][][][grow]", "[]");
        panel.setLayout(filterPnlLayout);

        // ajout des composants au panneau
        panel.add(startDateLabel);
        panel.add(startDatePicker);
        panel.add(endDateLabel);
        panel.add(endDatePicker);
        panel.add(represLabel);
        panel.add(represComboBox, "grow");
        panel.add(filterButton);

    }

    /**
     * Retourne la date de debut saisie dans le filtre.
     * 
     * @return la date de debut saisie dans le filtre, peut etre nulle
     */
    @Override
    public final DateTime getStartDate() {

        DateTime startDate = null;
        final java.util.Date utilDate = startDatePicker.getDate();
        if (utilDate != null) {
            startDate = new DateTime(utilDate.getTime()).withTimeAtStartOfDay();
        }
        return startDate;
    }

    /**
     * Retourne la date de fin saisie dans le filtre.
     * 
     * @return la date de fin saisie dans le filtre, peut etre nulle
     */
    @Override
    public final DateTime getEndDate() {

        DateTime endDate = null;
        final java.util.Date utilDate = endDatePicker.getDate();
        if (utilDate != null) {
            int plusHour = 23;
            int plusMinute = 59;
            int plusSecond = 59;
            endDate = new DateTime(utilDate.getTime()).withTimeAtStartOfDay().plusHours(plusHour)
                    .plusMinutes(plusMinute).plusSeconds(plusSecond);
        }
        return endDate;
    }

    /**
     * Retourne le representant selectionne.
     * 
     * @return le representant selectionne
     */
    @Override
    public final ZRepresentant getRepresentant() {
        return (ZRepresentant) represComboBox.getSelectedItem();
    }

    /**
     * Affecte la liste de representant.
     * 
     * @param pListRepres
     *            la nouvelle liste de representant
     */
    @Override
    public final void setListRepresentant(final List<ZRepresentant> pListRepres) {
        represComboBox.removeAllItems();
        for (ZRepresentant repres : pListRepres) {
            represComboBox.addItem(repres);
        }
    }

    /**
     * Retourne le bouton de filtre.
     * 
     * @return le bouton de filtre
     */
    @Override
    public final JXButton getFilterButton() {
        return filterButton;
    }

    /**
     * Retourne le composant.
     * 
     * @return le composant
     */
    @Override
    public final Component asComponent() {
        return panel;
    }
}
