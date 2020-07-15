/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.tristanjl.netbeansperforce;

import java.awt.event.*;
import javax.swing.*;
import org.openide.awt.*;
import org.openide.util.*;
import java.util.List;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(
        category = "Menu/Versioning",
        id = "io.github.tristanjl.netbeansperforce.Perforce"
)
@ActionRegistration(
        displayName = "#CTL_Perforce",  lazy = false
)

@Messages("CTL_Perforce=Perforce")
public final class Perforce extends AbstractAction implements ActionListener, Presenter.Menu {

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public JMenuItem getMenuPresenter() {
        JMenu main = new JMenu(Bundle.CTL_Perforce());
        List<? extends Action> actionsForPath = Utilities.actionsForPath("Menu/Versioning/Perforce");
        for (Action action : actionsForPath) {
            main.add(action);
        }
        return main;
    }
}
