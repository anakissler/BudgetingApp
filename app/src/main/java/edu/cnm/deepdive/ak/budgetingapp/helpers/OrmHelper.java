package edu.cnm.deepdive.ak.budgetingapp.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import edu.cnm.deepdive.ak.budgetingapp.entities.Budget;
import edu.cnm.deepdive.ak.budgetingapp.entities.Category;
import edu.cnm.deepdive.ak.budgetingapp.entities.Transaction;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 * Database OrmLite
 */
public class OrmHelper extends OrmLiteSqliteOpenHelper  {


  private static final String DATABASE_NAME = "budget.db";
  private static final int DATABASE_VERSION = 1;
  //Dao stands for Data Access Object
  private Dao<Transaction, Integer> transactionDao = null;
  private Dao<Category, Integer> categoryDao = null;
  private Dao<Budget, Integer> budgetDao = null;

  /**
   * Creates Budget, category and transaction tables.
   * @param context
   */
  public OrmHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
  @Override
  public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    try {
      TableUtils.createTable(connectionSource, Category.class);
      TableUtils.createTable(connectionSource, Budget.class);
      TableUtils.createTable(connectionSource, Transaction.class);
      populateDatabase();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param database
   * @param connectionSource
   * @param oldVersion
   * @param newVersion
   */
  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion,
      int newVersion) {

  }

  @Override
  public void close() {
    transactionDao = null;
    categoryDao = null;
    budgetDao = null;
    super.close();
  }

  /**
   * Accesses the Transaction Objects
   * @return
   * @throws SQLException
   */

    public synchronized Dao<Transaction, Integer> getTransactionDao() throws SQLException {
      if(transactionDao == null) {
        transactionDao = getDao(Transaction.class);
      }
      return transactionDao;
    }

  /**
   * Accesses the Category Objects
   * @return
   * @throws SQLException
   */
    public synchronized Dao<Category, Integer> getCategoryDao() throws SQLException {
      if(categoryDao == null) {
        categoryDao = getDao(Category.class);
      }
      return categoryDao;
    }

  /**
   * Accesses the Budget Objects
   * @return
   * @throws SQLException
   */
  public synchronized Dao<Budget, Integer> getBudgetDao() throws SQLException {
    if (budgetDao == null) {
      budgetDao = getDao(Budget.class);
    }
    return budgetDao;
  }

  /**
   * Populates my database with the Categories, budget information and transaction information
   * @throws SQLException
   */
  private void populateDatabase() throws SQLException {
    Calendar calendar = Calendar.getInstance();

    Transaction transaction = new Transaction();
    Category category = new Category();
    Budget budget = new Budget();

    category = new Category();
    category.setName("Groceries");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);


    category = new Category();
    category.setName("Gas");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);

//    List<Category> testList = getCategoryDao().queryForAll();
//    Assert.assertEquals(testList.size(), 1);
//    Assert.assertEquals(testList.get(0).getTransactions().size(), 1);

    category = new Category();
    category.setName("Vehicle Payment");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);



    category = new Category();
    category.setName("Entertainment");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);



    category = new Category();
    category.setName("Mortgage/Rent");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);



    category = new Category();
    category.setName("Phone");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);



    category = new Category();
    category.setName("Utilities");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);


    category = new Category();
    category.setName("Insurance");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);

    category = new Category();
    category.setName("Credit Card");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);


    category = new Category();
    category.setName("Cable");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);


    category = new Category();
    category.setName("Personal Items");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);

    category = new Category();
    category.setName("Household Items");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);

    category = new Category();
    category.setName("Dining Out");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);


    category = new Category();
    category.setName("Car Repair/Maintenance");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);

    category = new Category();
    category.setName("Internet");
    getCategoryDao().create(category);
    budget = new Budget();
    budget.setCategory(category);
    budget.setAmount(0.00);
    budget.setMonth(11);
    budget.setYear(2017);
    getBudgetDao().create(budget);

  }
  public interface OrmInteraction {
    OrmHelper getHelper();
  }
}
