/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeansplugins.perforce;

import java.awt.event.*;
import org.openide.awt.*;
import java.io.IOException;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.filesystems.FileObject;

@ActionID(
        category = "Menu/Team/Perforce",
        id = "org.netbeansplugins.perforce.PerforceCheckOut"
)
@ActionRegistration(
        displayName = "#CTL_PerforceCheckOut"
)
@ActionReference(path = "Menu/Team/Perforce", position = 0)
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
            Runtime.getRuntime().exec("p4 " + perforceSpec + " edit " + fileobject.getPath());
        }
        catch (IOException ex)
        {
            try
            {
                Runtime.getRuntime().exec("/usr/local/bin/p4 " + perforceSpec + " edit " + fileobject.getPath());
            }
            catch (IOException innerEx)
            {
                final String msg = "Perforce checkout error";
                StatusDisplayer.getDefault().setStatusText(msg);
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
