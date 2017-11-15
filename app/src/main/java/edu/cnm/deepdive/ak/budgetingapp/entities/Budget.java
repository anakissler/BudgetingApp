package edu.cnm.deepdive.ak.budgetingapp.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "Budget")
public class Budget {


  @DatabaseField(columnName = "BUDGET_ID", generatedId = true)
  private int id;

  @DatabaseField(columnName = "CREATED", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
      format = "yyyy-MM-dd HH:mm:ss", canBeNull = false, readOnly = true)
  private Timestamp created;

  @DatabaseField(columnName = "CATEGORY_ID", canBeNull = false, foreign = true,
      foreignAutoRefresh = true, uniqueCombo = true, index = true)
  private Category category;

  @DatabaseField(columnName = "YEAR", canBeNull = false, uniqueCombo = true)
  private int year;

  @DatabaseField(columnName = "MONTH", canBeNull = false, uniqueCombo = true)
  private int month;

  @DatabaseField(columnName = "AMOUNT", canBeNull = false)
  private double amount;

  public void setAmount(double amount) {this.amount = amount;}

  public double getAmount() {return amount;}

  public void setMonth(int month) {this.month = month;}

  public int getMonth() {return month;}

  public void setYear(int year) {this.year = year;}

  public int getYear() {return year;}

  public Category getCategory() {return category;}

  public void setCategory(Category category) {
    this.category = category;
  }

  public Timestamp getCreated() {
    return created;
  }

  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("created", created);
    return super.toString();
  }
}
