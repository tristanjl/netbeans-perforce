/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.tristanjl.netbeansperforce;

import java.util.*;
import java.util.regex.*;
import org.openide.loaders.*;
import java.io.*;
import org.apache.commons.lang3.SystemUtils;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.openide.windows.TopComponent;

/**
 *
 * @author tristanjl
 */
public class PerforceAction
{
    public static String getDefaultP4Path()
    {
        if (SystemUtils.IS_OS_MAC)
        {
            return "/usr/local/bin/p4";
        }
        return "p4";
    }
    
    public static String getCurrentP4Path()
    {
        return NbPreferences.forModule(Perforce.class).get("p4Path", getDefaultP4Path());
    }
    
    public static String getDefaultP4VCPath()
    {
        if (!SystemUtils.IS_OS_LINUX)
        {
            if (!SystemUtils.IS_OS_MAC)
            {
                return "p4vc";
            }
            else
            {
                return "/Applications/p4vc";
            }
        }
        return "/opt/p4/bin/p4vc";
    }
    
    public static String getCurrentP4VCPath()
    {
        return NbPreferences.forModule(Perforce.class).get("p4vcPath", getDefaultP4VCPath());
    }
    
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
            proc = Runtime.getRuntime().exec(getCurrentP4Path() + " set");
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
            proc = Runtime.getRuntime().exec(getCurrentP4Path() + " info");
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
            proc = Runtime.getRuntime().exec(getCurrentP4Path() + " " + p4port + user + " workspaces" + user);
        }
        catch (IOException ex)
        {
            return null;
        }
        stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        try
        {
            boolean foundHostMatch = false;
            int greatestLength = 0;
            Pattern workspacePattern = Pattern.compile("[^\\s]+ ([^\\s]+) [^\\s]+ root ([^']+)'");
            while ((s = stdInput.readLine()) != null)
            {
                boolean isHostMatch = false;
                Matcher clientMatcher = workspacePattern.matcher(s);
                if (clientMatcher.find())
                {
                    String currentClient = clientMatcher.group(1);
                    if (!currentClient.toLowerCase().contains(host.toLowerCase()))
                    {
                        if (foundHostMatch)
                            continue;
                    }
                    else
                    {
                        isHostMatch = true;
                    }
                    String checkingDirectory = clientMatcher.group(2).trim();
                    int currentLength = checkingDirectory.length();
                    if (currentLength < greatestLength && (!isHostMatch || foundHostMatch))
                    {
                        continue;
                    }
                    if (!currentFile.getPath().contains(checkingDirectory))
                    {
                        continue;
                    }
                    if (isHostMatch)
                    {
                        foundHostMatch = true;
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
        if (perforceSpec == null)
        {
            StatusDisplayer.getDefault().setStatusText("Unable to determine p4 workspace. Check p4 can be run from the command line.");
            perforceSpec = "";
        }
        try
        {
            String processCmd = getCurrentP4VCPath() + " " + perforceSpec + " ";
            List<String> processArgs = new ArrayList<>(Arrays.asList(processCmd.split(" ")));
            processArgs.add(command);
            processArgs.add(currentFile.getPath());
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
