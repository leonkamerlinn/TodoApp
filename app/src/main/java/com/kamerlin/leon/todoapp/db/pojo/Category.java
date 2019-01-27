package com.kamerlin.leon.todoapp.db.pojo;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "category_table",
        indices = {
                @Index(value = {"name"}, unique = true)
        }
)
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "color_name")
    private String mColorName;

    public Category() {}

    public Category(String categoryName, String colorName) {
        mName = categoryName;
        mColorName = colorName;
    }



    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getColorName() {
        return mColorName;
    }

    public void setColorName(String themeName) {
        mColorName = themeName;
    }


    @Override
    public String toString() {
        return String.format("ID: %s, NAME: %s, COLOR %s", getId(), getName(), getColorName());
    }
}
