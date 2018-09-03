package cn.edu.bupt.rsx.htmlparser.model;

import java.io.Serializable;

public class HtmlParserRecord implements Serializable,IIdAware{
	
	private static final long serialVersionUID = 4180599509706045110L;

	private long id;
	
	private String url;
	
	private String content;
	
	private String title;
	
	private String keyWords;
	
	private String description;

	private String keyWordsMap;
	
	private String type;
	
	private long createTime;
	
	private long modifiTime;

	private long fileSign = 0;



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
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

	public long getModifiTime() {
		return modifiTime;
	}

	public void setModifiTime(long modifiTime) {
		this.modifiTime = modifiTime;
	}

	public String getKeyWordsMap() {
		return keyWordsMap;
	}

	public void setKeyWordsMap(String keyWordsMap) {
		this.keyWordsMap = keyWordsMap;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "HtmlParserRecord{" +
				"id=" + id +
				", url='" + url + '\'' +
				", content='" + content + '\'' +
				", title='" + title + '\'' +
				", keyWords='" + keyWords + '\'' +
				", description='" + description + '\'' +
				", keyWordsMap='" + keyWordsMap + '\'' +
				", type=" + type +
				", createTime=" + createTime +
				", modifiTime=" + modifiTime +
				'}';
	}

	public long getFileSign() {
		return fileSign;
	}

	public void setFileSign(long fileSign) {
		this.fileSign = fileSign;
	}
}
