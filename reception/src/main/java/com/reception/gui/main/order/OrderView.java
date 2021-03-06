package com.reception.gui.main.order;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.StringValues;
import org.joda.time.DateTime;

import com.cegim.reception.commons.bean.ZSynCommande;
import com.cegim.reception.commons.entite.ZClient;
import com.cegim.reception.commons.entite.ZRepresentant;
import com.cegim.reception.commons.gui.filter.OrderFilter;
import com.cegim.reception.commons.gui.iconvalue.PrintStateIconValue;
import com.cegim.reception.commons.gui.image.CommonsImage;
import com.cegim.reception.commons.gui.stringvalue.ReadableInstantStringValue;
import com.cegim.reception.commons.gui.table.ColumnConfig;
import com.cegim.reception.commons.gui.table.ColumnModel;
import com.cegim.reception.commons.gui.table.FilterTable;
import com.google.inject.Inject;
import com.reception.gui.iconvalue.OrderStateIconValue;
import com.reception.gui.main.filter.FilterPresenter;
import com.reception.gui.main.order.filter.OrderFilterMatcherEditor;

/**
 * Vue des commandes synchronisees.
 * 
 * @author Laurent2403 
 */
public class OrderView implements OrderPresenter.View {

    /**
     * Le panneau conteneur.
     */
    private transient JXPanel panel;
    /**
     * Bouton Imprimer.
     */
    private transient JXButton printButton;
    /**
     * La vue du filtre.
     */
    private final transient FilterPresenter.View filterView;
    /**
     * Le matcher editor des commandes synchronisees.
     */
    private final transient OrderFilterMatcherEditor orderFilterMtchEdt;
    /**
     * La table des commandes synchronisees.
     */
    private transient FilterTable<ZSynCommande<?>> synCommandeTable;

    /**
     * Largeur de la colonne origine.
     */
    private static final int ORIGINE_COL_WIDTH = 50;
    /**
     * Largeur de la colonne numero de commande.
     */
    private static final int NUMBER_COL_WIDTH = 150;
    /**
     * Largeur des colonnes date.
     */
    private static final int DATE_COL_WIDTH = 150;
    /**
     * Largeur des colonnes etat.
     */
    private static final int ETAT_COL_WIDTH = 50;
    /**
     * Largeur de la colonne code client.
     */
    private static final int CODE_CLIENT_COL_WIDTH = 60;
    /**
     * Largeur de la colonne code repres.
     */
    private static final int CODE_REPRES_COL_WIDTH = 40;
    /**
     * Largeur de la colonne montant.
     */
    private static final int AMOUNT_COL_WIDTH = 110;
    /**
     * Hauteur de ligne.
     */
    private static final int ROW_HEIGHT = 24;

    /**
     * Cree la vue des commandes.
     * 
     * @param pFilterView
     *            la vue du filtre
     * @param pOrderFilterMtchEdt
     *            le matcher editor
     */
    @Inject
    public OrderView(@OrderFilter final FilterPresenter.View pFilterView,
            final OrderFilterMatcherEditor pOrderFilterMtchEdt) {

        filterView = pFilterView;
        orderFilterMtchEdt = pOrderFilterMtchEdt;

        initComponents();
    }

    /**
     * Initialise les composants.
     */
    private void initComponents() {

        // creation du bouton Imprimer
        printButton = new JXButton("Imprimer");
        printButton.setIcon(CommonsImage.PRINT_16);
        printButton.setToolTipText("Imprimer les commandes sélectionnées");

        // creation de la table des commandes synchronisees
        synCommandeTable = createSynCommandeTable();

        // scrollPane de la table des commandes
        final JScrollPane synCmdTableScrollPane = new JScrollPane(synCommandeTable.getTable());

        // creation du panneau contenant les composants
        panel = new JXPanel();
        final MigLayout mainPnlLayout = new MigLayout("", "[grow]", "[][grow]");
        panel.setLayout(mainPnlLayout);

        // ajout de la vue du filtre
        panel.add(filterView.asComponent(), "split 2");
        // ajout du bouton imprimer
        panel.add(printButton, "wrap");
        // ajout de la table au panneau
        panel.add(synCmdTableScrollPane, "grow");

    }

