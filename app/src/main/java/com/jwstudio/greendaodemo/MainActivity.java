package com.jwstudio.greendaodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jwstudio.greendao.CardDao;
import com.jwstudio.greendao.CourseDao;
import com.jwstudio.greendao.DaoMaster;
import com.jwstudio.greendao.DaoSession;
import com.jwstudio.greendao.JoinTeacherWithCourseDao;
import com.jwstudio.greendao.OrdersDao;
import com.jwstudio.greendao.TeacherDao;
import com.jwstudio.greendao.UserDao;

import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserDao userDao;
    private CardDao cardDao;
    private OrdersDao ordersDao;
    private TeacherDao teacherDao;
    private CourseDao courseDao;
    private JoinTeacherWithCourseDao teacherWithCourseDao;
    private List<User> userList;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userList = new ArrayList<>();
        tvResult = findViewById(R.id.tv_result);
        userDao = MyGreenDAOApplication.getInstances().getmDaoSession().getUserDao();
        cardDao = MyGreenDAOApplication.getInstances().getmDaoSession().getCardDao();
        ordersDao = MyGreenDAOApplication.getInstances().getmDaoSession().getOrdersDao();
        teacherDao = MyGreenDAOApplication.getInstances().getmDaoSession().getTeacherDao();
        courseDao = MyGreenDAOApplication.getInstances().getmDaoSession().getCourseDao();
        teacherWithCourseDao = MyGreenDAOApplication.getInstances().getmDaoSession().getJoinTeacherWithCourseDao();

        QueryBuilder.LOG_VALUES = true;
        QueryBuilder.LOG_SQL = true;

//        cardDao.deleteAll();
//        userDao.deleteAll();
//        ordersDao.deleteAll();
//        insertMany();
//        queryList();
//        insertOneToOne();
//        queryOneToOne();
//        insertCardOneToOne();
//        queryCardOneToOne();
//        insertOneToMany();
//        multiQueryTwoTb();
//        multiQueryTwoOrders();
//        queryToOneOrderToUser();
//        queryThreeTb();

