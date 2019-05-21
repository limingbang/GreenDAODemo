package com.jwstudio.greendaodemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.jwstudio.greendao.DaoSession;
import com.jwstudio.greendao.CardDao;
import com.jwstudio.greendao.UserDao;

import java.util.List;
import com.jwstudio.greendao.OrdersDao;

@Entity(
        nameInDb = "USERS", // 表名
        indexes = {
                @Index(value = "name DESC"), // 为属性name设置索引
        }
)
public class User {
    @Id(autoincrement = true) // 主键，要求是Long型
    private Long id;

    private Long cardId;

    @ToOne(joinProperty = "cardId") // 设置一对一关联，连接属性是cardId
    private Card card;

    @ToMany(referencedJoinProperty = "userId") // 设置一对多关联，连接属性是Orders类的外键userId
    private List<Orders> orders;

    @Index(name = "usercode_index", unique = true) // 设置索引且是唯一索引
    private String usercode;

    @Property(nameInDb = "username") // 设置该属性对应的列名
    @NotNull                         // 非空
    private String name;

    private String userAddress; // 可以为空

    // 为数据库升级测试用
    private String sex;

    @Transient // 临时存储
    private int tempUserSign;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    @Generated(hash = 10293163)
    private transient Long card__resolvedKey;

    @Generated(hash = 2093679242)
    public User(Long id, Long cardId, String usercode, @NotNull String name, String userAddress,
            String sex) {
        this.id = id;
        this.cardId = cardId;
        this.usercode = usercode;
        this.name = name;
        this.userAddress = userAddress;
        this.sex = sex;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserAddress() {
        return this.userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Long getCardId() {
        return this.cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 2012690778)
    public Card getCard() {
        Long __key = this.cardId;
        if (card__resolvedKey == null || !card__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CardDao targetDao = daoSession.getCardDao();
            Card cardNew = targetDao.load(__key);
            synchronized (this) {
                card = cardNew;
                card__resolvedKey = __key;
            }
        }
        return card;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 364675812)
    public void setCard(Card card) {
        synchronized (this) {
            this.card = card;
            cardId = card == null ? null : card.getId();
            card__resolvedKey = cardId;
        }
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1907478680)
    public List<Orders> getOrders() {
        if (orders == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrdersDao targetDao = daoSession.getOrdersDao();
            List<Orders> ordersNew = targetDao._queryUser_Orders(id);
            synchronized (this) {
                if (orders == null) {
                    orders = ordersNew;
                }
            }
        }
        return orders;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1446109810)
    public synchronized void resetOrders() {
        orders = null;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
