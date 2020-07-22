/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.tristanjl.netbeansperforce;

import java.awt.event.*;
import org.openide.awt.*;
import java.io.IOException;
import org.openide.util.Exceptions;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Menu/Versioning/Perforce",
        id = "io.github.tristanjl.netbeansperforce.PerforceCheckOut"
)
@ActionRegistration(
        displayName = "#CTL_PerforceCheckOut"
)
@ActionReference(path = "Menu/Versioning/Perforce", position = 0)
@Messages("CTL_PerforceCheckOut=Check Out")
public final class PerforceCheckOut extends PerforceAction implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        FileObject fileobject = getFileObject();
        
        if (fileobject == null)
        {
            return;
        }
        
        String perforceSpec = getPerforceSpec(fileobject);
        try
        {
            Runtime.getRuntime().exec(getCurrentP4Path() + " " + perforceSpec + " edit " + fileobject.getPath());
        }
        catch (IOException ex)
        {
            final String msg = "Perforce checkout error";
            StatusDisplayer.getDefault().setStatusText(msg);
            Exceptions.printStackTrace(ex);
        }
    }
}
