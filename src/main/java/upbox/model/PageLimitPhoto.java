package upbox.model;

import java.io.Serializable;


/**
 * 相册分页实体bean
 * @author yuancao
 *
 */
public class PageLimitPhoto implements Serializable{
	private static final long serialVersionUID = 173943921168627332L;
	private int limit; //分页每页显示条数
	private int offset; //分页起始位置
	private int page; //当前页数
	private int totalPage; //总页数
	private int totalCount; //总条数
	
	public int getLimit()
	{
		return limit;
	}
	public void setLimit(int limit)
	{
		this.limit = limit;
	}
	public int getOffset()
	{
		return offset;
	}
	public void setOffset(int offset)
	{
		this.offset = offset;
	}
	public int getPage()
	{
		return page;
	}
	public void setPage(int page)
	{
		this.page = page;
	}
	public int getTotalPage()
	{
		return totalPage;
	}
	public void setTotalPage(int totalPage)
	{
		this.totalPage = totalPage;
	}
	public int getTotalCount()
	{
		return totalCount; 
	}
	public void setTotalCount(int totalCount)
	{
		this.totalCount = totalCount;
	}
	
	public PageLimitPhoto(int page,int totalCount)
	{
		super();
		this.page = page <= 0 ? 1 : page;
		this.limit = 40;
		this.offset = (this.limit * this.page) - this.limit;
		this.totalCount = totalCount;
		this.totalPage = this.totalCount % this.limit != 0 ? Math.round(this.totalCount / this.limit) + 1 : Math.round(this.totalCount / this.limit);
	}
}
