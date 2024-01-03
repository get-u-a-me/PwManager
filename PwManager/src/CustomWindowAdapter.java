import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CustomWindowAdapter extends WindowAdapter {

    private Manager manager;

    public CustomWindowAdapter(Manager manager){
        this.manager = manager;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        manager.save();
    }
}
