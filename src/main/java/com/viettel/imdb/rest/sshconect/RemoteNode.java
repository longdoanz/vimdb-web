package com.viettel.imdb.rest.sshconect;

import com.viettel.imdb.rest.model.AddClusterNodeRequest;
import org.pmw.tinylog.Logger;

import java.util.List;

public class RemoteNode {
    private String host;
    private int port;
    private String binaryPath;
    private String configFile;
    private boolean overrideSetting;
    private boolean reloadOldData;
    private String preload;
    private String configContent;
    private AddClusterNodeRequest request;
    SSHManager sshManager;




    public RemoteNode(AddClusterNodeRequest request){
        host = request.getVimdbServerInfo().getHost();
        port = request.getVimdbServerInfo().getPort();
        binaryPath = request.getVimdbServerInfo().getBinaryPath();
        configFile = request.getVimdbServerInfo().getOverridedConfig().getConfigFile();
        overrideSetting = request.getVimdbServerInfo().isOverridedSetting();
        reloadOldData = request.getVimdbServerInfo().isOverridedSetting();
        preload = "LD_PRELOAD=\\\"bin/libyaml-cpp.so.0.5 bin/libprotobuf.so.8\\\" ";
        configContent = request.getVimdbServerInfo().getOverridedConfig().getNewConfigContent();
        this.request = request;
        System.out.println();
        sshManager = new SSHManager(request.getSshInfo().getUsername()
                ,request.getSshInfo().getIp());
    }


    public void startNode(){
        sshManager.connectUseSSHKey();
        //create file
        //StringBuilder cmd = new StringBuilder;
        sshManager.createRemoteFile(configFile, configContent);
        System.out.println("configpath: "+configFile+"configContent:"+configContent);
        StringBuilder cmd = new StringBuilder(this.binaryPath);
        cmd.append(" --config=").append(this.configFile);
        cmd.append(" --init-new-cluster=").append(reloadOldData);
        String result = sshManager.sendCommand(cmd.toString());
        System.out.println(cmd.toString());


    }
    public void startNodeDefaultConfig(){
        sshManager.connectUseSSHKey();
        //create file
        //StringBuilder cmd = new StringBuilder;
        //sshManager.createRemoteFile(configFile, configContent);
        System.out.println("configpath: "+configFile+"configContent:"+configContent);
        StringBuilder cmd = new StringBuilder(this.binaryPath);
        cmd.append(" --config=").append(this.configFile);
        cmd.append(" --init-new-cluster=").append(reloadOldData);
        String result = sshManager.sendCommand(cmd.toString());
        Logger.info(cmd.toString());


    }
    public StringBuilder genConfig(String cluster_name,
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
                                   int commit_level,
                                   String otherSetting, String logSetting) {

        StringBuilder res = new StringBuilder("cluster_name = \"" + cluster_name + "\"" +
                "\nhost = \"" + ip + "\"" +
                "\nport = " + port +
                "\nreplication_factor = " + replication +
                "\nmetric_port = " + metric_port + "\n");

        res.append("seeds = [\n");
        if((clusterSeeds == null || clusterSeeds.isEmpty()) && replication == 1){
            res.append("\"").append(ip).append(":").append(port).append("\"\n");
        }
        else{
            for (String host: clusterSeeds){
                res.append("\"").append(host).append("\"\n");
            }
        }
        res.append("]\n");


        if(secondaryClusters!=null && !secondaryClusters.isEmpty()){
            res.append("other_cluster_seeds = [\n");
            for (List<String> secondaryCluster: secondaryClusters){
                res.append("[\n");
                for(String secondaryHost: secondaryCluster)
                {
                    res.append("\"").append(secondaryHost).append("\"\n");
                }
                res.append("]\n");
            }
            res.append("]\n");
        }
        res.append(otherSetting);
        res.append("default_log_level = \"info\"\n")
//                .append("election_min = 9000\n")
//                .append("election_max = 15900\n")
//                .append("heartbeat_interval = 400\n")
//                .append("heartbeat_retry = 30\n")
                .append("console_log = true\n")
                .append("backup_flush_interval = 2000\n")
                .append("cluster_backup_log = \"").append(cluster_backup_log).append("\"\n")
                .append("backup_data_directory = \"").append(backup_data_directory).append("\"\n")
                .append("xdr_doc_transfer_quantity = ").append(xdr_doc_transfer_quantity).append("\n")
                .append("xdr_doc_transfer_interval = ").append(xdr_doc_transfer_interval).append("\n")
                .append("commit_level = ").append(commit_level).append("\n")
//                .append("synchronization_interval = 30\n")
                .append("[log_level]\n")
                .append("vimdb = \"error\"\n")
                .append("Storage_Test = \"error\"\n")
                .append("Backup_Test = \"error\"\n")
                .append("Cluster = \"debug\"\n");
        res.append(logSetting);

        return res;
    }

//    public void genConfigFile(String otherSetting, String logSetting) {
//        StringBuilder configvalue = CommonTmTools.genConfig(_cluster._name, _ip, _port,
//                _cluster._replication, _port * 2, _cluster.getSeedList(), _cluster.getRepList(),
//                CommonTmTools.BACKUP_DIR + "/cluster.log",
//                CommonTmTools.BACKUP_DIR,256, 1000, 1,
//                otherSetting, logSetting);
//
//        applyConfig(configvalue);
//
//        CmdHandler.createRemoteFile(_ip, CommonTmTools.CONF_FILE, configvalue.toString());
//    }
//    public void connect_ssh() {
//        run("ssh imdb@" + _ip);
//        run("cd " + CommonTmTools.WORKING_DIR);
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//        }
//        _connected = true;
//    }
}
