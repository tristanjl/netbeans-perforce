/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeansplugins.perforce;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

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
            final String msg = "Perforce checkout error";
            StatusDisplayer.getDefault().setStatusText(msg);
            Exceptions.printStackTrace(ex);
        }
    }
}
