package twa.tools.updater.controller.app;

import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Component
public abstract class AbstractInterfaceController implements ActionListener, ListSelectionListener, MouseListener {
    /**
     * Ein Wert (z.B. Textfeld) wurde geändert.
     *
     * @param event  Event
     * @param source Quelle
     */
    protected abstract void onActionPerformed(ActionEvent event, JComponent source);

    /**
     * Eine Aktion (z.B. Button Click) wurde ausgelöst.
     *
     * @param event  Event
     * @param source Quelle
     */
    protected abstract void onValueChanged(ListSelectionEvent event, JComponent source);

    @Override
    public void actionPerformed(ActionEvent e) {
        onActionPerformed(e, (JComponent) e.getSource());
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void valueChanged(ListSelectionEvent e) {
        onValueChanged(e, (JComponent) e.getSource());
    }
}

