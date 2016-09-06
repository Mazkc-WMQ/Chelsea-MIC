package upbox.xhy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import upbox.service.OLDLBSService;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/6/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class BaiduLBSActionTest {

    @Resource
    private OLDLBSService oldlbsService;
    @Test
    public void addLbsDuelChallengeTest(){
        try {
            this.oldlbsService.addLbsDuelChallenge(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
