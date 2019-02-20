package com.kamerlin.leon.todoapp.db.task;

import android.os.Parcel;
import android.os.Parcelable;

import com.kamerlin.leon.todoapp.db.category.Category;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Entity(
        tableName = "task_table",
        foreignKeys = {
            @ForeignKey(
                entity = Category.class,
                parentColumns = "category_name",
                childColumns = "task_category_name",
                onDelete = ForeignKey.CASCADE
            )
        }
)
public class Task implements Parcelable {

    public static final int PRIORITY_NONE = 0;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_HIGH = 3;
    private static final long NOT_SET = -1;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private long mId;

    @ColumnInfo(name = "due_date")
    private long mDueDate;

    @NonNull
    @ColumnInfo(name = "task_name")
    private String mName;

    @ColumnInfo(name = "task_description")
    private String mDescription;

    @ColumnInfo(name = "favorite")
    private boolean mIsFavorite;

    @ColumnInfo(name="priority_code")
    private int mPriorityCode;

    @NonNull
    @ColumnInfo(name = "created_at")
    private long mCreatedAt;


    @ColumnInfo(name = "remind_me")
    private long mRemindMe;

    @NonNull
    @ColumnInfo(name = "is_completed")
    private boolean mIsCompleted;

    @NonNull
    @ColumnInfo(name = "task_category_name")
    private String mCategoryName;






    public Task() {
        setRemindMe(NOT_SET);
        setDueDate(NOT_SET);
        setPriorityCode(PRIORITY_NONE);
        setIsCompleted(false);
    }

    @Ignore
    public Task(String name) {
        this();
        setName(name);
    }

    @NonNull
    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    @Ignore
    public Task(int id, String name) {
        this();
        setName(name);
        setId(id);
    }

    @Ignore
    public Task(long id, @NonNull String name, String description, long dueDate, boolean favorite, int priorityCode, long createdAt, long remindMe, boolean isCompleted, String categoryName) {
        setId(id);
        setDueDate(dueDate);
        setName(name);
        setDescription(description);
        setIsFavorite(favorite);
        setPriorityCode(priorityCode);
        setCreatedAt(createdAt);
        setRemindMe(remindMe);
        setIsCompleted(isCompleted);
        setCategoryName(categoryName);
    }

    public boolean hasId() {
        return getId() != 0;
    }

    public boolean hasDueDate() {
        return getDueDate() != NOT_SET;
    }

    public boolean hasRemaindMe() {
        return getRemindMe() != NOT_SET;
    }

    public boolean hasDescription() {
        return (getDescription() != null && !getDescription().isEmpty());
    }

    public int getPriorityCode() {
        return mPriorityCode;
    }

    public void setPriorityCode(int priorityCode) {
        mPriorityCode = priorityCode;
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
        return (mName == null) ? "" : mName;
    }

    public void setName(@NonNull String name) {
        mName = name;
    }

    public String getDescription() {
        return (mDescription == null) ? "" : mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        mIsFavorite = favorite;
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

    public boolean isCompleted() {
        return mIsCompleted;
    }

    public void setIsCompleted(boolean completed) {
        mIsCompleted = completed;
    }

    @Nullable
    public String getDueDateFormat() {
        if (hasDueDate()) {
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd @ HH:mm", Locale.getDefault());
            return format.format(new Date(getDueDate()));
        }

        return null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "mId=" + mId +
                ", mDueDate=" + mDueDate +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mIsFavorite=" + mIsFavorite +
                ", mPriorityCode=" + mPriorityCode +
                ", mCreatedAt=" + mCreatedAt +
                ", mRemindMe=" + mRemindMe +
                ", mIsCompleted=" + mIsCompleted +
                ", mCategoryName='" + mCategoryName + '\'' +
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
        dest.writeByte(this.mIsFavorite ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mPriorityCode);
        dest.writeLong(this.mCreatedAt);
        dest.writeLong(this.mRemindMe);
        dest.writeByte(this.mIsCompleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.mCategoryName);
    }




    @Ignore
    public Task(Parcel in) {
        this.mId = in.readLong();
        this.mDueDate = in.readLong();
        this.mName = Objects.requireNonNull(in.readString());
        this.mDescription = in.readString();
        this.mIsFavorite = in.readByte() != 0;
        this.mPriorityCode = in.readInt();
        this.mCreatedAt = in.readLong();
        this.mRemindMe = in.readLong();
        this.mIsCompleted = in.readByte() != 0;
        this.mCategoryName = in.readString();
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Task) {
            Task task = (Task) obj;
            return task.toString().equals(this.toString());
        }
        return false;
    }
}
