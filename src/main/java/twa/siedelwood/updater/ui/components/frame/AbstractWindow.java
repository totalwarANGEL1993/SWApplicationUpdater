package twa.siedelwood.updater.ui.components.frame;

import lombok.Getter;
import lombok.Setter;
import twa.siedelwood.updater.controller.app.ApplicationFeatureController;
import twa.siedelwood.updater.controller.app.ApplicationInterfaceController;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Abstraktes Basisfenster, damit die implementierungen der Interfaces nicht das eigentliche Main Window aufblähen.
 */
public abstract class AbstractWindow extends JFrame implements WindowListener {
    @Getter
    @Setter
    protected ApplicationInterfaceController applicationInterfaceController;

    @Getter
    @Setter
    protected ApplicationFeatureController applicationFeatureController;

    /**
     * Rendert die Komponenten des Fensters.
     */
    public abstract void initComponents();

    //// Window listener stuff ////

    @Override
    public void windowOpened(WindowEvent windowEvent) {}

    @Override
    public void windowClosing(WindowEvent windowEvent) {}

    @Override
    public void windowClosed(WindowEvent windowEvent) {}

    @Override
    public void windowIconified(WindowEvent windowEvent) {}

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {}

    @Override
    public void windowActivated(WindowEvent windowEvent) {}

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {}
}
