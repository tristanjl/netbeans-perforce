/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeansplugins.perforce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;

/**
 *
 * @author tristanjl
 */
public class PerforceAction
{
    public FileObject getFileObject()
    {
        DataObject dataObject = TopComponent.getRegistry().getActivated().getLookup().lookup(DataObject.class);
        if (dataObject == null)
        {
            return null;
        }

        if (dataObject instanceof DataShadow)
        {
            dataObject = ((DataShadow) dataObject).getOriginal();
        }

        return dataObject.getPrimaryFile();
    }
    
    public String getPerforceSpec(FileObject currentFile)
    {
        String p4port = "";
        String user = "";
        String workspace = "";
        
        String host = null;
        
        Process proc = null;
        try
        {
            proc = Runtime.getRuntime().exec("p4 set");
        }
        catch (IOException ex)
        {
            return null;
        }
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        
        String s = null;
        try
        {
            Pattern p4PortPattern = Pattern.compile("P4PORT=([^\\s]+).*");
            Pattern p4UserPattern = Pattern.compile("P4USER=([^\\s]+).*");
            while ((s = stdInput.readLine()) != null)
            {
                Matcher mp = p4PortPattern.matcher(s);
                if (mp.find())
                {
                    p4port = "-p " + mp.group(1);
                }
                Matcher mu = p4UserPattern.matcher(s);
                if (mu.find())
                {
                    user = " -u " + mu.group(1);
                }
            }
        }
        catch (IOException ex)
        {
            return null;
        }
        
        try
        {
            proc = Runtime.getRuntime().exec("p4 info");
        }
        catch (IOException ex)
        {
            return null;
        }
        stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        
        s = null;
        try
        {
            Pattern p4PortPattern = Pattern.compile("Server address: (.+)");
            Pattern p4UserPattern = Pattern.compile("User name: (.+)");
            Pattern p4HostPattern = Pattern.compile("Client host: (.+)");
            while ((s = stdInput.readLine()) != null)
            {
                if (p4port.isEmpty())
                {
                    Matcher mp = p4PortPattern.matcher(s);
                    if (mp.find())
                    {
                        p4port = "-p " + mp.group(1);
                    }
                }
                if (user.isEmpty())
                {
                    Matcher mu = p4UserPattern.matcher(s);
                    if (mu.find())
                    {
                        user = " -u " + mu.group(1);
                    }
                }
                Matcher mh = p4HostPattern.matcher(s);
                if (mh.find())
                {
                    host = mh.group(1);
                }
            }
        }
        catch (IOException ex)
        {
            return null;
        }
        
        try
        {
            proc = Runtime.getRuntime().exec("p4 " + p4port + user + " workspaces" + user);
        }
        catch (IOException ex)
        {
            return null;
        }
        stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        try
        {
            int greatestLength = 0;
            Pattern workspacePattern = Pattern.compile("[^\\s]+ ([^\\s]+) [^\\s]+ root ([^']+)'");
            while ((s = stdInput.readLine()) != null)
            {
                Matcher clientMatcher = workspacePattern.matcher(s);
                if (clientMatcher.find())
                {
                    String currentClient = clientMatcher.group(1);
                    if (!currentClient.toLowerCase().contains(host.toLowerCase()))
                    {
                        continue;
                    }
                    String checkingDirectory = clientMatcher.group(2).trim();
                    int currentLength = checkingDirectory.length();
                    if (currentLength < greatestLength)
                    {
                        continue;
                    }
                    if (!currentFile.getPath().contains(checkingDirectory))
                    {
                        continue;
                    }
                    greatestLength = currentLength;
                    workspace = " -c " + currentClient;
                }
            }
        }
        catch (IOException ex)
        {
            return null;
        }
        return p4port + user + workspace;
    }
    
    public void runP4VCommand(String command, FileObject currentFile)
    {
        String perforceSpec = getPerforceSpec(currentFile);
        try
        {
            String processCmd = "/opt/p4/bin/p4v " + perforceSpec + " -cmd";
            List<String> processArgs = new ArrayList<>(Arrays.asList(processCmd.split(" ")));
            processArgs.add(command + " " + currentFile.getPath());
            ProcessBuilder builder = new ProcessBuilder(processArgs);
            Process process = builder.start();
        }
        catch (IOException ex)
        {
            final String msg = "Perforce " + command + " error";
            StatusDisplayer.getDefault().setStatusText(msg);
            Exceptions.printStackTrace(ex);
        }
    }
}
