package edu.cnm.deepdive.ak.budgetingapp.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a Database table for my Budget
 */
@DatabaseTable(tableName = "Budget")
public class Budget {

  /**
   * Assigns an ID to my columns
   */
  @DatabaseField(columnName = "BUDGET_ID", generatedId = true)
  private int id;
  /**
   * Puts a Month, day, and year timestamp on each of my columns
   */
  @DatabaseField(columnName = "CREATED", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
      format = "yyyy-MM-dd HH:mm:ss", canBeNull = false, readOnly = true)
  private Timestamp created;
  /**
   * Assigns an ID to each of my Categories
   */
  @DatabaseField(columnName = "CATEGORY_ID", canBeNull = false, foreign = true,
      foreignAutoRefresh = true, uniqueCombo = true, index = true)
  private Category category;
  /**
   * Assigns a unique combination of Year, Month, and Day to each of my Categories.
   */
  @DatabaseField(columnName = "YEAR", canBeNull = false, uniqueCombo = true)
  private int year;

  @DatabaseField(columnName = "MONTH", canBeNull = false, uniqueCombo = true)
  private int month;
  /**
   * Creates a column for the Budget amount for each of my categories
   */
  @DatabaseField(columnName = "AMOUNT", canBeNull = false)
  private double amount;

  /**
   * Setter and Getter for amount
   * @param amount
   */
  public void setAmount(double amount) {this.amount = amount;}

  public double getAmount() {return amount;}

  /**
   * Setter and Getter for month
   * @param month
   */

  public void setMonth(int month) {this.month = month;}

  public int getMonth() {return month;}

  /**
   * Setter and Getter for year
   * @param year
   */

  public void setYear(int year) {this.year = year;}

  public int getYear() {return year;}

  /**
   * Setter and Getter for category
   * @return
   */
  public Category getCategory() {return category;}

  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Getter for Timestamp
   * @return
   */

  public Timestamp getCreated() {
    return created;
  }

  /**
   * Puts ID and when it was created into a Map.
   * @return a string
   */
  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("created", created);
    return super.toString();
  }
}
