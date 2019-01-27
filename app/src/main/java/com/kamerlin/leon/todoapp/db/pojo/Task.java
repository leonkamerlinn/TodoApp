package com.kamerlin.leon.todoapp.db.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.kamerlin.leon.todoapp.db.DateConverter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(
        tableName = "task_table",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id"
        )
)
public class Task implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "due_date")
    @TypeConverters(DateConverter.class)
    private long mDueDate;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "favorite")
    private boolean mFavorite;

    @NonNull
    @ColumnInfo(name = "created_at")
    @TypeConverters(DateConverter.class)
    private long mCreatedAt;


    @ColumnInfo(name = "remind_me")
    @TypeConverters(DateConverter.class)
    private long mRemindMe;

    @NonNull
    @ColumnInfo(name = "is_completed")
    private boolean mIsCompleted;

    @NonNull
    @ColumnInfo(name = "category_id")
    private long mCategoryId;


    public Task() {

    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getDueDate() {
        return mDueDate;
    }

    public void setDueDate(long dueDate) {
        mDueDate = dueDate;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
    }

    public long getRemindMe() {
        return mRemindMe;
    }

    public void setRemindMe(long remindMe) {
        mRemindMe = remindMe;
    }

    public boolean getIsCompleted() {
        return mIsCompleted;
    }

    public void setIsCompleted(boolean completed) {
        mIsCompleted = completed;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(long categoryId) {
        mCategoryId = categoryId;
    }




    @Override
    public String toString() {
        return "Task{" +
                "mId=" + mId +
                ", mDueDate=" + mDueDate +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mFavorite=" + mFavorite +
                ", mCreatedAt=" + mCreatedAt +
                ", mRemindMe=" + mRemindMe +
                ", mIsCompleted=" + mIsCompleted +
                ", mCategoryId=" + mCategoryId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeLong(this.mDueDate);
        dest.writeString(this.mName);
        dest.writeString(this.mDescription);
        dest.writeByte(this.mFavorite ? (byte) 1 : (byte) 0);
        dest.writeLong(this.mCreatedAt);
        dest.writeLong(this.mRemindMe);
        dest.writeByte(this.mIsCompleted ? (byte) 1 : (byte) 0);
        dest.writeLong(this.mCategoryId);
    }

    @Ignore
    public Task(Parcel in) {
        this.mId = in.readLong();
        this.mDueDate = in.readLong();
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mFavorite = in.readByte() != 0;
        this.mCreatedAt = in.readLong();
        this.mRemindMe = in.readLong();
        this.mIsCompleted = in.readByte() != 0;
        this.mCategoryId = in.readLong();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
