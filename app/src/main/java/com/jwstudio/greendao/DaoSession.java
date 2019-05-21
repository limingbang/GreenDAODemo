package com.jwstudio.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.jwstudio.greendaodemo.Card;
import com.jwstudio.greendaodemo.Course;
import com.jwstudio.greendaodemo.JoinTeacherWithCourse;
import com.jwstudio.greendaodemo.Orders;
import com.jwstudio.greendaodemo.Teacher;
import com.jwstudio.greendaodemo.User;

import com.jwstudio.greendao.CardDao;
import com.jwstudio.greendao.CourseDao;
import com.jwstudio.greendao.JoinTeacherWithCourseDao;
import com.jwstudio.greendao.OrdersDao;
import com.jwstudio.greendao.TeacherDao;
import com.jwstudio.greendao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig cardDaoConfig;
    private final DaoConfig courseDaoConfig;
    private final DaoConfig joinTeacherWithCourseDaoConfig;
    private final DaoConfig ordersDaoConfig;
    private final DaoConfig teacherDaoConfig;
    private final DaoConfig userDaoConfig;

    private final CardDao cardDao;
    private final CourseDao courseDao;
    private final JoinTeacherWithCourseDao joinTeacherWithCourseDao;
    private final OrdersDao ordersDao;
    private final TeacherDao teacherDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        cardDaoConfig = daoConfigMap.get(CardDao.class).clone();
        cardDaoConfig.initIdentityScope(type);

        courseDaoConfig = daoConfigMap.get(CourseDao.class).clone();
        courseDaoConfig.initIdentityScope(type);

        joinTeacherWithCourseDaoConfig = daoConfigMap.get(JoinTeacherWithCourseDao.class).clone();
        joinTeacherWithCourseDaoConfig.initIdentityScope(type);

        ordersDaoConfig = daoConfigMap.get(OrdersDao.class).clone();
        ordersDaoConfig.initIdentityScope(type);

        teacherDaoConfig = daoConfigMap.get(TeacherDao.class).clone();
        teacherDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        cardDao = new CardDao(cardDaoConfig, this);
        courseDao = new CourseDao(courseDaoConfig, this);
        joinTeacherWithCourseDao = new JoinTeacherWithCourseDao(joinTeacherWithCourseDaoConfig, this);
        ordersDao = new OrdersDao(ordersDaoConfig, this);
        teacherDao = new TeacherDao(teacherDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Card.class, cardDao);
        registerDao(Course.class, courseDao);
        registerDao(JoinTeacherWithCourse.class, joinTeacherWithCourseDao);
        registerDao(Orders.class, ordersDao);
        registerDao(Teacher.class, teacherDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        cardDaoConfig.clearIdentityScope();
        courseDaoConfig.clearIdentityScope();
        joinTeacherWithCourseDaoConfig.clearIdentityScope();
        ordersDaoConfig.clearIdentityScope();
        teacherDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    public CourseDao getCourseDao() {
        return courseDao;
    }

    public JoinTeacherWithCourseDao getJoinTeacherWithCourseDao() {
        return joinTeacherWithCourseDao;
    }

    public OrdersDao getOrdersDao() {
        return ordersDao;
    }

    public TeacherDao getTeacherDao() {
        return teacherDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
