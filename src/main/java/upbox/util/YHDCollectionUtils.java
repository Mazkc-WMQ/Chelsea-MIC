package upbox.util;

import java.util.Collection;
import java.util.List;

import upbox.model.NullCollection;

/**
 * LIST去除null数据工具类
 * @author wmq
 *
 * 15618777630
 */
public class YHDCollectionUtils {
	public static final Collection NULL_COLLECTION = new NullCollection();  
    
    public static final <T> Collection<T> nullCollection() {  
        return (List<T>) NULL_COLLECTION;  
    }
}
