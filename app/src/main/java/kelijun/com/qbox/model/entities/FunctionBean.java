package kelijun.com.qbox.model.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by ${kelijun} on 2018/6/25.
 */
@Entity
public class FunctionBean {
    @Id(autoincrement = true)
    private Long functionId;

    private int id;
    private int mark;
    @Unique
    private String name;
    private String code;
    private boolean notOpen;
    @Generated(hash = 1500686151)
    public FunctionBean(Long functionId, int id, int mark, String name, String code,
            boolean notOpen) {
        this.functionId = functionId;
        this.id = id;
        this.mark = mark;
        this.name = name;
        this.code = code;
        this.notOpen = notOpen;
    }
    @Generated(hash = 1500552263)
    public FunctionBean() {
    }
    public Long getFunctionId() {
        return this.functionId;
    }
    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMark() {
        return this.mark;
    }
    public void setMark(int mark) {
        this.mark = mark;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public boolean getNotOpen() {
        return this.notOpen;
    }
    public void setNotOpen(boolean notOpen) {
        this.notOpen = notOpen;
    }
}
