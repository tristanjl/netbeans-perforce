/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeansplugins.perforce;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Menu/Team/Perforce",
        id = "org.netbeansplugins.perforce.PerforceHistory"
)
@ActionRegistration(
        displayName = "#CTL_PerforceHistory"
)
@ActionReference(path = "Menu/Team/Perforce", position = 2)
@Messages("CTL_PerforceHistory=History")
public final class PerforceHistory extends PerforceAction implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        FileObject fileobject = getFileObject();
        
        if (fileobject == null)
        {
            return;
        }
        runP4VCommand("history", fileobject);
    }
}
