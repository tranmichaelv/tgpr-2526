package tgpr.restaurant.model;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.ObjectTable;
import com.googlecode.lanterna.gui2.Panel;

public abstract class PanelProductAndMenu<E> extends Panel {
    public ObjectTable<E> tableProduct;

    public PanelProductAndMenu(){
        this.asVerticalPanel(1);
    }

    // va charger les donnes en fonction de sa classe et des besoins de la classe
    public abstract void loadData();

    // cree une table en fonction de sa classe et des besoins de la classe
    public abstract ObjectTable<E> createTable();

    // action à faire apres avoir selectionne un product
    public void onProductSelected(){

    }

    // envoie les donnes des produits
    // dans une table et les refreshOrders à chaque appel de cette classe
    public void reloadData(){
        // vide le tableau
        tableProduct.clear();
        loadData();
        // requis à cause d'un bug quand on ferme la fenêtre popup avec la touche Escape
        tableProduct.invalidate();
    }

    // initialise la table
    public void initTable(int rows){
        tableProduct = createTable();
        tableProduct.sizeTo(100, rows);
        tableProduct.setSelectAction(this::onProductSelected);
        tableProduct.addTo(this);
        reloadData();
    }

    // cree un nouveau button
    public void createButtonAddPanel(String label, Runnable action) {
        Panel panel = new Panel();
        panel.addComponent(new Button(label, action));
        panel.addTo(this); // ajoute le bouton a this qui est un panel
    }
}
