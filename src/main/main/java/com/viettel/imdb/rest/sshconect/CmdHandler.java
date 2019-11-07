package com.viettel.imdb.rest.sshconect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CmdHandler {
    public static final String SCRIPT_DIR = System.getProperty("user.dir") + "/src/test/java/com/viettel/imdb/auto/tmtools/";

    public static int run_raw(String command, String args) {
        ProcessBuilder builder = new ProcessBuilder();

        builder.command(command, args);

        builder.directory(new File(System.getProperty("user.home")));
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exitCode;
    }

    public static int run(String command, String args) {
        ProcessBuilder builder = new ProcessBuilder();

        builder.command(SCRIPT_DIR + command, args);

        //builder.directory(new File(System.getProperty("user.home")));
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exitCode;
    }

    public static int exec(String command, String args) {
        return run("runner", command + " " + args);
    }


    public static int run(String command, List<String> args) {
        ProcessBuilder builder = new ProcessBuilder();

        LinkedList<String> cmds = new LinkedList<>(args);
        cmds.addFirst(command);
        builder.command(cmds);

        //builder.directory(new File(System.getProperty("user.home")));
        Process process = null;
        int exitCode = 0;
        try {
            process = builder.start();
            exitCode = process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exitCode;
    }

    public static int iptb(String... argv) {
        ArrayList<String> args = new ArrayList<>();
        for (String i : argv) {
            args.add(i);
        }
        return run(SCRIPT_DIR + "iptb", args);
    }

    public static String getRemoteFileContent(String ip, String filename) {

        File temp = null;
        String res = null;
        try {
            try {
                Files.delete(Paths.get("/tmp/a"));
            } catch (Exception e) {
            }
            String arg = "scp imdb@"+ip+":"+filename + " " + "/tmp/a";
            int ecode = run("runner", arg);
            if (ecode != 0) {
                System.out.println("Cannot get file: " + ip + ":" + filename);
                return "";
            }

            System.out.println(arg);
//            System.out.println(ecode);

            res = new String(Files.readAllBytes(Paths.get("/tmp/a")));
        } catch (IOException e1) {
            System.out.println("Something went wrong in getRemoteFileContent");
            e1.printStackTrace();
            return "";
        }

        return res;
    }
    /*
    public static int createConfigureFile(String cluster_name,
                                          String ip,
                                          int port,
                                          int replication,
                                          int metric_port,
                                          List<String> clusterSeeds,
                                          List<List<String>>secondaryClusters,
                                          String cluster_backup_log,
                                          String backup_data_directory,
                                          int xdr_doc_transfer_quantity,
                                          int xdr_doc_transfer_interval,
                                          int commit_level
    )
    {
        try {

            //create a temp file
            File file = new File(CommonTmTools.CONF_DIR+"/" + ip+"_"+port+".toml");
            String seedConfig = "cluster_name = \""+cluster_name+"\"\n"+
                    "host = \""+ip+"\"\n" +
                    "port = "+port+"\n"+
                    "replication_factor = "+ replication+"\n"+
                    "metric_port = "+metric_port+"\n";
            //write it
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(seedConfig);
            bw.write("seeds = [\n");
            if((clusterSeeds.isEmpty()||clusterSeeds ==null)&& replication == 1){
                bw.write("\""+ip+":"+port+"\"\n");
            }
            else{
                for (String host: clusterSeeds){
                    bw.write("\""+ host+"\"\n");
                }
            }
            bw.write("]\n");
            if(secondaryClusters!=null && !secondaryClusters.isEmpty()){
                bw.write("other_cluster_seeds = [\n");
                for (List<String> secondaryCluster: secondaryClusters){
                    bw.write("[\n");
                    for(String secondaryHost: secondaryCluster)
                    {
                        bw.write("\""+secondaryHost+"\"\n");
                    }
                    bw.write("]\n");
                }
                bw.write("]\n");
            }
            bw.write("default_log_level = \"info\"\n"+
                    "cluster_backup_log = \""+CommonTmTools.BACKUP_DIR +"/"+ip+"_"+port+"_"+cluster_backup_log+"\"\n"+
                    "backup_data_directory = \""+CommonTmTools.BACKUP_DIR +"/"+ip+"_"+port+"_"+backup_data_directory+"\"\n"+
                    "xdr_doc_transfer_quantity = "+ xdr_doc_transfer_quantity+ "\n"+
                    "xdr_doc_transfer_interval = "+ xdr_doc_transfer_interval + "\n"+
                    "commit_level = "+commit_level+"\n"+
                    "[log_level]\n");
            bw.close();
            return 0;
        }catch(IOException e){

            System.out.println("Cannot create file");
            e.printStackTrace();

        }
        return -1;
    }
    */
    public static int createRemoteFile(String ip, String filename, String content) {
        try{

            //create a temp file
            File temp = File.createTempFile("tempfile", ".tmp");

            //write it
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            bw.write(content);
            bw.close();
            String arg = "scp " + temp.getAbsolutePath() + " imdb@"+ip+":"+filename;
            int ecode = run("runner", arg);
            if (ecode != 0) {
                System.out.println("Cannot create remote file");
            }
            return ecode;
        }catch(IOException e){

            System.out.println("Cannot create file");
            e.printStackTrace();

        }
        return -1;
    }


    public static int removeRemoteFile(String ip, String filename) {
        String arg = "ssh " + " imdb@" + ip + " rm " + filename;
        int ecode = run("runner", arg);
        if (ecode != 0) {
            System.out.println("Cannot remove file");
        }
        return ecode;
    }

    public static int renameRemoteFile(String ip, String filename, String newFilename) {

        String arg = "ssh " + " imdb@" + ip + " mv " + filename + " " + newFilename;
        int ecode = run("runner", arg);
        if (ecode != 0) {
            System.out.println("Cannot rename file");
        }
        return ecode;
    }

    public static boolean isRemFileExist(String ip, String filename) {
        String arg = "ssh imdb@"+ip+" test -f " + filename;
        return run("runner", arg) == 0;
    }

    public static void cleanIpTable(String ip) {
        CmdHandler.iptb("imdb", ip, "iptables -F");
        CmdHandler.iptb("imdb", ip, "iptables -X");
    }

//    public static void main(String argv[]) {
//        createRemoteFile("172.16.28.120", "~/fortest.txt", "{$.a:1}");
//        String res = getRemoteFileContent("172.16.28.120", "~/fortest.txt");
//        System.out.println("res=|" + res + "|");
//    }

}
