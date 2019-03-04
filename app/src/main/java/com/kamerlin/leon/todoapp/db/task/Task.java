package com.kamerlin.leon.todoapp.db.task;

import android.os.Parcel;
import android.os.Parcelable;

import com.kamerlin.leon.todoapp.db.category.Category;
import com.kamerlin.leon.todoapp.worker.ReminderWorker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    public enum Sort {
        NONE,
        PRIORITY,
        FAVORITE,
        ALPHABETICAL_A_Z,
        ALPHABETICAL_Z_A,
        CREATED_NEWEST_FIRST,
        CREATED_OLDEST_FIRST,
        DUE_DATE,
        DUE_DATE_INVERSE
    }

    public static final int PRIORITY_NONE = 0;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_HIGH = 3;
    public static final long NOT_SET = -1;
    public static final String TITLE_TAG = "title_tag";
    public static final String DESCRIPTION_TAG = "description_tag";
    public static final String WORKER_ID_TAG = "worker_id";
    public static final String CREATED_AT_TAG = "created_at";


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

    @ColumnInfo(name = "worker_id")
    private String mWorkerId;

    @NonNull
    @ColumnInfo(name = "is_scheduled")
    private boolean mIsScheduled;




    public Task() {
        setRemindMe(NOT_SET);
        setDueDate(NOT_SET);
        setPriorityCode(PRIORITY_NONE);
        setIsCompleted(false);
        setIsScheduled(false);
        setCreatedAt(System.currentTimeMillis());
        if (!hasWorkId()) {
            setWorkerId(UUID.randomUUID().toString());
        }

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

    public boolean hasRemindMe() {
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
        return mName;
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

    public long getRemindDiff() {
        return getDueDate() - getRemindMe();
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


    public String getWorkerId() {
        return mWorkerId;
    }

    public void setWorkerId(String workerId) {
        mWorkerId = workerId;
    }
    public boolean hasWorkId() {
        return getWorkerId() != null && !getWorkerId().isEmpty();
    }

    public boolean isScheduled() {
        return mIsScheduled;
    }

    public void setIsScheduled(boolean scheduled) {
        mIsScheduled = scheduled;
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

    public boolean scheduleReminder() {
        if (!hasWorkId() || !hasRemindMe() || !hasDueDate()) return false;

        long delayFromNow = getRemindMe() - System.currentTimeMillis();
        Data.Builder data = new Data.Builder()
                .putString(Task.TITLE_TAG, getName())
                .putLong(Task.CREATED_AT_TAG, getCreatedAt());
        if (hasDescription()) {
            data.putString(Task.DESCRIPTION_TAG, getDescription());
        }

        OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(delayFromNow, TimeUnit.MILLISECONDS)
                .setInputData(data.build())
                .build();





        WorkManager.getInstance().enqueueUniqueWork(getWorkerId(), ExistingWorkPolicy.REPLACE, simpleRequest);

        return true;
    }

    public boolean cancelReminder() {
        if(!hasWorkId()) return false;
        WorkManager.getInstance().cancelUniqueWork(getWorkerId());
        return true;
    }
}
