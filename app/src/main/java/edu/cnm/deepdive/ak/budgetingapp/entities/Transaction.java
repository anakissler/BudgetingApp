package edu.cnm.deepdive.ak.budgetingapp.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "TRANSACTION")
public class Transaction {

  @DatabaseField(columnName = "TRANSACTION_ID", generatedId = true)
  private int id;

  @DatabaseField(columnName = "CREATED", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
      format = "yyyy-MM-dd HH:mm:ss", canBeNull = false, readOnly = true)
  private Timestamp created;

  @DatabaseField(columnName = "AMOUNT", canBeNull = false)
  private double amount;

  @DatabaseField(columnName = "PLACE")
  private String place;

  @DatabaseField(columnName = "NOTES")
  private String notes;

  @DatabaseField(columnName = "CATEGORY_ID", canBeNull = false, foreign = true, foreignAutoRefresh = true)
  private Category category;

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public int getId() {
    return id;
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
