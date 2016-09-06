package upbox.wmq.test;

import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import upbox.service.URegionService;

/**
 * 
 * @author yuancao
 *
 * 13611929818
 */
@ContextConfiguration(locations={"file:src/main/resources/applicationContext.xml"})  
public class AdministrativeAreas  extends AbstractJUnit4SpringContextTests{
	
	@Resource
	private URegionService uregionService;
	
	/**
	 * B级别找A是code前2位一致
	 * C级别找B是code前4位一致
	 * @throws Exception
	 */
	@Test
	public void savaAreas() {
		try {
			uregionService.initAreas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
