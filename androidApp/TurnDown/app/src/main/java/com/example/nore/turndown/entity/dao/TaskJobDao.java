package com.example.nore.turndown.entity.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TASK_JOB.
*/
public class TaskJobDao extends AbstractDao<TaskJob, Long> {

    public static final String TABLENAME = "TASK_JOB";

    /**
     * Properties of entity TaskJob.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Descripcion = new Property(1, String.class, "descripcion", false, "DESCRIPCION");
        public final static Property Date = new Property(2, java.util.Date.class, "date", false, "DATE");
        public final static Property JobId = new Property(3, long.class, "jobId", false, "JOB_ID");
    };

    private DaoSession daoSession;

    private Query<TaskJob> job_TasksQuery;

    public TaskJobDao(DaoConfig config) {
        super(config);
    }
    
    public TaskJobDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TASK_JOB' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'DESCRIPCION' TEXT," + // 1: descripcion
                "'DATE' INTEGER," + // 2: date
                "'JOB_ID' INTEGER NOT NULL );"); // 3: jobId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TASK_JOB'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TaskJob entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String descripcion = entity.getDescripcion();
        if (descripcion != null) {
            stmt.bindString(2, descripcion);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(3, date.getTime());
        }
        stmt.bindLong(4, entity.getJobId());
    }

    @Override
    protected void attachEntity(TaskJob entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TaskJob readEntity(Cursor cursor, int offset) {
        TaskJob entity = new TaskJob( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // descripcion
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // date
            cursor.getLong(offset + 3) // jobId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TaskJob entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDescripcion(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setJobId(cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TaskJob entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TaskJob entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "tasks" to-many relationship of Job. */
    public List<TaskJob> _queryJob_Tasks(long jobId) {
        synchronized (this) {
            if (job_TasksQuery == null) {
                QueryBuilder<TaskJob> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.JobId.eq(null));
                queryBuilder.orderRaw("DATE ASC");
                job_TasksQuery = queryBuilder.build();
            }
        }
        Query<TaskJob> query = job_TasksQuery.forCurrentThread();
        query.setParameter(0, jobId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getJobDao().getAllColumns());
            builder.append(" FROM TASK_JOB T");
            builder.append(" LEFT JOIN JOB T0 ON T.'JOB_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected TaskJob loadCurrentDeep(Cursor cursor, boolean lock) {
        TaskJob entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Job job = loadCurrentOther(daoSession.getJobDao(), cursor, offset);
         if(job != null) {
            entity.setJob(job);
        }

        return entity;    
    }

    public TaskJob loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<TaskJob> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<TaskJob> list = new ArrayList<TaskJob>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<TaskJob> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<TaskJob> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}