/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeansplugins.perforce;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.*;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(
        category = "Versioning",
        id = "org.netbeansplugins.perforce.Perforce"
)
@ActionRegistration(
        displayName = "#CTL_Perforce",  lazy = false
)
@ActionReference(path = "Menu/Team", position = 51)
@Messages("CTL_Perforce=Perforce")
public final class Perforce extends AbstractAction implements ActionListener, Presenter.Menu {

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public JMenuItem getMenuPresenter() {
        JMenu main = new JMenu(Bundle.CTL_Perforce());
        List<? extends Action> actionsForPath = Utilities.actionsForPath("Menu/Team/Perforce");
        for (Action action : actionsForPath) {
            main.add(action);
        }
        return main;
    }
}
