package ssh;
import com.viettel.imdb.rest.sshconect.SSHManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;

public class sshTest {
    @BeforeMethod
    public void setUp() throws Exception {

    }

    @Test
    public void testSendCommand()
    {
        System.out.println("sendCommand");

        /**
         * YOU MUST CHANGE THE FOLLOWING
         * FILE_NAME: A FILE IN THE DIRECTORY
         * USER: LOGIN USER NAME
         * PASSWORD: PASSWORD FOR THAT USER
         * HOST: IP ADDRESS OF THE SSH SERVER
         **/
        String command = "ls ";
        String userName = "imdb";
        String password = "";
        String connectionIP = "172.16.28.124";
        //String connectionIP = "172.16.25.68";
        SSHManager instance = new SSHManager(userName, connectionIP);
        String errorMessage = instance.connectUseSSHKey();

        if(errorMessage != null)
        {
            System.out.println(errorMessage);
            fail();
        }

        String expResult = "FILE_NAME\n";
        // call sendCommand for each command and the output
        //(without prompts) is returned
        String result = instance.sendCommand(command);
        // close only after all commands are sent
        System.out.println(result);
        instance.close();
        assertEquals(expResult, result);
    }
    @Test
    public void testSendFile()
    {
        System.out.println("sendFile");

        String command = "ls ";
        String userName = "imdb";
        String password = "";
        String connectionIP = "172.16.28.124";
        //String connectionIP = "172.16.25.68";
        SSHManager instance = new SSHManager(userName, connectionIP);
        String errorMessage = instance.connectUseSSHKey();

        if(errorMessage != null)
        {
            System.out.println(errorMessage);
            fail();
        }

        String expResult = "FILE_NAME\n";
        // call sendCommand for each command and the output
        //(without prompts) is returned
        int result = instance.createRemoteFile("thurv.txt", "Content file");
        // close only after all commands are sent
        System.out.println(result);
        instance.close();
        //assertEquals(expResult, result);
    }
    @Test
    public void testSendCommandkey()
    {
        System.out.println("sendCommand");

        /**
         * YOU MUST CHANGE THE FOLLOWING
         * FILE_NAME: A FILE IN THE DIRECTORY
         * USER: LOGIN USER NAME
         * PASSWORD: PASSWORD FOR THAT USER
         * HOST: IP ADDRESS OF THE SSH SERVER
         **/
        String command = "ls ";
        String userName = "chungphb";
        String password = "1";
        String connectionIP = "172.16.25.68";
        SSHManager instance = new SSHManager(userName, password,connectionIP, "");
        String errorMessage = instance.connectUsePassword();

        if(errorMessage != null)
        {
            System.out.println(errorMessage);
            //fail();
        }

        String expResult = "FILE_NAME\n";
        // call sendCommand for each command and the output
        //(without prompts) is returned
        String result = instance.sendCommand(command);
        // close only after all commands are sent
        System.out.println(result);
        instance.close();
        assertEquals(expResult, result);
    }

    @Test
    public void test0123() {
        Pattern pattern = Pattern.compile("host.*\"([0-9.]*)\".*");
        Matcher m = pattern.matcher("host =    \"172.16.31.54\" ");
        System.out.println(m.find());
        System.out.println(m.group(1));
    }
}
