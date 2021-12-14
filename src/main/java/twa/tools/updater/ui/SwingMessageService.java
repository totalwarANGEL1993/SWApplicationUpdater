package twa.tools.updater.ui;

import org.springframework.stereotype.Service;
import twa.tools.updater.ui.components.frame.MainWindow;

import javax.swing.*;

/**
 * Dialog message implementation for Swing.
 */
@Service
public class SwingMessageService {
    /**
     * Zeigt eine Fehlermeldung an.
     *
     * @param title Title of dialog
     * @param text Text of dialog
     * @param parent Parent
     */
    public void displayErrorMessage(String title, String text, MainWindow parent) {
        JOptionPane.showMessageDialog(parent, text, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Zeigt eine Informationsmeldung an.
     *
     * @param title Title of dialog
     * @param text Text of dialog
     * @param parent Parent
     */
    public void displayInfoMessage(String title, String text, MainWindow parent) {
        JOptionPane.showMessageDialog(parent, text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Zeigt eine Warnungsmeldung an.
     *
     * @param title Title of dialog
     * @param text Text of dialog
     * @param parent Parent
     */
    public void displayWarningMessage(String title, String text, MainWindow parent) {
        JOptionPane.showMessageDialog(parent, text, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Zeigt eine Fehlermeldung an ohne eine Parent Component.
     *
     * @param title Title of dialog
     * @param text Text of dialog
     */
    public void displayErrorMessage(String title, String text) {
        JOptionPane.showMessageDialog(null, text, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Zeigt eine Informationsmeldung an ohne eine Parent Component.
     * @param title Title of dialog
     * @param text Text of dialog
     */
    public void displayInfoMessage(String title, String text) {
        JOptionPane.showMessageDialog(null, text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Zeigt eine Warnungsmeldung an ohne eine Parent Component.
     * @param title Title of dialog
     * @param text Text of dialog
     */
    public void displayWarningMessage(String title, String text) {
        JOptionPane.showMessageDialog(null, text, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Zeigt eine Meldung an, die bestätigt werden muss.
     *
     * @param title Title of dialog
     * @param text Text of dialog
     * @param parent Parent
     * @return Result
     */
    public int displayConfirmDialog(String title, String text, MainWindow parent) {
        return JOptionPane.showConfirmDialog(parent, text, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Zeigt eine Meldung an, die bestätigt werden muss. Die Meldung hat keinen Parent.
     *
     * @param title Title of dialog
     * @param text Text of dialog
     * @return Result
     */
    public int displayConfirmDialog(String title, String text) {
        return JOptionPane.showConfirmDialog(null, text, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    }
}
