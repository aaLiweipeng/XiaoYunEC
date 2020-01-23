package com.lwp.xiaoyun.ec.main.sort.content;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/20 6:08
 *     desc   : 对应Json数据中，data键下一层的数据 的Bean
 *
 *             SectionEntity<SectionContentItemEntity>中的 SectionContentItemEntity
 *             乃是自建的适配json数据的类
 *
 *             注意继承自 SectionEntity，有头部bean 和 非头部bean 的分别
 * </pre>
 */
public class SectionBean extends SectionEntity<SectionContentItemEntity> {

    private boolean mIsMore = false;//是否更多
    private int mId = -1;//每个section的id
    //header 即 对应数据中的 section键

    public SectionBean(SectionContentItemEntity sectionContentItemEntity) {
        super(sectionContentItemEntity);
    }

    public SectionBean(boolean isHeader, String header) {
        super(isHeader, header);
    }


    public boolean isMore() {
        return mIsMore;
    }

    public void setIsMore(boolean isMore) {
        this.mIsMore = isMore;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
}
