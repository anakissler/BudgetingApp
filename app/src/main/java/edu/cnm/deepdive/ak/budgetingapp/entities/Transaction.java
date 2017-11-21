package edu.cnm.deepdive.ak.budgetingapp.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a table in my Database for my transactions
 */
@DatabaseTable(tableName = "TRANSACTION")
public class Transaction {

  /**
   * Gives each transaction a unique ID
   */

  @DatabaseField(columnName = "TRANSACTION_ID", generatedId = true)
  private int id;

  /**
   * Puts a year, month and day when each transaction was created
   */

  @DatabaseField(columnName = "CREATED", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
      format = "yyyy-MM-dd HH:mm:ss", canBeNull = false, readOnly = true)
  private Timestamp created;

  /**
   * Creates a column for the amount field of transaction in database
   */
  @DatabaseField(columnName = "AMOUNT", canBeNull = false)
  private double amount;


  /**
   * Creates a column for the place field in database
   */
  @DatabaseField(columnName = "PLACE")
  private String place;

  /**
   * Creates a column for the notes field in database
   */

  @DatabaseField(columnName = "NOTES")
  private String notes;

  /**
   *Creates a column for the date field in database
   */

  @DatabaseField(columnName = "DATE" , format = "yyyy-MM-dd",
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", canBeNull = false)
  private Date date;

  /**
   * Setter and Getter for the date
   * @return Date
   */
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Assigns the transaction to a specific category ID
   */

  @DatabaseField(columnName = "CATEGORY_ID", canBeNull = false, foreign = true,
      foreignAutoRefresh = true, index = true)
  private Category category;

  /**
   * Setter and Getter for category
   * @return category
   */
  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Setter and Getter for the amount of transaction
   * @return Integer Amount
   */

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * Setter and Getter for the place where transaction occurred
   * @return place
   */
  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  /**
   * Setter and Getter for any notes form user
   * @return notes
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * Getter for the ID of category
   * @return ID
   */
  public int getId() {
    return id;
  }

  /**
   * Getter for the timestamp
   * @return Timestamp
   */

  public Timestamp getCreated() {
    return created;
  }

  /**
   * Puts ID and when transaction was created to a map
   * @return
   */
  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("created", created);
    return super.toString();
  }
}