    /**
     * Cree la table des commandes synchronisees.
     * 
     * @return la table des commandes synchronisees
     */
    private FilterTable<ZSynCommande<?>> createSynCommandeTable() {

        final List<ColumnConfig<ZSynCommande<?>, ?>> listColConfig = createSynCommandeTblColConfig();
        final ColumnModel<ZSynCommande<?>> columnModel = new ColumnModel<ZSynCommande<?>>(listColConfig);
        synCommandeTable = new FilterTable<ZSynCommande<?>>(columnModel, null, orderFilterMtchEdt);
        // supprime les traits des lignes et des colonnes
        synCommandeTable.getTable().setShowHorizontalLines(false);
        synCommandeTable.getTable().setShowVerticalLines(false);
        // rend toutes les cellules de la table non editable
        synCommandeTable.getTable().setEditable(false);
        // autorise la selection de ligne
        synCommandeTable.getTable().setRowSelectionAllowed(true);
        // interdit la selection de colonne
        synCommandeTable.getTable().setColumnSelectionAllowed(false);
        // definition de la hauteur de ligne
        synCommandeTable.getTable().setRowHeight(ROW_HEIGHT);
        // ajout d'un surligneur lors du passage de la souris
        synCommandeTable.getTable()
                .addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.RED));
        // definition d'une selection unique
        synCommandeTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // definit un renderer particulier comme renderer de DateTime
        synCommandeTable.getTable().setDefaultRenderer(DateTime.class,
                new DefaultTableRenderer(new ReadableInstantStringValue("dd/MM/yyyy HH:mm:ss"), JLabel.CENTER));

        return synCommandeTable;

    }

    /**
     * Cree la liste de la configuration des colonnes de la table des commandes
     * synchronisees.
     * 
     * @return la liste de la configuration des colonnes de la table des
     *         commandes synchronisees
     */
    private List<ColumnConfig<ZSynCommande<?>, ?>> createSynCommandeTblColConfig() {

        // creation de la liste des configurations de colonne
        final List<ColumnConfig<ZSynCommande<?>, ?>> listColConfig = new ArrayList<ColumnConfig<ZSynCommande<?>, ?>>();

        // configuration de la colonne origine
        final ColumnConfig<ZSynCommande<?>, String> colOrigineConfig = new ColumnConfig<ZSynCommande<?>, String>() {
            @Override
            public String getValue(final ZSynCommande<?> pZSynCommande) {
                return pZSynCommande.getOrigine();
            }
        };
        colOrigineConfig.setColumnClass(String.class);
        colOrigineConfig.setHeader("Orig.");
        colOrigineConfig.setWidth(ORIGINE_COL_WIDTH);
        colOrigineConfig.setFixed(true);
        listColConfig.add(colOrigineConfig);

        // configuration de la colonne numero
        final ColumnConfig<ZSynCommande<?>, String> colNumberConfig = new ColumnConfig<ZSynCommande<?>, String>() {
            @Override
            public String getValue(final ZSynCommande<?> pZSynCommande) {
                return pZSynCommande.getNumero();
            }
        };
        colNumberConfig.setColumnClass(String.class);
        colNumberConfig.setHeader("Numéro");
        colNumberConfig.setWidth(NUMBER_COL_WIDTH);
        colNumberConfig.setFixed(true);
        listColConfig.add(colNumberConfig);

        // configuration de la colonne etat
        final ColumnConfig<ZSynCommande<?>, Integer> colStateConfig = new ColumnConfig<ZSynCommande<?>, Integer>() {
            @Override
            public Integer getValue(final ZSynCommande<?> pZSynCommande) {
                return pZSynCommande.getEtat();
            }
        };
        colStateConfig.setId(OrderPresenter.View.COL_ETAT_ID);
        colStateConfig.setColumnClass(Integer.class);
        colStateConfig.setHeader("Etat");
        colStateConfig.setWidth(ETAT_COL_WIDTH);
        colStateConfig.setFixed(true);
        colStateConfig.setCellRenderer(
                new DefaultTableRenderer(StringValues.EMPTY, new OrderStateIconValue(), JLabel.CENTER));
        listColConfig.add(colStateConfig);

        // configuration de la colonne code client
        final ColumnConfig<ZSynCommande<?>, String> colCodeClientConfig = new ColumnConfig<ZSynCommande<?>, String>() {
            @Override
            public String getValue(final ZSynCommande<?> pZSynCommande) {
                final ZClient client = pZSynCommande.getClient();
                if (client == null) {
                    return null;
                } else {
                    return client.getCode();
                }
            }
        };
        colCodeClientConfig.setColumnClass(String.class);
        colCodeClientConfig.setHeader("Client");
        colCodeClientConfig.setWidth(CODE_CLIENT_COL_WIDTH);
        colCodeClientConfig.setFixed(true);
        listColConfig.add(colCodeClientConfig);

        // configuration de la colonne nom client
        final ColumnConfig<ZSynCommande<?>, String> colNameConfig = new ColumnConfig<ZSynCommande<?>, String>() {
            @Override
            public String getValue(final ZSynCommande<?> pZSynCommande) {
                final ZClient client = pZSynCommande.getClient();
                if (client == null) {
                    return null;
                } else {
                    return client.getNom();
                }
            }
        };
        colNameConfig.setColumnClass(String.class);
        colNameConfig.setHeader("Nom client");
        listColConfig.add(colNameConfig);

        // configuration de la colonne code representant
        final ColumnConfig<ZSynCommande<?>, String> colCodeRepresConfig = new ColumnConfig<ZSynCommande<?>, String>() {
            @Override
            public String getValue(final ZSynCommande<?> pZSynCommande) {
                final ZRepresentant repres = pZSynCommande.getRepresentant();
                if (repres == null) {
                    return null;
                } else {
                    return repres.getCode();
                }
            }
        };
        colCodeRepresConfig.setColumnClass(String.class);
        colCodeRepresConfig.setHeader("Rep.");
        colCodeRepresConfig.setWidth(CODE_REPRES_COL_WIDTH);
        colCodeRepresConfig.setFixed(true);
        listColConfig.add(colCodeRepresConfig);

        // configuration de la colonne date de creation de la commande
        final ColumnConfig<ZSynCommande<?>, DateTime> colDateCreationConfig = new ColumnConfig<ZSynCommande<?>, DateTime>() {
            @Override
            public DateTime getValue(final ZSynCommande<?> pZSynCommande) {
                return pZSynCommande.getDateCreation();
            }
        };
        colDateCreationConfig.setColumnClass(DateTime.class);
        colDateCreationConfig.setHeader("Date création");
        colDateCreationConfig.setWidth(DATE_COL_WIDTH);
        colDateCreationConfig.setFixed(true);
        listColConfig.add(colDateCreationConfig);

        // configuration de la colonne date de synchronisation de la commande
        final ColumnConfig<ZSynCommande<?>, DateTime> colDateSynchroConfig = new ColumnConfig<ZSynCommande<?>, DateTime>() {
            @Override
            public DateTime getValue(final ZSynCommande<?> pZSynCommande) {
                return pZSynCommande.getDateSynchro();
            }
        };
        colDateSynchroConfig.setColumnClass(DateTime.class);
        colDateSynchroConfig.setHeader("Date synchro");
        colDateSynchroConfig.setWidth(DATE_COL_WIDTH);
        colDateSynchroConfig.setFixed(true);
        listColConfig.add(colDateSynchroConfig);

        // configuration de la colonne etat impression
        final ColumnConfig<ZSynCommande<?>, Integer> colStatePrintConfig = new ColumnConfig<ZSynCommande<?>, Integer>() {
            @Override
            public Integer getValue(final ZSynCommande<?> pZSynCommande) {
                return pZSynCommande.getEtatImpr();
            }
        };
        colStatePrintConfig.setId(OrderPresenter.View.COL_ETAT_IMPR_ID);
        colStatePrintConfig.setColumnClass(Integer.class);
        colStatePrintConfig.setHeader("Imp.");
        colStatePrintConfig.setWidth(ETAT_COL_WIDTH);
        colStatePrintConfig.setFixed(true);
        colStatePrintConfig.setCellRenderer(
                new DefaultTableRenderer(StringValues.EMPTY, new PrintStateIconValue(), JLabel.CENTER));
        listColConfig.add(colStatePrintConfig);

        // configuration de la colonne montant de la commande
        final ColumnConfig<ZSynCommande<?>, BigDecimal> colAmountConfig = new ColumnConfig<ZSynCommande<?>, BigDecimal>() {
            @Override
            public BigDecimal getValue(final ZSynCommande<?> pZSynCommande) {
                return pZSynCommande.getTotal();
            }
        };
        colAmountConfig.setColumnClass(BigDecimal.class);
        colAmountConfig.setHeader("Montant");
        colAmountConfig.setWidth(AMOUNT_COL_WIDTH);
        colAmountConfig.setFixed(true);
        listColConfig.add(colAmountConfig);

        return listColConfig;
    }

    /**
     * Retourne le bouton Imprimer.
     * 
     * @return le bouton Imprimer
     */
    @Override
    public final JXButton getPrintButton() {
        return printButton;
    }

    /**
     * Retourne la table.
     * 
     * @return la table
     */
    @Override
    public final FilterTable<ZSynCommande<?>> getFilterTable() {
        return synCommandeTable;
    }

    /**
     * Affecte la liste.
     * 
     * @param pList
     *            la liste
     */
    @Override
    public final void setList(final List<ZSynCommande<?>> pList) {
        synCommandeTable.setList(pList);
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
