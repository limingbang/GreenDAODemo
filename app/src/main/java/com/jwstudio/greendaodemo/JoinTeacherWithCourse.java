package com.jwstudio.greendaodemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinTeacherWithCourse {
    @Id(autoincrement = true)
    private Long id;

    // 代表老师的id
    private Long tId;

    // 代表课程的id
    private Long cId;

    @Generated(hash = 152070862)
    public JoinTeacherWithCourse(Long id, Long tId, Long cId) {
        this.id = id;
        this.tId = tId;
        this.cId = cId;
    }

    @Generated(hash = 1724527908)
    public JoinTeacherWithCourse() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTId() {
        return this.tId;
    }

    public void setTId(Long tId) {
        this.tId = tId;
    }

    public Long getCId() {
        return this.cId;
    }

    public void setCId(Long cId) {
        this.cId = cId;
    }
}
