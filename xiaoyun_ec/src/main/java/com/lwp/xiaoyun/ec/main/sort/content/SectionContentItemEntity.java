package com.lwp.xiaoyun.ec.main.sort.content;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/20 6:12
 *     desc   : 对应Json数据中，data键下一层的 goods键下一层的数据 的Bean
 * </pre>
 */
public class SectionContentItemEntity {
    private int mGoodsId = 0;
    private String mGoodsName = null;
    private String mGoodsThumb = null;//缩略图

    public int getGoodsId() {
        return mGoodsId;
    }

    public void setGoodsId(int goodsId) {
        this.mGoodsId = goodsId;
    }

    public String getGoodsName() {
        return mGoodsName;
    }

    public void setGoodsName(String goodsName) {
        this.mGoodsName = goodsName;
    }

    public String getGoodsThumb() {
        return mGoodsThumb;
    }

    public void setGoodsThumb(String goodsThumb) {
        this.mGoodsThumb = goodsThumb;
    }
}
