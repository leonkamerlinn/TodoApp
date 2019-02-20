package com.kamerlin.leon.todoapp.db.category;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "category_table",
        indices = {
                @Index(value = {"category_name"}, unique = true)
        }
)
public class Category implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    private long mId;

    @NonNull
    @ColumnInfo(name = "category_name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "color_name")
    private String mColorName;

    public Category() {}

    @Ignore
    public Category(String categoryName, String colorName) {
        setName(categoryName);
        setColorName(colorName);
    }

    @Ignore
    public Category(int id, String categoryName, String colorName) {
        setId(id);
        setName(categoryName);
        setColorName(colorName);
    }

    @Ignore
    public Category(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mColorName = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return (mName == null) ? "" : mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getColorName() {
        return (mColorName == null) ? "" : mColorName;
    }

    public void setColorName(String themeName) {
        mColorName = themeName;
    }


    @Override
    public String toString() {
        return "Category{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mColorName='" + mColorName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mColorName);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Category) {
            Category category = (Category)obj;
            return category.toString().equals(this.toString());
        }
        return false;
    }
}