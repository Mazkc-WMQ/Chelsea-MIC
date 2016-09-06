package upbox.xhy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.UPlayer;
import upbox.model.UPlayerHonor;
import upbox.service.UPlayerService;
import upbox.service.UTeamService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class UPlayerServiceTest {

    @Resource
    private OperDAOImpl baseDAO;
    @Resource
    private UPlayerService uplayerService;
    private String keyId;

    @Test
    public void testPlayerHonorListPage() throws Exception {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("token","f971a161-ec0f-4eb1-b067-c2d9cb2505ce");
        hashMap.put("page","1");
        HashMap<String, Object> retMap = this.uplayerService.playerHonorListPage(hashMap);
        System.out.println();
    }


    @Test
    public void testSavePlayerHonor() throws Exception {
        for (int i = 1; i < 31; i++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("token", "d0a3efa6-f7d0-4296-8306-96d2aa626b44");
            hashMap.put("remark", "我是队长" + i);
            hashMap.put("honorDate","2015-06-"+i);
            HashMap<String, Object> retMap = this.uplayerService.savePlayerHonor(hashMap);
        }

        System.out.println();
    }

    @Test
    public void testUpdatePlayerHonor() throws Exception {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("keyId","789d51fb-0eea-44cd-a07b-fc2079ef28fd");
        hashMap.put("remark","我是队长23333333");
        HashMap<String,Object> retMap = this.uplayerService.updatePlayerHonor(hashMap);
        System.out.println();
    }

    @Test
    public void testDeletePlayerHonor() throws Exception{
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("keyId","789d51fb-0eea-44cd-a07b-fc2079ef28fd");
        HashMap<String,Object> retMap = this.uplayerService.deletePlayerHonor(hashMap);
        System.out.println();
    }

    @Resource
    UTeamService uTeamService;

    @Test
    public void testPlayerData() throws Exception {
        HashMap<String,String> hashMap  = new HashMap<String,String>();
        hashMap.put("playerId","4d26d77b-8a7f-4b8f-be95-e8e104221f88");
        HashMap<String,Object> retMap = this.uplayerService.uPlayerInfoDetail203(hashMap);

        System.out.println();
    }
}