//        teacherWithCourseDao.deleteAll();
//        courseDao.deleteAll();
//        teacherDao.deleteAll();
//        insertManyToMany();
//        queryManyToManyT();
//        queryManyToManyC();

        migrationTest();
        migrationQueryList();
    }

    private void migrationTest() {
        User user1 = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();
        user1.setSex("男");
        userDao.update(user1);
    }

    private void migrationQueryList() {
        String result = "显示结果为：";
        List<User> users = userDao.loadAll();
        int i = 0;
        for (User user : users) {
            i++;
            String res = result + "i=" + i + ",id:" + user.getId() + ",name:" + user.getName() +
                    ",address:" + user.getUserAddress() +
                    ",sex:" + user.getSex();
            Log.d("TAG", res);
        }
    }

    // 查询买了紫金冠的客户的身份证号
    private void queryThreeTb() {
        QueryBuilder<Card> qb = cardDao.queryBuilder()
                .where(CardDao.Properties.CardCode.like("1987%"));
        Join user = qb.join(CardDao.Properties.UserId, User.class);
        Join order = qb.join(user, UserDao.Properties.Id, Orders.class, OrdersDao.Properties.UserId);
        order.where(OrdersDao.Properties.GoodsName.eq("紫金冠"));
        List<Card> cardList = qb.list();

        if (cardList != null) {
            Log.d("TAG", "买了紫金冠的身份证前四位是1987的用户：");
            for (Card card : cardList) {
                Log.d("TAG", "身份证：" + card.getCardCode() + "名字：" + card.getUser().getName());
            }
        }
    }

    // 两表查询，购买紫金冠的用户有
    private void multiQueryTwoTb() {
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.join(Orders.class, OrdersDao.Properties.UserId)
                .where(OrdersDao.Properties.GoodsName.eq("紫金冠"));
        List<User> users = qb.list();

        if (users != null) {
            for (User u : users) {
                Log.d("TAG", "购买紫金冠的用户有：" + u.getName());
            }
        }
    }

    // 两表查询，用户是孙悟空所购买的商品有
    private void multiQueryTwoOrders() {
        QueryBuilder<Orders> qb = ordersDao.queryBuilder();
        qb.join(OrdersDao.Properties.UserId, User.class)
                .where(UserDao.Properties.Name.eq("孙悟空"));
        List<Orders> ordersList = qb.list();

        if (ordersList != null) {
            for (Orders o : ordersList) {
                Log.d("TAG", o.getUser().getName() + "购买的商品有：" + o.getGoodsName());
            }
        }
    }

    // 多对多插入
    private void insertManyToMany() {
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setName("英语");

        Course course2 = new Course();
        course2.setName("语文");

        Course course3 = new Course();
        course3.setName("数学");

        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courseDao.insertInTx(courses);

        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher1 = new Teacher();
        teacher1.setName("孙悟空");

        Teacher teacher2 = new Teacher();
        teacher2.setName("猪八戒");

        Teacher teacher3 = new Teacher();
        teacher3.setName("沙和尚");

        teacherList.add(teacher1);
        teacherList.add(teacher2);
        teacherList.add(teacher3);
        teacherDao.insertInTx(teacherList);

        List<JoinTeacherWithCourse> teacherWithCourses = new ArrayList<>();
        // 悟空教英语
        JoinTeacherWithCourse teacherWithCourse1 = new JoinTeacherWithCourse();
        teacherWithCourse1.setTId(teacher1.getId());
        teacherWithCourse1.setCId(course1.getId());

        // 悟空叫语文
        JoinTeacherWithCourse teacherWithCourse2 = new JoinTeacherWithCourse();
        teacherWithCourse2.setTId(teacher1.getId());
        teacherWithCourse2.setCId(course2.getId());

        // 悟空叫数学
        JoinTeacherWithCourse teacherWithCourse3 = new JoinTeacherWithCourse();
        teacherWithCourse3.setTId(teacher1.getId());
        teacherWithCourse3.setCId(course3.getId());

        // 沙和尚教语文
        JoinTeacherWithCourse teacherWithCourse4 = new JoinTeacherWithCourse();
        teacherWithCourse4.setTId(teacher2.getId());
        teacherWithCourse4.setCId(course2.getId());

        teacherWithCourses.add(teacherWithCourse1);
        teacherWithCourses.add(teacherWithCourse2);
        teacherWithCourses.add(teacherWithCourse3);
        teacherWithCourses.add(teacherWithCourse4);
        teacherWithCourseDao.insertInTx(teacherWithCourses);
    }

    // 多对多查询,通过”教师“找到课程
    private void queryManyToManyT() {
        Teacher teacher = teacherDao.queryBuilder().where(TeacherDao.Properties.Name.eq("孙悟空"))
                .build().unique();
        List<Course> courses = teacher.getCourses();

        if (courses != null) {
            Log.d("TAG", "孙悟空所教的课程：");
            for (Course course : courses) {
                Log.d("TAG", "课程名：" + course.getName());
            }
        }
    }

    // 多对多查询,通过”课程“找到课程
    private void queryManyToManyC() {
        Course course = courseDao.queryBuilder().where(CourseDao.Properties.Name.eq("语文"))
                .build().unique();
        List<Teacher> teachers = course.getTeachers();

        if (teachers != null) {
            Log.d("TAG", "教语文的老师有：");
            for (Teacher teacher : teachers) {
                Log.d("TAG", "教师名：" + teacher.getName());
            }
        }
    }

    // 一对多插入
    private void insertOneToMany() {
        List<Orders> ordersList = new ArrayList<>();
        User user1 = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();
        User user2 = userDao.queryBuilder().where(UserDao.Properties.Name.eq("猪八戒")).build().unique();

        Orders orders1 = new Orders();
        orders1.setGoodsName("金箍棒");
        orders1.setUser(user1);

        Orders orders2 = new Orders();
        orders2.setGoodsName("黄金甲");
        orders2.setUser(user1);

        Orders orders3 = new Orders();
        orders3.setGoodsName("紫金冠");
        orders3.setUser(user1);

        Orders orders4 = new Orders();
        orders4.setGoodsName("紫金冠");
        orders4.setUser(user2);

        ordersList.add(orders1);
        ordersList.add(orders2);
        ordersList.add(orders3);
        ordersList.add(orders4);

        ordersDao.insertInTx(ordersList);
    }

    private void queryToManyUserToOrder() {
        List<Orders> ordersList;
        User user1 = userDao.queryBuilder().where(UserDao.Properties.Name.eq("猪八戒")).build().unique();

        //直接通过User对象的getOrders()方法获得此用户的所有订单
        ordersList = user1.getOrders();
        Log.d("TAG", user1.getName() + "的订单内容为：");

        int i = 0;
        if (ordersList != null) {
            for (Orders order : ordersList) {
                i = i + 1;
                Log.d("TAG", "第" + i + "条订单的结果：" + ",id:" + order.getId()
                        + ",商品名：" + order.getGoodsName()
                        + ",用户名：" + user1.getName());
            }
        }
    }

    // 一对多查询
    private void queryToOneOrderToUser() {
        List<Orders> ordersList = ordersDao.queryBuilder().where(OrdersDao.Properties.GoodsName.eq("紫金冠")).list();

        if (ordersList != null) {
            for (Orders o : ordersList) {
                Log.d("TAG", "购买紫金冠的用户有：" + o.getUser().getName());
            }
        }
    }

    private void insertOneToOne() {
        Card card1 = new Card();
        card1.setCardCode("198798168");
        cardDao.insert(card1);

        User user1 = new User();
        user1.setName("孙悟空");
        user1.setUserAddress("花果山水帘洞");
        user1.setUsercode("001");
        user1.setCard(card1);
        userDao.insert(user1);
    }

    private void insertCardOneToOne() {
        User user1 = new User();
        user1.setName("孙悟空");
        user1.setUserAddress("花果山水帘洞");
        user1.setUsercode("001");

        Card card1 = new Card();
        card1.setCardCode("198798168");

        /* 注意以下代码的顺序 */
        userDao.insert(user1);
        card1.setUser(user1);

        cardDao.insert(card1);
        // 补上之前没有设置的user1的外键值
        user1.setCard(card1);
        // 更新user1对象
        userDao.update(user1);

        User user2 = new User();
        user2.setName("猪八戒");
        user2.setUserAddress("高老庄");
        user2.setUsercode("002");

        Card card2 = new Card();
        card2.setCardCode("4339999999");

        /* 注意以下代码的顺序 */
        userDao.insert(user2);
        card2.setUser(user2);

        cardDao.insert(card2);
        // 补上之前没有设置的user1的外键值
        user2.setCard(card2);
        // 更新user1对象
        userDao.update(user2);
    }

    private void queryCardOneToOne() {
        Card card = cardDao.queryBuilder().where(CardDao.Properties.CardCode.eq("198798168")).build().unique();
        User user = card.getUser();
        if (card != null && user != null) {
            Log.d("TAG", "username:" + user.getName() + ",user表cardId:" + user.getCardId() +
                    ",Card表id:" + card.getId() + ",cardcode:" + card.getCardCode());
        }
    }

    private void queryOneToOne() {
        User user = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();
        Card card = user.getCard();
        if (user != null && card != null) {
            Log.d("TAG", "username:" + user.getName() + ",user表cardId:" + user.getCardId() +
                    ",Card表id:" + card.getId() + ",cardcode:" + card.getCardCode());
        }
    }

    private void loadOneById() {
//        User user = userDao.load(new Long((long) 6));
        User user = userDao.loadByRowId(new Long((long) 6));
        Log.d("TAG", "name:" + user.getName());
    }

    private void queryQB() {
        QueryBuilder qb = userDao.queryBuilder();
        // 查询名字带有“悟”字的，并且按照属性usercode进行降序排列
//        List<User> userList=qb.where(UserDao.Properties.Name.like("%悟%"))
//                .orderDesc(UserDao.Properties.Usercode).list();

        //查询名字带有“悟”字的，而且地址时“流沙河”的或者是带有“山”字的且usercode==001的
        List<User> users = qb.where(UserDao.Properties.Name.like("%悟%"),
                qb.or(UserDao.Properties.UserAddress.eq("流沙河"),
                        qb.and(UserDao.Properties.UserAddress.like("%山%"),
                                UserDao.Properties.Usercode.eq("001")))).list();

        if (users != null) {
            int i = 0;
            for (User user : users) {
                i++;
                String res = "i=" + i + ",id:" + user.getId() + ",name:" + user.getName() + ",address:" + user.getUserAddress();
                Log.d("TAG", res);
            }
        }
    }

    private void queryRepeat() {
        Query query=userDao.queryBuilder().where(UserDao.Properties.Name.like("%悟%"),
                UserDao.Properties.UserAddress.like("%山%")).build();
        query.setParameter(0,"%怪%");
        query.setParameter(1,"%洞%");
        List<User> userList=query.list();
    }

    private void updateUser() {
        User user = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();
        user.setUserAddress("五指山");
        userDao.update(user);
    }

    private void deleteByName() {
        QueryBuilder qb = userDao.queryBuilder();
        List<User> users = qb.where(UserDao.Properties.Name.eq("猪八戒")).list();
        for (User user : users) {
            userDao.delete(user);
        }
//        userDao.deleteInTx(users);
    }

    private void insterOne() {
        User user1 = new User();
        user1.setName("孙悟空");
        user1.setUsercode("001");
        user1.setUserAddress("花果山水帘洞");
        userDao.insert(user1);
    }

    private void insertMany() {
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setName("孙悟空");
        user1.setUsercode("001");
        user1.setUserAddress("花果山水帘洞");

        User user2 = new User();
        user2.setName("猪八戒");
        user2.setUsercode("002");
        user2.setUserAddress("高老庄");

        User user3 = new User();
        user3.setName("沙悟净");
        user3.setUsercode("003");
        user3.setUserAddress("流沙河");

        User user4 = new User();
        user4.setName("黑熊怪");
        user4.setUsercode("004");
        user4.setUserAddress("黑风山");

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        userDao.insertInTx(users);
    }

    private void queryOneByName() {
        User user = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();
        if (user != null) {
            tvResult.setText("添加一条记录：id:" + user.getId() + ",地址：" + user.getUserAddress());
        }
    }

    private void queryList() {
        String result = "显示结果为：";
        List<User> users = userDao.loadAll();
        int i = 0;
        for (User user : users) {
            i++;
            String res = result + "i=" + i + ",id:" + user.getId() + ",name:" + user.getName() + ",address:" + user.getUserAddress();
            Log.d("TAG", res);
        }
    }
}
