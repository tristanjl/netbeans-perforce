/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.tristanjl.netbeansperforce;

import java.awt.event.*;
import org.openide.awt.*;
import org.openide.util.NbBundle.Messages;
import org.openide.filesystems.FileObject;

@ActionID(
        category = "Menu/Versioning/Perforce",
        id = "io.github.tristanjl.netbeansperforce.PerforceTimelapse"
)
@ActionRegistration(
        displayName = "#CTL_PerforceTimelapse"
)
@ActionReference(path = "Menu/Versioning/Perforce", position = 3)
@Messages("CTL_PerforceTimelapse=Timelapse")
public final class PerforceTimelapse extends PerforceAction implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        FileObject fileobject = getFileObject();
        
        if (fileobject == null)
        {
            return;
        }
        runP4VCommand("annotate", fileobject);
    }
}
