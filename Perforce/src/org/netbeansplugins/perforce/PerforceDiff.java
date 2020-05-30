/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeansplugins.perforce;

import java.awt.event.*;
import org.openide.awt.*;
import org.openide.util.NbBundle.Messages;
import org.openide.filesystems.FileObject;

@ActionID(
        category = "Menu/Team/Perforce",
        id = "org.netbeansplugins.perforce.PerforceDiff"
)
@ActionRegistration(
        displayName = "#CTL_PerforceDiff"
)
@ActionReference(path = "Menu/Team/Perforce", position = 1)
@Messages("CTL_PerforceDiff=Diff")
public final class PerforceDiff extends PerforceAction implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        FileObject fileobject = getFileObject();
        
        if (fileobject == null)
        {
            return;
        }
        runP4VCommand("prevdiff", fileobject);
    }
}
