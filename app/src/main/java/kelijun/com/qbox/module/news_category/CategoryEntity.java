package kelijun.com.qbox.module.news_category;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ${kelijun} on 2018/6/25.
 */
@Entity
public class CategoryEntity {
    @Id(autoincrement = true)
    private Long id;

    private String name;
    private String key;
    private int order;
    @Generated(hash = 747555915)
    public CategoryEntity(Long id, String name, String key, int order) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.order = order;
    }
    @Generated(hash = 725894750)
    public CategoryEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public int getOrder() {
        return this.order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
}
