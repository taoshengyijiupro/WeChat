package com.shipparts.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ArticleItem {

    private String Title;
    private String Description;
    private String PicUrl;
    private String Url;
}
