package composants;

import javafx.stage.Stage;

/**
 * Created by louzw on 22/12/2015.
 */
public abstract class ParentFrame {

    protected Stage stage;

    public void show(){
        stage.show();
    }

    public Stage getStage(){
        return stage;
    }
}
