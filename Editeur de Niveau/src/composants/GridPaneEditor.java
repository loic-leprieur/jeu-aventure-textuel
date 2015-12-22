package composants;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import util.UtilEditor;

/**
 * Created by louzw on 22/12/2015.
 */
public abstract class GridPaneEditor extends GridPane {

    public GridPaneEditor(){
        this.setHgap(6);
        this.setVgap(6);
        this.setPadding(new Insets(0, 6, 0, 0));
    }

    protected void createComponent(ListView<String> lv, ParentFrame pf){
        this.add(UtilEditor.createButton(pf.getStage(),"Ajouter",pf),1,0);
        this.add(UtilEditor.createButton(pf.getStage(),"Modifier",null),2,0);
        this.add(UtilEditor.createButton(pf.getStage(),"Supprimer",null),3,0);

        this.add(lv ,1,1,3,1);
    }

}
