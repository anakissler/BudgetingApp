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

public class OrmHelper extends OrmLiteSqliteOpenHelper  {


  private static final String DATABASE_NAME = "budget.db";
  private static final int DATABASE_VERSION = 1;
  //Dao stands for Data Access Object
  private Dao<Transaction, Integer> transactionDao = null;
  private Dao<Category, Integer> categoryDao = null;


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

  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion,
      int newVersion) {

  }

  @Override
  public void close() {
    transactionDao = null;
    categoryDao = null;
    super.close();
  }


    public synchronized Dao<Transaction, Integer> getTransactionDao() throws SQLException {
      if(transactionDao == null) {
        transactionDao = getDao(Transaction.class);
      }
      return transactionDao;
    }
    public synchronized Dao<Category, Integer> getCategoryDao() throws SQLException {
      if(categoryDao == null) {
        categoryDao = getDao(Category.class);
      }
      return categoryDao;
    }
  private void populateDatabase() throws SQLException {
    Transaction transaction = new Transaction();
    Category category = new Category();
    category.setName("Groceries and Dining out");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Gas");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Vehicle Payment");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Entertainment");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Mortgage/Rent");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Phone");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Utilities");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Insurance");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Credit Card");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Cable and Internet");
    getCategoryDao().create(category);
    category = new Category();
    category.setName("Personal Items");
    getCategoryDao().create(category);

  }

}
