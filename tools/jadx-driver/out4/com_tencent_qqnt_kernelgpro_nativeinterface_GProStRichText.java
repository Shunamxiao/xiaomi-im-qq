package com.tencent.qqnt.kernelgpro.nativeinterface;

import java.io.Serializable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public final class GProStRichText implements Serializable {
    public boolean isMarkdown;
    long serialVersionUID = 1;
    public ArrayList<GProStRichTextContent> contents = new ArrayList<>();
    public ArrayList<GProStImage> images = new ArrayList<>();
    public GProBaseEmoji sticker = new GProBaseEmoji();
    public String ipLocationProvince = "";
    public String sourceMarkdown = "";
    public ArrayList<GProStAudio> audios = new ArrayList<>();

    public ArrayList<GProStAudio> getAudios() {
        return this.audios;
    }

    public ArrayList<GProStRichTextContent> getContents() {
        return this.contents;
    }

    public ArrayList<GProStImage> getImages() {
        return this.images;
    }

    public String getIpLocationProvince() {
        return this.ipLocationProvince;
    }

    public boolean getIsMarkdown() {
        return this.isMarkdown;
    }

    public String getSourceMarkdown() {
        return this.sourceMarkdown;
    }

    public GProBaseEmoji getSticker() {
        return this.sticker;
    }

    public String toString() {
        return "GProStRichText{contents=" + this.contents + ",images=" + this.images + ",sticker=" + this.sticker + ",ipLocationProvince=" + this.ipLocationProvince + ",isMarkdown=" + this.isMarkdown + ",sourceMarkdown=" + this.sourceMarkdown + ",audios=" + this.audios + ",}";
    }
}