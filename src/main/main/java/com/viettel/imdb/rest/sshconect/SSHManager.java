package com.viettel.imdb.rest.sshconect;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSHManager {
    private static final Logger LOGGER =
            Logger.getLogger(SSHManager.class.getName());
    private JSch jschSSHChannel;
    private String strUserName;
    private String strConnectionIP;
    private int intConnectionPort;
    private String strPassword;
    private String privateKey;
    private String know_hosts;
    private Session sesConnection;
    private int intTimeOut;
    private void doCommonConstructorActions(String userName,
                                            String password, String connectionIP)
    {
        jschSSHChannel = new JSch();

        try
        {
            know_hosts = System.getProperty("user.home")+"/.ssh/known_hosts";
            jschSSHChannel.setKnownHosts(know_hosts);
        }
        catch(JSchException jschX)
        {
            logError(jschX.getMessage());
        }

        strUserName = userName;
        strPassword = password;

        strConnectionIP = connectionIP;
    }
    private void doCommonConstructorActions(String userName,
                                             String connectionIP)
    {
        jschSSHChannel = new JSch();

        try
        {
            know_hosts = System.getProperty("user.home")+"/.ssh/known_hosts";
            jschSSHChannel.setKnownHosts(know_hosts);
            System.out.println("known host");
        }
        catch(JSchException jschX)
        {
            logError(jschX.getMessage());
        }

        strUserName = userName;
        privateKey = System.getProperty("user.home")+"/.ssh/id_rsa";
        System.out.println(privateKey);

        try {
            jschSSHChannel.addIdentity(privateKey);
            System.out.println("identity added ");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        strConnectionIP = connectionIP;
    }
    public SSHManager(String userName,
                      String connectionIP)
    {
        doCommonConstructorActions(userName,
                connectionIP);
        intConnectionPort = 22;
        intTimeOut = 60000;
    }
    public SSHManager(String userName, String password,
                      String connectionIP, String knownHostsFileName)
    {
        doCommonConstructorActions(userName, password,
                connectionIP);
        intConnectionPort = 22;
        intTimeOut = 60000;
    }

    public SSHManager(String userName, String password, String connectionIP,
                      String knownHostsFileName, int connectionPort)
    {
        doCommonConstructorActions(userName, password, connectionIP);
        intConnectionPort = connectionPort;
        intTimeOut = 60000;
    }

    public SSHManager(String userName, String password, String connectionIP,
                      String knownHostsFileName, int connectionPort, int timeOutMilliseconds)
    {
        doCommonConstructorActions(userName, password, connectionIP);
        intConnectionPort = connectionPort;
        intTimeOut = timeOutMilliseconds;
    }

    public String connectUsePassword()
    {
        String errorMessage = null;

        try
        {
            sesConnection = jschSSHChannel.getSession(strUserName,
                    strConnectionIP, intConnectionPort);
            sesConnection.setPassword(strPassword);
            // UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
            sesConnection.setConfig("StrictHostKeyChecking", "no");
            sesConnection.connect(intTimeOut);
        }
        catch(JSchException jschX)
        {
            errorMessage = jschX.getMessage();
        }

        return errorMessage;
    }
    public String connectUseSSHKey()
    {
        String errorMessage = null;

        try
        {
            sesConnection = jschSSHChannel.getSession(strUserName,
                    strConnectionIP, intConnectionPort);
            System.out.println("session created.");
            // UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
            //sesConnection.setConfig("StrictHostKeyChecking", "no");
            sesConnection.setConfig("server_host_key","ecdsa-sha2-nistp256");


            sesConnection.connect(intTimeOut);
            System.out.println("session connected.....");
        }
        catch(JSchException jschX)
        {
            errorMessage = jschX.getMessage();
        }

        return errorMessage;
    }

    private String logError(String errorMessage)
    {
        if(errorMessage != null)
        {
            LOGGER.log(Level.SEVERE, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, errorMessage});
        }

        return errorMessage;
    }

    private String logWarning(String warnMessage)
    {
        if(warnMessage != null)
        {
            LOGGER.log(Level.WARNING, "{0}:{1} - {2}",
                    new Object[]{strConnectionIP, intConnectionPort, warnMessage});
        }

        return warnMessage;
    }

    public String sendCommand(String command)
    {
        StringBuilder outputBuffer = new StringBuilder();

        try
        {
            Channel channel = sesConnection.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while(readByte != 0xffffffff)
            {
                outputBuffer.append((char)readByte);
                readByte = commandOutput.read();
            }

            channel.disconnect();
        }
        catch(IOException ioX)
        {
            logWarning(ioX.getMessage());
            return null;
        }
        catch(JSchException jschX)
        {
            logWarning(jschX.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }
    public int createRemoteFile(String filename, String content){
        try{

            //create a temp file
            File temp = File.createTempFile("tempfile", ".tmp");

            //write it
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            bw.write(content);
            bw.close();

            //send
            ChannelSftp sftpChannel = (ChannelSftp) sesConnection.openChannel("sftp");
            sftpChannel.connect();
            sftpChannel.put(temp.getAbsolutePath(), filename);

            sftpChannel.disconnect();
            temp.deleteOnExit();
//            String arg = "scp " + temp.getAbsolutePath() + " imdb@"+ip+":"+filename;
//            //int ecode = run("runner", arg);
//            if (ecode != 0) {
//                System.out.println("Cannot create remote file");
//            }
//            return ecode;
        }catch(IOException | JSchException | SftpException e){

            System.out.println("Cannot create file");
            e.printStackTrace();

        }
        return -1;
    }

    public void close()
    {
        sesConnection.disconnect();
    }
}
