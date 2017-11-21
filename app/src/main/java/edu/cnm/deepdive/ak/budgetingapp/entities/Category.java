package edu.cnm.deepdive.ak.budgetingapp.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a table to the database for my categories
 */
@DatabaseTable(tableName = "CATEGORY")
public class Category {

  /**
   * Generates a unique ID to each one of my categories
   */
  @DatabaseField(columnName = "CATEGORY_ID", generatedId = true)
  private int id;

  /**
   * Puts a year, month and day timestamp on when each one of these categories was created
   */
  @DatabaseField(columnName = "CREATED", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
      format = "yyyy-MM-dd HH:mm:ss", canBeNull = false, readOnly = true)
  private Timestamp created;

  /**
   * Adds my category name to each field in the Database
   */
  @DatabaseField(columnName = "NAME", canBeNull = false)
  private String name;

  /**
   * Getter for category ID
   * @return ID
   */
  public int getId() {
    return id;
  }

  /**
   * Getter for timestamp for when category was created
   * @return timestamp
   */

  public Timestamp getCreated() {
    return created;
  }

  /**
   * Getter and Setter for the name of my category
   * @return name
   */

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Puts the ID, the name and the date it was created to a map.
   * @return string
   */
  @Override
  public String toString() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("name", name);
    map.put("created", created);
    return super.toString();
  }

}

