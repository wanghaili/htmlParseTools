package cn.edu.bupt.rsx.htmlparser.model;

import java.io.Serializable;

public class WebPageFeature implements Serializable,IIdAware{

	private static final long serialVersionUID = -4501396474180345802L;

	private long id;
	private String url;
	private int slashNum;
	private double noLinkRatio;
	private double punctuationRatio;
	private double textLinkRatio;
	private String type;
	private long createTime;
	private long updateTime;

	public long getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSlashNum() {
		return slashNum;
	}

	public void setSlashNum(int slashNum) {
		this.slashNum = slashNum;
	}

	public double getNoLinkRatio() {
		return noLinkRatio;
	}

	public void setNoLinkRatio(double noLinkRatio) {
		this.noLinkRatio = noLinkRatio;
	}

	public double getPunctuationRatio() {
		return punctuationRatio;
	}

	public void setPunctuationRatio(double punctuationRatio) {
		this.punctuationRatio = punctuationRatio;
	}

	public double getTextLinkRatio() {
		return textLinkRatio;
	}

	public void setTextLinkRatio(double textLinkRatio) {
		this.textLinkRatio = textLinkRatio;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebPageFeature [id=");
		builder.append(id);
		builder.append(", url=");
		builder.append(url);
		builder.append(", slashNum=");
		builder.append(slashNum);
		builder.append(", noLinkRatio=");
		builder.append(noLinkRatio);
		builder.append(", punctuationRatio=");
		builder.append(punctuationRatio);
		builder.append(", textLinkRatio=");
		builder.append(textLinkRatio);
		builder.append(", type=");
		builder.append(type);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append("]");
		return builder.toString();
	}

	
}
