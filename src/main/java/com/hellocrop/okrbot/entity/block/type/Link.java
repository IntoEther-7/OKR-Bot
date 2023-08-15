package com.hellocrop.okrbot.entity.block.type;

import com.hellocrop.okrbot.entity.contentblock.ContentLink;
import lombok.Data;

@Data
public class Link {
    String url;

    public Link() {
    }

    public Link(String url) {
        this.url = url;
    }

    public static Link fromContentLink(ContentLink contentLink) {
        if (contentLink == null) return null;
        Link link = new Link(contentLink.getUrl());
        return link;
    }
}
