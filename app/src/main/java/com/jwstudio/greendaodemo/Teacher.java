package com.jwstudio.greendaodemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jwstudio.greendao.DaoSession;
import com.jwstudio.greendao.CourseDao;
import com.jwstudio.greendao.TeacherDao;

@Entity
public class Teacher {
    @Id(autoincrement = true)
    private Long id;

    private String name;

    // 多对多
    @ToMany
    // 连接两个多对多实体类的中间类
    @JoinEntity(
            // 中间类类名
            entity = JoinTeacherWithCourse.class,
            // 源属性，中间类的外键，对应Teacher类的主键
            sourceProperty = "tId",
            // 目标属性，中间类的外键，对应Course类的主键
            targetProperty = "cId"
    )
    private List<Course> courses;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 648119699)
    private transient TeacherDao myDao;

    @Generated(hash = 1434396195)
    public Teacher(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1630413260)
    public Teacher() {
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2116680280)
    public List<Course> getCourses() {
        if (courses == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CourseDao targetDao = daoSession.getCourseDao();
            List<Course> coursesNew = targetDao._queryTeacher_Courses(id);
            synchronized (this) {
                if (courses == null) {
                    courses = coursesNew;
                }
            }
        }
        return courses;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1373990168)
    public synchronized void resetCourses() {
        courses = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1349174479)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTeacherDao() : null;
    }
}
